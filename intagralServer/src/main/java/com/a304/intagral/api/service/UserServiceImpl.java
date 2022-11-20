package com.a304.intagral.api.service;

import com.a304.intagral.api.request.UserProfileImageUpdatePostReq;
import com.a304.intagral.api.request.UserProfileUpdatePostReq;
import com.a304.intagral.api.response.TokenRes;
import com.a304.intagral.common.response.FileDetail;
import com.a304.intagral.common.util.AmazonS3ResourceStorageUtil;
import com.a304.intagral.common.util.JwtTokenUtil;
import com.a304.intagral.common.util.MultipartUtil;
import com.a304.intagral.db.dto.HashtagProfileDto;
import com.a304.intagral.db.dto.UserMyProfileDto;
import com.a304.intagral.db.dto.UserProfileDto;
import com.a304.intagral.db.entity.ClassificationTarget;
import com.a304.intagral.db.entity.Hashtag;
import com.a304.intagral.db.entity.HashtagPreset;
import com.a304.intagral.db.entity.User;
import com.a304.intagral.db.repository.ClassificationTargetRepository;
import com.a304.intagral.db.repository.HashtagFollowRepository;
import com.a304.intagral.db.repository.HashtagPresetRepository;
import com.a304.intagral.db.repository.HashtagRepository;
import com.a304.intagral.db.repository.UserFollowRepository;
import com.a304.intagral.db.repository.UserRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;


@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserFollowRepository userFollowRepository;
    @Autowired
    HashtagFollowRepository hashtagFollowRepository;
    @Autowired
    HashtagPresetRepository hashtagPresetRepository;
    @Autowired
    ClassificationTargetRepository classificationTargetRepository;
    @Autowired
    HashtagRepository hashtagRepository;
    @Autowired
    AmazonS3ResourceStorageUtil amazonS3ResourceStorageUtil;

    final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
    final java.util.Random rand = new java.util.Random();
    final Integer NICKNAME_MAX_LENGTH = 20;

    @Override
    public User getUserByUserId(Long userId) {
        return userRepository.findById(userId).get();
    }

    @Override
    public User getUserByUserName(String nickname) {
        return userRepository.findByNickname(nickname).get();
    }

    public TokenRes login(String idTokenString) {

        JSONObject jsonObj = verifyGoogleToken(idTokenString);
        if(jsonObj == null){
            throw new RuntimeException("token is invalid");
        }
        String name = jsonObj.get("name").toString();
        String email = jsonObj.get("email").toString();
        String imageUrl = jsonObj.get("picture").toString();

        //2.회원인지 확인
        User user = null;
        try{
            user = userRepository.findByEmail(email).get();
        } catch (Exception ex){
            // TODO: 회원 없을 때 위에 exception 구체화 시켜야함
            //3.회원 아니면 자동으로 회원가입 시켜줌
            user = User.builder()
                .email(email)
                .nickname(generateNickname())
                .oauthPlatform("gg")
                .profileImgPath(imageUrl)
                .build();

            // user 생성
            user = userRepository.saveAndFlush(user);
            initHashtagPreset(user.getId());
        }
        //4.토큰 반환
        String accessToken = JwtTokenUtil.getToken(String.valueOf(user.getId()));

        user.setAuthToken(accessToken);
        userRepository.save(user);

        return new TokenRes(accessToken, null);
    }

    public String generateNickname() {
        StringBuilder builder = new StringBuilder("temp_");
        while("temp_".equals(builder.toString())) {
            int length = 6;
            for(int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            if(isDuplicateNickname(builder.toString())) {
                builder = new StringBuilder("temp_");
            }
        }
        return builder.toString();
    }

    public JSONObject verifyGoogleToken(String idToken){
        BufferedReader in  = null;
        InputStream is = null;
        InputStreamReader isr = null;
        JSONParser jsonParser = new JSONParser();;

        try {
            String url = "https://oauth2.googleapis.com/tokeninfo";
            url += "?id_token="+idToken;

            URL gUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) gUrl.openConnection();

            is = conn.getInputStream();
            isr = new InputStreamReader(is, "UTF-8");
            in = new BufferedReader(isr);

            return (JSONObject)jsonParser.parse(in);

        }catch(Exception e) {
            return null;
        }
    }

    @Override
    public void logout(Long userId){
        User user = userRepository.findById(userId).get();
        user.setAuthToken(null);
        userRepository.save(user);
    }

    @Override
    public UserProfileDto getProfile(Long userId, String nickname) {
        User user = userRepository.findByNickname(nickname).get();
        Integer targetUserId = user.getId().intValue();

        Long followerCnt = userFollowRepository.countByUserIdTo(targetUserId);
        Long followingCnt = userFollowRepository.countByUserIdFrom(targetUserId);
        Long hashtagFollowCnt = hashtagFollowRepository.countByUserId(targetUserId);
        boolean isFollow = userFollowRepository.countByUserIdToAndUserIdFrom(targetUserId, userId.intValue()) != 0;

        UserProfileDto userProfileDto = UserProfileDto.builder()
                .nickname(nickname)
                .imgPath(user.getProfileImgPath())
                .intro(user.getIntro())
                .following(followingCnt)
                .follower(followerCnt)
                .hashtag(hashtagFollowCnt)
                .isFollow(isFollow)
                .build();

        return userProfileDto;
    }

    @Override
    public void updateProfile(Long userId, UserProfileUpdatePostReq userProfileUpdatePostReq) {
        User user = userRepository.findById(userId).get();
        if("nickname".equals(userProfileUpdatePostReq.getType())){
            user.setNickname(userProfileUpdatePostReq.getData());
        } else {
            user.setIntro(userProfileUpdatePostReq.getData());
        }

        userRepository.save(user);
    }

    @Override
    public void updateProfileImage(Long userId, UserProfileImageUpdatePostReq userProfileImageUpdatePostReq) {
        MultipartFile multipartFile = userProfileImageUpdatePostReq.getData();
        FileDetail fileDetail = FileDetail.multipartOf(multipartFile);
        // resizing
        String fileName = fileDetail.getName();
        String fileFormatName = fileDetail.getFormat();
        MultipartFile resizedFile = MultipartUtil.resizeImage(fileName, fileFormatName, multipartFile, 768);

        String resourceUrl = amazonS3ResourceStorageUtil.store(fileDetail.getPath(), resizedFile);

        User user = userRepository.findById(userId).get();
        user.setProfileImgPath(resourceUrl);
        userRepository.save(user);
    }

    @Override
    public boolean checkNicknameDuplication(String nickname) {
        return isDuplicateNickname(nickname);
    }

    @Override
    public UserMyProfileDto getMyProfile(Long userId) {

        User user = userRepository.findById(userId).get();

        UserMyProfileDto userMyProfileDto = UserMyProfileDto.builder()
                .nickname(user.getNickname())
                .imgPath(user.getProfileImgPath())
                .intro(user.getIntro())
                .build();

        return userMyProfileDto;
    }

    @Override
    public boolean checkNicknameLength(String nickname) {
        return nickname.length() > NICKNAME_MAX_LENGTH;
    }

    private boolean isDuplicateNickname(String nickname){
        return userRepository.countByNickname(nickname) != 0;
    }

    private void initHashtagPreset(Long userId) {
        Integer intUserId = userId.intValue();
        List<HashtagPreset> hashtagPresetList = new ArrayList<>();
        // 각 클래스의 이름에 맞는 프리셋
        List<ClassificationTarget> classificationTargetList = classificationTargetRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        classificationTargetList.remove(0);
        for (ClassificationTarget classificationTarget : classificationTargetList) {
            Hashtag hashtag;
            try {
                hashtag = hashtagRepository.findByContent(classificationTarget.getTargetNameKor()).get();
            } catch (NoSuchElementException e){
                hashtag = Hashtag.builder()
                        .content(classificationTarget.getTargetNameKor())
                        .searchCnt(0)
                        .build();
                hashtag = hashtagRepository.save(hashtag);
             }
            hashtagPresetList.add(HashtagPreset.builder()
                    .hashtagId(hashtag.getId().intValue())
                    .clsTargetId(classificationTarget.getId().intValue())
                    .userId(intUserId)
                    .build());
        }
        // 기본 프리셋
        List<String> defaultTagList = new ArrayList<>(Arrays.asList(new String[]{"인테리어", "디자인", "감성", "인테리어소품", "가구"}));
        hashtagPresetList.addAll(tagToPresetList(intUserId, 1, defaultTagList));
        // 안내 프리셋
        List<String> noticeTagList = new ArrayList<>(Arrays.asList(new String[]{"우측상단의", "편집버튼을", "터치해", "태그수정이", "가능합니다"}));
        hashtagPresetList.addAll(tagToPresetList(intUserId, 2, noticeTagList));

        hashtagPresetRepository.saveAll(hashtagPresetList);
    }

    private List<HashtagPreset> tagToPresetList(int userId, int clsId, List<String> contents){
        List<HashtagPreset> res = new ArrayList<>();

        for (String content : contents) {
            Hashtag hashtag;
            try {
                hashtag = hashtagRepository.findByContent(content).get();
            } catch (NoSuchElementException e){
                hashtag = Hashtag.builder()
                        .content(content)
                        .searchCnt(0)
                        .build();
                hashtag = hashtagRepository.save(hashtag);
            }
            res.add(HashtagPreset.builder()
                    .hashtagId(hashtag.getId().intValue())
                    .clsTargetId(clsId)
                    .userId(userId)
                    .build());
        }
        return res;
    }
}