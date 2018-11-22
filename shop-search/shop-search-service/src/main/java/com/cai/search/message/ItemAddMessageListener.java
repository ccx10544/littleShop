package com.cai.search.message;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.cai.common.pojo.SearchItem;
import com.cai.search.mapper.ItemMapper;

/**
 * @ClassName:ItemAddMessageListener
 * @Description:监听商品添加消息,将对应的的商品信息同步到索引库
 * @author Vergil
 * @date 2018年11月10日 下午11:29:45
 * @version V1.0
 * @Copyright:2018 公司名 Inc. All rights reserved
 */
public class ItemAddMessageListener implements MessageListener {

	@Autowired
	private ItemMapper itemMapper;

	@Autowired
	private SolrServer solrServer;

	@Override
	public void onMessage(Message message) {
		try {
			// 从消息中取商品id
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			Long itemId = new Long(text);
			// 等待事务提交
			Thread.sleep(1000);
			// 根据商品id查询商品信息
			SearchItem item = itemMapper.getItemById(itemId);
			// 创建一个文档对象
			SolrInputDocument document = new SolrInputDocument();
			// 向文档对象中添加域
			document.setField("id", item.getId());
			document.setField("item_title", item.getTitle());
			document.setField("item_sell_point", item.getSell_point());
			document.setField("item_price", item.getPrice());
			document.setField("item_image", item.getImage());
			document.setField("item_category_name", item.getCategory_name());
			document.setField("item_desc", item.getItem_des());
			// 把文档写入索引库
			solrServer.add(document);
			// 提交
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
