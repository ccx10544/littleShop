package com.cai.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.cai.common.jedis.JedisClient;
import com.cai.common.pojo.ShopResult;
import com.cai.common.utils.JsonUtils;
import com.cai.mapper.TbUserMapper;
import com.cai.pojo.TbUser;
import com.cai.pojo.TbUserExample;
import com.cai.pojo.TbUserExample.Criteria;
import com.cai.sso.service.LoginService;

/**
 * @ClassName LoginServiceImpl
 * @Description 用户登录处理
 * @author Vergil
 * @date 2018年11月12日 上午11:32:17
 * @version V1.0
 * @Copyright 2018 公司名 Inc. All rights reserved
 */
@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private TbUserMapper userMapper;

	@Autowired
	private JedisClient jedisClient;

	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	@Value("${REDIS_USER_SESSION_KEY}")
	private String REDIS_USER_SESSION_KEY;

	@Override
	public ShopResult userLogin(String username, String password) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example);
		// 如果没有此用户名
		if (list == null || list.size() == 0) {
			return ShopResult.build(400, "用户名或密码错误");
		}
		TbUser user = list.get(0);
		// 对比密码
		if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
			return ShopResult.build(400, "用户名或密码错误");
		}
		// 生成token
		String token = UUID.randomUUID().toString();
		// 保存用户之前，把对象中的密码清除
		user.setPassword(null);
		// 存放用户数据到redis中，使用jedis的客户端,为了管理方便加一个前缀"kkk:token"
		jedisClient.set(REDIS_USER_SESSION_KEY + ":" + token, JsonUtils.objectToJson(user));
		// 设置过期时间 来模拟session
		jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token, SESSION_EXPIRE);
		// 返回token
		return ShopResult.ok(token);
	}

}
