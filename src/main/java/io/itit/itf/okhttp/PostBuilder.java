package io.itit.itf.okhttp;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.itit.itf.okhttp.PostRequest.FileInfo;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;

/**
 * 
 * @author icecooly
 *
 */
public class PostBuilder extends OkHttpRequestBuilder<PostBuilder> {

	private List<FileInfo> fileInfos;
	private String postBody;
	private MultipartBody multipartBody;
	//
	public PostBuilder(OkHttpClient httpClient){
		super(httpClient);
		fileInfos=new ArrayList<>();
	}
	
	@Override
	public RequestCall build() {
		return new PostRequest(
					url,
					tag, 
					params,
					headers,
					fileInfos,
					postBody,
					multipartBody,
					id).
				build(httpClient);
	}

	public PostBuilder params(Map<String, String> params) {
		this.params = params;
		return this;
	}

	public PostBuilder addParams(String key, String val) {
		params.put(key, val);
		return this;
	}
	
	public PostBuilder addParams(Map<String,String> paramMap) {
		if(paramMap==null){
			return this;
		}
		paramMap.forEach((k,v)->{params.put(k, v);});
		return this;
	}
	
	public PostBuilder body(String postBody) {
		this.postBody = postBody;
		return this;
	}
	
	public PostBuilder multipartBody(MultipartBody multipartBody) {
		this.multipartBody = multipartBody;
		return this;
	}
	
	public PostBuilder addFile(String partName,String fileName,byte[] content){
		FileInfo fileInfo=new FileInfo();
		fileInfo.partName=partName;
		fileInfo.fileName=fileName;
		fileInfo.fileContent=content;
		fileInfos.add(fileInfo);
		return this;
	}
	
	public PostBuilder addFile(String partName,String fileName,String content) 
	throws UnsupportedEncodingException{
		return addFile(partName, fileName, content,StandardCharsets.UTF_8.toString());
	}
	
	public PostBuilder addFile(String partName,String fileName,String content,String charsetName) 
	throws UnsupportedEncodingException{
		return addFile(partName, fileName, content.getBytes(charsetName));
	}
	
	public PostBuilder addFile(String partName,String fileName,byte[] content,String charsetName) 
	throws UnsupportedEncodingException{
		return addFile(partName, fileName,content);
	}
}
