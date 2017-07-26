package io.itit.itf.okhttp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author icecooly
 *
 */
public class FastHttpClientStat {
	//
	private static final int MAX_ERROR_MSG_COUNT=100;
	//
	private static boolean isStop=false;
	//
	protected static AtomicInteger reqTotalCount=new AtomicInteger(0);
	protected static AtomicInteger reqFailureCount=new AtomicInteger(0);
	protected static AtomicInteger reqExceptionCount=new AtomicInteger(0);
	protected static Date startTime=new Date();
	protected static Date lastAccessTime;
	protected static LinkedBlockingDeque<String> errorMsgs=new LinkedBlockingDeque<String>(MAX_ERROR_MSG_COUNT);
	//
	public static void stopStat(){
		FastHttpClientStat.isStop=true;
	}
	//
	public static int getReqTotalCount(){
		return reqTotalCount.get();
	}
	//
	public static int getReqFailureCount(){
		return reqFailureCount.get();
	}
	//
	public static int getReqExceptionCount(){
		return reqExceptionCount.get();
	}
	//
	public static Date getStartTime() {
		return startTime;
	}
	//
	public static Date getLastAccessTime() {
		return lastAccessTime;
	}
	//
	public static LinkedBlockingDeque<String> getErrorMsgs() {
		return errorMsgs;
	}
	//
	protected static void onReqFailure(String url,Exception e){
		if(isStop){
			return;
		}
		lastAccessTime=new Date();
		reqTotalCount.incrementAndGet();
		reqFailureCount.incrementAndGet();
		if(e!=null){
			reqExceptionCount.incrementAndGet();
			if(errorMsgs.size()>=MAX_ERROR_MSG_COUNT){
				errorMsgs.removeFirst();
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			StringBuilder errorMsg=new StringBuilder();
			errorMsg.append(sdf.format(new Date())).append("\t").
				append(url).append("\t").
				append(e.getClass().getName()).append("\t").
				append(e.getMessage());
			errorMsgs.add(errorMsg.toString());
		}
	}
	
	protected static void onReqSuccess(){
		if(isStop){
			return;
		}
		lastAccessTime=new Date();
		reqTotalCount.incrementAndGet();
	}
}
