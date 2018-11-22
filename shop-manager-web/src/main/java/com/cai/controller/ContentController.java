package com.cai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cai.common.pojo.EUDataGridResult;
import com.cai.common.pojo.ShopResult;
import com.cai.content.service.ContentService;
import com.cai.pojo.TbContent;

@Controller
@RequestMapping("/content")
public class ContentController {
	@Autowired
	private ContentService contentService;

	@RequestMapping("/query/list")
	@ResponseBody
	public EUDataGridResult selectContent(Long categoryId, Integer page, Integer rows) {
		return contentService.selectContent(categoryId, page, rows);
	}

	@RequestMapping("/save")
	@ResponseBody
	public ShopResult insertContent(TbContent content) {
		return contentService.insertContent(content);
	}
}
