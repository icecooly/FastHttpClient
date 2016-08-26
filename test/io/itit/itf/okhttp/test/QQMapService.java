package io.itit.itf.okhttp.test;

import java.io.IOException;

import io.itit.itf.okhttp.HttpClient;
import okhttp3.Response;

/**
 * http://lbs.qq.com/webservice_v1/guide-gcoder.html
 * @author icecooly
 *
 */
public class QQMapService {
	//
	String key;
	
	public QQMapService() {
		key="ZXRBZ-HX5KJ-M25F3-KZOTJ-BKHAQ-OABTC";
	}
	
	public void searchLocation(String region,String keyword){
		String url="http://apis.map.qq.com/ws/place/v1/suggestion?keyword="+keyword+
				"&key="+key;
		try {
			Response response=HttpClient.get().url(url).build().execute();
			if(response.code()!=200){
				throw new IllegalArgumentException("定位失败，请稍后再试");
			}
			System.out.println(response.body().string());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//
	public static void main(String[] args) {
		QQMapService service=new QQMapService();
		service.searchLocation(null,"深圳市南山区飞亚达大厦");
	}
}
