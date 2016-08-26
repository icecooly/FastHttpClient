package io.itit.itf.okhttp;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 
 * @author icecooly
 */
public abstract class Callback{
	//
	public abstract void onFailure(Call call,Exception e,int id);
	//
	public abstract void onResponse(Call call,Response response, int id);
}