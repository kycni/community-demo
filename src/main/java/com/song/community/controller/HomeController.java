package com.song.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Kycni
 * @date 2022/2/20 7:53
 */
@Controller
public class HomeController {
    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String index () {
        
        return "/index";
    }
}
