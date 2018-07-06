package io.itit.itf.okhttp;

import java.io.File;
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
public class PutBuilder extends OkHttpRequestBuilder<PutBuilder> {

	private List<FileInfo> fileInfos;
	private String body;
	private MultipartBody multipartBody;
	//
	public PutBuilder(OkHttpClient httpClient){
		super(httpClient);
		fileInfos=new ArrayList<>();
	}
	
	@Override
	public RequestCall build() {
		return new PutRequest(
					url,
					tag, 
					params,
					headers,
					fileInfos,
					body,
					multipartBody,
					id).
				build(httpClient);
	}

	public PutBuilder params(Map<String, String> params) {
		this.params = params;
		return this;
	}

	public PutBuilder addParams(String key, String val) {
		params.put(key, val);
		return this;
	}
	
	public PutBuilder addParams(Map<String,String> paramMap) {
		if(paramMap==null){
			return this;
		}
		paramMap.forEach((k,v)->{params.put(k, v);});
		return this;
	}
	
	public PutBuilder body(String body) {
		this.body = body;
		return this;
	}
	
	public PutBuilder multipartBody(MultipartBody multipartBody) {
		this.multipartBody = multipartBody;
		return this;
	}
	
	public PutBuilder addFile(String partName,String fileName,byte[] content){
		FileInfo fileInfo=new FileInfo();
		fileInfo.partName=partName;
		fileInfo.fileName=fileName;
		fileInfo.fileContent=content;
		fileInfos.add(fileInfo);
		return this;
	}
	
	public PutBuilder addFile(String partName,String fileName,File file){
		FileInfo fileInfo=new FileInfo();
		fileInfo.partName=partName;
		fileInfo.fileName=fileName;
		fileInfo.file=file;
		fileInfos.add(fileInfo);
		return this;
	}
	
	public PutBuilder addFile(String partName,String fileName,String content) 
	throws UnsupportedEncodingException{
		return addFile(partName, fileName, content,StandardCharsets.UTF_8.toString());
	}
	
	public PutBuilder addFile(String partName,String fileName,String content,String charsetName) 
	throws UnsupportedEncodingException{
		return addFile(partName, fileName, content.getBytes(charsetName));
	}
	
	public PutBuilder addFile(String partName,String fileName,byte[] content,String charsetName) 
	throws UnsupportedEncodingException{
		return addFile(partName, fileName,content);
	}
}
