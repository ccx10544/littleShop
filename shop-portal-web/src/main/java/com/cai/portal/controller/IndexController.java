package com.cai.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cai.content.service.ContentService;
import com.cai.pojo.TbContent;

@Controller
public class IndexController {

	@Autowired
	private ContentService contentService;

	@Value("${CONTENT_LUNBO_ID}")
	private Long CONTENT_LUNBO_ID;

	@RequestMapping("/index")
	public String showIndex(Model model) {
		// 查询内容列表
		List<TbContent> list = contentService.getContentList(CONTENT_LUNBO_ID);
		model.addAttribute("ad1List", list);
		return "index";
	}
}
