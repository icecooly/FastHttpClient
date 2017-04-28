package io.itit.itf.okhttp.test;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;

/**
 * 
 * @author skydu
 *
 */
public class AuthorizationRequest {
	//
	private static Logger logger=LoggerFactory.getLogger(AuthorizationRequest.class);
	//
	public void request(){
		try {
			String userName="60CF3Ce97nRS1Z1Wp5m9kMmzHHEh8Rkuj31QCtVxjPWGYA9FymyqsK0Enm1P6mHJf0THbR";
			String password="API-P4ss";
			String url="https://api.sandbox.ewaypayments.com/AccessCode/44DD7aVwPYUPemGRf7pcWxyX2FJS-0Wk7xr9iE7Vatk_5vJimEbHveGSqX52B00QsBXqbLh9mGZxMHcjThQ_ITsCZ3JxKOY88WOVsFTLPrGtHRkK0E9ZDVh_Wz326QZlNlwx2";
			String basicInfo=Base64.getEncoder().encodeToString((userName+":"+password).getBytes());
			Response response=FastHttpClient.
					get().
					addHeader("Authorization", "Basic "+basicInfo).
					url(url).
					build().
					execute();
			logger.info(response.string());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	//
	public static void main(String[] args) {
		new AuthorizationRequest().request();
	}
}
