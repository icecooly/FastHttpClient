package io.itit.itf.okhttp.test;

import java.io.InputStream;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;


/**
 * 
 * @author icecooly
 *
 */
public class URLUtil {
	//
	private static Logger logger=LoggerFactory.getLogger(URLUtil.class);
	//
	static{
		System.setProperty ("jsse.enableSNIExtension","false");
	}
	//
	public static String httpGet(String url) throws Exception {
		Response response = FastHttpClient.get().
				url(url).
				build().
				execute();
		return response.body().string();
	}
	//
	public static String httpsGet(String url) throws Exception {
		Response response = FastHttpClient.get().
				url(url).
				build().
				execute();
		return response.body().string();
	}
	//
	public static String httpsGet(String url,SSLContext sslContext) throws Exception {
		Response response = FastHttpClient.
				newBuilder().
				sslContext(sslContext).
				build().
				get().
				url(url).
				build().
				execute();
		return response.body().string();
	}
	//
	public static String httpPost(String url,Map<String,String> paramMap) throws Exception{ 
		Response response = FastHttpClient.post().
				url(url).
				addParams(paramMap).
				build().
				execute();
		return response.body().string();
	}
	//
	public static String httpPostWithBody(String url,String body) throws Exception{ 
		Response response = FastHttpClient.post().
				url(url).
				body(body).
				build().
				execute();
		return response.body().string();
	}
	//
	public static String httpsPost(String url) throws Exception{ 
		return httpsPost(url, null, null);
	}
	//
	public static String httpsPost(String url,Map<String,String> paramMap) throws Exception{ 
		return httpsPost(url, paramMap, null);
	}
	//
	public static String httpsPost(String url,Map<String,String> paramMap,SSLContext sslContext) throws Exception{ 
		Response response = FastHttpClient.
				newBuilder().
				sslContext(sslContext).
				build().
				post().
				url(url).
				addParams(paramMap).
				build().
				
				execute();
		return response.body().string();
	}
	//
	public static String httpsPostWithBody(String url,String body) throws Exception{ 
		return httpsPostWithBody(url, body, null);
	}
	//
	public static String httpsPostWithBody(String url,String body,SSLContext sslContext) throws Exception{ 
		Response response = FastHttpClient.
				newBuilder().
				sslContext(sslContext).
				build().
				post().
				url(url).
				body(body).
				build().
				
				execute();
		return response.body().string();
	}
	//
	private static void checkResult(Response response) {
		if(!response.isSuccessful()) {
			throw new IllegalArgumentException(response.code()+"/"+response.message());
		}
	}
	//
	public static InputStream httpGetReturnInputStream(String url){
		InputStream rspBody=null;
		try {
			Response response=FastHttpClient.get().
					url(url).
					build().
					execute();
			checkResult(response);
			rspBody=response.byteStream();
		}catch (Exception e) {
			throw new RuntimeException(e.getMessage(),e);
		}finally {
			if(logger.isInfoEnabled()){
				logger.info("url:{} \nresponse:{}",url,rspBody);
			}
		}
		return rspBody;
	}
}
