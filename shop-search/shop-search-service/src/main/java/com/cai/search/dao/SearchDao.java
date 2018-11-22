package com.cai.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cai.common.pojo.SearchItem;
import com.cai.common.pojo.SearchResult;

@Repository
public class SearchDao {

	@Autowired
	private SolrServer solrServer;

	public SearchResult search(SolrQuery query) {
		try {
			SearchResult searchResult = new SearchResult();
			QueryResponse queryResponse = solrServer.query(query);
			SolrDocumentList documentList = queryResponse.getResults();
			long numFound = documentList.getNumFound();
			searchResult.setRecordCount(numFound);
			List<SearchItem> itemList = new ArrayList<>();
			Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
			for (SolrDocument solrDocument : documentList) {
				SearchItem item = new SearchItem();
				item.setId((String) solrDocument.get("id"));
				item.setCategory_name((String) solrDocument.get("item_category_name"));
				item.setImage((String) solrDocument.get("item_image"));
				item.setItem_des((String) solrDocument.get("item_desc"));
				item.setSell_point((String) solrDocument.get("item_sell_point"));
				List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
				String title = "";
				if (list != null && list.size() > 0) {
					title = list.get(0);
				} else {
					title = (String) solrDocument.get("item_title");
				}
				item.setTitle(title);
				item.setPrice((long) solrDocument.get("item_price"));
				itemList.add(item);
			}
			searchResult.setItemList(itemList);
			return searchResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
