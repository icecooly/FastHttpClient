package io.itit.itf.okhttp.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.HttpClient;
import io.itit.itf.okhttp.Response;
import io.itit.itf.okhttp.callback.DownloadFileCallback;
import io.itit.itf.okhttp.callback.StringCallback;
import io.itit.itf.okhttp.interceptor.DownloadFileInterceptor;
import io.itit.itf.okhttp.util.FileUtil;
import junit.framework.TestCase;
import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

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
	//
	@SuppressWarnings("unused")
	private static class ObjectParam{
		public String wd;
		public String tn;
	}
	public void testObjectParam() throws Exception{
		ObjectParam param=new ObjectParam();
		param.wd="微信机器人";
		param.tn="baidu";
		String resp=FastHttpClient.get().
				url("http://www.baidu.com/s").
				addParams(param).
				build().
				execute().string();
		logger.info(resp);
	}
	
	/**
	 * 异步下载一张百度图片，有下载进度,保存为tmp.jpg
	 * @throws InterruptedException
	 */
	public void testAsyncDownloadFile() throws InterruptedException{
		String savePath="tmp.jpg";
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
		Thread.sleep(5000);
	}
	
	/**
	 * 同步下载文件
	 * @throws Exception
	 */
	public void testSyncDownloadFile() throws Exception{
		String savePath="tmp.jpg";
		String imageUrl="http://e.hiphotos.baidu.com/image/pic/item/faedab64034f78f0b31a05a671310a55b3191c55.jpg";
		InputStream is=FastHttpClient.get().url(imageUrl).build().execute().byteStream();
		FileUtil.saveContent(is, new File(savePath));
	}
	
	/**
	 * 上传文件(支持多个文件同时上传)
	 * @throws Exception
	 */
	public void testUploadFile() throws Exception{
		byte[] imageContent=FileUtil.getBytes("/tmp/logo.jpg");
		Response response = FastHttpClient.newBuilder().
				connectTimeout(10, TimeUnit.SECONDS).
				build().
				post().
				url("上传地址").
				addFile("file", "logo.jpg",imageContent).
				build().
				execute();
		logger.info(response.body().string());
	}
	
	/**
	 * 上传文件(通过文件流)
	 * @throws Exception
	 */
	public void testUploadFileWithStream() throws Exception{
		InputStream is=new FileInputStream("/tmp/logo.jpg");
		Response response = FastHttpClient.newBuilder().
				connectTimeout(10, TimeUnit.SECONDS).
				build().
				post().
				url("上传地址").
				addFile("file", "logo.jpg",is).
				build().
				execute();
		logger.info(response.body().string());
	}
	
	/**
	 * 设置网络代理
	 * @throws Exception
	 */
	public void testProxy() throws Exception{
		Proxy proxy = new Proxy(Proxy.Type.SOCKS,new InetSocketAddress("1.1.1.1", 1088));
		Response response = FastHttpClient.
				newBuilder().
				proxy(proxy).
				build().
				get().
				url("http://ip111.cn/").
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
	private class LocalCookieJar implements CookieJar{
	    List<Cookie> cookies;
	    @Override
	    public List<Cookie> loadForRequest(HttpUrl arg0) {
	         if (cookies != null) {
	                return cookies;
	         }
	         return new ArrayList<Cookie>();
	    }
	    @Override
	    public void saveFromResponse(HttpUrl arg0, List<Cookie> cookies) {
	        this.cookies = cookies;
	    }
	}
	//自动携带Cookie进行请求
	public void testCookie() throws Exception {
		LocalCookieJar cookie=new LocalCookieJar();
		HttpClient client=FastHttpClient.newBuilder()
                .followRedirects(false) //禁制OkHttp的重定向操作，我们自己处理重定向
                .followSslRedirects(false)
                .cookieJar(cookie)   //为OkHttp设置自动携带Cookie的功能
                .build();
		String url="https://www.baidu.com/";
		client.get().addHeader("Referer","https://www.baidu.com/").
			url(url).
			build().
			execute();
		System.out.println(cookie.cookies);
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
