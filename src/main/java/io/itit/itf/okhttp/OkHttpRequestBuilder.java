package io.itit.itf.okhttp;

import java.util.LinkedHashMap;
import java.util.Map;

import io.itit.itf.okhttp.util.ClassUtils;
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
	protected Map<String, String> encodedParams;
	protected String id;
	//
	public OkHttpRequestBuilder(OkHttpClient httpClient){
		this.httpClient=httpClient;
		headers=new LinkedHashMap<>();
		params=new LinkedHashMap<>();
		encodedParams=new LinkedHashMap<>();
	}
	//
	public T id(String id) {
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

	public T addHeaders(Map<String, String> headers) {
		if(headers!=null) {
			headers.forEach((k,v)->{this.headers.put(k, v);});
		}
		return (T) this;
	}
	
	public T addHeader(String key, String val) {
		headers.put(key, val);
		return (T) this;
	}
	
	public T params(Map<String, String> params) {
		this.params = params;
		return (T) this;
	}
	
	public T addParams(String key, String val) {
		this.params.put(key, val);
		return (T)this;
	}
	
	public T addParams(Map<String,String> paramMap) {
		if(paramMap==null){
			return (T) this;
		}
		paramMap.forEach((k,v)->{params.put(k, v);});
		return (T) this;
	}
	
	public T addParams(Object obj) {
		if(obj!=null) {
			Map<String, String> map=ClassUtils.objectToMap(obj);
			map.forEach((key,val)->{
				addParams(key, val);
			});
		}
		return (T) this;
	}
	

	public T encodedParams(Map<String, String> params) {
		this.encodedParams = params;
		return (T) this;
	}

	public T addEncodedParams(String key, String val) {
		this.encodedParams.put(key, val);
		return (T) this;
	}
	 
	public abstract RequestCall build();
}
