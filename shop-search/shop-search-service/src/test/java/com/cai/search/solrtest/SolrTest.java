package com.cai.search.solrtest;

//import org.apache.solr.client.solrj.SolrQuery;
//import org.apache.solr.client.solrj.impl.HttpSolrServer;
//import org.apache.solr.client.solrj.response.QueryResponse;
//import org.apache.solr.common.SolrDocument;
//import org.apache.solr.common.SolrDocumentList;
//import org.apache.solr.common.SolrInputDocument;
//import org.junit.Test;

public class SolrTest {
//	@Test
//	public void addDocument() throws Exception {
//		HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.130:8080/solr");
//		SolrInputDocument inputDocument = new SolrInputDocument();
//		inputDocument.addField("id", "test001");
//		inputDocument.addField("item_title", "测试商品1");
//		inputDocument.addField("item_price", 12345);
//		// 把文档对象写入索引库
//		solrServer.add(inputDocument);
//		// 提交
//		solrServer.commit();
//	}
//
//	@Test
//	public void deleteDocument() throws Exception {
//		HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.130:8080/solr");
////		solrServer.deleteById("test001");
//		solrServer.deleteByQuery("*:*");
//		solrServer.commit();
//	}
//
//	@Test
//	public void queryDocument() throws Exception {
//		HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.130:8080/solr");
//		SolrQuery solrQuery = new SolrQuery();
//		solrQuery.setQuery("*:*");
//		solrQuery.setStart(20);
//		solrQuery.setRows(50);
//		QueryResponse queryResponse = solrServer.query(solrQuery);
//		SolrDocumentList documentList = queryResponse.getResults();
//		System.out.println("一共查询到的数据:" + documentList.getNumFound());
//		for (SolrDocument solrDocument : documentList) {
//			System.out.println(solrDocument.get("id"));
//			System.out.println(solrDocument.get("item_title"));
//			System.out.println(solrDocument.get("item_price"));
//			System.out.println(solrDocument.get("item_image"));
//		}
//	}
}
