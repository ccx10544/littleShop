package com.cai.search.mapper;

import java.util.List;

import com.cai.common.pojo.SearchItem;

public interface ItemMapper {
	/**
	 * @Title:getItemList
	 * @Description:获取商品列表
	 * @return List<SearchItem>
	 */
	List<SearchItem> getItemList();

	/**
	 * @Title:getItemById
	 * @Description:根据id获取商品列表
	 * @param itemId
	 * @return SearchItem
	 */
	SearchItem getItemById(Long itemId);
}
