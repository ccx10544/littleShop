package com.cai.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cai.common.pojo.ShopResult;
import com.cai.common.utils.CookieUtils;
import com.cai.pojo.TbUser;
import com.cai.sso.service.TokenService;

/**
 * @ClassName LoginInterceptor
 * @Description 用户登录处理拦截器
 * @author Vergil
 * @date 2018年11月12日 下午3:59:09
 * @version V1.0
 * @Copyright 2018 公司名 Inc. All rights reserved
 */
public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private TokenService tokenService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 在Handler执行之前执行
		// 返回值决定handlerInterceptor是否执行
		// 在Handler执行之前处理
		// 判断用户是否登录
		// 从cookie中取token
		String token = CookieUtils.getCookieValue(request, "token");
		// 根据token换取用户信息，调用sso系统的接口。
		if (StringUtils.isBlank(token)) {
			// 取不到用户信息
			return true;
		}
		ShopResult shopResult = tokenService.getUserByToken(token);
		if (shopResult.getStatus() != 200) {
			return true;
		}
		// 取到用户信息，放行
		TbUser user = (TbUser) shopResult.getData();
		// 把用户信息放入Request
		request.setAttribute("user", user);
		// 返回值决定handler是否执行。true：执行，false：不执行。
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// handler执行之后,返回ModelAndView之前
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 返回ModelAndView之后,响应用户之后
	}

}
