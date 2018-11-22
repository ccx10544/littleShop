package com.cai.order.pojo;

import java.io.Serializable;
import java.util.List;

import com.cai.pojo.TbOrder;
import com.cai.pojo.TbOrderItem;
import com.cai.pojo.TbOrderShipping;

/**
 * @ClassName OrderInfo
 * @Description 订单pojo
 * @author Vergil
 * @date 2018年11月12日 下午10:30:58
 * @version V1.0
 * @Copyright 2018 公司名 Inc. All rights reserved
 */
public class OrderInfo extends TbOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<TbOrderItem> orderItems;

	private TbOrderShipping orderShipping;

	public List<TbOrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<TbOrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public TbOrderShipping getOrderShipping() {
		return orderShipping;
	}

	public void setOrderShipping(TbOrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}
}
