package io.itit.itf.okhttp;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import okhttp3.OkHttpClient;

/**
 * 
 * @author icecooly
 *
 */
public class HttpClient {
	//
	public static Log log = LogFactory.getLog(HttpClient.class);
	//
	public static OkHttpClient okHttpClient=getDefaultOkHttpClient();
	//
	public static OkHttpClient getDefaultOkHttpClient() {
		OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
		final X509TrustManager trustManager = new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
			}
			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) 
			throws CertificateException {
			}
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
		};
		SSLSocketFactory sslSocketFactory=null;
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, new TrustManager[] { trustManager }, new SecureRandom());
			sslSocketFactory = sslContext.getSocketFactory();
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return builder.sslSocketFactory(sslSocketFactory, trustManager).hostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		}).build();
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
