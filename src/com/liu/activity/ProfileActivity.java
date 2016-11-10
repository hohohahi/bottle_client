package com.liu.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.broadcastReceiver.MyBroadcastReceiver;
import com.liu.activity.LoginActivity.LoginMessageRecieved;
import com.service.PlayerIntentService;
import com.service.MySqlLiteDBHelper;
import com.service.PlayerDao;
import com.util.CacheManager;
import com.zxing.activity.CaptureActivity;

public class ProfileActivity extends Activity {
    /** Called when the activity is first created. */
	private TextView resultTextView;
	
	private EditText edit_text_profile_tel,	editText_profile_userName,editText_profile_amount,edit_text_profile_mountClient;
	
	private Long tel =0L;
	private String userName="";
	private String scanResult="";
	private String amount="";
	
	public class UnountRemoteClientRecieved  extends MyBroadcastReceiver{
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			 super.onReceive(arg0, arg1);
			 if (this.isSucceed()){
				 Toast.makeText( ProfileActivity.this, "解绑终端成功", Toast.LENGTH_SHORT).show();		   
				 resultTextView.setText("");
				 edit_text_profile_mountClient.setText("未绑定");
		     }else{  
		    	 String errorMsg=getErrorMsg();
		    	 Toast.makeText( ProfileActivity.this, "解绑终端失败,code:"+getErrorCode()+",Msg:"+errorMsg, Toast.LENGTH_SHORT).show();
		     }
		}
	}
	
	public class UserLogoutRecieved  extends MyBroadcastReceiver{
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			 super.onReceive(arg0, arg1);
			 if (this.isSucceed()){
				 Toast.makeText( ProfileActivity.this, "退出成功", Toast.LENGTH_SHORT).show();					 
				 Intent nextIntent = new Intent(ProfileActivity.this, LoginActivity.class);
		    	 startActivity(nextIntent);  
		    	  // 结束该Activity  
		    	  finish();  
		     }else{  
		    	 String errorMsg=getErrorMsg();
		    	 Toast.makeText( ProfileActivity.this, "退出失败,code:"+getErrorCode()+",Msg:"+errorMsg, Toast.LENGTH_SHORT).show();
		    	 Intent nextIntent = new Intent(ProfileActivity.this, LoginActivity.class);
		    	 startActivity(nextIntent);  
		    	 finish();  
		     }
		}
	}
	
	public class MountRemoteClientRecieved  extends MyBroadcastReceiver{
		
		
		 //接收广播类
		/* (non-Javadoc)
		 * @see com.broadcastReceiver.MyBroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
		 */
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			 super.onReceive(arg0, arg1);
		     if (this.isSucceed()){		    	 
		    	 edit_text_profile_mountClient.setText("已绑定");
		    	 Toast.makeText( ProfileActivity.this, "绑定终端成功", Toast.LENGTH_SHORT).show();		    	    
		     }else{  
		    	 String errorMsg=getErrorMsg();
		    	 Toast.makeText( ProfileActivity.this, "绑定终端失败,code:"+getErrorCode()+",Msg:"+errorMsg, Toast.LENGTH_SHORT).show();
		    	 
		     }
		}
	}
	
	private void setValueByIntent(){
		

		Bundle bundle = this.getIntent().getExtras();    	
    	if(bundle !=null){
    		 String data = bundle.getString("data");
    		 Toast.makeText( ProfileActivity.this, "bundle 数据 "+data, Toast.LENGTH_SHORT).show();
    		 try {
    			 if(data!=null){
    				JSONObject dataJson =new JSONObject(data);
    				setValueToUI(dataJson);
    			 }else{
    				 Log.d("enter getData", "null");			
    			 }
			} catch (JSONException e) {				
				e.printStackTrace();
			}
    		
    	}else{
    		Log.d(" bundle is ", "null");
    	}
	   
	}


	private void setValueToUI(JSONObject dataJson) throws JSONException {
		if(dataJson!=null){    					
			
			Object phoneNumber=dataJson.get("phoneNumber");
			if(phoneNumber!=null && phoneNumber instanceof Long){
				Log.d("ProfileActivity phoneNumberLong", phoneNumber.toString());   
				tel=(Long)phoneNumber;
			}
			Object amountObj=dataJson.get("amount");
			if(amountObj!=null && amountObj instanceof Integer){
				Log.d("ProfileActivity amount", amountObj.toString());   
				amount=amountObj.toString();
			}else if(amountObj!=null && amountObj instanceof Double){
				Log.d("ProfileActivity amount", amountObj.toString());   
				amount=amountObj.toString();
			}else  if(amountObj!=null && amountObj instanceof Long){
				Log.d("ProfileActivity amount", amountObj.toString());   
				amount=amountObj.toString();
			}else  if(amountObj!=null && amountObj instanceof String){
				Log.d("ProfileActivity amount", amountObj.toString());   
				amount=amountObj.toString();
			}
			
			Object name=dataJson.get("name");
			if(name!=null && name instanceof String){
				Log.d("ProfileActivity name", name.toString());   
				userName=name.toString();
			}
		}else{
			Log.d("dataJson", "null");			
		}
	}
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {    	  
    	
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.profile);
        resultTextView = (TextView) this.findViewById(R.id.profile_scan_result);        
        edit_text_profile_tel = (EditText)this.findViewById(R.id.edit_text_profile_tel);        
        editText_profile_userName = (EditText)this.findViewById(R.id.editText_profile_userName);        
        editText_profile_amount=(EditText)this.findViewById(R.id.edit_text_profile_amount);   
        edit_text_profile_mountClient=(EditText)this.findViewById(R.id.edit_text_profile_mountClient);   
        
        
        setValueByIntent();
        edit_text_profile_mountClient.setText("未绑定");
        editText_profile_userName.setText(this.userName);
        edit_text_profile_tel.setText(this.tel.toString());
        editText_profile_amount.setText(this.amount.toString());
        
        Button scanBarCodeButton = (Button) this.findViewById(R.id.btn_profile_scanPhoto);
        scanBarCodeButton.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View v) {
					//打开扫描界面扫描条形码或二维码
					Intent openCameraIntent = new Intent(ProfileActivity.this,CaptureActivity.class);
					startActivityForResult(openCameraIntent, 0);
			}
		});    
        
        ImageButton logoutButton = (ImageButton) this.findViewById(R.id.img_btn_logout);
        logoutButton.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View v) {
				
				

				IntentFilter filter = new IntentFilter(PlayerIntentService.ACTION_RECV_MSG_Logout);    
		        filter.addCategory(Intent.CATEGORY_DEFAULT);    
		        UserLogoutRecieved receiver = new UserLogoutRecieved();    
		        registerReceiver(receiver, filter); 
				
				Intent msgIntent = new Intent(ProfileActivity.this, PlayerIntentService.class);  
				String telStr = edit_text_profile_tel.getText().toString();
				msgIntent.putExtra("tel", telStr);		       
		        msgIntent.putExtra(PlayerIntentService.ActionType_Key, PlayerIntentService.ActionType_UserLogout);
		        startService(msgIntent); 
				
			}
		});    
        
        Button unmountButton = (Button) this.findViewById(R.id.btn_profile_unmountClient);
        unmountButton.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View v) {				
				
				String telStr = edit_text_profile_tel.getText().toString();
				String scanResultStr=resultTextView.getText().toString();
				
				
				IntentFilter filter = new IntentFilter(PlayerIntentService.ACTION_RECV_MSG_UnmountRemoteClient);    
		        filter.addCategory(Intent.CATEGORY_DEFAULT);    
		        UnountRemoteClientRecieved receiver = new UnountRemoteClientRecieved();    
		        registerReceiver(receiver, filter); 
				  
			
				Intent msgIntent = new Intent(ProfileActivity.this, PlayerIntentService.class);  
				msgIntent.putExtra("tel", telStr );				
				msgIntent.putExtra("scanResult", scanResultStr);		       
		        msgIntent.putExtra(PlayerIntentService.ActionType_Key, PlayerIntentService.ActionType_UnmountRemoteClient);
		        
		        startService(msgIntent); 
			}
		}); 
        
        
        Button withdrawButton = (Button) this.findViewById(R.id.btn_profile_withdraw);
        withdrawButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				
				
				  Intent nextIntent = new Intent(ProfileActivity.this, WithdrawActivity.class);
		    	  Bundle bundle = new Bundle();
		    	  /*字符、字符串、布尔、字节数组、浮点数等等，都可以传*/
		    	  
		    	  
		   	    
		    	 String tel = edit_text_profile_tel.getText().toString();
		    	 String amount=editText_profile_amount.getText().toString();
		    	 Log.d(" withdrawButton ", "tel = "+tel+",amount = "+amount );
		    	 try {
		    		  JSONObject json=new JSONObject();
					  json.put("tel", tel);
					  json.put("amount", amount);
					  bundle.putString("data", json.toString());
			    	  nextIntent.putExtras(bundle);
			    	  startActivity(nextIntent);
				 } catch (JSONException e) {
					 Log.d(" withdrawButton JSONException", "tel = "+tel+",amount = "+amount );
				 }
			}
		});    
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//处理扫描结果（在界面上显示）		
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();			
			String scanResult = bundle.getString("result");
			resultTextView.setText(scanResult);
			
			//打开扫描界面扫描条形码或二维码
			IntentFilter filter = new IntentFilter(PlayerIntentService.ACTION_RECV_MSG_MountRemoteClient);    
	        filter.addCategory(Intent.CATEGORY_DEFAULT);    
	        MountRemoteClientRecieved  receiver = new MountRemoteClientRecieved();    
	        registerReceiver(receiver, filter); 
					    
		
			Intent msgIntent = new Intent(ProfileActivity.this, PlayerIntentService.class);  
			msgIntent.putExtra("barCode", scanResult);
			msgIntent.putExtra("tel", tel.toString());
	        msgIntent.putExtra(PlayerIntentService.ActionType_Key, PlayerIntentService.ActionType_MountRemoteClient);
	        
	        startService(msgIntent); 
		}
	}
}