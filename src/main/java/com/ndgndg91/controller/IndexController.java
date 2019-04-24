package com.ndgndg91.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j
@Controller
public class IndexController {

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }
}
