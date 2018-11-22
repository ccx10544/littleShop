package com.cai.sso.service;

import com.cai.common.pojo.ShopResult;

/**
 * @ClassName LoginService
 * @Description 用户登录操作
 * @author Vergil
 * @date 2018年11月12日 上午11:28:16
 * @version V1.0
 * @Copyright 2018 公司名 Inc. All rights reserved
 */
public interface LoginService {
	ShopResult userLogin(String username, String password);
}
