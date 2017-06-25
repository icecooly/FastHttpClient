package io.itit.itf.okhttp;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.itit.itf.okhttp.ssl.X509TrustManagerImpl;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * 
 * @author icecooly
 *
 */
public class FastHttpClient {
	//
	public static Logger logger = LoggerFactory.getLogger(FastHttpClient.class);
	//
	public static final String VERSION="1.1";
	//
	public static OkHttpClient okHttpClient=getDefaultOkHttpClient();
	//
	private static OkHttpClient getDefaultOkHttpClient() {
		OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
		//
		builder.cookieJar(
				new CookieJar() {
			 private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
			@Override
			public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
				dumpCookie("saveFromResponse",cookies);
				cookieStore.put(url.host(), cookies);
			}
			@Override
			public List<Cookie> loadForRequest(HttpUrl url) {
				List<Cookie> cookies = cookieStore.get(url.host());
				dumpCookie("loadForRequest",cookies);
               return cookies!=null?cookies:new ArrayList<Cookie>();
			}
		});
		final X509TrustManager trustManager=new X509TrustManagerImpl();
		SSLSocketFactory sslSocketFactory=null;
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, new TrustManager[] { trustManager },new SecureRandom());
			sslSocketFactory = sslContext.getSocketFactory();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return builder.sslSocketFactory(sslSocketFactory, trustManager).hostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		}).build();
	}
	//
	private static void dumpCookie(String func,List<Cookie> cookies){
		if(cookies!=null){
			for (Cookie cookie : cookies) {
				if(logger.isDebugEnabled()){
					logger.debug("func:{} cookie:{} {} {}",
							func,
							cookie.name(),
							cookie.domain(),
							cookie.value());
				}
			}
		}
	}
	//
	public static GetBuilder get() {
		return new GetBuilder();
	}
	//
	public static PostBuilder post() {
		return new PostBuilder();
	}
}
