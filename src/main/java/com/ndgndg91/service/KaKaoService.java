package com.ndgndg91.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ndgndg91.auth.KaKaoAuth2Info;
import com.ndgndg91.model.MemberDTO;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.ndgndg91.model.enums.LoginType.KAKAO;

@Log4j
@Service
public class KaKaoService {

    private KaKaoAuth2Info kakaoAuthInfo;

    public KaKaoService(KaKaoAuth2Info kaKaoAuth2Info) {
        this.kakaoAuthInfo = kaKaoAuth2Info;
    }

    public String getAccessToken(String authorize_code) throws IOException {
        final String RequestUrl = "https://kauth.kakao.com/oauth/token";
        final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
        postParams.add(new BasicNameValuePair("client_id", kakaoAuthInfo.getClientId())); // REST API KEY
        postParams.add(new BasicNameValuePair("redirect_uri", kakaoAuthInfo.getRedirectUri())); // 리다이렉트 URI
        postParams.add(new BasicNameValuePair("code", authorize_code)); // 로그인 과정 중 얻은 code 값

        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(RequestUrl);
        JsonNode returnNode = null;

        post.setEntity(new UrlEncodedFormEntity(postParams));
        final HttpResponse response = client.execute(post);
        final int responseCode = response.getStatusLine().getStatusCode();

        // JSON 형태 반환값 처리

        ObjectMapper mapper = new ObjectMapper();
        returnNode = mapper.readTree(response.getEntity().getContent());

        return returnNode.get("access_token").toString();
    }

    public JsonNode getKakaoUserInfo(String authorize_code, HttpSession session) throws IOException {
        final String RequestUrl = "https://kapi.kakao.com/v1/user/me";
        //String CLIENT_ID = K_CLIENT_ID; // REST API KEY
        //String REDIRECT_URI = K_REDIRECT_URI; // 리다이렉트 URI
        //String code = autorize_code; // 로그인 과정중 얻은 토큰 값
        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(RequestUrl);
        String accessToken = getAccessToken(authorize_code);
        session.setAttribute("KaKaoAccessToken", accessToken);
        // add header
        post.addHeader("Authorization", "Bearer " + accessToken);

        JsonNode returnNode = null;

        final HttpResponse response = client.execute(post);
        final int responseCode = response.getStatusLine().getStatusCode();
        log.info("\nSending 'POST' request to URL : " + RequestUrl);
        log.info("Response Code : " + responseCode);

        // JSON 형태 반환값 처리
        ObjectMapper mapper = new ObjectMapper();
        returnNode = mapper.readTree(response.getEntity().getContent());
        return returnNode;
    }

    public MemberDTO parseKakaoJson(JsonNode userInfo) {
        JsonParser kakaoParser = new JsonParser();
        JsonElement outer = kakaoParser.parse(userInfo.toString());
        String id = outer.getAsJsonObject().get("id").getAsString();
        String email = "";
        if (outer.getAsJsonObject().has("kaccount_email"))
            email = outer.getAsJsonObject().get("kaccount_email").getAsString();
        Boolean emailVerified = Boolean.FALSE;
        if (outer.getAsJsonObject().has("kaccount_email_verified"))
            emailVerified = Boolean.valueOf(outer.getAsJsonObject().get("kaccount_email_verified").getAsString());
        JsonObject properties = outer.getAsJsonObject().get("properties").getAsJsonObject();
        String profileImageUrl = properties.get("profile_image").toString().replaceAll("\"","");
        if (StringUtils.equals(profileImageUrl, "null"))
            profileImageUrl = "";
        String nickName = properties.get("nickname").getAsString();
        String thumbnailImageUrl = properties.get("thumbnail_image").toString().replaceAll("\"","");
        if (StringUtils.equals(thumbnailImageUrl, "null"))
            thumbnailImageUrl = "";
        return new MemberDTO.Builder(id, emailVerified ? email : KAKAO.toString()).nick(nickName).pictureUrl(profileImageUrl).thumbnailImageUrl(thumbnailImageUrl).loginType(KAKAO.toString()).build();
    }
    public JsonNode kakaoLogout(String authorize_code) throws IOException {
        final String RequestUrl = "https://kapi.kakao.com/v1/user/logout";
        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(RequestUrl);

        post.addHeader("Authorization", "Bearer " + authorize_code);

        JsonNode returnNode = null;

        final HttpResponse response = client.execute(post);
        log.info("kakao logout : " + response.getStatusLine().getStatusCode());

        ObjectMapper mapper = new ObjectMapper();

        returnNode = mapper.readTree(response.getEntity().getContent());

        return returnNode;
    }

    public void printIterator(Iterator iterator) {
        while (iterator.hasNext()) {
            log.info(iterator.next());
        }
    }
}
