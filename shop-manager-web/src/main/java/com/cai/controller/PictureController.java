package com.cai.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cai.common.utils.FastDFSClient;
import com.cai.common.utils.JsonUtils;

@Controller
public class PictureController {

	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;

	@RequestMapping(value = "/pic/upload", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
	@ResponseBody
	public String prictureUpload(MultipartFile uploadFile) {
		Map<String, Object> map = null;
		try {
			// 把图片上传的图片服务器
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
			// 得到一个图片的地址和文件名
			String originalFilename = uploadFile.getOriginalFilename();
			String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			// 补充为完整的url
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			url = IMAGE_SERVER_URL + url;
			// 封装成map结合
			map = new HashMap<>();
			map.put("error", 0);
			map.put("url", url);

		} catch (Exception e) {
			e.printStackTrace();
			map = new HashMap<>();
			map.put("error", 1);
			map.put("message", "图片上传失败");
		}
		return JsonUtils.objectToJson(map);
	}
}
