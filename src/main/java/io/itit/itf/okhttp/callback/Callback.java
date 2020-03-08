package io.itit.itf.okhttp.callback;

import io.itit.itf.okhttp.Response;
import okhttp3.Call;

/**
 * 
 * @author icecooly
 */
public abstract class Callback{
	//
	public abstract void onFailure(Call call,Exception e,String id);
	//
	public abstract void onResponse(Call call,Response response, String id);
}