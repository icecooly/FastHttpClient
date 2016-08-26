package io.itit.itf.okhttp;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 
 * @author icecooly
 *
 */
public class RequestCall {
	//
	private OkHttpRequest okHttpRequest;
	private Request request;
	private Call call;
	//
	private long readTimeOut;
	private long writeTimeOut;
	private long connTimeOut;
	//
	public RequestCall(OkHttpRequest request) {
		this.okHttpRequest = request;
	}

	public RequestCall readTimeOut(long readTimeOut) {
		this.readTimeOut = readTimeOut;
		return this;
	}

	public RequestCall writeTimeOut(long writeTimeOut) {
		this.writeTimeOut = writeTimeOut;
		return this;
	}

	public RequestCall connTimeOut(long connTimeOut) {
		this.connTimeOut = connTimeOut;
		return this;
	}

	public Call buildCall(Callback callback) {
		request=createRequest(callback);
		//
		if (readTimeOut > 0 || writeTimeOut > 0 || connTimeOut > 0) {
			OkHttpClient.Builder builder=HttpClient.okHttpClient.newBuilder();
			if(connTimeOut>0){
				builder.readTimeout(connTimeOut, TimeUnit.MILLISECONDS);
			}
			if(readTimeOut>0){
				builder.readTimeout(readTimeOut, TimeUnit.MILLISECONDS);
			}
			if(writeTimeOut>0){
				builder.readTimeout(writeTimeOut, TimeUnit.MILLISECONDS);
			}
			OkHttpClient okHttpClient = builder.build();
			call = okHttpClient.newCall(request);
		} else {
			call = HttpClient.okHttpClient.newCall(request);
		}
		return call;
	}

	private Request createRequest(Callback callback) {
		return okHttpRequest.createRequest(callback);
	}
	
	public Response execute() throws IOException {
		buildCall(null);
		return call.execute();
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
				if(finalCallback!=null){
					finalCallback.onFailure(call,e,id);
				}
			}
			@Override
			public void onResponse(final Call call, final Response response) {
				if(finalCallback!=null){
					finalCallback.onResponse(call,response,id);
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
