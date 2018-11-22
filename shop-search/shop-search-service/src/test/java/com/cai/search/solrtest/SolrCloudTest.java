package com.cai.search.solrtest;

//import org.apache.solr.client.solrj.SolrServer;
//import org.apache.solr.client.solrj.impl.CloudSolrServer;
//import org.apache.solr.common.SolrInputDocument;
//import org.junit.Test;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SolrCloudTest {
//	@Test
//	public void testAddDocument() throws Exception {
//		String zkHost = "168.25.128:2182,192.168.25.130:2183,192.168.25.130:2184";
//		// 创建一个集群的连接
//		CloudSolrServer cloudSolrServer = new CloudSolrServer(zkHost);
//		// 设置一个defaultCollection属性
//		cloudSolrServer.setDefaultCollection("collection2");
//		SolrInputDocument inputDocument = new SolrInputDocument();
//		inputDocument.setField("id", "solrCloud01");
//		inputDocument.setField("item_title", "测试商品01");
//		inputDocument.setField("item_price", 123);
//		cloudSolrServer.add(inputDocument);
//		cloudSolrServer.commit();
//	}
//
//	@Test
//	public void testAddDocumentSpring() throws Exception {
//		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
//				"classpath:spring/applicationContext-*.xml");
//		SolrServer cloudSolrServer = applicationContext.getBean(SolrServer.class);
//		SolrInputDocument inputDocument = new SolrInputDocument();
//		inputDocument.setField("id", "solrCloud01");
//		inputDocument.setField("item_title", "测试商品01");
//		inputDocument.setField("item_price", 123);
//		cloudSolrServer.add(inputDocument);
//		cloudSolrServer.commit();
//	}
}
