package com.ndgndg91.service;

import com.ndgndg91.dao.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberDao memberDao;

    public String selectNow(){
        return memberDao.selectNow();
    }
}
