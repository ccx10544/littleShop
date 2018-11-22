package com.cai.sso.service;

import com.cai.common.pojo.ShopResult;
import com.cai.pojo.TbUser;

/**
 * @ClassName RegisterService
 * @Description 注册操作
 * @author Vergil
 * @date 2018年11月12日 上午11:07:30
 * @version V1.0
 * @Copyright 2018 公司名 Inc. All rights reserved
 */
public interface RegisterService {
	/**
	 * @Title checkData
	 * @Description 校验数据
	 * @param param
	 * @param type
	 * @return TaotaoResult
	 */
	ShopResult checkData(String param, Integer type);

	/**
	 * @Title createUser
	 * @Description 注册用户
	 * @param user
	 * @return TaotaoResult
	 */
	ShopResult createUser(TbUser user);
}
