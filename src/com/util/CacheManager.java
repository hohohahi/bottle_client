package com.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.service.PlayerRequestService;

public class CacheManager {
	private static double userAmount=-1d;
	private static String tel;
	private static CacheManager cache=null;
	private static JSONObject userInfo=null;
	
	
	private CacheManager(){
		ExecutorService exeService = Executors.newFixedThreadPool(1);
		exeService.execute(new GetInfoTask());
		
	}
	
	public static CacheManager getInstance(String telStr){
		if(cache==null){
			cache= new CacheManager();
		}
		tel=telStr;
		return cache;
	}
	
	
	public  JSONObject getUserInfo() {
		return userInfo;
	}

	public  void setUserInfoJson(JSONObject userInfoJson){
	
		if(userInfoJson!=null){
			
			try {				
				JSONObject dataJson= userInfoJson.getJSONObject("data");	
				if( dataJson !=null ){
					userInfo=dataJson;
					Log.d("setUserInfoJson ", dataJson.toString());
					Object amountObj=dataJson.get("amount");
					if(amountObj instanceof Double){
						userAmount=(Double)amountObj;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	public static CacheManager getInstance(){
		if(cache==null){
			cache= new CacheManager();
		}
		return cache;
	}
	
	public  String getTel() {
		return tel;
	}

	public  void setTel(String tel) {
		CacheManager.tel = tel;
	}

	public double getUserAmount() {
		return userAmount;
	}


	class GetInfoTask implements Runnable{

		@Override
		public void run() {
			while(true){
				JSONObject returnJson =PlayerRequestService.getUserInfo(tel);
				if(null!=returnJson){
					setUserInfoJson(returnJson);
				}
				try {
					Thread.sleep(10000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
				
		}
		
	}
}
