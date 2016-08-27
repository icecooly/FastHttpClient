package io.itit.itf.okhttp.test;

import io.itit.itf.okhttp.HttpClient;
import okhttp3.Response;

/**
 * 
 * @author icecooly
 *
 */
public class HttpsClientTest {
	//
	public static void main(String[] args) throws Exception {
		Response response = null;
		// 1.get
		response = HttpClient.get().url("https://kyfw.12306.cn/otn/").
				build()
				.execute();
		System.out.println(response.body().string());
	}
}
