package com.cai.common.pojo;

import java.io.Serializable;

/**
 * easyUI树形控件节点格式
 * 
 * @author Administrator
 *
 */
public class EUTreeNode implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String text;
	private String state;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
