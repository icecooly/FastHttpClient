package io.itit.itf.okhttp;

import java.net.Proxy;
import java.net.ProxySelector;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.itit.itf.okhttp.ssl.X509TrustManagerImpl;
import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.CertificatePinner;
import okhttp3.ConnectionPool;
import okhttp3.ConnectionSpec;
import okhttp3.CookieJar;
import okhttp3.Dispatcher;
import okhttp3.Dns;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Protocol;

/**
 * 
 * @author skydu
 *
 */
public class FastHttpClientBuilder {
	//
	private static Logger logger=LoggerFactory.getLogger(FastHttpClientBuilder.class);
	//
	private Builder builder;
	//
	public FastHttpClientBuilder(){
		this.builder =new Builder();
	}
	//
	public FastHttpClientBuilder(OkHttpClient okHttpClient){
		this.builder =okHttpClient.newBuilder();
	}
	//
	public FastHttpClientBuilder connectTimeout(long timeout, TimeUnit unit) {
		builder.connectTimeout(timeout, unit);
		return this;
	}

	public FastHttpClientBuilder readTimeout(long timeout, TimeUnit unit) {
		builder.readTimeout(timeout, unit);
		return this;
	}

	/**
	 * 
	 * @param timeout
	 * @param unit
	 * @return
	 */
	public FastHttpClientBuilder writeTimeout(long timeout, TimeUnit unit) {
		builder.writeTimeout(timeout, unit);
		return this;
	}

	/**
	 * 
	 * @param interval
	 * @param unit
	 * @return
	 */
	public FastHttpClientBuilder pingInterval(long interval, TimeUnit unit) {
		builder.pingInterval(interval, unit);
		return this;
	}

	/**
	 * 
	 * @param proxy
	 * @return
	 */
	public FastHttpClientBuilder proxy(Proxy proxy) {
		builder.proxy(proxy);
		return this;
	}

	/**
	 * 
	 * @param proxySelector
	 * @return
	 */
	public FastHttpClientBuilder proxySelector(ProxySelector proxySelector) {
		builder.proxySelector(proxySelector);
		return this;
	}

	/**
	 * 
	 * @param cookieJar
	 * @return
	 */
	public FastHttpClientBuilder cookieJar(CookieJar cookieJar) {
		builder.cookieJar(cookieJar);
		return this;
	}

	/**
	 * 
	 * @param cache
	 * @return
	 */
	public FastHttpClientBuilder cache(Cache cache) {
		builder.cache(cache);
		return this;
	}

	/**
	 * 
	 * @param dns
	 * @return
	 */
	public FastHttpClientBuilder dns(Dns dns) {
		builder.dns(dns);
		return this;
	}

	/**
	 * 
	 * @param socketFactory
	 * @return
	 */
	public FastHttpClientBuilder socketFactory(SocketFactory socketFactory) {
		builder.socketFactory(socketFactory);
		return this;
	}

	/**
	 * 
	 * @param sslSocketFactory
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public FastHttpClientBuilder sslSocketFactory(SSLSocketFactory sslSocketFactory) {
		builder.sslSocketFactory(sslSocketFactory);
		return this;
	}

	/**
	 * 
	 * @param sslSocketFactory
	 * @param trustManager
	 * @return
	 */
	public FastHttpClientBuilder sslSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager) {
		builder.sslSocketFactory(sslSocketFactory, trustManager);
		return this;
	}

	/**
	 * 
	 * @param hostnameVerifier
	 * @return
	 */
	public FastHttpClientBuilder hostnameVerifier(HostnameVerifier hostnameVerifier) {
		builder.hostnameVerifier(hostnameVerifier);
		return this;
	}

	public FastHttpClientBuilder certificatePinner(CertificatePinner certificatePinner) {
		builder.certificatePinner(certificatePinner);
		return this;
	}

	public FastHttpClientBuilder authenticator(Authenticator authenticator) {
		builder.authenticator(authenticator);
		return this;
	}

	public FastHttpClientBuilder proxyAuthenticator(Authenticator proxyAuthenticator) {
		builder.proxyAuthenticator(proxyAuthenticator);
		return this;
	}

	public FastHttpClientBuilder connectionPool(ConnectionPool connectionPool) {
		builder.connectionPool(connectionPool);
		return this;
	}

	public FastHttpClientBuilder followSslRedirects(boolean followProtocolRedirects) {
		builder.followSslRedirects(followProtocolRedirects);
		return this;
	}

	public FastHttpClientBuilder followRedirects(boolean followRedirects) {
		builder.followRedirects(followRedirects);
		return this;
	}

	public FastHttpClientBuilder retryOnConnectionFailure(boolean retryOnConnectionFailure) {
		builder.retryOnConnectionFailure(retryOnConnectionFailure);
		return this;
	}

	public FastHttpClientBuilder dispatcher(Dispatcher dispatcher) {
		builder.dispatcher(dispatcher);
		return this;
	}

	public FastHttpClientBuilder protocols(List<Protocol> protocols) {
		builder.protocols(protocols);
		return this;
	}

	public FastHttpClientBuilder connectionSpecs(List<ConnectionSpec> connectionSpecs) {
		builder.connectionSpecs(connectionSpecs);
		return this;
	}

	public FastHttpClientBuilder addInterceptor(Interceptor interceptor) {
		builder.addInterceptor(interceptor);
		return this;
	}

	public FastHttpClientBuilder addNetworkInterceptor(Interceptor interceptor) {
		builder.addNetworkInterceptor(interceptor);
		return this;
	}
	//
	/**
	 * @return the builder
	 */
	public okhttp3.OkHttpClient.Builder getBuilder() {
		return builder;
	}
	
	/**
	 * 
	 * @param sslContext
	 * @return
	 */
	public FastHttpClientBuilder sslContext(SSLContext sslContext) {
		SSLSocketFactory sslSocketFactory=null;
		final X509TrustManager trustManager=new X509TrustManagerImpl();
		try {
			sslSocketFactory = sslContext.getSocketFactory();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		builder.sslSocketFactory(sslSocketFactory, trustManager).
		hostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
		return this;
	}
	/**
	 * 
	 * @return
	 */
	public HttpClient build() {
		HttpClient httpClient=new HttpClient(builder.build());
		return httpClient;
	}
}
