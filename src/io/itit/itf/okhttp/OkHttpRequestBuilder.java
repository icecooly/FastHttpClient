package io.itit.itf.okhttp;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * 
 * @author icecooly
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class OkHttpRequestBuilder<T extends OkHttpRequestBuilder> {
	protected OkHttpClient httpClient;
	protected String url;
	protected Object tag;
	protected Map<String, String> headers;
	protected Map<String, String> params;
	protected int id;
	//
	public OkHttpRequestBuilder(OkHttpClient httpClient){
		this.httpClient=httpClient;
		headers=new LinkedHashMap<>();
		params=new LinkedHashMap<>();
	}
	//
	public T id(int id) {
		this.id = id;
		return (T) this;
	}

	public T url(String url) {
		this.url = url;
		return (T) this;
	}

	public T tag(Object tag) {
		this.tag = tag;
		return (T) this;
	}

	public T headers(Map<String, String> headers) {
		this.headers = headers;
		return (T) this;
	}

	public T addHeader(String key, String val) {
		headers.put(key, val);
		return (T) this;
	}
	 
	public abstract RequestCall build();
}
