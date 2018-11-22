package com.cai.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @ClassName: GlobalExceptionResolver
 * @Description:全局异常处理器
 * @author: Vergil
 * @date: 2018年11月10日 下午5:21:08
 * 
 * @Copyright: 2018 公司名 Inc. All rights reserved. 注意：。。。。。。。。。。。。。。。
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		// 打印到控制台
		ex.printStackTrace();
		// 写日志
		logger.debug("测试系统输出日志......");
		logger.info("系统发生异常。。。。。");
		logger.error("系统发生异常", ex);
		// 发邮件、发短信
		// 发邮件使用jmail工具包,发短信使用第三方webservice
		// 跳转到友好页面
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error/exception");
		return modelAndView;
	}

}
