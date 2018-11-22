package com.cai.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cai.common.jedis.JedisClient;
import com.cai.common.pojo.ShopResult;
import com.cai.mapper.TbOrderItemMapper;
import com.cai.mapper.TbOrderMapper;
import com.cai.mapper.TbOrderShippingMapper;
import com.cai.order.pojo.OrderInfo;
import com.cai.order.service.OrderService;
import com.cai.pojo.TbOrderItem;
import com.cai.pojo.TbOrderShipping;

/**
 * @ClassName OrderServiceImpl
 * @Description 订单处理服务
 * @author Vergil
 * @date 2018年11月12日 下午10:33:13
 * @version V1.0
 * @Copyright 2018 公司名 Inc. All rights reserved
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;

	@Autowired
	private TbOrderItemMapper orderItemMapper;

	@Autowired
	private TbOrderShippingMapper orderShippingMapper;

	@Autowired
	private JedisClient jedisClient;

	@Value("${ORDER_ID_GEN_KEY}")
	private String ORDER_ID_GEN_KEY;

	@Value("${ORDER_ID_START}")
	private String ORDER_ID_START;

	@Value("${ORDER_DETAIL_ID_GEN_KEY}")
	private String ORDER_DETAIL_ID_GEN_KEY;

	@Override
	public ShopResult createOrder(OrderInfo orderInfo) {
		// 生成订单id,使用redis的incr生成
		if (!jedisClient.exists(ORDER_ID_GEN_KEY)) {
			// 若订单号不存在给一个初始值
			jedisClient.set(ORDER_ID_GEN_KEY, ORDER_ID_START);
		}
		String orderId = jedisClient.incr(ORDER_ID_GEN_KEY).toString();
		// 补全TbOrder的属性也就是补全OrderInfo的属性
		orderInfo.setOrderId(orderId);
		// 1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
		orderInfo.setStatus(1);
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		// 插入订单表
		orderMapper.insert(orderInfo);
		// 向订单明细表插入数据
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			// 生成明细id
			String orderItemId = jedisClient.incr(ORDER_DETAIL_ID_GEN_KEY).toString();
			// 补全pojo的属性
			tbOrderItem.setId(orderItemId);
			tbOrderItem.setOrderId(orderId);
			// 向明细表插入数据
			orderItemMapper.insert(tbOrderItem);
		}
		// 向订单物流表插入数据
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		orderShippingMapper.insert(orderShipping);
		// 返回ShopResult,其中包含订单号
		return ShopResult.ok(orderId);
	}

}
