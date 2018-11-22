package com.cai.content.service;

import java.util.List;

import com.cai.common.pojo.EUDataGridResult;
import com.cai.common.pojo.ShopResult;
import com.cai.pojo.TbContent;

public interface ContentService {
	/**
	 * 新增内容列表
	 * 
	 * @param content
	 * @return
	 */
	ShopResult insertContent(TbContent content);

	/**
	 * 查询内容列表
	 * 
	 * @param categoryId
	 * @param page
	 * @param rows
	 * @return
	 */
	EUDataGridResult selectContent(Long categoryId, Integer page, Integer rows);

	/**
	 * 根据内容分类id查询分类列表
	 * 
	 * @param categoryId
	 * @return
	 */
	List<TbContent> getContentList(Long contentCid);
}
