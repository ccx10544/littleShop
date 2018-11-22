package com.cai.sso.service;

import com.cai.common.pojo.ShopResult;

/**
 * @Title getUserByToken
 * @Description 根据token操作
 * @param token
 * @return ShopResult
 */
public interface TokenService {
	ShopResult getUserByToken(String token);
}
