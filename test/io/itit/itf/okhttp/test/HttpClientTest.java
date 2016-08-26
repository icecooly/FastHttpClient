package io.itit.itf.okhttp.test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.itit.itf.okhttp.Callback;
import io.itit.itf.okhttp.HttpClient;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * 
 * @author icecooly
 *
 */
public class HttpClientTest {
	//
	public static void main(String[] args) throws IOException {
		OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10000L, TimeUnit.MILLISECONDS)
				.readTimeout(10000L, TimeUnit.MILLISECONDS).build();
		HttpClient.okHttpClient=okHttpClient;
		String url = "http://localhost:7002/p/api/test";
		Response response=null;
		//
		HttpClient.get().
			url(url).
			addParams("userName","icecool").
			addParams("password", "111111").
			build().
			executeAsync(new Callback() {
			@Override
			public void onFailure(Call call, Exception e, int id) {
				//TODO
			}

			@Override
			public void onResponse(Call call,Response response, int id) {
				try {
					System.out.println(response.body().string());
				} catch (IOException e) {
				}
			}
		});
		//
//		Response response=OKHttpUtil.
//			post().
//			url(url).
//			addParams("para1", "123456").
//			addParams("para2", "测试").
//			addHeader("head1", "head1Value").
//			build().
//			execute();
//		System.out.println(response.body().string());
		//
		HttpClient.get().
		url(url).
		addParams("userName", "icecool").
		addParams("password", "111111").
		build().
		executeSync();
		
		HttpClient.post().
		url(url).
		addParams("userName", "icecool").
		addParams("password", "111111").
		build().
		executeSync();
		
		response=HttpClient.post().
				url(url).
				addFile("file1","a.txt","123").
				addFile("file2","b.jpg","456").
				build().
				connTimeOut(1000).
				executeSync();
		System.out.println(response.body().string());
	}
}
