package com.cai.service;

import com.cai.common.pojo.EUDataGridResult;
import com.cai.common.pojo.ShopResult;
import com.cai.pojo.TbItem;
import com.cai.pojo.TbItemDesc;

/**
 * @ClassName ItemService
 * @Description 商品操作service
 * @author Vergil
 * @date 2018年11月11日 上午9:28:16
 * @version V1.0
 * @Copyright 2018 公司名 Inc. All rights reserved
 */
public interface ItemService {

	/**
	 * @Title getItemList
	 * @Description 获取所有商品
	 * @param page
	 * @param rows
	 * @return EUDataGridResult
	 */
	EUDataGridResult getItemList(Integer page, Integer rows);

	/**
	 * @Title createItem
	 * @Description 增加商品
	 * @param item
	 * @param desc
	 * @param itemParams
	 * @return TaotaoResult
	 * @throws Exception
	 */
	ShopResult createItem(TbItem item, String desc, String itemParams) throws Exception;

	/**
	 * @Title getItemById
	 * @Description 根据商品id获取商品信息
	 * @param itemId
	 * @return TbItem
	 */
	TbItem getItemById(Long itemId);

	/**
	 * @Title getItemDescById
	 * @Description 根据商品id获取商品详情信息
	 * @param itemId
	 * @return TbItemDesc
	 */
	TbItemDesc getItemDescById(Long itemId);
}
