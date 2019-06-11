package com.ndgndg91.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndgndg91.common.FileUtils;
import com.ndgndg91.controller.LoginInterface.Login;
import com.ndgndg91.dao.MemberDao;
import com.ndgndg91.model.FriendDTO;
import com.ndgndg91.model.MemberDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

import static com.ndgndg91.controller.LoginInterface.LoginProcess.*;

@Service
public class MemberService implements Login {
    private static final ObjectMapper mapper = new ObjectMapper();
    private MemberDao memberDao;
    public MemberService(MemberDao memberDao){
        this.memberDao = memberDao;
    }

    public List<MemberDTO> selectMemberListExceptMe(String id){ return memberDao.selectMemberListExceptMe(id);}

    public ResponseEntity selectApplicantMemberListForMe(String id) throws JsonProcessingException {
        List<MemberDTO> applicantList = memberDao.selectApplicantMemberListForMe(id);
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<>(mapper.writeValueAsString(applicantList), resHeaders, HttpStatus.CREATED);
    }

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

    public void updateMember(MemberDTO memberDTO){
        memberDao.updateMember(memberDTO);
    }

    public ResponseEntity loginProcess(String email, String password, HttpSession session) throws JsonProcessingException {
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");
        Optional<MemberDTO> optionalMemberDTO = Optional.ofNullable(this.selectOneMeberByEmail(email));
        if (!optionalMemberDTO.isPresent())
            return new ResponseEntity<>(mapper.writeValueAsString(NOT_EXIST_EMAIL.toString()), resHeaders, HttpStatus.CREATED);
        if (!StringUtils.equals(optionalMemberDTO.get().getPw(), password))
            return new ResponseEntity<>(mapper.writeValueAsString(INCORRECT_PASSWORD.toString()), resHeaders, HttpStatus.CREATED);

        setMemberToSession(optionalMemberDTO.get(), session);
        return new ResponseEntity<>(mapper.writeValueAsString(LOGIN_SUCCESS.toString()), resHeaders, HttpStatus.CREATED);
    }

    public ResponseEntity applyFriend(FriendDTO friendDTO) throws JsonProcessingException {
        String returnString = "친구 신청 성공";
        try {
            memberDao.applyFriend(friendDTO);
        } catch (Exception e) {
            e.printStackTrace();
            returnString = "친구 신청 실패";
        }
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<>(mapper.writeValueAsString(returnString), resHeaders, HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity acceptFriend(FriendDTO friendDTO) throws JsonProcessingException {
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");
        String result = "친구 추가 성공";
        try {
            memberDao.acceptFriendFirst(friendDTO);
            memberDao.acceptFriendSecond(friendDTO);
        } catch (Exception e){
            result = "친구 추가 실패";
        }
        return new ResponseEntity<>(mapper.writeValueAsString(result), resHeaders, HttpStatus.CREATED);
    }

    @Transactional
    public void joinMember(MemberDTO memberDTO, String imageFilePath, MultipartFile multipartFile){
        FileUtils.makeDirectory(imageFilePath);
        FileUtils.uploadImageOfMember(imageFilePath, multipartFile);
        memberDao.joinMember(memberDTO);
    }
}

