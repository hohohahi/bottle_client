package com.broadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public abstract class MyBroadcastReceiver extends BroadcastReceiver {
	protected JSONObject retJson;
	
	
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		 String message = arg1.getStringExtra("result");    
		 try {
			retJson=new JSONObject(message);
			
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	
	
	
	 protected JSONObject getData(){
		
		try {				
			JSONObject dataJson= retJson.getJSONObject("data");				
			if( dataJson !=null ){
				Log.d("getData ", dataJson.toString());
				return  dataJson;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	protected String getErrorMsg(){
		
		Object data;
		try {
			data = this.retJson.get("errorMessage");
			if(data instanceof String){
				Log.d("errorMessage", (String)data);
				return (String)data;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	protected String getErrorCode(){
		String erroCode1="unknown";
		if (retJson!=null) {
			try {
				Object erroCode = retJson.get("errorCode");
				
				if(erroCode instanceof String){
					String errorCodeStr=(String)erroCode;
					Log.d("errorCodeStr  ", errorCodeStr);
					return errorCodeStr;
				}
				if(erroCode instanceof Integer){
					
					Integer errorCodeInt=(Integer)erroCode;
					return errorCodeInt.toString();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} 
		return erroCode1;		
	}
	protected boolean isSucceed(){
		
		boolean returnFlag=false;
		if (retJson!=null) {
			try {
				Object erroCode = retJson.get("errorCode");
				
				if(erroCode instanceof String){
					String errorCodeStr=(String)erroCode;
					Log.d("errorCodeStr  ", errorCodeStr);
					if("0".equals(errorCodeStr)){
						returnFlag=true;
					}
				}
				if(erroCode instanceof Integer){
					
					Integer errorCodeInt=(Integer)erroCode;
					Log.d("  errorCodeInt",String.valueOf(errorCodeInt ));
					if(0 == errorCodeInt){
						returnFlag=true;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} 
		return returnFlag;		
	}
	

}
