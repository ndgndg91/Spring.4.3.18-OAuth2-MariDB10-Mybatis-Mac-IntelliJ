package com.ndgndg91.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.ndgndg91.controller.LoginInterface.Login;
import com.ndgndg91.model.ButtonVO;
import com.ndgndg91.model.MemberDTO;
import com.ndgndg91.service.KaKaoService;
import com.ndgndg91.service.MemberService;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

@Log4j
@Controller
public class KaKaoController implements Login {

    private MemberService memberService;
    private KaKaoService kaKaoService;


    public KaKaoController(MemberService memberService, KaKaoService kaKaoService){
        this.memberService = memberService;
        this.kaKaoService = kaKaoService;
    }

    @RequestMapping(value = "/auth/kakao/redirect")
    public String getKakaoSignIn(@RequestParam("code") String code, HttpSession session) throws IOException {

        JsonNode userInfo = kaKaoService.getKakaoUserInfo(code, session);
        MemberDTO kakaoUserInfo = kaKaoService.parseKakaoJson(userInfo);
        Optional<MemberDTO> optionalMember = Optional.ofNullable(memberService.selectOneMemberById(kakaoUserInfo.getId()));
        if (!optionalMember.isPresent()) {
            memberService.insertMemberExcludePwParameter(kakaoUserInfo);
            setMemberToSession(kakaoUserInfo, session);
            return "redirect:/";
        }

        if (optionalMember.get().isPictureDifferent(kakaoUserInfo))
            memberService.updateMember(kakaoUserInfo);
        setMemberToSession(kakaoUserInfo, session);
        Iterator iterator = userInfo.iterator();
        kaKaoService.printIterator(iterator);
        return "redirect:/";
    }


    @RequestMapping(value = "/auth/kakao/logout")
    public String kakaoLogout(HttpSession session) throws IOException {
        log.info("kakao Logout");
        String KaKaoAccessToken = (String) session.getAttribute("KaKaoAccessToken");
        session.removeAttribute("KaKaoAccessToken");
        session.removeAttribute("loginUserInfo");
        session.invalidate();
        log.info("accessToken : " + KaKaoAccessToken);
        JsonNode afterLogout = kaKaoService.kakaoLogout(KaKaoAccessToken);
        Iterator iterator = afterLogout.iterator();
        kaKaoService.printIterator(iterator);
        return "redirect:/";
    }



    @ResponseBody
    @RequestMapping(value = "/keyboard", method = RequestMethod.GET)
    public ButtonVO keyboard() {
        return new ButtonVO(new String[]{"test1", "test2", "test3"});
    }
}
