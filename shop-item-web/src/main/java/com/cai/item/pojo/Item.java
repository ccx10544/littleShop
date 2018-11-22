package com.cai.item.pojo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.cai.pojo.TbItem;

public class Item extends TbItem {

	private static final long serialVersionUID = 1L;

	public Item(TbItem tbItem) {
		// 将原来的数据有的属性值拷贝到item有的属性中
		BeanUtils.copyProperties(tbItem, this);
	}

	public String[] getImages() {
		if (StringUtils.isNotBlank(super.getImage())) {
			return super.getImage().split(",");
		}
		return null;
	}
}
