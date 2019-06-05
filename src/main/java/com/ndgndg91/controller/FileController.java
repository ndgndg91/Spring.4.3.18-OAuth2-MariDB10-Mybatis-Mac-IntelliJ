package com.ndgndg91.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Log4j
@Controller
public class FileController {

    @PostMapping("/upload/imgFile")
    public void uploadImageFile(MultipartHttpServletRequest request){
        log.info(request.getParameter(""));
    }
}
