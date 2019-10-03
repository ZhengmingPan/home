package com.home.core.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 登录页面
 */
@Controller
public class LoginController extends BaseController {
	
	public LoginController() {
		setTemplatePrefix("admin/");
	}
 

    @RequestMapping(value = "/login")
    public String login(ModelAndView modelAndView) {
        return renderTemplate("login");
    }
    
    @RequestMapping(value = "/index")
    public String index(ModelAndView modelAndView) {
        return renderTemplate("index");
    }
    
    @RequestMapping(value = "/main")
    public String main(ModelAndView modelAndView) {
        return renderTemplate("main");
    }
}
