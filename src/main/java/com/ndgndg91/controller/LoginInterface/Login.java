package com.ndgndg91.controller.LoginInterface;

import com.ndgndg91.model.MemberDTO;

import javax.servlet.http.HttpSession;

public interface Login {
    default void setMemberToSession(MemberDTO loginMember, HttpSession session){
        loginMember.makePwBlank();
        session.setAttribute("loginUserInfo", loginMember);
    }
}
