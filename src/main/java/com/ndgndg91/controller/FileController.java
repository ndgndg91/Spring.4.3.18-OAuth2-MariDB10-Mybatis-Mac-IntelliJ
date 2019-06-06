package com.ndgndg91.controller;

import com.ndgndg91.model.MemberDTO;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import java.io.*;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ndgndg91.model.enums.LoginType.DEFAULT;

@Log4j
@Controller
public class FileController {
    private static final Pattern notStringPattern = Pattern.compile("([^a-zA-z])");

    @PostMapping("/upload/imgFile")
    public void uploadImageFile(MultipartHttpServletRequest request, HttpServletRequest servletRequest) throws IOException {
        String email = request.getParameter("uEmail");
        String uPassword = request.getParameter("uPassword");
        String uRealName = request.getParameter("uRealName");
        String uNick = request.getParameter("uNick");
        String uBirth = request.getParameter("uBirth");
        String uGender = request.getParameter("uGender");
        MultipartFile multipartFile = request.getFile("uPicture");


        Matcher notStringMatcher = notStringPattern.matcher(email);
        String prefixId = LocalDate.now().toString().replaceAll("-", "");
        String suffixId = notStringMatcher.replaceAll("");
        String memberId = prefixId + suffixId;
        log.info(new MemberDTO.Builder(memberId, email).pw(uPassword).loginType(DEFAULT.toString()).realName(uRealName)
                .nick(uNick).birth(uBirth).gender(uGender).build().toString());


        log.info("@@@@@@@@@@@@@@@@");
        log.info(email);
        log.info(uPassword);
        log.info(uRealName);
        log.info(uNick);
        log.info(uBirth);
        log.info(uGender);
        log.info(multipartFile);

        String rootPath = servletRequest.getSession().getServletContext().getRealPath("/");
        String imgFilePath = rootPath + "upload/" + memberId + "/";
        File folder = new File(imgFilePath);
        if(folder.mkdirs())
            log.info("폴더 생성 완료!");
        log.info(rootPath);
        log.info(imgFilePath);


        File file = convert(imgFilePath, multipartFile);
//        if(file.mkdirs())
//            log.info("폴더 생성 완료");
    }

    private File convert(String path, MultipartFile file) throws IOException {
        File conFile = new File(path, file.getOriginalFilename());
        conFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(conFile);
        fos.write(file.getBytes());
        fos.close();
        return conFile;
    }
}
