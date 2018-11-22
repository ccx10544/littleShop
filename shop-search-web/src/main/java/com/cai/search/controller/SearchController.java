package com.cai.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cai.common.pojo.SearchResult;
import com.cai.search.service.SearchService;

@Controller
public class SearchController {
	@Autowired
	private SearchService searchService;

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(@RequestParam("keyword") String queryString, @RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "60") Integer rows, Model model) {
		try {
			if (queryString != null) {
				try {
					queryString = new String(queryString.getBytes("iso8859-1"), "utf-8");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			SearchResult result = searchService.search(queryString, page, rows);
			model.addAttribute("query", queryString);
			model.addAttribute("totalPages", result.getPageCount());// 总页数
			model.addAttribute("itemList", result.getItemList());
			model.addAttribute("page", page);
			return "search";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
