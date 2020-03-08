package io.itit.itf.okhttp.callback;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.itit.itf.okhttp.Response;
import okhttp3.Call;

/**
 * 
 * @author icecooly
 *
 */
public abstract class StringCallback extends Callback{
	//
	public static Logger logger = LoggerFactory.getLogger(StringCallback.class);
	//
	@Override
	public void onResponse(Call call, Response response, String id) {
		try {
			onSuccess(call,response.body().string(),id);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
	}
	//
	@Override
	public void onFailure(Call call,Exception e,String id) {
		logger.error("onFailure id:{}",id);
		logger.error(e.getMessage(),e);
	}
	
	/**
	 * 
	 * @param call
	 * @param response
	 * @param id
	 * @param tag
	 */
	public abstract void onSuccess(Call call,String response,String id);
}
