package com.cai.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.cai.item.pojo.Item;
import com.cai.pojo.TbItem;
import com.cai.pojo.TbItemDesc;
import com.cai.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @ClassName HtmlGenListener
 * @Description 监听商品添加消息,生成对应的静态页面
 * @author Vergil
 * @date 2018年11月11日 下午9:04:46
 * @version V1.0
 * @Copyright 2018 公司名 Inc. All rights reserved
 */
public class HtmlGenListener implements MessageListener {

	@Autowired
	private ItemService itemService;

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Value("${HTML_GEN_PATH}")
	private String HTML_GEN_PATH;

	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			// 创建一个模板,参考jsp
			String text = textMessage.getText();
			// 从消息到取商品id
			Long itemId = new Long(text);
			// 等待事务提交
			Thread.sleep(1000);
			// 根据商品id查询商品信息,商品基本信息和商品描述
			TbItem tbitem = itemService.getItemById(itemId);
			Item item = new Item(tbitem);
			TbItemDesc itemDesc = itemService.getItemDescById(itemId);
			// 创建一个数据集,把商品封装
			Map<String, Object> map = new HashMap<>();
			map.put("item", item);
			map.put("itemDesc", itemDesc);
			// 加载模板对象
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			// 创建一个输出流,指定输出的目录和文件名
			Writer out = new FileWriter(new File(HTML_GEN_PATH + itemId + ".html"));
			// 生成静态页面
			template.process(map, out);
			// 关闭流
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
