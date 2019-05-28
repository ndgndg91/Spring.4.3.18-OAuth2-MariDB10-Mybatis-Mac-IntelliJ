package com.ndgndg91.service;

import com.ndgndg91.dao.MemberDao;
import com.ndgndg91.model.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberDao memberDao;

    public List<Object> selectMemberListExceptMe(String id){ return memberDao.selectMemberListExceptMe(id);}

    public MemberDTO selectOneMemberById(String id){
        return memberDao.selectOneMemberById(id);
    }

    public MemberDTO selectOneMeberByEmail(String email){
        return memberDao.selectOneMemberByEmail(email);
    }

    public void insertMemberAllParameters(MemberDTO memberDTO) {
        memberDao.insertMemberAllParameters(memberDTO);
    }

    public void insertMemberExcludePwParameter(MemberDTO memberDTO) {
        memberDao.insertMemberExcludePwParameter(memberDTO);
    }
}
