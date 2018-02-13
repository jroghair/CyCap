package com.cycapservers.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomepageController {
    @GetMapping("/")
	//@RequestMapping(value = "/")
    public String homepage() {
        return "homepage";
    }
}
