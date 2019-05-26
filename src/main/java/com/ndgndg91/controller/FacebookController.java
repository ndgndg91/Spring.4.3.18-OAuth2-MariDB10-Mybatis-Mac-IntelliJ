package com.ndgndg91.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.UserOperations;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j
@Controller
public class FacebookController {

    @Autowired
    @Qualifier("facebookOAuth2Parameters")
    private OAuth2Parameters facebookOAuth2Parameters;

    @Autowired
    private FacebookConnectionFactory facebookConnectionFactory;

    @RequestMapping(value = "/auth/facebook/redirect")
    public String getFacebookSignIn(@RequestParam String code){
        try {
            String redirectUri = facebookOAuth2Parameters.getRedirectUri();
            log.info("Redirect URI : " + redirectUri);
            log.info("Code : " + code);

            OAuth2Operations oauthOperations = facebookConnectionFactory.getOAuthOperations();
            AccessGrant accessGrant = oauthOperations.exchangeForAccess(code, redirectUri, null);
            String accessToken = accessGrant.getAccessToken();
            log.info("AccessToken: " + accessToken);
            Long expireTime = accessGrant.getExpireTime();


            if (expireTime != null && expireTime < System.currentTimeMillis()) {
                accessToken = accessGrant.getRefreshToken();
                log.info("accessToken is expired. refresh token = " + accessToken);
            };


            Connection<Facebook> connection = facebookConnectionFactory.createConnection(accessGrant);
            Facebook facebook = connection == null ? new FacebookTemplate(accessToken) : connection.getApi();
            UserOperations userOperations = facebook.userOperations();

            try

            {
                String [] fields = { "id", "email",  "name"};
                User userProfile = facebook.fetchObject("me", User.class, fields);
                log.info("유저이메일 : " + userProfile.getEmail());
                log.info("유저 id : " + userProfile.getId());
                log.info("유저 name : " + userProfile.getName());

            } catch (MissingAuthorizationException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {

            e.printStackTrace();

        }
        return "redirect:/";
    }
}
