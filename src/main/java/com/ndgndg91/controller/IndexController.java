package com.ndgndg91.controller;

import com.ndgndg91.model.MemberDTO;
import com.ndgndg91.service.MemberService;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Log4j
@Controller
public class IndexController {

    @Autowired
    private MemberService memberService;

    @RequestMapping(value = "/")
    public String index(HttpSession session, Model model) {
        if(isLoginStatus(session))
            model.addAttribute("exceptMeMemberList",
                    memberService.selectMemberListExceptMe(
                            ((MemberDTO) session.getAttribute("loginUserInfo")).getId())
                    );
        return "index";
    }


    private boolean isLoginStatus(HttpSession session){
        return ObjectUtils.anyNotNull(session.getAttribute("loginUserInfo"));
    }
}
