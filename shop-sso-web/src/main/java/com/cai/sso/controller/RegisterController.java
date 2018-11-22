package com.cai.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cai.common.pojo.ShopResult;
import com.cai.common.utils.ExceptionUtil;
import com.cai.pojo.TbUser;
import com.cai.sso.service.RegisterService;

/**
 * @ClassName RegisterController
 * @Description 注册功能
 * @author Vergil
 * @date 2018年11月12日 上午10:09:01
 * @version V1.0
 * @Copyright 2018 公司名 Inc. All rights reserved
 */
@Controller
public class RegisterController {

	@Autowired
	private RegisterService registerService;

	@RequestMapping("/page/register")
	public String showRegister() {
		return "register";
	}

	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public ShopResult checkData(@PathVariable String param, @PathVariable Integer type) {
		ShopResult taotaoResult = registerService.checkData(param, type);
		return taotaoResult;
	}

	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	@ResponseBody
	public ShopResult createUser(TbUser user) {
		try {
			ShopResult taotaoResult = registerService.createUser(user);
			return taotaoResult;
		} catch (Exception e) {
			e.printStackTrace();
			return ShopResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
}
