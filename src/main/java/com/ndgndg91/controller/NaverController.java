package com.ndgndg91.controller;

import com.ndgndg91.common.JsonUtils;
import com.ndgndg91.model.MemberDTO;
import com.ndgndg91.service.MemberService;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.*;
import java.security.SecureRandom;
import java.util.Optional;

import static com.ndgndg91.model.enums.LoginType.NAVER;

@Log4j
@Controller
public class NaverController {
    private static final String CLIENT_ID = "aKzz8HPYVueuyowOPc55";
    private static final String CLIENT_SECRET = "gmThUARPjr";
    private static final String N_REDIRECT_URL = "http://localhost:8080/auth/naver/redirect";
    private static final String N_ACCESS_TOKEN_URL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&redirect_uri=" + N_REDIRECT_URL;
    private final static String PROFILE_API_URL = "https://openapi.naver.com/v1/nid/me";

    @Autowired
    private MemberService memberService;

    @RequestMapping(value = "/auth/naver/logout")
    public String naverLogout(HttpSession session){
        session.removeAttribute("NaverUserInfo");
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping(value = "/auth/naver/redirect")
    public String getNaverSignIn(HttpServletRequest request, HttpSession session, Model model) {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        StringBuilder apiURL = new StringBuilder()
                .append(N_ACCESS_TOKEN_URL)
                .append("&code=").append(code)
                .append("&state=").append(state);
        try {
            HttpURLConnection con = getNaverAccessTokenHttpUrlConnection(apiURL.toString());
            int responseCode = con.getResponseCode();
            BufferedReader br = getBufferedReader(responseCode, con);
            StringBuilder res = getResponseToStringBuilder(br);
            br.close();
            if (responseCode == 200) {
                log.info(res.toString());
                parseAccessToken(res.toString(), session, model);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    private void parseAccessToken(String response, HttpSession session, Model model){
        String access_token = JsonUtils.parse1Depth(response, "access_token");
        String refresh_token = JsonUtils.parse1Depth(response, "refresh_token");
        String token_type = JsonUtils.parse1Depth(response, "token_type");
        log.info("access_token : "+ access_token);
        log.info("refresh_token : "+ refresh_token);
        log.info(token_type);
        try {
            HttpURLConnection con = getNaverUserInfoHttpURLConnection(access_token);
            int responseCode = con.getResponseCode();
            BufferedReader br = getBufferedReader(responseCode, con);
            StringBuilder res = getResponseToStringBuilder(br);
            log.info(res.toString());
            parseNaverUserInfo(res.toString(), session, model);
            br.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void parseNaverUserInfo(String naverUserInfo, HttpSession session, Model model){
        String resultCode = JsonUtils.parse1Depth(naverUserInfo, "resultcode");
        if (StringUtils.equals(resultCode, "00")) {
            String id = JsonUtils.parse2Depth(naverUserInfo, "response", "id");
            Optional<MemberDTO> isExistMember = Optional.ofNullable(memberService.selectOneMemberById(id));
            if (isExistMember.isPresent()){
                setMemberToSession(isExistMember.get(), session);
                return;
            }

            String enc_id = JsonUtils.parse2Depth(naverUserInfo, "response", "enc_id");
            String profile_image = JsonUtils.parse2Depth(naverUserInfo, "response", "profile_image");
            String age = JsonUtils.parse2Depth(naverUserInfo, "response", "age");
            String gender = JsonUtils.parse2Depth(naverUserInfo, "response", "gender");
            String nickname = JsonUtils.parse2Depth(naverUserInfo, "response", "nickname");
            String email = JsonUtils.parse2Depth(naverUserInfo, "response", "email");
            String name = JsonUtils.parse2Depth(naverUserInfo, "response", "name");
            String birthday = JsonUtils.parse2Depth(naverUserInfo, "response", "birthday");
            MemberDTO newMember = new MemberDTO.Builder(id, email)
                    .loginType(NAVER.toString())
                    .subId(enc_id)
                    .pictureUrl(profile_image)
                    .age(age)
                    .gender(gender)
                    .nick(nickname)
                    .realName(name)
                    .birth(birthday)
                    .build();
            memberService.insertMemberExcludePwParameter(newMember);
            setMemberToSession(newMember, session);
            return;
        }

        String message = JsonUtils.parse1Depth(naverUserInfo, "message");
        model.addAttribute("errMessage", message);
    }

    private void setMemberToSession(MemberDTO loginMember, HttpSession session){
        session.setAttribute("NaverUserInfo", loginMember);
    }

    private HttpURLConnection getNaverAccessTokenHttpUrlConnection(String apiURL) throws IOException {
        URL url = new URL(apiURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        return con;
    }

    private HttpURLConnection getNaverUserInfoHttpURLConnection(String access_token) throws IOException {
        URL url = new URL(PROFILE_API_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer "+access_token);
        return con;
    }

    private BufferedReader getBufferedReader(int responseCode, HttpURLConnection con) throws IOException {
        if (responseCode != 200){
            return new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        return new BufferedReader(new InputStreamReader(con.getInputStream()));
    }

    private StringBuilder getResponseToStringBuilder(BufferedReader br) throws IOException{
        String inputLine;
        StringBuilder res = new StringBuilder();
        while ((inputLine = br.readLine()) != null) {
            res.append(inputLine);
        }
        return res;
    }

    public static String getNaverLoginUrl(HttpSession session) throws UnsupportedEncodingException {
        String redirectURI = URLEncoder.encode(N_REDIRECT_URL, "UTF-8");
        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130, random).toString();
        session.setAttribute("state", state);
        return "https://nid.naver.com/oauth2.0/authorize?response_type=code" + "&client_id=" + CLIENT_ID + "&redirect_uri=" + redirectURI + "&state=" + state;
    }

}
