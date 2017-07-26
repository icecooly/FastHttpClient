package io.itit.itf.okhttp;

import java.io.IOException;

import io.itit.itf.okhttp.callback.Callback;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 
 * @author icecooly
 *
 */
public class RequestCall {
	//
	private OkHttpClient okHttpClient;
	private OkHttpRequest okHttpRequest;
	private Request request;
	private Call call;
	//
	public RequestCall(OkHttpRequest request,OkHttpClient okHttpClient) {
		this.okHttpRequest = request;
		this.okHttpClient=okHttpClient;
	}
	
	public Call buildCall(Callback callback) {
		request=createRequest(callback);
		call = okHttpClient.newCall(request);
		return call;
	}

	private Request createRequest(Callback callback) {
		return okHttpRequest.createRequest(callback);
	}
	
	public Response execute() throws Exception{
		buildCall(null);
		try {
			Response rsp=new Response(call.execute());
			if(rsp.isSuccessful()){
				FastHttpClientStat.onReqSuccess();
			}else{
				FastHttpClientStat.onReqFailure(call.request().url().toString(),null);
			}
			return rsp;
		} catch (Exception e) {
			FastHttpClientStat.onReqFailure(call.request().url().toString(),e);
			throw e;
		}
	}

	public void executeAsync(Callback callback) {
		buildCall(callback);
		execute(this,callback);
	}

	private void execute(final RequestCall requestCall, Callback callback) {
		final Callback finalCallback = callback;
		final int id = requestCall.getOkHttpRequest().getId();
		requestCall.getCall().enqueue(new okhttp3.Callback() {
			@Override
			public void onFailure(Call call, final IOException e) {
				FastHttpClientStat.onReqFailure(call.request().url().toString(),e);
				if(finalCallback!=null){
					finalCallback.onFailure(call,e,id);
				}
			}
			@Override
			public void onResponse(final Call call, final okhttp3.Response response) {
				FastHttpClientStat.onReqSuccess();
				if(finalCallback!=null){
					finalCallback.onResponse(call,new Response(response),id);
				}
			}
		});
	}
	
	public Call getCall() {
		return call;
	}

	public Request getRequest() {
		return request;
	}

	public OkHttpRequest getOkHttpRequest() {
		return okHttpRequest;
	}

	public void cancel() {
		if (call != null) {
			call.cancel();
		}
	}
}
