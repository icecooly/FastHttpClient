package io.itit.itf.okhttp;

import okhttp3.OkHttpClient;

/**
 * 
 * @author icecooly
 *
 */
public class HttpClient {
	//
	public static OkHttpClient okHttpClient = new OkHttpClient();
	//
	public static GetBuilder get() {
		return new GetBuilder();
	}
	//
	public static PostBuilder post(){
        return new PostBuilder();
    }
}
