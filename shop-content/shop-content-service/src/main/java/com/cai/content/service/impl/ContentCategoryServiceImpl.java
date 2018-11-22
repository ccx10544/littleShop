package com.cai.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cai.common.pojo.EUTreeNode;
import com.cai.common.pojo.ShopResult;
import com.cai.content.service.ContentCategoryService;
import com.cai.mapper.TbContentCategoryMapper;
import com.cai.pojo.TbContentCategory;
import com.cai.pojo.TbContentCategoryExample;
import com.cai.pojo.TbContentCategoryExample.Criteria;

/**
 * 内容分类管理service
 * 
 * @author Administrator
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;

	@Override
	public List<EUTreeNode> getCategoryList(Long parentId) {
		// 根据parentid查询节点列表
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		// 执行查询
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		List<EUTreeNode> resultList = new ArrayList<>();
		for (TbContentCategory tbContentCategory : list) {
			// 创建一个节点
			EUTreeNode node = new EUTreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent() ? "closed" : "open");
			resultList.add(node);
		}
		return resultList;
	}

	@Override
	public ShopResult insertContentCategory(Long parentId, String name) {
		// 创建一个pojo
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setName(name);
		contentCategory.setIsParent(false);
		// '状态。可选值:1(正常),2(删除)',
		contentCategory.setStatus(1);
		contentCategory.setParentId(parentId);
		contentCategory.setSortOrder(1);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		// 添加记录
		contentCategoryMapper.insert(contentCategory);
		// 查看父节点的isParent列是否为true，如果不是true改成true
		TbContentCategory parentCat = contentCategoryMapper.selectByPrimaryKey(parentId);
		// 判断是否为true
		if (!parentCat.getIsParent()) {
			parentCat.setIsParent(true);
			// 更新父节点
			contentCategoryMapper.updateByPrimaryKey(parentCat);
		}
		// 返回结果
		return ShopResult.ok(contentCategory);
	}

	@Override
	public ShopResult deleteContentCategory(Long id) {
		deleteContentCategoryList(id);
		return ShopResult.ok();
	}

	/**
	 * 获取父节点下的所有子节点
	 * 
	 * @param parentId
	 * @return
	 */
	private List<TbContentCategory> getAllChildNodeList(Long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		return contentCategoryMapper.selectByExample(example);
	}

	/**
	 * 递归删除节点
	 * 
	 * @param parentId
	 * @param id
	 */
	public void deleteContentCategoryList(Long id) {
		System.out.println(id);
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		// 判断是否为父节点
		if (contentCategory.getIsParent()) {
			List<TbContentCategory> childNodeList = getAllChildNodeList(id);
			// 删除所有该孩子节点
			for (TbContentCategory tbContentCategory : childNodeList) {
				deleteContentCategoryList(tbContentCategory.getId());
			}
		}
		// 判断父节点下是否还有其他子节点
		if (getAllChildNodeList(contentCategory.getParentId()).size() == 1) {
			// 没有则将父节点标记为子节点
			TbContentCategory selectByPrimaryKey = contentCategoryMapper
					.selectByPrimaryKey(contentCategory.getParentId());
			selectByPrimaryKey.setIsParent(false);
			contentCategoryMapper.updateByPrimaryKey(selectByPrimaryKey);
		}
		// 删除本节点
		contentCategoryMapper.deleteByPrimaryKey(id);
	}

	@Override
	public ShopResult ReNameContentCategory(Long id, String name) {
		TbContentCategory tbContentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		tbContentCategory.setName(name);
		contentCategoryMapper.updateByPrimaryKey(tbContentCategory);
		return ShopResult.ok();
	}

}
