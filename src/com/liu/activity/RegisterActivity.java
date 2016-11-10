package com.liu.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.broadcastReceiver.MyBroadcastReceiver;
import com.liu.activity.ProfileActivity.UserLogoutRecieved;
import com.service.PlayerIntentService;

public class RegisterActivity extends Activity {
	
	private EditText edit_text_register_tel, editText_register_code,editText_register_userName,editText_register_password;	
	private Button btn_regitser_Confirm;
	private Button btn_regitser_getCode;
	private ImageButton btnQuit;
    private String tel,confirmCode,userName,password;	
    private TimeCount time;
	
    
    public class UserLogoutRecieved  extends MyBroadcastReceiver{
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			 super.onReceive(arg0, arg1);
			 if (this.isSucceed()){
				 Toast.makeText( RegisterActivity.this, "退出成功", Toast.LENGTH_SHORT).show();					 
				 Intent nextIntent = new Intent(RegisterActivity.this, LoginActivity.class);
		    	 startActivity(nextIntent);  
		    	  // 结束该Activity  
		    	  finish();  
		     }else{  
		    	 String errorMsg=getErrorMsg();
		    	 Toast.makeText( RegisterActivity.this, "退出失败,code:"+getErrorCode()+",Msg:"+errorMsg, Toast.LENGTH_SHORT).show();
		     }
		}
	}
    
	public class RegisterMessageRecieved  extends MyBroadcastReceiver{
		
		
		 //接收广播类
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			 super.onReceive(arg0, arg1);
			 if(this.isSucceed()==true){
				 Toast.makeText( RegisterActivity.this, "注册成功,请登录", Toast.LENGTH_SHORT).show();	
				 Intent nextIntent = new Intent(RegisterActivity.this, LoginActivity.class);
		    	 startActivity(nextIntent);  
		    	  // 结束该Activity  
		    	 finish();  
		    	  //注销广播接收器  
		    	 arg0.unregisterReceiver(this);  
		     }else{
		    	 String errorMsg=getErrorMsg();
		    	 Toast.makeText( RegisterActivity.this, "注册失败,code:"+getErrorCode()+",Msg:"+errorMsg, Toast.LENGTH_SHORT).show();	    	  
		     }  
		       
		}
	}
	


	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}
		
		@Override
		public void onFinish() {//计时完毕时触发
			btn_regitser_getCode.setText("重新获取");
			btn_regitser_getCode.setClickable(true);
		}
		
		@Override
		public void onTick(long millisUntilFinished){//计时过程显示
			btn_regitser_getCode.setClickable(false);
			btn_regitser_getCode.setText(millisUntilFinished /1000+"秒");
		}
	}
		
		
		
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		
		time = new TimeCount(60000, 1000);//构造CountDownTimer对象
		btn_regitser_getCode = (Button) findViewById(R.id.btn_regitser_getCode);
		
		
		edit_text_register_tel = (EditText) findViewById(R.id.edit_text_register_tel);
		editText_register_code = (EditText) findViewById(R.id.editText_register_code);
		editText_register_userName=(EditText) findViewById(R.id.editText_register_userName);
		editText_register_password=(EditText) findViewById(R.id.editText_register_password);
		
		
		
		btn_regitser_getCode = (Button) findViewById(R.id.btn_regitser_getCode);
		btn_regitser_Confirm = (Button) findViewById(R.id.btn_regitser_Confirm);
		

        btnQuit = (ImageButton)findViewById(R.id.img_btn_logout);
        
        
        
     

		btn_regitser_Confirm.setOnClickListener(new OnClickListener(){
	    	   public void onClick(View v) {
	    		   
	    		    IntentFilter filter = new IntentFilter(PlayerIntentService.ACTION_RECV_MSG_Register);    
	    	        filter.addCategory(Intent.CATEGORY_DEFAULT);    
	    	        RegisterMessageRecieved  receiver = new RegisterMessageRecieved();    
	    	        registerReceiver(receiver, filter); 
	    		   
	    		    tel = edit_text_register_tel.getText().toString();
	    		    confirmCode = editText_register_code.getText().toString();
	    		    password = editText_register_password.getText().toString();
	    		    userName = editText_register_userName.getText().toString();
	    		    
	    		    
	    		    
					Intent msgIntent = new Intent(RegisterActivity.this, PlayerIntentService.class);  
			        msgIntent.putExtra("tel", tel);
			        msgIntent.putExtra("confirmCode", confirmCode);
			        msgIntent.putExtra("password", password);
			        msgIntent.putExtra("userName", userName);
			        msgIntent.putExtra(PlayerIntentService.ActionType_Key, PlayerIntentService.ActionType_UserRegister);
			        startService(msgIntent); 
					
				}
	       });

		btn_regitser_getCode.setOnClickListener(new OnClickListener(){
    	   public void onClick(View v) {
    		    tel = edit_text_register_tel.getText().toString();
				Intent msgIntent = new Intent(RegisterActivity.this, PlayerIntentService.class);  
		        msgIntent.putExtra("tel", tel);
		        msgIntent.putExtra(PlayerIntentService.ActionType_Key, PlayerIntentService.ActionType_GetRegisterCode);
		        startService(msgIntent); 
//				
		        
		        time.start();//开始计时
			}
       });
//
//		
		
		
		ImageButton logoutButton = (ImageButton) this.findViewById(R.id.img_btn_logout);
        logoutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent nextIntent = new Intent(RegisterActivity.this, LoginActivity.class);
		    	 startActivity(nextIntent); 
		    	 finish();  
			
				
			}
		});    
      
	}
}