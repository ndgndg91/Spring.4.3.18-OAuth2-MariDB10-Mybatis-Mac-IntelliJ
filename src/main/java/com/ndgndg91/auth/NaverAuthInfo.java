package com.ndgndg91.auth;

import lombok.Getter;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.SecureRandom;

@Getter
public class NaverAuthInfo {
    private String clientId;
    private String clientSecret;
    private String redirectUrl;
    private String accessTokenUrl;
    private String profileApiUrl;

    private NaverAuthInfo(){}
    private NaverAuthInfo(String clientId, String clientSecret, String redirectUrl,
                          String profileApiUrl){
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUrl = redirectUrl;
        this.profileApiUrl = profileApiUrl;
        this.accessTokenUrl = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id=" + this.clientId + "&client_secret=" + this.clientSecret + "&redirect_uri=" + this.redirectUrl;
    }

    public String getNaverLoginUrl(HttpSession session) throws IOException {
        String redirectURI = URLEncoder.encode(this.redirectUrl, "UTF-8");
        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130, random).toString();
        session.setAttribute("state", state);
        return "https://nid.naver.com/oauth2.0/authorize?response_type=code" + "&client_id=" + this.clientId + "&redirect_uri=" + redirectURI + "&state=" + state;
    }
}
