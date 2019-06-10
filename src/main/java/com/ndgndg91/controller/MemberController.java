package com.ndgndg91.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ndgndg91.auth.KaKaoAuth2Info;
import com.ndgndg91.auth.NaverAuthInfo;
import com.ndgndg91.common.MemberUtils;
import com.ndgndg91.controller.LoginInterface.Login;
import com.ndgndg91.model.FriendDTO;
import com.ndgndg91.model.MemberDTO;
import com.ndgndg91.service.MemberService;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ndgndg91.model.enums.LoginType.DEFAULT;

@Log4j
@Controller
public class MemberController implements Login {

    private static final Pattern notStringPattern = Pattern.compile("([^a-zA-z])");
    private MemberService memberService;
    private GoogleConnectionFactory googleConnectionFactory;
    private OAuth2Parameters googleOAuth2Parameters;
    private FacebookConnectionFactory facebookConnectionFactory;
    private OAuth2Parameters facebookOAuth2Parameters;
    private KaKaoAuth2Info kaKaoAuth2Info;
    private NaverAuthInfo naverAuthInfo;

    public MemberController(MemberService memberService, GoogleConnectionFactory googleConnectionFactory,
                            OAuth2Parameters googleOAuth2Parameters, FacebookConnectionFactory facebookConnectionFactory,
                            OAuth2Parameters facebookOAuth2Parameters, KaKaoAuth2Info kaKaoAuth2Info,
                            NaverAuthInfo naverAuthInfo) {
        this.memberService = memberService;
        this.googleConnectionFactory = googleConnectionFactory;
        this.googleOAuth2Parameters = googleOAuth2Parameters;
        this.facebookConnectionFactory = facebookConnectionFactory;
        this.facebookOAuth2Parameters = facebookOAuth2Parameters;
        this.kaKaoAuth2Info = kaKaoAuth2Info;
        this.naverAuthInfo = naverAuthInfo;
    }

    @GetMapping("/login")
    public String enterLogin(Model model, HttpSession session) throws IOException {
        //URL을 생성한다.
        OAuth2Operations googleOAuthOperations = googleConnectionFactory.getOAuthOperations();
        String googleLoginUrl = googleOAuthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, googleOAuth2Parameters);

        OAuth2Operations facebookOAuth2Operations = facebookConnectionFactory.getOAuthOperations();
        String facebookLoginUrl = facebookOAuth2Operations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, facebookOAuth2Parameters);

        log.info("/facebookLogin url : " + facebookLoginUrl);
        log.info("/googleLogin, url : " + googleLoginUrl);
        log.info("/kakaoLogin, url : " + kaKaoAuth2Info.getLoginUrl());
        log.info("/naverLogin, url : " + naverAuthInfo.getNaverLoginUrl(session));
        model.addAttribute("facebook_url", facebookLoginUrl);
        model.addAttribute("google_url", googleLoginUrl);
        model.addAttribute("kakao_url", kaKaoAuth2Info.getLoginUrl());
        model.addAttribute("naver_url", naverAuthInfo.getNaverLoginUrl(session));
        return "/member/login";
    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity loginProcess(@RequestParam("username") String username,
                                       @RequestParam("pass") String password, HttpSession session) throws JsonProcessingException {
        return memberService.loginProcess(username, password, session);
    }

    @GetMapping("/logout")
    public String logout(@RequestParam("loginType") String loginType, HttpSession session) {
        log.info(loginType);
        return logoutByType(loginType, session);
    }

    @GetMapping("/join")
    public String enterJoin() {
        return "/member/join";
    }

    @PostMapping("/join")
    public String joinProcess(MultipartHttpServletRequest request, HttpServletRequest servletRequest) {
        String email = request.getParameter("uEmail");
        String uPassword = request.getParameter("uPassword");
        String uRealName = request.getParameter("uRealName");
        String uNick = request.getParameter("uNick");
        String uBirth = request.getParameter("uBirth");
        String uGender = request.getParameter("uGender");
        MultipartFile multipartFile = request.getFile("uPicture");

        Matcher notStringMatcher = notStringPattern.matcher(email);
        String prefixId = LocalDate.now().toString().replaceAll("-", "");
        String suffixId = notStringMatcher.replaceAll("");
        String memberId = prefixId + suffixId;

        String rootPath = servletRequest.getSession().getServletContext().getRealPath("/");
//        String imgFilePath = rootPath + "upload/" + memberId + "/";
        String imgFilePath = "/var/services/homes/ndgndg91/www/image/";
        log.info(rootPath);
        log.info(imgFilePath);

        MemberDTO joinMember = new MemberDTO.Builder(memberId, email).pw(uPassword).loginType(DEFAULT.toString()).realName(uRealName)
                .nick(uNick).birth(uBirth).age(MemberUtils.getMemberKoreanAge(uBirth)).gender(uGender).build();
        memberService.joinMember(joinMember, imgFilePath, multipartFile);

        log.info(new MemberDTO.Builder(memberId, email).pw(uPassword).loginType(DEFAULT.toString()).realName(uRealName)
                .nick(uNick).birth(uBirth).gender(uGender).build().toString());
        return "redirect:/";
    }

    @ResponseBody
    @PostMapping("/apply/friend")
    public ResponseEntity applyFriend(FriendDTO friendDTO) throws JsonProcessingException {
        return memberService.applyFriend(friendDTO);
    }

    @ResponseBody
    @GetMapping("/applyFor/friend")
    public ResponseEntity applyForFriendList(HttpSession session) throws JsonProcessingException {
        String loginUserId = ((MemberDTO) session.getAttribute("loginUserInfo")).getId();
        return memberService.selectApplicantMemberListForMe(loginUserId);
    }

    @ResponseBody
    @PostMapping("/accept/friend")
    public ResponseEntity acceptFriend(FriendDTO friendDTO) throws JsonProcessingException {
        return memberService.acceptFriend(friendDTO);
    }
}
