package com.cai.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.cai.common.jedis.JedisClient;
import com.cai.common.pojo.EUDataGridResult;
import com.cai.common.pojo.ShopResult;
import com.cai.common.utils.IDUtils;
import com.cai.common.utils.JsonUtils;
import com.cai.mapper.TbItemDescMapper;
import com.cai.mapper.TbItemMapper;
import com.cai.mapper.TbItemParamItemMapper;
import com.cai.pojo.TbItem;
import com.cai.pojo.TbItemDesc;
import com.cai.pojo.TbItemExample;
import com.cai.pojo.TbItemParamItem;
import com.cai.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;

	@Autowired
	private TbItemDescMapper itemDescMapper;

	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private JedisClient jedisClient;

	@Resource(name = "topicDestination")
	private Destination destination;

	@Value("${ITEM_INFO_KEY}")
	private String ITEM_INFO_KEY;

	@Value("${ITEM_INFO_KEY_EXPIRE}")
	private Integer ITEM_INFO_KEY_EXPIRE;

	@Override
	public EUDataGridResult getItemList(Integer page, Integer rows) {
		// 查询商品列表
		TbItemExample example = new TbItemExample();
		// 分页处理
		PageHelper.startPage(page, rows);
		List<TbItem> list = itemMapper.selectByExample(example);
		// 创建一个返回值对象
		EUDataGridResult result = new EUDataGridResult();
		result.setRows(list);
		// 取记录总条数
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}

	@Override
	public ShopResult createItem(TbItem item, String desc, String itemParams) throws Exception {
		// 生成商品id
		final Long itemId = IDUtils.genItemId();
		item.setId(itemId);
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		itemMapper.insert(item);
		ShopResult result = insertItemDesc(itemId, desc);
		if (result.getStatus() != 200) {
			throw new Exception();
		}
		result = insertItemParamItem(itemId, itemParams);
		if (result.getStatus() != 200) {
			throw new Exception();
		}
		// 发送商品添加消息
		jmsTemplate.send(destination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(itemId.toString());
			}
		});
		return ShopResult.ok();
	}

	/**
	 * @Title insertItemDesc
	 * @Description 添加商品描述
	 * @param itemId
	 * @param desc
	 * @return TaotaoResult
	 */
	private ShopResult insertItemDesc(Long itemId, String desc) {
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		itemDescMapper.insert(itemDesc);
		return ShopResult.ok();
	}

	/**
	 * 添加规格参数
	 * 
	 * @param itemId
	 * @param itemParams
	 * @return TaotaoResult
	 */
	private ShopResult insertItemParamItem(Long itemId, String itemParams) {
		TbItemParamItem itemParamItem = new TbItemParamItem();
		itemParamItem.setItemId(itemId);
		itemParamItem.setParamData(itemParams);
		itemParamItem.setCreated(new Date());
		itemParamItem.setUpdated(new Date());
		itemParamItemMapper.insert(itemParamItem);
		return ShopResult.ok();
	}

	@Override
	public TbItem getItemById(Long itemId) {
		// 从缓存中取出数据,有则直接返回数据
		try {
			String redisItem = jedisClient.get(ITEM_INFO_KEY + ":" + itemId + ":BASE");
			if (StringUtils.isNotBlank(redisItem)) {
				jedisClient.expire(ITEM_INFO_KEY + ":" + itemId + ":BASE", ITEM_INFO_KEY_EXPIRE);
				return JsonUtils.jsonToPojo(redisItem, TbItem.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		// 添加缓存
		try {
			jedisClient.set(ITEM_INFO_KEY + ":" + itemId + ":BASE", JsonUtils.objectToJson(item));
			jedisClient.expire(ITEM_INFO_KEY + ":" + itemId + ":BASE", ITEM_INFO_KEY_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public TbItemDesc getItemDescById(Long itemId) {
		// 从缓存中取出数据,有则直接返回数据
		try {
			String redisItemDesc = jedisClient.get(ITEM_INFO_KEY + ":" + itemId + ":DESC");
			if (StringUtils.isNotBlank(redisItemDesc)) {
				jedisClient.expire(ITEM_INFO_KEY + ":" + itemId + ":DESC", ITEM_INFO_KEY_EXPIRE);
				return JsonUtils.jsonToPojo(redisItemDesc, TbItemDesc.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		// 添加缓存
		try {
			jedisClient.set(ITEM_INFO_KEY + ":" + itemId + ":DESC", JsonUtils.objectToJson(itemDesc));
			jedisClient.expire(ITEM_INFO_KEY + ":" + itemId + ":DESC", ITEM_INFO_KEY_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}
}
