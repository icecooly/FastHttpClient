package io.itit.itf.okhttp.test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import io.itit.itf.okhttp.Callback;
import io.itit.itf.okhttp.HttpClient;
import junit.framework.TestCase;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 
 * @author icecooly
 *
 */
public class HttpClientTestCase extends TestCase{
	//
	private static String url = "http://localhost:7002/p/api/test";
	//
	public void testGetSync() throws IOException{
		Response response = HttpClient.get().url(url).
				addParams("para1", "icecool").
				addParams("para2", "111111").
				build()
				.execute();
		System.out.println(response.body().string());
	}
	//
	public void testPostSync() throws IOException{
		Response response = HttpClient.post().url(url).
				addParams("para1", "123456").
				addParams("para2", "测试").
				build().
				execute();
		System.out.println(response.body().string());
	}
	//
	public void testGetAsync() throws InterruptedException{
		HttpClient.get().url(url).
		addParams("userName", "icecool").
		addParams("password", "111111").
		build().
		executeAsync(new Callback() {
				@Override
				public void onFailure(Call call, Exception e, int id) {
					e.printStackTrace();
				}
				@Override
				public void onResponse(Call call, Response response, int id) {
					try {
						System.out.println(response.body().string());
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.exit(0);
				}
		});
		Thread.sleep(50000);
	}
	//
	public void testPostAsync() throws InterruptedException{
		HttpClient.post().url(url).
		addParams("userName", "icecool").
		addParams("password", "111111").
		build().
		executeAsync(new Callback() {
				@Override
				public void onFailure(Call call, Exception e, int id) {
					e.printStackTrace();
				}
				@Override
				public void onResponse(Call call, Response response, int id) {
					try {
						System.out.println(response.body().string());
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.exit(0);
				}
		});
		Thread.sleep(50000);
	}
	//
	public void testDownloadFile() throws InterruptedException{
		HttpClient.get().
		url("http://e.hiphotos.baidu.com/image/pic/item/faedab64034f78f0b31a05a671310a55b3191c55.jpg").
		build().
		executeAsync(new Callback() {
				@Override
				public void onFailure(Call call, Exception e, int id) {
					e.printStackTrace();
				}
				@Override
				public void onResponse(Call call, Response response, int id) {
					try {
						FileUtil.saveContent(response.body().bytes(),new File("/tmp/tmp.jpg"));
						System.exit(0);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		});
		Thread.sleep(50000);
	}
	//
	public void testUploadFile() throws UnsupportedEncodingException, IOException{
		byte[] imageContent=FileUtil.getBytes("/tmp/tmp.jpg");
		Response response = HttpClient.post().url(url).
				addFile("file1", "a.txt", "123").
				addFile("file2", "b.jpg", imageContent).
				build()
				.connTimeOut(10000).
				execute();
		System.out.println(response.body().string());
	}
	//
	public void testHttpsGet() throws IOException{
		Response response = HttpClient.get().url("https://kyfw.12306.cn/otn/").
				build()
				.execute();
		System.out.println(response.body().string());
	}
	//
	public void testHttpsPost() throws IOException{
		Response response = HttpClient.post().url("https://kyfw.12306.cn/otn/").
				build()
				.execute();
		System.out.println(response.body().string());
	}
	//
}
