package com.service;

import org.json.JSONObject;

import android.util.Log;

import com.util.CacheManager;
import com.util.HttpUtil;

public class PlayerRequestService {
	public static JSONObject doGetSMSCode(String tel) {
		// 使用Map封装请求参数
		JSONObject returnJson  =new JSONObject();	
		JSONObject postJson  =new JSONObject();	
		// 定义发送请求的URL
		String url = HttpUtil.BASE_URL + "player/smscode/application"; 
		Log.d("url", url);
		Log.d("tel", tel);
		try {
			postJson.put("phoneNumber", tel);			
			returnJson = HttpUtil.postRequestForJson(url, postJson); // POST方式
			Log.d("doGetTelConfirmCode 服务器返回值", returnJson.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnJson;
	}
	
	
	public static JSONObject doRegisterUser(String tel,String confirmCode,String userName,String password) {		
		// 定义发送请求的URL
		JSONObject returnJson  =new JSONObject();	
		JSONObject postJson  =new JSONObject();	
			
		
		String url = HttpUtil.BASE_URL + "player/registeration"; 
		try {
			postJson.put("name", userName);
			postJson.put("phoneNumber", tel);
			postJson.put("password", password);
			postJson.put("smsCode", confirmCode);
			returnJson = HttpUtil.postRequestForJson(url, postJson); // POST方式
			Log.d("doRegisterUser 服务器返回值", returnJson.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		return returnJson;
	}
	

	// 定义发送请求的方法
	public static    JSONObject doLogin(String tel, String password) {
		
		// 使用Map封装请求参数
		JSONObject returnJson  =new JSONObject();	
		JSONObject postJson  =new JSONObject();
		
		// 定义发送请求的URL
		String url = HttpUtil.BASE_URL + "player/login";
		Log.d("url", url);
		try {
			postJson.put("phoneNumber", tel);
			postJson.put("password", password);
			returnJson = HttpUtil.postRequestForJson(url, postJson);
					
			Log.d(" doLogin 服务器返回值", returnJson.toString());
			
			
//			JSONObject userInfoJson=getUserInfo(tel);
//			CacheManager.getInstance(tel).setUserInfoJson(userInfoJson);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnJson;
	}
	
	
	public static    JSONObject doMountRomoteClient(String tel, String identifier) {
		
		// 使用Map封装请求参数
		JSONObject returnJson  =new JSONObject();	
		JSONObject postJson  =new JSONObject();
		
		// 定义发送请求的URL
		String url = HttpUtil.BASE_URL + "player/mount";
		Log.d("url", url);
		try {
			Log.d("phoneNumber", tel);
			Log.d("identifier", identifier);
			postJson.put("phoneNumber", Long.valueOf(tel));
			postJson.put("identifier", identifier);
			returnJson = HttpUtil.postRequestForJson(url, postJson);
			JSONObject userInfoJson=getUserInfo(tel);
//			CacheManager.getInstance(tel).setUserInfoJson(userInfoJson);		
//			Log.d(" doLoginRomoteClient 服务器返回值", returnJson.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnJson;
	}
	
	public static    JSONObject doUnMountRomoteClient(String tel, String identifier) {
		
		// 使用Map封装请求参数
		JSONObject returnJson  =new JSONObject();	
		JSONObject postJson  =new JSONObject();
		
		// 定义发送请求的URL
		String url = HttpUtil.BASE_URL + "player/unmount";
		Log.d("url", url);
		try {
			Log.d("phoneNumber", tel);
			Log.d("identifier", identifier);
			postJson.put("phoneNumber", Long.valueOf(tel));
			postJson.put("identifier", identifier);
			returnJson = HttpUtil.postRequestForJson(url, postJson);
			JSONObject userInfoJson=getUserInfo(tel);
//			CacheManager.getInstance(tel).setUserInfoJson(userInfoJson);		
//			Log.d("unmountRomoteClient 服务器返回值", returnJson.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnJson;
	}
	
	
	public static    JSONObject playerLogout(String tel) {
		
		// 使用Map封装请求参数
		JSONObject returnJson  =new JSONObject();	
		JSONObject postJson  =new JSONObject();
		
		// 定义发送请求的URL
		String url = HttpUtil.BASE_URL + "player/logout";
		Log.d("url", url);
		try {
			Log.d("phoneNumber", tel);			
			postJson.put("phoneNumber", Long.valueOf(tel));			
			returnJson = HttpUtil.postRequestForJson(url, postJson);
					
			Log.d("playerLogout   服务器返回值", returnJson.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnJson;
	}
	
	
	public static JSONObject playerAmountWithdraw(String tel,double amount,int withdrawType){
		// 使用Map封装请求参数
		JSONObject returnJson  =new JSONObject();	
		JSONObject postJson  =new JSONObject();
		
		// 定义发送请求的URL
		String url = HttpUtil.BASE_URL + "player/telWithdraw";
		Log.d("playerAmountWithdraw url", url);
		try {
			Log.d("playerAmountWithdraw", "phoneNumber="+tel+",withdrawType="+withdrawType+",amount="+amount);			
			postJson.put("phoneNumber", Long.valueOf(tel));
		//	postJson.put("withdrawType", Long.valueOf(withdrawType));	
			postJson.put("amount", Double.valueOf(amount));	
			returnJson = HttpUtil.postRequestForJson(url, postJson);
			if(returnJson==null){
				Log.d("playerAmountWithdraw   服务器返回值", "null");	
			}else{
				Log.d("playerAmountWithdraw   服务器返回值", returnJson.toString());
			}		
//			JSONObject userInfoJson=getUserInfo(tel);
//			CacheManager.getInstance(tel).setUserInfoJson(userInfoJson);
			
		} catch (Exception e) {
			Log.d("playerAmountWithdraw   服务器异常", e.getMessage());
		}
		return returnJson;
	}
	
	public static JSONObject getUserInfo(String tel){
		// 使用Map封装请求参数
			JSONObject returnJson  =new JSONObject();	
			JSONObject postJson  =new JSONObject();
			
			// 定义发送请求的URL
			String url = HttpUtil.BASE_URL + "player/information";
			Log.d("getUserInfo url", url);
			try {
				Log.d("getUserInfo", "phoneNumber="+tel);			
				postJson.put("phoneNumber",Long.valueOf(tel));		
				returnJson = HttpUtil.postRequestForJson(url, postJson);
				if(returnJson==null){
					Log.d("getUserInfo   服务器返回值", "null");	
				}else{
					Log.d("getUserInfo   服务器返回值", returnJson.toString());
				}
				
			} catch (Exception e) {
				Log.d("getUserInfo   服务器异常", e.getMessage());
			}
			return returnJson;
	}
	
	public static void  main(String[] args){
		System.out.print(getUserInfo("13787210140"));
	}
}
