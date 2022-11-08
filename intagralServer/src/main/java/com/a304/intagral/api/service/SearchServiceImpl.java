package com.a304.intagral.api.service;

import com.a304.intagral.db.dto.SearchHashtagDto;
import com.a304.intagral.db.dto.SearchUserDto;
import com.a304.intagral.db.entity.Hashtag;
import com.a304.intagral.db.entity.HashtagFollow;
import com.a304.intagral.db.entity.User;
import com.a304.intagral.db.entity.UserFollow;
import com.a304.intagral.db.repository.HashtagRepository;
import com.a304.intagral.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("searchService")
public class SearchServiceImpl implements SearchService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    HashtagRepository hashtagRepository;

    @Override
    public List<SearchUserDto> searchUser(Long userId, String target) {
        //유저가 팔로우하고 있는 계정의 ID 리스트 생성
        User user = userRepository.findById(userId).get();
        List<UserFollow> userFollowingList = user.getUserFollowingList();
        List<Long> followingIdList = new ArrayList<>();
        for(UserFollow userFollow : userFollowingList){
            followingIdList.add(userFollow.getUserIdTo().longValue());
        }
        // 검색 결과와 일치하는 유저들을 불러와 비교
        List<User> userList = userRepository.findAllByNicknameContaining(target);
        List<SearchUserDto> searchUserDtoList = new ArrayList<>();
        for(User searchedUser : userList){
            SearchUserDto searchUserDto = SearchUserDto.builder()
                    .name(searchedUser.getNickname())
                    .profileImage(searchedUser.getProfileImgPath())
                    .isFollow(false)
                    .build();
            if(followingIdList.contains(searchedUser.getId())){
                searchUserDto.setFollow(true);
            }
            searchUserDtoList.add(searchUserDto);
        }

        return searchUserDtoList;
    }

    @Override
    public List<SearchHashtagDto> searchHashtag(Long userId, String target) {
        User user = userRepository.findById(userId).get();
        List<HashtagFollow> hashtagFollowList = user.getHashtagFollowList();
        List<Long> followingIdList = new ArrayList<>();
        for(HashtagFollow hashtagFollow : hashtagFollowList){
            followingIdList.add(hashtagFollow.getHashtagId().longValue());
        }
        List<Hashtag> hashtagList = hashtagRepository.findAllByContentContaining(target);
        List<SearchHashtagDto> searchHashtagDtoList = new ArrayList<>();
        for(Hashtag searchedHashtag : hashtagList){
            SearchHashtagDto searchHashtagDto = SearchHashtagDto.builder()
                    .name(searchedHashtag.getContent())
                    .isFollow(false)
                    .build();
            if(followingIdList.contains(searchedHashtag.getId())){
                searchHashtagDto.setFollow(true);
            }
            searchHashtagDtoList.add(searchHashtagDto);
        }

        return searchHashtagDtoList;
    }

    @Override
    public void countUpHashtagSearchCnt(String content) {
        Hashtag hashtag = hashtagRepository.findByContent(content).get();
        increaseHashtagSearchCnt(hashtag);
    }

    private void increaseHashtagSearchCnt(Hashtag hashtag){
        hashtag.setSearchCnt(hashtag.getSearchCnt() + 1);
        hashtagRepository.save(hashtag);
    }
}
