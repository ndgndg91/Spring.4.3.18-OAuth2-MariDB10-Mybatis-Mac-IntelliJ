package com.ndgndg91.auth;

import lombok.Getter;

@Getter
public class KaKaoAuth2Info {
    private String clientId;
    private String redirectUri;
    private String loginUrl;

    private KaKaoAuth2Info(){}
    private KaKaoAuth2Info(String clientId, String redirectUri){
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.loginUrl = "https://kauth.kakao.com/oauth/authorize?"
                + "client_id=" + this.clientId + "&redirect_uri="
                + this.redirectUri + "&response_type=code&scope=account_email,gender,age_range,birthday";
    }
}
