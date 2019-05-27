package com.ndgndg91.controller;

import com.ndgndg91.service.MemberService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;


@Log4j
@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private GoogleConnectionFactory googleConnectionFactory;

    @Autowired
    @Qualifier("googleOAuth2Parameters")
    private OAuth2Parameters googleOAuth2Parameters;

    @Autowired
    private FacebookConnectionFactory facebookConnectionFactory;

    @Autowired
    @Qualifier("facebookOAuth2Parameters")
    private OAuth2Parameters facebookOAuth2Parameters;

    @RequestMapping("/login")
    public String enterLogin(Model model, HttpSession session) throws UnsupportedEncodingException {
        //URL을 생성한다.
        OAuth2Operations googleOAuthOperations = googleConnectionFactory.getOAuthOperations();
        String googleLoginUrl = googleOAuthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, googleOAuth2Parameters);

        OAuth2Operations facebookOAuth2Operations = facebookConnectionFactory.getOAuthOperations();
        String facebookLoginUrl = facebookOAuth2Operations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, facebookOAuth2Parameters);

        log.info("/facebookLogin url : " + facebookLoginUrl);
        log.info("/googleLogin, url : " + googleLoginUrl);
        log.info("/kakaoLogin, url : " + KaKaoController.K_URL);
        log.info("/naverLogin, url : " + NaverController.getNaverLoginUrl(session));
        model.addAttribute("facebook_url", facebookLoginUrl);
        model.addAttribute("google_url", googleLoginUrl);
        model.addAttribute("kakao_url", KaKaoController.K_URL);
        model.addAttribute("naver_url", NaverController.getNaverLoginUrl(session));
        return "/member/login";
    }
}
