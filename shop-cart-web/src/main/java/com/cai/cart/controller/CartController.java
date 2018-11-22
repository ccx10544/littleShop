package com.cai.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cai.cart.service.CartService;
import com.cai.common.pojo.ShopResult;
import com.cai.common.utils.CookieUtils;
import com.cai.common.utils.JsonUtils;
import com.cai.pojo.TbItem;
import com.cai.pojo.TbUser;
import com.cai.service.ItemService;

/**
 * @ClassName CartController
 * @Description 购物车处理
 * @author Vergil
 * @date 2018年11月12日 下午2:59:52
 * @version V1.0
 * @Copyright 2018 公司名 Inc. All rights reserved
 */
@Controller
public class CartController {

	@Autowired
	private ItemService itemService;

	@Value("${COOKIE_CART_EXPIRE}")
	private Integer COOKIE_CART_EXPIRE;

	@Autowired
	private CartService cartService;

	@RequestMapping("/cart/add/{itemId}")
	public String addCart(HttpServletResponse response, HttpServletRequest request, @PathVariable Long itemId,
			@RequestParam(defaultValue = "1") Integer num) {
		// 判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			// 保存到服务端
			cartService.addCart(user.getId(), itemId, num);
			return "cartSuccess";
		}
		// 如果是登录状态,把购物车写入redis
		// 如果未登录使用用cookie
		// 从cookie中取购物车列表
		List<TbItem> listFromCookie = getCartListFromCookie(request);
		boolean flag = false;
		// 判断商品在商品列表中是否存在
		for (TbItem tbItem : listFromCookie) {
			// 如果存在此商品
			if (tbItem.getId() == itemId.longValue()) {
				flag = true;
				// 增加商品数量
				tbItem.setNum(tbItem.getNum() + num);
				break;
			}
		}
		// 如果存在数量相加
		// 如果不存在,根据商品id查询商品信息。得到一个TbItem对象
		if (!flag) {
			TbItem tbItem = itemService.getItemById(itemId);
			// 设置商品数量
			tbItem.setNum(num);
			// 取一张图片
			String image = tbItem.getImage();
			if (StringUtils.isNotBlank(image)) {
				String[] split = image.split(",");
				tbItem.setImage(split[0]);
			}
			// 把商品添加到商品列表
			listFromCookie.add(tbItem);
		}
		// 写入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(listFromCookie), COOKIE_CART_EXPIRE,
				true);
		// 返回添加成页面
		return "cartSuccess";
	}

	/**
	 * @Title getCartListFromCookie
	 * @Description 从cookie取购物车列表
	 * @param request
	 * @return List<TbItem>
	 */
	private List<TbItem> getCartListFromCookie(HttpServletRequest request) {
		String cartJson = CookieUtils.getCookieValue(request, "cart", true);
		if (StringUtils.isBlank(cartJson)) {
			return new ArrayList<>();
		}
		// 把json转换成商品列表
		try {
			List<TbItem> list = JsonUtils.jsonToList(cartJson, TbItem.class);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	/**
	 * @Title showCart
	 * @Description 展示购物车列表
	 * @param request
	 * @return String
	 */
	@RequestMapping("/cart/cart")
	public String showCart(HttpServletRequest request, HttpServletResponse response) {
		// 判断用户是否登录
		// 如果是登录状态
		// 从cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			// 查看本地cookie取购物车列表,如果不为空,把cookie中的商品和redis中的购物车商品合并
			// 从redis取购物车列表
			cartService.mergeCart(user.getId(), cartList);
			// 删除cookie中的购物车
			CookieUtils.deleteCookie(request, response, "cart");
			cartList = cartService.getCartList(user.getId());
		}
		// 未登录执行以下
		// 把列表传递到页面
		request.setAttribute("cartList", cartList);
		// 返回逻辑页面
		return "cart";
	}

	/**
	 * @Title updateCartNum
	 * @Description 更新购物车商品数量
	 * @param response
	 * @param request
	 * @param itemId
	 * @param num
	 * @return ShopResult
	 */
	@RequestMapping(value = "/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public ShopResult updateCartNum(HttpServletResponse response, HttpServletRequest request, @PathVariable Long itemId,
			@PathVariable Integer num) {
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			cartService.updateCartNum(user.getId(), itemId, num);
			return ShopResult.ok();
		}
		// 从cookie中取出商品列表
		List<TbItem> cartList = getCartListFromCookie(request);
		// 遍历商品列表找出对应的商品
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().longValue() == itemId) {
				// 更新数量
				tbItem.setNum(num);
				break;
			}
		}
		// 把购物车列表重新写入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		// 返回成功
		return ShopResult.ok();
	}

	/**
	 * @Title deleteCartItem
	 * @Description 删除商品列表中的某个商品
	 * @param response
	 * @param request
	 * @param itemId
	 * @return String
	 */
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(HttpServletResponse response, HttpServletRequest request, @PathVariable Long itemId) {
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			cartService.deleteCartItem(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		// 从cookie中取出商品列表
		List<TbItem> cartList = getCartListFromCookie(request);
		// 遍历商品列表找出对应的商品
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().longValue() == itemId) {
				// 删除商品
				cartList.remove(tbItem);
				break;
			}
		}
		// 把购物车列表重新写入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		return "redirect:/cart/cart.html";
	}
}
