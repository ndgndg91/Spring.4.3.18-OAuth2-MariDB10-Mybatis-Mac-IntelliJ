package com.ndgndg91.auth;

import lombok.Getter;

@Getter
public class GoogleAuthInfo {
    private String clientId;
    private String clientSecret;

    private GoogleAuthInfo(){}

    private GoogleAuthInfo(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
}
