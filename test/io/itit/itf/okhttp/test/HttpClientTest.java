package io.itit.itf.okhttp.test;

import java.io.IOException;

import io.itit.itf.okhttp.Callback;
import io.itit.itf.okhttp.HttpClient;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 
 * @author icecooly
 *
 */
public class HttpClientTest {
	//
	public static void main(String[] args) throws IOException {
		String url = "http://localhost:7002/p/api/test";
		Response response = null;
		// 1.get
		response = HttpClient.get().url(url).
				addParams("para1", "icecool").
				addParams("para2", "111111").
				build()
				.execute();
		System.out.println(response.body().string());
		
		response = HttpClient.get().url("https://kyfw.12306.cn/otn/").
				build()
				.execute();
		System.out.println(response.body().string());

		// 2.post
		response = HttpClient.post().url(url).
				addParams("para1", "123456").
				addParams("para2", "测试").
				build().
				execute();
		System.out.println(response.body().string());

		// 3.post normal file and image
		byte[] imageContent=FileUtil.getBytes("/tmp/test.png");
		response = HttpClient.post().url(url).
				addFile("file1", "a.txt", "123").
				addFile("file2", "b.jpg", imageContent).
				build()
				.connTimeOut(10000).
				execute();
		System.out.println(response.body().string());

		// 4.get async
		HttpClient.get().url(url).
			addParams("userName", "icecool").
			addParams("password", "111111").
			build()
			.executeAsync(new Callback() {
					@Override
					public void onFailure(Call call, Exception e, int id) {
						// TODO
					}

					@Override
					public void onResponse(Call call, Response response, int id) {
						try {
							System.out.println(response.body().string());
						} catch (IOException e) {
						}
					}
		});
	}
}
