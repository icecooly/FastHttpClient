# okhttpclient
简易封装OkHttp(jdk8以上)

OKHttpClient
======
powered by icecooly(icecooly.du@gmail.com).

Usage
==============
1.synchronized get
```java
HttpClient.get().
		url(url).
		addParams("userName", "icecool").
		addParams("password", "111111").
		build().
		execute();
```
		
2.synchronized post
```java
HttpClient.post().
		url(url).
		addParams("userName", "icecool").
		addParams("password", "111111").
		build().
		execute();
```

3.asynchronized get
```java
HttpClient.get().
			url(url).
			addParams("userName","icecool").
			addParams("password", "111111").
			build().
			executeAsync(new Callback() {
			@Override
			public void onFailure(Call call, Exception e, int id) {
				//TODO
			}

			@Override
			public void onResponse(Call call,Response response, int id) {
				try {
					System.out.println(response.body().string());
				} catch (IOException e) {
				}
			}
		});
```
		
4.post file
```java
byte[] imageContent=FileUtil.getBytes("/tmp/test.png");
		response = HttpClient.post().
				url(url).
				addFile("file1", "a.txt", "123").
				addFile("file2", "b.jpg", imageContent).
				build().
				connTimeOut(10000).
				execute();
System.out.println(response.body().string());
```
5.https TODO
