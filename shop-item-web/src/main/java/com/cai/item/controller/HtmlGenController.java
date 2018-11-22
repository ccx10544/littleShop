package com.cai.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @ClassName HtmlGenController
 * @Description 生成静态页面测试
 * @author Vergil
 * @date 2018年11月11日 下午7:26:52
 * @version V1.0
 * @Copyright 2018 公司名 Inc. All rights reserved
 */
@Controller
public class HtmlGenController {

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@RequestMapping("/genhtml")
	@ResponseBody
	public String genHtml() throws Exception {
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		// 加载模板
		Template template = configuration.getTemplate("hello.ftl");
		// 创建数据集
		Map<String, Object> map = new HashMap<>();
		map.put("hello", "123456");
		// 指定文件输出的路径及文件名
		Writer out = new FileWriter(new File("F:\\Freemarker\\hello2.html"));
		// 输出文件
		template.process(map, out);
		// 关闭流
		out.close();
		return "ok";
	}
}
