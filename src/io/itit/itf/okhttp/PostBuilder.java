package io.itit.itf.okhttp;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.itit.itf.okhttp.PostRequest.FileInfo;

/**
 * 
 * @author icecooly
 *
 */
public class PostBuilder extends OkHttpRequestBuilder<PostBuilder> {

	private List<FileInfo> fileInfos;
	//
	public PostBuilder(){
		fileInfos=new ArrayList<>();
	}
	
	@Override
	public RequestCall build() {
		return new PostRequest(url,tag, params,headers,fileInfos, id).build();
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
		paramMap.forEach((k,v)->{params.put(k, v);});
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
		return addFile(partName, fileName, content.getBytes("UTF-8"));
	}
}
