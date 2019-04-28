# FastHttpClient
封装OkHttp3

- 支持多线程异步请求
- 支持Http/Https协议
- 支持同步/异步请求
- 支持异步延迟执行
- 支持Cookie持久化
- 支持JSON、表单提交
- 支持文件和图片上传/批量上传，支持同步/异步上传，支持进度提示
- 支持文件流上传


Download
--------

Download Jar or grab via Maven:
```xml
<dependency>
  <groupId>com.github.icecooly</groupId>
  <artifactId>FastHttpClient</artifactId>
  <version>1.6</version>
</dependency>
```
or Gradle:
```groovy
compile 'com.github.icecooly:FastHttpClient:1.6'
```

简单的例子
==============
1.同步Get请求(访问百度首页,自动处理https单向认证)
```java
String url="https://www.baidu.com";
String resp=FastHttpClient.get().url(url).build().execute().string();
```

2.异步Get请求(访问百度首页)
```java
FastHttpClient.get().url("https://www.baidu.com").build().
	executeAsync(new StringCallback() {
		@Override
		public void onFailure(Call call, Exception e, int id) {
			logger.error(e.getMessage(),e);
		}
		@Override
		public void onSuccess(Call call, String response, int id) {
			logger.info("response:{}",response);
		}
	});
```

3.百度搜索关键字'微信机器人'
```java
String html = FastHttpClient.get().
				url("http://www.baidu.com/s").
				addParams("wd", "微信机器人").
				addParams("tn", "baidu").
				build().
				execute().
				string();
```

4.异步下载一张百度图片，有下载进度,保存为/tmp/tmp.jpg
```java
String savePath="/tmp/tmp.jpg";
FastHttpClient.get().
		url("http://e.hiphotos.baidu.com/image/pic/item/faedab64034f78f0b31a05a671310a55b3191c55.jpg").
		build().addNetworkInterceptor(new DownloadFileInterceptor(){
			@Override
			public void updateProgress(long downloadLenth, long totalLength, boolean isFinish) {
				System.out.println("updateProgress downloadLenth:"+downloadLenth+
						",totalLength:"+totalLength+",isFinish:"+isFinish);
			}
		}).
		executeAsync(new DownloadFileCallback(savePath) {
				@Override
				public void onFailure(Call call, Exception e, int id) {
					e.printStackTrace();
				}
				@Override
				public void onSuccess(Call call, File file, int id) {
					super.onSuccess(call, file, id);
					System.out.println("filePath:"+file.getAbsolutePath());
				}
				@Override
				public void onSuccess(Call call, InputStream fileStream, int id) {
					System.out.println("onSuccessWithInputStream");
				}
		});
```

5.同步下载文件
```java
public void testSyncDownloadFile() throws Exception{
	String savePath="tmp.jpg";
	String imageUrl="http://e.hiphotos.baidu.com/image/pic/item/faedab64034f78f0b31a05a671310a55b3191c55.jpg";
	InputStream is=FastHttpClient.get().url(imageUrl).build().execute().byteStream();
	FileUtil.saveContent(is, new File(savePath));
}
```
	
6.上传文件
```java
byte[] imageContent=FileUtil.getBytes("/tmp/test.png");
		response = FastHttpClient.post().
				url(url).
				addFile("file", "b.jpg", imageContent).
				build().
				connTimeOut(10000).
				execute();
System.out.println(response.body().string());
```

7.上传文件(通过文件流)
```java
InputStream is=new FileInputStream("/tmp/logo.jpg");
Response response = FastHttpClient.newBuilder().
		connectTimeout(10, TimeUnit.SECONDS).
		build().
		post().
		url("上传地址").
		addFile("file", "logo.jpg",is).
		build().
		execute();
logger.info(response.body().string());
```

8.设置网络代理
```java
Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 1088));
Authenticator.setDefault(new Authenticator(){//如果没有设置账号密码，则可以注释掉这块
	         private PasswordAuthentication authentication = 
	         		new PasswordAuthentication("username","password".toCharArray());
	         @Override
	         protected PasswordAuthentication getPasswordAuthentication(){
	             return authentication;
	         }
	     });
Response response = FastHttpClient.
		newBuilder().
		proxy(proxy).
		build().
		get().
		url("http://ip111.cn/").
		build().
		execute();
logger.info(response.string());
```

9.设置Http头部信息
```java
String url="https://www.baidu.com";
Response response=FastHttpClient.
			get().
			addHeader("Referer","http://news.baidu.com/").
			addHeader("cookie", "uin=test;skey=111111;").
			url(url).
			build().
			execute();
System.out.println(response.string());
```

9.设置https证书
```java
SSLContext sslContext=getxxx();
Response response=FastHttpClient.
			get().
			sslContext(sslContext).
			url(url).
			build().
			execute();
System.out.println(response.string());
```
