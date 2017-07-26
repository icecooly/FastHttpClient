package io.itit.itf.okhttp;

import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * 
 * @author icecooly
 *
 */
public class GetBuilder extends OkHttpRequestBuilder<GetBuilder> {
	//
	public GetBuilder(OkHttpClient httpClient) {
		super(httpClient);
	}

	@Override
	public RequestCall build() {
		if (params != null) {
			url = appendParams(url, params);
		}
		return new GetRequest(url, tag, params, headers, id).build(httpClient);
	}

	protected String appendParams(String url, Map<String, String> params) {
		if (url == null || params == null || params.isEmpty()) {
			return url;
		}
		StringBuilder builder = new StringBuilder();
		params.forEach((k,v)->{
			if(builder.length()==0){
				builder.append("?");
			}else if (builder.length()>0) {
				builder.append("&");
			}
			builder.append(k);
			builder.append("=").append(v);
		});
		return url+builder.toString();
	}

	public GetBuilder params(Map<String, String> params) {
		this.params = params;
		return this;
	}

	public GetBuilder addParams(String key, String val) {
		params.put(key, val);
		return this;
	}

}
