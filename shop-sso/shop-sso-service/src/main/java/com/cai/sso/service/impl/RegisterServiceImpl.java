package com.cai.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.cai.common.pojo.ShopResult;
import com.cai.mapper.TbUserMapper;
import com.cai.pojo.TbUser;
import com.cai.pojo.TbUserExample;
import com.cai.pojo.TbUserExample.Criteria;
import com.cai.sso.service.RegisterService;

/**
 * @ClassName RegisterServiceImpl
 * @Description 用户注册service
 * @author Vergil
 * @date 2018年11月12日 上午10:19:51
 * @version V1.0
 * @Copyright 2018 公司名 Inc. All rights reserved
 */
@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private TbUserMapper userMapper;

	@Override
	public ShopResult checkData(String param, Integer type) {
		// 创建查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		// 对数据进行校验：1、2、3分别代表username、phone、email
		switch (type) {
		// 用户名校验
		case 1:
			criteria.andUsernameEqualTo(param);
			break;
		// 电话校验
		case 2:
			criteria.andPhoneEqualTo(param);
			break;
		// email校验
		case 3:
			criteria.andEmailEqualTo(param);
			break;
		default:
			return ShopResult.build(400, "数据类型错误");
		}
		// 执行查询
		List<TbUser> list = userMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return ShopResult.ok(true);
		}
		return ShopResult.ok(false);
	}

	@Override
	public ShopResult createUser(TbUser user) {
		// 2.校验数据
		// 2.1 校验用户名和密码不能为空
		if (StringUtils.isEmpty(user.getUsername())) {
			return ShopResult.build(400, "注册失败. 请校验数据后请再提交数据");
		}
		if (StringUtils.isEmpty(user.getPassword())) {
			return ShopResult.build(400, "注册失败. 请校验数据后请再提交数据");
		}
		// 2.2 校验用户名是否被注册了
		ShopResult result = checkData(user.getUsername(), 1);
		if (!(boolean) result.getData()) {
			// 数据不可用
			return ShopResult.build(400, "注册失败. 请校验数据后请再提交数据");
		}
		// 2.3 校验电话号码是否被注册了
		if (StringUtils.isNotBlank(user.getPhone())) {
			ShopResult result2 = checkData(user.getPhone(), 2);
			if (!(boolean) result2.getData()) {
				// 数据不可用
				return ShopResult.build(400, "注册失败. 请校验数据后请再提交数据");
			}
		}
		// 2.4 校验email是否被注册了
		if (StringUtils.isNotBlank(user.getEmail())) {
			ShopResult result2 = checkData(user.getEmail(), 3);
			if (!(boolean) result2.getData()) {
				// 数据不可用
				return ShopResult.build(400, "注册失败. 请校验数据后请再提交数据");
			}
		}
		user.setCreated(new Date());
		user.setUpdated(new Date());
		// DigestUtils工具类,是spring自带的md5加密的工具类使用md5DigestAsHex方法进行md5加密
		String digestAsHex = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(digestAsHex);
		userMapper.insertSelective(user);
		return ShopResult.ok();
	}

}
