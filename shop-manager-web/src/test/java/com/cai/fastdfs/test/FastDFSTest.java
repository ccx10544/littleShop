package com.cai.fastdfs.test;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import com.cai.common.utils.FastDFSClient;

public class FastDFSTest {
	@Test
	public void testUpload() throws Exception {
		// 创建一个配置文件,文件名任意,内容就是tracker服务器的地址
		// 使用全局对象加载配置文件
		ClientGlobal.init("D:\\my-shop-project\\shop-manager-web\\src\\main\\resources\\conf\\client.conf");
		// 创建一个TrackerClient对象
		TrackerClient trackerClient = new TrackerClient();
		// 通过TrackerClient获得一个TrackerServer对象
		TrackerServer trackerServer = trackerClient.getConnection();
		// 创建一个StorageServer的引用,可以是null
		StorageServer storageServer = null;
		// 创建一个StorageClient,参数需要TrackerServer和StorageServer
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		// 使用StorageClient上传文件
		String[] upload_file = storageClient.upload_file("C:\\Users\\Administrator\\Pictures\\5b5ebc7cNf8557ab9.jpg",
				"jpg", null);
		for (String string : upload_file) {
			System.out.println(string);
		}
	}

	@Test
	public void testFastDFSUtils() throws Exception {
		String conf = "D:\\my-shop-project\\shop-manager-web\\src\\main\\resources\\conf\\client.conf";
		FastDFSClient dfsClient = new FastDFSClient(conf);
		String uploadFile = dfsClient.uploadFile("C:\\Users\\Administrator\\Pictures\\5b6018c4Ne0fec044.jpg");
		System.out.println(uploadFile);
	}
}
