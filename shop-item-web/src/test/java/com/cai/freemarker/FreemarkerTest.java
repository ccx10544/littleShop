package com.cai.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerTest {
	@Test
	public void testFreemarker() throws Exception {
		// 1.创建一个模板对象
		// 2.创建一个configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		// 3.设置一个模板文件保存的目录
		configuration.setDirectoryForTemplateLoading(
				new File("D:\\my-shop-project\\shop-item-web\\src\\main\\webapp\\WEB-INF\\ftl"));
		// 4.模板文件的编码格式,一般是utf-8
		configuration.setDefaultEncoding("utf-8");
		// 5.加载一个模板文件,创建一个模板对象
//		Template template = configuration.getTemplate("hello.ftl");
		Template template = configuration.getTemplate("student.ftl");
		// 6.创建一个数据集,可以是pojo也可以是map。推荐使用map
		Map<Object, Object> map = new HashMap<>();
		map.put("hello", "hello freemarker");
		// 创建一个pojo对象
		Student student = new Student(1, "张三", 18, "中山");
		map.put("student", student);
		List<Student> stuList = new ArrayList<>();
		stuList.add(new Student(1, "张三1", 18, "中山"));
		stuList.add(new Student(2, "张三2", 19, "中山"));
		stuList.add(new Student(3, "张三3", 20, "中山"));
		stuList.add(new Student(4, "张三4", 21, "中山"));
		stuList.add(new Student(5, "张三5", 22, "中山"));
		stuList.add(new Student(6, "张三6", 23, "中山"));
		stuList.add(new Student(7, "张三7", 24, "中山"));
		stuList.add(new Student(8, "张三8", 25, "中山"));
		stuList.add(new Student(9, "张三9", 26, "中山"));
		map.put("stuList", stuList);
		// 添加一个日期
		map.put("date", new Date());
		// null值得测试
		map.put("val", null);
		// 7.创建一个writer对象,指定输出文件的路径及文件名
//		Writer out = new FileWriter(new File("F:\\Freemarker\\hello.txt"));
		Writer out = new FileWriter(new File("F:\\Freemarker\\student.html"));
		// 8.生成静态页面
		template.process(map, out);
		// 9.关闭流
		out.close();
	}
}
