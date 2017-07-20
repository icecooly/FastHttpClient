package io.itit.itf.okhttp;

import okhttp3.OkHttpClient;

/**
 * 
 * @author skydu
 *
 */
public class HttpClient {
	//
	private OkHttpClient okHttpClient;
	//
	public HttpClient(OkHttpClient okHttpClient){
		this.okHttpClient=okHttpClient;
	}
	//
	public GetBuilder get(){
		return new GetBuilder(okHttpClient);
	}
	//
	public PostBuilder post() {
		return new PostBuilder(okHttpClient);
	}
	//
	public OkHttpClient getOkHttpClient() {
		return okHttpClient;
	}
	//
	public void setOkHttpClient(OkHttpClient okHttpClient) {
		this.okHttpClient = okHttpClient;
	}
	
}
