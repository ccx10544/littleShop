package com.cai.cart.service;

import java.util.List;

import com.cai.common.pojo.ShopResult;
import com.cai.pojo.TbItem;

public interface CartService {
	/**
	 * @Title addCart
	 * @Description 添加商品购物车
	 * @param userId
	 * @param itemId
	 * @return ShopResult
	 */
	ShopResult addCart(Long userId, Long itemId, Integer num);

	/**
	 * @Title mergeCart
	 * @Description 合并购物车
	 * @param userId
	 * @param itmeList
	 * @return ShopResult
	 */
	ShopResult mergeCart(Long userId, List<TbItem> itmeList);

	/**
	 * @Title getCartList
	 * @Description 获取购物车列表
	 * @param userId
	 * @return List<TbItem>
	 */
	List<TbItem> getCartList(Long userId);

	/**
	 * @Title updateCartNum
	 * @Description 更新购物车商品数量
	 * @param userId
	 * @param itemId
	 * @param num
	 * @return ShopResult
	 */
	ShopResult updateCartNum(Long userId, Long itemId, Integer num);

	/**
	 * @Title deleteCartItem
	 * @Description 删除购物车中的商品
	 * @param userId
	 * @param itemId
	 * @return ShopResult
	 */
	ShopResult deleteCartItem(Long userId, Long itemId);

	/**
	 * @Title clearCart
	 * @Description 清空购物车
	 * @param userId
	 * @return ShopResult
	 */
	ShopResult clearCart(Long userId);
}
