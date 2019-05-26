package com.ndgndg91.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ndgndg91.model.ButtonVO;
import com.ndgndg91.model.MemberDTO;
import com.ndgndg91.service.MemberService;
import lombok.extern.log4j.Log4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.ndgndg91.model.enums.LoginType.KAKAO;

@Log4j
@Controller
public class KaKaoController {
    private final static String K_CLIENT_ID = "e604e6d3fe22ec52f468680d8a0018ee";
    private final static String K_REDIRECT_URI = "http://localhost:8080/auth/kakao/redirect";
    public final static String K_URL = "https://kauth.kakao.com/oauth/authorize?"
            + "client_id=" + K_CLIENT_ID + "&redirect_uri="
            + K_REDIRECT_URI + "&response_type=code&scope=account_email,gender,age_range,birthday";

    @Autowired
    private MemberService memberService;


    public String getAccessToken(String authorize_code) {
        final String RequestUrl = "https://kauth.kakao.com/oauth/token";
        final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
        postParams.add(new BasicNameValuePair("client_id", K_CLIENT_ID)); // REST API KEY
        postParams.add(new BasicNameValuePair("redirect_uri", K_REDIRECT_URI)); // 리다이렉트 URI
        postParams.add(new BasicNameValuePair("code", authorize_code)); // 로그인 과정 중 얻은 code 값

        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(RequestUrl);
        JsonNode returnNode = null;

        try {

            post.setEntity(new UrlEncodedFormEntity(postParams));
            final HttpResponse response = client.execute(post);
            final int responseCode = response.getStatusLine().getStatusCode();

            // JSON 형태 반환값 처리

            ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.getEntity().getContent());

        } catch (UnsupportedEncodingException | ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnNode.get("access_token").toString();
    }

    public JsonNode getKakaoUserInfo(String authorize_code, HttpSession session) {
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
        try {

            final HttpResponse response = client.execute(post);
            final int responseCode = response.getStatusLine().getStatusCode();
            log.info("\nSending 'POST' request to URL : " + RequestUrl);
            log.info("Response Code : " + responseCode);

            // JSON 형태 반환값 처리
            ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.getEntity().getContent());
        } catch (UnsupportedEncodingException | ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnNode;
    }

    private MemberDTO parseKakaoJson(JsonNode userInfo) {
        JsonParser kakaoParser = new JsonParser();
        JsonElement outer = kakaoParser.parse(userInfo.toString());
        String id = outer.getAsJsonObject().get("id").getAsString();
        String email = outer.getAsJsonObject().get("kaccount_email").getAsString();
        Boolean emailVerified = Boolean.valueOf(outer.getAsJsonObject().get("kaccount_email_verified").getAsString());
        JsonObject properties = outer.getAsJsonObject().get("properties").getAsJsonObject();
        String profileImageUrl = properties.get("profile_image").getAsString();
        String nickName = properties.get("nickname").getAsString();
        String thumbnailImageUrl = properties.get("thumbnail_image").getAsString();
        return new MemberDTO.Builder(id, emailVerified ? email : KAKAO.toString()).nick(nickName).pictureUrl(profileImageUrl).thumbnailImageUrl(thumbnailImageUrl).loginType(KAKAO.toString()).build();
    }


    @RequestMapping(value = "/auth/kakao/redirect")
    public String getKakaoSignIn(@RequestParam("code") String code, HttpSession session) {

        JsonNode userInfo = getKakaoUserInfo(code, session);
        session.setAttribute("kakaoUserInfo", userInfo);
        MemberDTO kakaoUserInfo = parseKakaoJson(userInfo);
        Optional<MemberDTO> optionalMember = Optional.ofNullable(memberService.selectOneMemberById(kakaoUserInfo.getId()));
        if (!optionalMember.isPresent())
            memberService.insertMemberExcludePwParameter(kakaoUserInfo);
        Iterator iterator = userInfo.iterator();
        printIterator(iterator);
        return "redirect:/";
    }


    @RequestMapping(value = "/auth/kakao/logout")
    public String kakaoLogout(HttpSession session) {
        log.info("kakao Logout");
        String KaKaoAccessToken = (String) session.getAttribute("KaKaoAccessToken");
        session.removeAttribute("KaKaoAccessToken");
        session.removeAttribute("kakaoUserInfo");
        session.invalidate();
        log.info("accessToken : " + KaKaoAccessToken);
        JsonNode afterLogout = kakaoLogout(KaKaoAccessToken);
        Iterator iterator = afterLogout.iterator();
        printIterator(iterator);
        return "redirect:/";
    }

    private JsonNode kakaoLogout(String authorize_code) {
        final String RequestUrl = "https://kapi.kakao.com/v1/user/logout";
        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(RequestUrl);

        post.addHeader("Authorization", "Bearer " + authorize_code);

        JsonNode returnNode = null;

        try {
            final HttpResponse response = client.execute(post);
            log.info("kakao logout : " + response.getStatusLine().getStatusCode());

            ObjectMapper mapper = new ObjectMapper();

            returnNode = mapper.readTree(response.getEntity().getContent());

        } catch (UnsupportedEncodingException | ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnNode;
    }

    private void printIterator(Iterator iterator) {
        while (iterator.hasNext()) {
            log.info(iterator.next());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/keyboard", method = RequestMethod.GET)
    public ButtonVO keyboard() {
        return new ButtonVO(new String[] {"test1", "test2", "test3"});
    }
}
