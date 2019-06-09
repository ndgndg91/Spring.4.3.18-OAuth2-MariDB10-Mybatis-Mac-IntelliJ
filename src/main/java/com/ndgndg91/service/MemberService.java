package com.ndgndg91.service;

import com.ndgndg91.common.FileUtils;
import com.ndgndg91.dao.MemberDao;
import com.ndgndg91.model.FriendDTO;
import com.ndgndg91.model.MemberDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class MemberService {

    private MemberDao memberDao;
    public MemberService(MemberDao memberDao){
        this.memberDao = memberDao;
    }

    public List<MemberDTO> selectMemberListExceptMe(String id){ return memberDao.selectMemberListExceptMe(id);}

    public List<MemberDTO> selectApplicantMemberListForMe(String id){ return memberDao.selectApplicantMemberListForMe(id);}

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

    public void applyFriend(FriendDTO friendDTO){
        memberDao.applyFriend(friendDTO);
    }

    @Transactional
    public String acceptFriend(FriendDTO friendDTO){
        try {
            memberDao.acceptFriendFirst(friendDTO);
            memberDao.acceptFriendSecond(friendDTO);
        } catch (Exception e){
            return "친구 추가 실패";
        }
        return "친구 추가 성공";
    }

    @Transactional
    public void joinMember(MemberDTO memberDTO, String imageFilePath, MultipartFile multipartFile){
        FileUtils.makeDirectory(imageFilePath);
        FileUtils.uploadImageOfMember(imageFilePath, multipartFile);
        memberDao.joinMember(memberDTO);
    }
}

