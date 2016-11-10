package com.service;

import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class PlayerIntentService extends IntentService {
	public static final String ACTION_RECV_MSG_Login = "com.example.logindemo.action.RECEIVE_MESSAGE_Login";
	public static final String ACTION_RECV_MSG_Register = "com.example.logindemo.action.RECEIVE_MESSAGE_Register";
	public static final String ACTION_RECV_MSG_MountRemoteClient = "com.example.logindemo.action.RECEIVE_MESSAGE_MountRemoteClient";
	public static final String ACTION_RECV_MSG_UnmountRemoteClient = "com.example.logindemo.action.RECEIVE_MESSAGE_UnmountRemoteClient";
	public static final String ACTION_RECV_MSG_Logout = "com.example.logindemo.action.RECEIVE_Logout";
	public static final String ACTION_RECV_MSG_AmountWithdraw = "com.example.logindemo.action.RECEIVE_AmountWithdraw";
	
	
	
	public static final String ActionType_Key = "actionType";
	public static final String ActionType_UserLogin = "1";
	public static final String ActionType_GetRegisterCode = "2";
	public static final String ActionType_UserRegister="3";
	public static final String ActionType_MountRemoteClient="4";
	public static final String ActionType_UnmountRemoteClient="5";
	public static final String ActionType_UserLogout="6";
	public static final String ActionType_UserAmountWithdraw="7";

	public PlayerIntentService() {
		super("TestIntentService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		/**
		 * 经测试，IntentService里面是可以进行耗时的操作的 IntentService使用队列的方式将请求的Intent加入队列，
		 * 然后开启一个worker thread(线程)来处理队列中的Intent
		 * 对于异步的startService请求，IntentService会处理完成一个之后再处理第二个
		 */

		// 通过intent获取主线程传来的用户名和密码字符串
		String actionType = intent.getStringExtra("actionType");
		Log.d("actionType", actionType);
		if (ActionType_UserLogin.equals(actionType)) {
			String username = intent.getStringExtra("username");
			String password = intent.getStringExtra("password");
			
			JSONObject returnJson = PlayerRequestService.doLogin(username, password);
			
			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction(ACTION_RECV_MSG_Login);
			broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
			broadcastIntent.putExtra("result",returnJson.toString());
			sendBroadcast(broadcastIntent);
			
			
		} else if (ActionType_GetRegisterCode.equals(actionType)) {
			String tel = intent.getStringExtra("tel");
			JSONObject  flag =PlayerRequestService.doGetSMSCode(tel);
			Log.d("验证码结果", flag.toString());
			
		} else if (ActionType_UserRegister.equals(actionType)) {			
					
			String tel = intent.getStringExtra("tel");
			String confirmCode = intent.getStringExtra("confirmCode");
			String password = intent.getStringExtra("password");
			String userName = intent.getStringExtra("userName");
			
			
			JSONObject flag=PlayerRequestService.doRegisterUser(tel,confirmCode,userName,password);
			
			
			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction(ACTION_RECV_MSG_Register);
			broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
			broadcastIntent.putExtra("result", flag.toString());
			sendBroadcast(broadcastIntent);
		}else if (ActionType_MountRemoteClient.equals(actionType)){
			String tel = intent.getStringExtra("tel");
			String barCode = intent.getStringExtra("barCode");
			
			
			JSONObject flag=PlayerRequestService.doMountRomoteClient(tel, barCode);
			
			
			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction(ACTION_RECV_MSG_MountRemoteClient);
			broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
			broadcastIntent.putExtra("result", flag.toString());
			sendBroadcast(broadcastIntent);
		}else if (ActionType_UnmountRemoteClient.equals(actionType)){
			
			String tel = intent.getStringExtra("tel");
			String barCode = intent.getStringExtra("scanResult");
			
			JSONObject flag=PlayerRequestService.doUnMountRomoteClient(tel, barCode);
			
			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction(ACTION_RECV_MSG_UnmountRemoteClient);
			broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
			broadcastIntent.putExtra("result", flag.toString());
			sendBroadcast(broadcastIntent);
		}else if (ActionType_UserLogout.equals(actionType)){
			
			String tel = intent.getStringExtra("tel");			
			JSONObject flag=PlayerRequestService.playerLogout(tel);		
			
			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction( ACTION_RECV_MSG_Logout);
			broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
			broadcastIntent.putExtra("result", flag.toString());
			sendBroadcast(broadcastIntent);
		}else if(ActionType_UserAmountWithdraw.equals(actionType)){
			String tel = intent.getStringExtra("tel");	
			String amount = intent.getStringExtra("amount");		
			String withdrawType = intent.getStringExtra("withdrawType");
			
			JSONObject flag=PlayerRequestService.playerAmountWithdraw(tel,Double.valueOf(amount),Integer.valueOf(withdrawType));		
			
			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction( ACTION_RECV_MSG_AmountWithdraw);
			broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
			broadcastIntent.putExtra("result", flag.toString());
			sendBroadcast(broadcastIntent);
		}

	}

	
}
