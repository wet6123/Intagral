package com.a304.intagral.api.service;

import com.a304.intagral.api.response.TokenRes;
import com.a304.intagral.common.util.JwtTokenUtil;
import com.a304.intagral.db.entity.User;
import com.a304.intagral.db.repository.UserRepository;
import com.google.gson.JsonObject;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        JsonObject jsonObj = verifyGoogleToken(idTokenString);
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
                .oauthPlatform("google")
                .profileImagePath(imageUrl)
                .build();
        }

        //4.토큰 반환
        String accessToken = JwtTokenUtil.getToken(String.valueOf(user.getId()));

        user.setAuthToken(accessToken);
        userRepository.save(user);

        return new TokenRes(accessToken, null);
    }

    public String generateNickname() {
        StringBuilder builder = new StringBuilder("temp_");
        while(builder.toString().length() == 0) {
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

    public JsonObject verifyGoogleToken(String idTokenString){
        BufferedReader in  = null;
        InputStream is = null;
        InputStreamReader isr = null;
        JSONParser jsonParser = null;

        try {
            String idToken = idTokenString.split("=")[1];

            String url = "https://oauth2.googleapis.com/tokeninfo";
            url += "?id_token="+idToken;

            URL gUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) gUrl.openConnection();

            is = conn.getInputStream();
            isr = new InputStreamReader(is, "UTF-8");
            in = new BufferedReader(isr);

            jsonParser = new JSONParser(in);
            return (JsonObject)jsonParser.parse();

        }catch(Exception e) {
            return null;
        }
    }
}