package com.cai.service;

import java.util.List;

import com.cai.common.pojo.EUTreeNode;

public interface ItemCatService {
	List<EUTreeNode> getCatList(long parentId);
}
