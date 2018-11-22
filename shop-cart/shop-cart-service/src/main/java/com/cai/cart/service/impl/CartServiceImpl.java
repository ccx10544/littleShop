package com.cai.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cai.cart.service.CartService;
import com.cai.common.jedis.JedisClient;
import com.cai.common.pojo.ShopResult;
import com.cai.common.utils.JsonUtils;
import com.cai.mapper.TbItemMapper;
import com.cai.pojo.TbItem;

/**
 * @ClassName CartServiceImpl
 * @Description 购物车商品处理服务
 * @author Vergil
 * @date 2018年11月12日 下午4:17:08
 * @version V1.0
 * @Copyright 2018 公司名 Inc. All rights reserved
 */
@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisClient jedisClient;

	@Value("${REIDS_CART_PRE}")
	private String REIDS_CART_PRE;

	@Autowired
	private TbItemMapper itemMapper;

	@Override
	public ShopResult addCart(Long userId, Long itemId, Integer num) {
		// 向redis中添加购物车
		// 数据类型hash key:用户id filed:商品id value:商品信息
		// 判断商品是否存在
		Boolean hexists = jedisClient.hexists(REIDS_CART_PRE + ":" + userId, userId.toString());
		if (hexists) {
			// 如果存在数量相加
			String hget = jedisClient.hget(REIDS_CART_PRE + ":" + userId, userId.toString());
			// 把hget转换成TbItem
			TbItem tbItem = JsonUtils.jsonToPojo(hget, TbItem.class);
			tbItem.setNum(tbItem.getNum() + num);
			// 写回redis
			jedisClient.hset(REIDS_CART_PRE + ":" + userId, userId.toString(), JsonUtils.objectToJson(tbItem));
			return ShopResult.ok();
		}
		// 如果不存在,根据商品id取商品信息
		TbItem item = itemMapper.selectByPrimaryKey(userId);
		// 设置商品购物车数量
		item.setNum(num);
		// 取一张图片
		String image = item.getImage();
		if (StringUtils.isNotBlank(image)) {
			item.setImage(image.split(",")[0]);
		}
		// 添加到购物车列表
		jedisClient.hset(REIDS_CART_PRE + ":" + userId, userId.toString(), JsonUtils.objectToJson(item));
		// 返回成功
		return ShopResult.ok();
	}

	@Override
	public ShopResult mergeCart(Long userId, List<TbItem> itmeList) {
		// 遍历商品列表
		// 把列表添加到购物车
		// 判断购物车是否有此商品
		// 如果有,数量相加
		// 如果没有添加到购物车
		for (TbItem tbItem : itmeList) {
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		// 返回成功
		return ShopResult.ok();
	}

	@Override
	public List<TbItem> getCartList(Long userId) {
		List<String> hvals = jedisClient.hvals(REIDS_CART_PRE + ":" + userId);
		List<TbItem> itemList = new ArrayList<>();
		for (String string : hvals) {
			TbItem tbItem = JsonUtils.jsonToPojo(string, TbItem.class);
			itemList.add(tbItem);
		}
		return itemList;
	}

	@Override
	public ShopResult updateCartNum(Long userId, Long itemId, Integer num) {
		String hget = jedisClient.hget(REIDS_CART_PRE + ":" + userId, itemId.toString());
		TbItem tbItem = JsonUtils.jsonToPojo(hget, TbItem.class);
		tbItem.setNum(num);
		// 写入redis
		jedisClient.hset(REIDS_CART_PRE + ":" + userId, itemId.toString(), JsonUtils.objectToJson(tbItem));
		return ShopResult.ok();
	}

	@Override
	public ShopResult deleteCartItem(Long userId, Long itemId) {
		jedisClient.hdel(REIDS_CART_PRE + ":" + userId, itemId.toString());
		return ShopResult.ok();
	}

	@Override
	public ShopResult clearCart(Long userId) {
		// 删除购物车信息
		jedisClient.del(REIDS_CART_PRE + ":" + userId);
		return ShopResult.ok();
	}

}
