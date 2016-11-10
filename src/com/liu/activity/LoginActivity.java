package com.liu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.broadcastReceiver.MyBroadcastReceiver;
import com.liu.activity.R;
import com.liu.activity.R.id;
import com.liu.activity.R.layout;
import com.service.PlayerIntentService;
import com.service.MySqlLiteDBHelper;
import com.service.PlayerDao;
import com.util.CacheManager;

import constances.DatabaseFeild;

public class LoginActivity extends Activity {
	
	private EditText userName, password;
	
	private Button btn_login;
	private Button btn_register;
	private ImageButton btnQuit;
    private String userNameValue,passwordValue;
	
	
	
	public class LoginMessageRecieved  extends MyBroadcastReceiver{
		
		 //接收广播类
		/* (non-Javadoc)
		 * @see com.broadcastReceiver.MyBroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
		 */
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			 super.onReceive(arg0, arg1);
		     if (this.isSucceed()){  
		    	  // 如果登录成功  
		    	  // 启动Main Activity		    	 
		    	  Intent nextIntent = new Intent(LoginActivity.this, ProfileActivity.class);
		    	  Bundle bundle = new Bundle();
		    	  /*字符、字符串、布尔、字节数组、浮点数等等，都可以传*/
		    	  
		    	  
		   	    
		    	  String tel = userName.getText().toString();
		    	  String pass= password.getText().toString();
		    	  
		    	  //Toast.makeText(LoginActivity.this,"登录成功后更新数据库", Toast.LENGTH_SHORT).show();
		    	  MySqlLiteDBHelper dbHelper = new MySqlLiteDBHelper(LoginActivity.this,DatabaseFeild.DB_Bottle, null, 1);
		    	  
		    	  String msg=PlayerDao.updateUserAfterLoginSuccess(dbHelper, tel, pass);
		    	  //Toast.makeText(LoginActivity.this,"登录成功后更新数据库  error"+msg, Toast.LENGTH_SHORT).show();
		    	  CacheManager.getInstance(tel).setUserInfoJson(this.retJson);
		    	  
		    	  bundle.putString("data", this.getData().toString());
		    	  nextIntent.putExtras(bundle);
		    	  startActivity(nextIntent);
		    	  finish();  
		
		    	  arg0.unregisterReceiver(this);  
		     }else{  
		    	 String errorMsg=getErrorMsg();
		    	 Toast.makeText(LoginActivity.this,errorMsg, Toast.LENGTH_SHORT).show();
  	      		     
		     }
		}
	}
	
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
				
		userName = (EditText) findViewById(R.id.et_zh);
		password = (EditText) findViewById(R.id.et_mima);      
        btn_login = (Button) findViewById(R.id.btn_login);
        btnQuit = (ImageButton)findViewById(R.id.img_btn);
        btn_register = (Button) findViewById(R.id.btn_register);
		
      
       btn_register.setOnClickListener(new OnClickListener(){
    	   public void onClick(View v) {    		   
    		    Intent nextIntent = new Intent(LoginActivity.this, RegisterActivity.class); 
		    	startActivity(nextIntent);  
		    	
			}
       });
       
		btn_login.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {				
				
				
				IntentFilter filter = new IntentFilter(PlayerIntentService.ACTION_RECV_MSG_Login);    
		        filter.addCategory(Intent.CATEGORY_DEFAULT);    
		        LoginMessageRecieved  receiver = new LoginMessageRecieved();    
		        registerReceiver(receiver, filter); 
				
				
				userNameValue = userName.getText().toString();
			    passwordValue = password.getText().toString();			    
			
				Intent msgIntent = new Intent(LoginActivity.this, PlayerIntentService.class);  
		        msgIntent.putExtra("username", userNameValue);  
		        msgIntent.putExtra("password", passwordValue);  
		        msgIntent.putExtra(PlayerIntentService.ActionType_Key, PlayerIntentService.ActionType_UserLogin);
		        
		        
		        startService(msgIntent); 
				
				Toast.makeText(LoginActivity.this,"ready to login", Toast.LENGTH_SHORT).show();
			}
		});

		
		
		btnQuit.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}