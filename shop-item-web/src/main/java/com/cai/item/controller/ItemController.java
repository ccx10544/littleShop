package com.cai.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cai.item.pojo.Item;
import com.cai.pojo.TbItem;
import com.cai.pojo.TbItemDesc;
import com.cai.service.ItemService;

/**
 * @ClassName ItemController
 * @Description 商品详情页面controller
 * @author Vergil
 * @date 2018年11月11日 上午9:42:58
 * @version V1.0
 * @Copyright 2018 公司名 Inc. All rights reserved
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;

	@RequestMapping("/item/{itemId}")
	public String showItemInfo(@PathVariable Long itemId, Model model) {
		TbItem tbItem = itemService.getItemById(itemId);
		Item item = new Item(tbItem);
		TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", tbItemDesc);
		return "item";
	}
}
