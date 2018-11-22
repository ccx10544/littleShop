package com.cai.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cai.common.jedis.JedisClient;
import com.cai.common.pojo.ShopResult;
import com.cai.common.utils.JsonUtils;
import com.cai.pojo.TbUser;
import com.cai.sso.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService {

	@Autowired
	private JedisClient jedisClient;

	@Value("${REDIS_USER_SESSION_KEY}")
	private String REDIS_USER_SESSION_KEY;

	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	@Override
	public ShopResult getUserByToken(String token) {
		// 根据token从redis中查询用户信息
		// REDIS_USER_SESSION_KEY + ":" + token
		String json = jedisClient.get(REDIS_USER_SESSION_KEY + ":" + token);
		if (StringUtils.isBlank(json)) {
			return ShopResult.build(400, "session已经过期,请重新登录");
		}
		// 重新重置session过期时间
		jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token, SESSION_EXPIRE);
		TbUser tbUser = JsonUtils.jsonToPojo(json, TbUser.class);
		return ShopResult.ok(tbUser);
	}

}
