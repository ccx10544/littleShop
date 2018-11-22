package com.cai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cai.common.pojo.EUDataGridResult;
import com.cai.common.pojo.ShopResult;
import com.cai.pojo.TbItem;
import com.cai.service.ItemService;

@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;

	@RequestMapping("/item/list")
	@ResponseBody
	public EUDataGridResult getItemList(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "30") Integer rows) {
		return itemService.getItemList(page, rows);
	}

	@RequestMapping(value = "/item/save", method = RequestMethod.POST)
	@ResponseBody
	private ShopResult createItem(TbItem item, String desc, String itemParams) {
		try {
			return itemService.createItem(item, desc, itemParams);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
