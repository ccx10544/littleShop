package com.cai.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cai.common.pojo.SearchItem;
import com.cai.common.pojo.ShopResult;
import com.cai.common.utils.ExceptionUtil;
import com.cai.search.mapper.ItemMapper;
import com.cai.search.service.SearchItemService;

@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private ItemMapper itemMapper;

	@Autowired
	private SolrServer solrServer;

	@Override
	public ShopResult importAllItems() {
		try {
			// 查询商品列表
			List<SearchItem> itemList = itemMapper.getItemList();
			// 把商品信息写入索引库
			for (SearchItem item : itemList) {
				// 创建一个SolrInputDocument对象
				SolrInputDocument document = new SolrInputDocument();
				document.setField("id", item.getId());
				document.setField("item_title", item.getTitle());
				document.setField("item_sell_point", item.getSell_point());
				document.setField("item_price", item.getPrice());
				document.setField("item_image", item.getImage());
				document.setField("item_category_name", item.getCategory_name());
				document.setField("item_desc", item.getItem_des());
				// 写入索引库
				solrServer.add(document);
			}
			// 提交修改
			solrServer.commit();
			return ShopResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return ShopResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
}
