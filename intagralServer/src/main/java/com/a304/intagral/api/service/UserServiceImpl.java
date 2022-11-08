package com.a304.intagral.api.service;

import com.a304.intagral.api.request.UserProfileImageUpdatePostReq;
import com.a304.intagral.api.request.UserProfileUpdatePostReq;
import com.a304.intagral.api.response.TokenRes;
import com.a304.intagral.common.response.FileDetail;
import com.a304.intagral.common.util.AmazonS3ResourceStorageUtil;
import com.a304.intagral.common.util.JwtTokenUtil;
import com.a304.intagral.db.dto.HashtagProfileDto;
import com.a304.intagral.db.dto.UserProfileDto;
import com.a304.intagral.db.entity.Hashtag;
import com.a304.intagral.db.entity.User;
import com.a304.intagral.db.repository.HashtagFollowRepository;
import com.a304.intagral.db.repository.UserFollowRepository;
import com.a304.intagral.db.repository.UserRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
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
    AmazonS3ResourceStorageUtil amazonS3ResourceStorageUtil;

    final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
    final java.util.Random rand = new java.util.Random();
    final Set<String> identifiers = new HashSet<String>();

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
            if(identifiers.contains(builder.toString())) {
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

        Long followingCnt = userFollowRepository.countByUserIdTo(targetUserId);
        Long followerCnt = userFollowRepository.countByUserIdFrom(targetUserId);
        Long hashtagFollowCnt = hashtagFollowRepository.countByUserId(targetUserId);
        Long isFollow = userFollowRepository.countByUserIdToAndUserIdFrom(userId.intValue(), targetUserId);

        UserProfileDto userProfileDto = UserProfileDto.builder()
                .nickname(nickname)
                .imgPath(user.getProfileImgPath())
                .intro(user.getIntro())
                .following(followingCnt)
                .follower(followerCnt)
                .hashtag(hashtagFollowCnt)
                .isFollow(isFollow != 0)
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
        String resourceUrl = amazonS3ResourceStorageUtil.store(fileDetail.getPath(), multipartFile);

        User user = userRepository.findById(userId).get();
        user.setProfileImgPath(resourceUrl);
        userRepository.save(user);
    }

    @Override
    public boolean checkNicknameDuplication(String nickname) {
        return !isDuplicateNickname(nickname);
    }

    private boolean isDuplicateNickname(String nickname){
        return userRepository.countByNickname(nickname) != 0;
    }
}