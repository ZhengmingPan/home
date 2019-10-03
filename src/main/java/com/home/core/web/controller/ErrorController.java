package com.home.core.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 错误页面
 * @author Zhengming Pan
 *
 */
@Controller
@RequestMapping("/error")
public class ErrorController extends BaseController {

	@GetMapping("{errorcode}")
	public String errorPage(@PathVariable Integer errorCode) {
		return renderTemplate(String.valueOf(errorCode));
	}
	
}
