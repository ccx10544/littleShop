package com.cai.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cai.common.jedis.JedisClient;
import com.cai.common.pojo.EUDataGridResult;
import com.cai.common.pojo.ShopResult;
import com.cai.common.utils.JsonUtils;
import com.cai.content.service.ContentService;
import com.cai.mapper.TbContentMapper;
import com.cai.pojo.TbContent;
import com.cai.pojo.TbContentExample;
import com.cai.pojo.TbContentExample.Criteria;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentMapper contentMapper;

	@Autowired
	private JedisClient jedisClient;

	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;

	@Override
	public ShopResult insertContent(TbContent content) {
		content.setCreated(new Date());
		content.setUpdated(new Date());
		contentMapper.insert(content);
		// 插入数据成功后,需要进行缓存同步,
		try {
			jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ShopResult.ok();
	}

	@Override
	public EUDataGridResult selectContent(Long categoryId, Integer page, Integer rows) {
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		PageHelper.startPage(page, rows);
		List<TbContent> list = contentMapper.selectByExample(example);
		EUDataGridResult result = new EUDataGridResult();
		result.setRows(list);
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}

	@Override
	public List<TbContent> getContentList(Long contentCid) {
		// 先从redis取商品内容列表数据,如果有直接返回
		try {
			String contentList = jedisClient.hget(CONTENT_LIST, contentCid.toString());
			if (StringUtils.isNotBlank(contentList)) {
				List<TbContent> resultList = JsonUtils.jsonToList(contentList, TbContent.class);
				return resultList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(contentCid);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		// 把结果添加到缓存中
		try {
			jedisClient.hset(CONTENT_LIST, contentCid.toString(), JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
