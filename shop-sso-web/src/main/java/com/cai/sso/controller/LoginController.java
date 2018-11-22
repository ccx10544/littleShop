package com.cai.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cai.common.pojo.ShopResult;
import com.cai.common.utils.CookieUtils;
import com.cai.sso.service.LoginService;

/**
 * @ClassName LoginController
 * @Description 用户登录处理
 * @author Vergil
 * @date 2018年11月12日 上午11:21:10
 * @version V1.0
 * @Copyright 2018 公司名 Inc. All rights reserved
 */
@Controller
public class LoginController {

	@Autowired
	private LoginService loginService;

	@Value("${TT_TOKEN_KEY}")
	private String TT_TOKEN_KEY;

	@RequestMapping("/page/login")
	public String showLogin(String redirect, Model model) {
		model.addAttribute("redirect", redirect);
		return "login";
	}

	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	@ResponseBody
	public ShopResult userLogin(HttpServletRequest request, HttpServletResponse response, String username,
			String password) {
		ShopResult shopResult = loginService.userLogin(username, password);
		// 判断是否登录成功
		if (shopResult.getStatus() == 200) {
			// 设置token到cookie中 可以使用 工具类 cookie需要跨域
			CookieUtils.setCookie(request, response, TT_TOKEN_KEY, shopResult.getData().toString());
		}
		return shopResult;
	}
}
