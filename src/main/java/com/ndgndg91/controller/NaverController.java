package com.ndgndg91.controller;

import com.ndgndg91.auth.NaverAuthInfo;
import com.ndgndg91.service.NaverService;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.*;

@Log4j
@Controller
public class NaverController {

    private NaverService naverService;
    private NaverAuthInfo naverAuthInfo;

    public NaverController(NaverService naverService, NaverAuthInfo naverAuthInfo){
        this.naverService = naverService;
        this.naverAuthInfo = naverAuthInfo;
    }

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
                .append(naverAuthInfo.getAccessTokenUrl())
                .append("&code=").append(code)
                .append("&state=").append(state);
        try {
            HttpURLConnection con = naverService.getNaverAccessTokenHttpUrlConnection(apiURL.toString());
            int responseCode = con.getResponseCode();
            BufferedReader br = naverService.getBufferedReader(responseCode, con);
            StringBuilder res = naverService.getResponseToStringBuilder(br);
            br.close();
            if (responseCode == 200) {
                log.info(res.toString());
                naverService.parseAccessToken(res.toString(), session, model);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }


}
