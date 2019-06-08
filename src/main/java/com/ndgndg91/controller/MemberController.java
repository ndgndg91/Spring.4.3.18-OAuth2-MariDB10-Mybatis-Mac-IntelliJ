package com.ndgndg91.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndgndg91.auth.KaKaoAuth2Info;
import com.ndgndg91.auth.NaverAuthInfo;
import com.ndgndg91.common.FileUtils;
import com.ndgndg91.common.JsonUtils;
import com.ndgndg91.controller.LoginInterface.Login;
import com.ndgndg91.model.FriendDTO;
import com.ndgndg91.model.MemberDTO;
import com.ndgndg91.service.MemberService;
import com.ndgndg91.syno.SynoUploadInfo;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ndgndg91.model.enums.LoginType.DEFAULT;

@Log4j
@Controller
public class MemberController implements Login {

    private static final Pattern notStringPattern = Pattern.compile("([^a-zA-z])");
    private static final ObjectMapper mapper = new ObjectMapper();
    private MemberService memberService;
    private GoogleConnectionFactory googleConnectionFactory;
    private OAuth2Parameters googleOAuth2Parameters;
    private FacebookConnectionFactory facebookConnectionFactory;
    private OAuth2Parameters facebookOAuth2Parameters;
    private KaKaoAuth2Info kaKaoAuth2Info;
    private NaverAuthInfo naverAuthInfo;
    private SynoUploadInfo synologyUploadInfo;

    public MemberController(MemberService memberService, GoogleConnectionFactory googleConnectionFactory,
                            OAuth2Parameters googleOAuth2Parameters, FacebookConnectionFactory facebookConnectionFactory,
                            OAuth2Parameters facebookOAuth2Parameters, KaKaoAuth2Info kaKaoAuth2Info,
                            NaverAuthInfo naverAuthInfo, SynoUploadInfo synologyUploadInfo) {
        this.memberService = memberService;
        this.googleConnectionFactory = googleConnectionFactory;
        this.googleOAuth2Parameters = googleOAuth2Parameters;
        this.facebookConnectionFactory = facebookConnectionFactory;
        this.facebookOAuth2Parameters = facebookOAuth2Parameters;
        this.kaKaoAuth2Info = kaKaoAuth2Info;
        this.naverAuthInfo = naverAuthInfo;
        this.synologyUploadInfo = synologyUploadInfo;
    }

    @RequestMapping("/login")
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
    public String joinProcess(MultipartHttpServletRequest request, HttpServletRequest servletRequest) throws IOException {
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

//        MemberDTO joinMember = new MemberDTO.Builder(memberId, email).pw(uPassword).loginType(DEFAULT.toString()).realName(uRealName)
//                .nick(uNick).birth(uBirth).gender(uGender).build();
//        memberService.joinMember(joinMember);
        log.info(new MemberDTO.Builder(memberId, email).pw(uPassword).loginType(DEFAULT.toString()).realName(uRealName)
                .nick(uNick).birth(uBirth).gender(uGender).build().toString());


        String rootPath = servletRequest.getSession().getServletContext().getRealPath("/");
        String imgFilePath = rootPath + "upload/" + memberId + "/";
        log.info(rootPath);
        log.info(imgFilePath);

        FileUtils.makeDirectory(imgFilePath);
        FileUtils.uploadImageOfMember(imgFilePath, multipartFile);
        String sid = synoLogin();
        synoUpload(multipartFile, sid);
        synoLogout(sid);
        return "redirect:/";
    }

    private String synoLogin() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String url = synologyUploadInfo.getHost() + synologyUploadInfo.getWebAPIAuthGet() + synologyUploadInfo.getLogin();
        log.info(url);
        HttpGet getRequest = new HttpGet(url);
        CloseableHttpResponse response = client.execute(getRequest);
        String responseResult = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        String sid = "";
        if(StringUtils.equals(JsonUtils.parse1Depth(responseResult, "success"), "true"))
            sid = JsonUtils.parse2Depth(responseResult, "data", "sid");
        if (response.getStatusLine().getStatusCode() == 200) {
            log.info("login 성공");
            log.info(responseResult);
        } else {
            log.info("login 실패");
            log.info(response.getStatusLine().getStatusCode());
            log.info(responseResult);
        }
        client.close();
        return sid;
    }

    private void synoUpload(MultipartFile multipartFile, String sid) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String url = synologyUploadInfo.getHost() + synologyUploadInfo.getWebAPIUploadPost();
        log.info(url);
        HttpPost postRequest = new HttpPost(url);
        postRequest.setHeader("Content-Type", "multipart/form-data");


        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("api", synologyUploadInfo.getApiName());
        builder.addTextBody("version", synologyUploadInfo.getApiVersion());
        builder.addTextBody("method", synologyUploadInfo.getApiMethod());
        builder.addTextBody("path", synologyUploadInfo.getUploadPath());
        builder.addTextBody("create_parents", synologyUploadInfo.getCreateParents());
        builder.addTextBody("_sid", sid);
        builder.addBinaryBody("file", multipartFile.getInputStream(),
                ContentType.APPLICATION_OCTET_STREAM, multipartFile.getOriginalFilename());
        HttpEntity multipart = builder.build();
        postRequest.setEntity(multipart);


        CloseableHttpResponse response = client.execute(postRequest);

        if (response.getStatusLine().getStatusCode() == 200) {
            log.info("업로드 성공");
        }
        else {
            log.info("업로드 실패");
        }
        client.close();
        log.info("업로드 프로세스 완료");
    }

    private void synoLogout(String sid) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String url = synologyUploadInfo.getHost() + synologyUploadInfo.getWebAPIAuthGet() + "_sid=" + sid + "&" + synologyUploadInfo.getLogout();
        log.info(url);
        HttpGet getRequest = new HttpGet(url);
        CloseableHttpResponse response = client.execute(getRequest);
        String responseResult = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        if (response.getStatusLine().getStatusCode() == 200) {
            log.info("로그아웃 성공");
            log.info(responseResult);
        } else {
            log.info("로그아웃 실패");
            log.info(responseResult);
        }
        client.close();
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
        String loginUserId = ((MemberDTO) session.getAttribute("loginUserInfo")).getId();
        List<MemberDTO> applicantList = memberService.selectApplicantMemberListForMe(loginUserId);
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
