package com.ndgndg91.controller;

import com.ndgndg91.service.MemberService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private OAuth2Parameters googleOAuth2Parameters;

    @RequestMapping("/login")
    public String enterLogin(Model model, HttpSession session) throws UnsupportedEncodingException {
        //URL을 생성한다.
        OAuth2Operations oauthOperations = googleConnectionFactory.getOAuthOperations();
        String url = oauthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, googleOAuth2Parameters);

        log.info("/googleLogin, url : " + url);
        log.info("/kakaoLogin, url : " + KaKaoController.K_URL);
        log.info("/naverLogin, url : " + NaverController.getNaverLoginUrl(session));
        model.addAttribute("google_url", url);
        model.addAttribute("kakao_url", KaKaoController.K_URL);
        model.addAttribute("naver_url", NaverController.getNaverLoginUrl(session));
        log.info(memberService.selectNow());
        return "/member/login";
    }
}
