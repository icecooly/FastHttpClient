package io.itit.itf.okhttp.test;

import java.io.File;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import io.itit.itf.okhttp.callback.DownloadFileCallback;
import io.itit.itf.okhttp.callback.StringCallback;
import io.itit.itf.okhttp.interceptor.DownloadFileInterceptor;
import io.itit.itf.okhttp.util.FileUtil;
import junit.framework.TestCase;
import okhttp3.Call;

/**
 * 
 * @author icecooly
 *
 */
public class HttpClientTestCase extends TestCase{
	//
	private static Logger logger=LoggerFactory.getLogger(HttpClientTestCase.class);
	//
	/**
	 * 同步Get请求(访问百度首页,自动处理https单向认证)
	 * @throws Exception
	 */
	public void testGetSync() throws Exception{
		String resp=FastHttpClient.get().
				url("https://www.baidu.com").
				build().
				execute().string();
		logger.info(resp);
	}
	
	/**
	 * 异步Get请求(访问百度首页)
	 * @throws InterruptedException
	 */
	public void testGetAsync() throws InterruptedException{
		FastHttpClient.get().url("https://www.baidu.com").build().
		executeAsync(new StringCallback() {
			@Override
			public void onFailure(Call call, Exception e, int id) {
				logger.error(e.getMessage(),e);
			}
			@Override
			public void onSuccess(Call call, String response, int id) {
				logger.info("response:{}",response);
			}
		});
		Thread.sleep(3000);
	}
	
	/**
	 * 百度搜索关键字'微信机器人'
	 * @throws Exception
	 */
	public void testBaiduSearch() throws Exception{
		String html = FastHttpClient.get().
				url("http://www.baidu.com/s").
				addParams("wd", "微信机器人").
				addParams("tn", "baidu").
				build().
				execute().
				string();
		logger.info(html);
	}
	
	/**
	 * 异步下载一张百度图片，有下载进度,保存为/tmp/tmp.jpg
	 * @throws InterruptedException
	 */
	public void testDownloadFile() throws InterruptedException{
		String savePath="/tmp/tmp.jpg";
		String imageUrl="http://e.hiphotos.baidu.com/image/pic/item/faedab64034f78f0b31a05a671310a55b3191c55.jpg";
		FastHttpClient.newBuilder().addNetworkInterceptor(new DownloadFileInterceptor(){
			@Override
			public void updateProgress(long downloadLenth, long totalLength, boolean isFinish) {
				logger.info("updateProgress downloadLenth:"+downloadLenth+
						",totalLength:"+totalLength+",isFinish:"+isFinish);
			}
		}).
		build().
		get().
		url(imageUrl).
		build().
		executeAsync(new DownloadFileCallback(savePath) {//save file to /tmp/tmp.jpg
				@Override
				public void onFailure(Call call, Exception e, int id) {
					logger.error(e.getMessage(),e);
				}
				@Override
				public void onSuccess(Call call, File file, int id) {
					logger.info("filePath:"+file.getAbsolutePath());
				}
				@Override
				public void onSuccess(Call call, InputStream fileStream, int id) {
					logger.info("onSuccessWithInputStream");
				}
		});
		Thread.sleep(3000);
	}
	
	/**
	 * 上传文件
	 * @throws Exception
	 */
	public void testUploadFile() throws Exception{
		byte[] imageContent=FileUtil.getBytes("/tmp/cdz.jpg");
		Response response = FastHttpClient.newBuilder().
				connectTimeout(10, TimeUnit.SECONDS).
				build().
				post().url("http://上传地址").
				addFile("file", "cdz.jpg",imageContent).
				build().
				execute();
		logger.info(response.body().string());
	}
	
	/**
	 * 设置网络代理
	 * @throws Exception
	 */
	public void testProxy() throws Exception{
		Proxy proxy = new Proxy(Proxy.Type.SOCKS,new InetSocketAddress("127.0.0.1", 1088));
		Response response = FastHttpClient.
				newBuilder().
				proxy(proxy).
				build().
				get().
				url("https://www.baidu.com").
				build().
				execute();
		logger.info(response.string());
	}
	
	/**
	 * 设置http头部信息
	 * @throws Exception
	 */
	public void testAddHeader() throws Exception{
		String url="https://www.baidu.com";
		Response response=FastHttpClient.
				get().
				addHeader("Referer","http://news.baidu.com/").
				addHeader("cookie", "uin=test;skey=111111;").
				url(url).
				build().
				execute();
		System.out.println(response.string());
	}
	//
	public void testXForwardedFor() throws Exception{
		String url="https://www.aex.com/";
		Response response=FastHttpClient.
				get().
				addHeader("X-Forwarded-For","234.45.124.12").
				url(url).
				build().
				execute();
		System.out.println(response.string());
	}
	//
	//
	public void testPut() throws Exception{
		String url="https://www.aex.com/";
		Response response=FastHttpClient.
				put().
				addHeader("X-Forwarded-For","234.45.124.12").
				url(url).
				build().
				execute();
		System.out.println(response.string());
	}
}
