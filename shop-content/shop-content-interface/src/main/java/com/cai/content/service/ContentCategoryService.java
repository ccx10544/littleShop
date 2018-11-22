package com.cai.content.service;

import java.util.List;

import com.cai.common.pojo.EUTreeNode;
import com.cai.common.pojo.ShopResult;

public interface ContentCategoryService {
	/**
	 * 获取商品内容
	 * 
	 * @param parentId
	 * @return
	 */
	List<EUTreeNode> getCategoryList(Long parentId);

	/**
	 * 插入商品内容
	 * 
	 * @param parentId
	 * @param name
	 * @return
	 */
	ShopResult insertContentCategory(Long parentId, String name);

	/**
	 * 删除商品内容
	 * 
	 * @param id
	 * @return
	 */
	ShopResult deleteContentCategory(Long id);

	/**
	 * 重命名商品内容
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	ShopResult ReNameContentCategory(Long id, String name);
}
