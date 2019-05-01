package com.ndgndg91.dao;

import com.ndgndg91.model.MemberDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    @Autowired
    private SqlSession sqlSession;

    public String selectNow(){
        return sqlSession.selectOne("member.selectNow");
    }

    public MemberDTO selectOneMemberById(String id){
        return sqlSession.selectOne("member.isExistMember", id);
    }

    public void insertMemberAllParameters(MemberDTO memberDTO) {
        sqlSession.insert("member.insertMemberAllParameters", memberDTO);
    }

    public void insertMemberExcludePwParameter(MemberDTO memberDTO) {
        sqlSession.insert("member.insertMemberExcludePwParameter", memberDTO);
    }
}
