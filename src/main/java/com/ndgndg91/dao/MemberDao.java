package com.ndgndg91.dao;

import com.ndgndg91.model.FriendDTO;
import com.ndgndg91.model.MemberDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberDao {

    private SqlSession sqlSession;

    public MemberDao(SqlSession sqlSession){
        this.sqlSession = sqlSession;
    }

    public List<MemberDTO> selectMemberListExceptMe(String id){ return sqlSession.selectList("member.selectMemberListExceptMe", id);}

    public List<MemberDTO> selectApplicantMemberListForMe(String id){ return sqlSession.selectList("member.selectApplicantMemberListForMe", id);}

    public MemberDTO selectOneMemberById(String id){
        return sqlSession.selectOne("member.isExistMember", id);
    }

    public MemberDTO selectOneMemberByEmail(String email){
        return sqlSession.selectOne("member.selectOneMemberByEmail", email);
    }

    public void insertMemberAllParameters(MemberDTO memberDTO) {
        sqlSession.insert("member.insertMemberAllParameters", memberDTO);
    }

    public void insertMemberExcludePwParameter(MemberDTO memberDTO) {
        sqlSession.insert("member.insertMemberExcludePwParameter", memberDTO);
    }

    public void applyFriend(FriendDTO friendDTO){
        sqlSession.insert("member.applyFriend", friendDTO);
    }

    public void acceptFriendFirst(FriendDTO friendDTO){
        sqlSession.update("member.acceptFriendFirst", friendDTO);
    }

    public void acceptFriendSecond(FriendDTO friendDTO){
        sqlSession.insert("member.acceptFriendSecond", friendDTO);
    }

    public void joinMember(MemberDTO memberDTO) { sqlSession.insert("member.joinMember", memberDTO);}
}
