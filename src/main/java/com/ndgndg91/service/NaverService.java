package com.ndgndg91.service;

import com.ndgndg91.auth.NaverAuthInfo;
import com.ndgndg91.common.JsonUtils;
import com.ndgndg91.controller.LoginInterface.Login;
import com.ndgndg91.model.MemberDTO;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

import static com.ndgndg91.model.enums.LoginType.NAVER;

@Log4j
@Service
public class NaverService implements Login {

    private MemberService memberService;
    private NaverAuthInfo naverAuthInfo;

    public NaverService(MemberService memberService, NaverAuthInfo naverAuthInfo){
        this.memberService = memberService;
        this.naverAuthInfo = naverAuthInfo;
    }


    public void parseAccessToken(String response, HttpSession session, Model model){
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

    public void parseNaverUserInfo(String naverUserInfo, HttpSession session, Model model){
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

    public HttpURLConnection getNaverAccessTokenHttpUrlConnection(String apiURL) throws IOException {
        URL url = new URL(apiURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        return con;
    }

    public HttpURLConnection getNaverUserInfoHttpURLConnection(String access_token) throws IOException {
        URL url = new URL(naverAuthInfo.getProfileApiUrl());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer "+access_token);
        return con;
    }

    public BufferedReader getBufferedReader(int responseCode, HttpURLConnection con) throws IOException {
        if (responseCode != 200){
            return new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        return new BufferedReader(new InputStreamReader(con.getInputStream()));
    }

    public StringBuilder getResponseToStringBuilder(BufferedReader br) throws IOException{
        String inputLine;
        StringBuilder res = new StringBuilder();
        while ((inputLine = br.readLine()) != null) {
            res.append(inputLine);
        }
        return res;
    }

}
