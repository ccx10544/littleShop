package com.cai.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cai.common.pojo.EUTreeNode;
import com.cai.common.pojo.ShopResult;
import com.cai.content.service.ContentCategoryService;

/**
 * 内容controller
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {
	@Autowired
	private ContentCategoryService contentCategoryService;

	@RequestMapping("/list")
	@ResponseBody
	public List<EUTreeNode> getCategoryList(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
		return contentCategoryService.getCategoryList(parentId);
	}

	@RequestMapping("/create")
	@ResponseBody
	public ShopResult createContentCategory(Long parentId, String name) {
		return contentCategoryService.insertContentCategory(parentId, name);
	}

	@RequestMapping("/delete")
	@ResponseBody
	public ShopResult deleteContentCategory(Long id) {
		return contentCategoryService.deleteContentCategory(id);
	}

	@RequestMapping("/update")
	@ResponseBody
	public ShopResult ReNameContentCategory(Long id, String name) {
		return contentCategoryService.ReNameContentCategory(id, name);
	}

}
