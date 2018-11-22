package com.cai.order.service;

import com.cai.common.pojo.ShopResult;
import com.cai.order.pojo.OrderInfo;

public interface OrderService {
	ShopResult createOrder(OrderInfo orderInfo);
}
