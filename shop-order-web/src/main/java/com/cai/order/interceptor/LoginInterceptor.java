package com.cai.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cai.cart.service.CartService;
import com.cai.common.pojo.ShopResult;
import com.cai.common.utils.CookieUtils;
import com.cai.common.utils.JsonUtils;
import com.cai.pojo.TbItem;
import com.cai.pojo.TbUser;
import com.cai.sso.service.TokenService;

/**
 * @ClassName LoginInterceptor
 * @Description 用户登录拦截器
 * @author Vergil
 * @date 2018年11月12日 下午9:38:54
 * @version V1.0
 * @Copyright 2018 公司名 Inc. All rights reserved
 */
public class LoginInterceptor implements HandlerInterceptor {

	@Value("${SSO_URL}")
	private String SSO_URL;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private CartService cartService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 判断用户是否登录
		String token = CookieUtils.getCookieValue(request, "token");
		// 从cookie取token
		// 判断token是否存在
		if (StringUtils.isBlank(token)) {
			// 如果token不存在，未登录，跳转到sso登录页面.用户登录成功后,跳转到当前请求url
			response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURI());
			// 拦截
			return false;
		}
		// 如果token存在,需要调用sso系统的服务,根据token取用户信息
		ShopResult shopResult = tokenService.getUserByToken(token);
		if (shopResult.getStatus() != 200) {
			// 如果取不到,用户登录已过期,需要登录
			response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURI());
			// 拦截
			return false;
		}
		// 如果取到用户信息,需要把用户写入request
		TbUser user = (TbUser) shopResult.getData();
		request.setAttribute("user", user);
		// 判断cookie中是否有购物车数据,如果有,就合并到服务端
		String cart = CookieUtils.getCookieValue(request, "cart", true);
		if (StringUtils.isNoneBlank(cart)) {
			cartService.mergeCart(user.getId(), JsonUtils.jsonToList(cart, TbItem.class));
		}
		// 放行
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
