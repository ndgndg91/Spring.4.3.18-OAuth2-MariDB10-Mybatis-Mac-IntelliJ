package com.ndgndg91.controller.LoginInterface;

import com.ndgndg91.model.MemberDTO;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpSession;

import static com.ndgndg91.model.enums.LoginType.*;

public interface Login {
    default void setMemberToSession(MemberDTO loginMember, HttpSession session){
        loginMember.makePwBlank();
        session.setAttribute("loginUserInfo", loginMember);
    }

    default String logoutByType(String loginType, HttpSession session){
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
}
