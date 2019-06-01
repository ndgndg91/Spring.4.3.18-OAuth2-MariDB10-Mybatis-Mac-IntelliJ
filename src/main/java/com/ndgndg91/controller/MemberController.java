package com.ndgndg91.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndgndg91.model.FriendDTO;
import com.ndgndg91.model.MemberDTO;
import com.ndgndg91.service.MemberService;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.ndgndg91.model.enums.LoginType.*;


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

    private static final ObjectMapper mapper = new ObjectMapper();

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

    @GetMapping("/logout")
    public String logout(@RequestParam("loginType") String loginType, HttpSession session){
        log.info(loginType);
        return logoutByType(loginType, session);
    }


    private String logoutByType(String loginType, HttpSession session){
        if (StringUtils.equals(KAKAO.toString(), loginType))
            return "forward:/auth/kakao/logout";
        else if (StringUtils.equals(NAVER.toString(), loginType))
            return "forward:/auth/naver/logout";
        else if (StringUtils.equals(GOOGLE.toString(), loginType))
            return "forward:/auth/google/logout";
        else {
            session.removeAttribute("loginUserInfo");
            session.invalidate();
            return "redirect:/";
        }
    }

    @ResponseBody
    @PostMapping("/apply/friend")
    public ResponseEntity<String> applyFriend(FriendDTO friendDTO) throws JsonProcessingException {
        String returnString = "친구 신청 성공";
        try {
            memberService.applyFriend(friendDTO);
        } catch (Exception e) {
            e.printStackTrace();
            returnString = "친구 신청 실패";
        }
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<>(mapper.writeValueAsString(returnString), resHeaders, HttpStatus.CREATED);
    }

    @ResponseBody
    @GetMapping("/applyFor/friend")
    public ResponseEntity<String> applyForFriendList(HttpSession session) throws JsonProcessingException {
        String loginUserId = ((MemberDTO)session.getAttribute("loginUserInfo")).getId();
        List<MemberDTO> applicantList =  memberService.selectApplicantMemberListForMe(loginUserId);
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<>(mapper.writeValueAsString(applicantList), resHeaders, HttpStatus.CREATED);
    }

    @ResponseBody
    @PostMapping("/accept/friend")
    public ResponseEntity<String> acceptFriend(FriendDTO friendDTO) throws JsonProcessingException {
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");
        String result = memberService.acceptFriend(friendDTO);
        return new ResponseEntity<>(mapper.writeValueAsString(result), resHeaders, HttpStatus.CREATED);
    }
}
