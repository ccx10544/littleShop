package com.cai.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cai.common.pojo.ShopResult;
import com.cai.common.utils.ExceptionUtil;
import com.cai.sso.service.TokenService;

/**
 * @ClassName TokenController
 * @Description 根据token查询用户信息
 * @author Vergil
 * @date 2018年11月12日 下午12:38:34
 * @version V1.0
 * @Copyright 2018 公司名 Inc. All rights reserved
 */
@Controller
public class TokenController {

	@Autowired
	private TokenService tokenService;

	@RequestMapping("/user/token/{token}")
	@ResponseBody
	public Object userLogin(@PathVariable String token, String callback) {
		ShopResult shopResult = null;
		try {
			shopResult = tokenService.getUserByToken(token);
		} catch (Exception e) {
			e.printStackTrace();
			shopResult = ShopResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		// 判断是否为jsonp调用
		if (StringUtils.isBlank(callback)) {
			return shopResult;
		} else {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(shopResult);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
	}
}
