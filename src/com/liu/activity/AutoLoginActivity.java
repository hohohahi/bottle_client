package com.liu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.broadcastReceiver.MyBroadcastReceiver;
import com.service.PlayerIntentService;
import com.service.MySqlLiteDBHelper;
import com.service.PlayerDao;

import constances.DatabaseFeild;

public class AutoLoginActivity extends Activity {
	
	
	public class LoginMessageRecieved  extends MyBroadcastReceiver{
		
		 //接收广播类
		/* (non-Javadoc)
		 * @see com.broadcastReceiver.MyBroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
		 */
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			 super.onReceive(arg0, arg1);
		     if (this.isSucceed()){  
		    	  Log.d("checkLoginInfoFromDB ", "自动登录成功");
		    	  
		    	  // 如果登录成功  
		    	  // 启动Main Activity		    	 
		    	  Intent nextIntent = new Intent(AutoLoginActivity.this, ProfileActivity.class);
		    	  Bundle bundle = new Bundle();
		    	  /*字符、字符串、布尔、字节数组、浮点数等等，都可以传*/
		    	  Toast.makeText(AutoLoginActivity.this,"自动登录成功,"+this.getData().toString(), Toast.LENGTH_SHORT).show();
		    	  bundle.putString("data", this.getData().toString());
		    	  nextIntent.putExtras(bundle);
		    	  startActivity(nextIntent);
		    	  finish();  
		
		    	  arg0.unregisterReceiver(this);  
		     }else{  
		    	 Log.d("checkLoginInfoFromDB ", "自动登录失败");
		    	 Intent nextIntent = new Intent(AutoLoginActivity.this, LoginActivity.class); 
			     startActivity(nextIntent);  
		    	 String errorMsg=getErrorMsg();
		    	 Toast.makeText(AutoLoginActivity.this,"自动登录失败,"+this.getData().toString(), Toast.LENGTH_SHORT).show();
		    	 Toast.makeText(AutoLoginActivity.this,errorMsg, Toast.LENGTH_SHORT).show();
		     }
		}
	}
	
	private boolean   checkLoginInfoFromDB(){
		
		  //从数据库获取账号信息  
       
        try {
        	MySqlLiteDBHelper dbHelper = new MySqlLiteDBHelper(AutoLoginActivity.this,	DatabaseFeild.DB_Bottle, null, 1);
        	SQLiteDatabase database = dbHelper.getReadableDatabase();  
        	
        	//UserDBUtil.updateUserAfterLoginSuccess(dbHelper, "13787210140","1234");

            Cursor cursor = database.query(DatabaseFeild.Table_User, new String[] {DatabaseFeild.Field_User_Tel, DatabaseFeild.Field_User_Password }, null, null, null, null,null);  
            Toast.makeText(AutoLoginActivity.this,"自动登录1", Toast.LENGTH_SHORT).show();
			 while (cursor.moveToNext()) {  
			    String tel=cursor.getString(cursor.getColumnIndex(DatabaseFeild.Field_User_Tel));  
			    String password=cursor.getString(cursor.getColumnIndex(DatabaseFeild.Field_User_Password));
			    Toast.makeText(AutoLoginActivity.this,"自动登录2 tel"+tel+",password="+password, Toast.LENGTH_SHORT).show();
			    Log.d("checkLoginInfoFromDB flag", "3");
			    Log.d("checkLoginInfoFromDB tel", tel);
			    Log.d("checkLoginInfoFromDB password", password);
			    if(tel==null||"".equals(tel)){
			    	return false;
			    }
			    else {
					IntentFilter filter = new IntentFilter(PlayerIntentService.ACTION_RECV_MSG_Login);    
			        filter.addCategory(Intent.CATEGORY_DEFAULT);    
			        LoginMessageRecieved  receiver = new LoginMessageRecieved();    
			        registerReceiver(receiver, filter); 
			        

					Intent msgIntent = new Intent(AutoLoginActivity.this, PlayerIntentService.class);  
			        msgIntent.putExtra("username", tel);  
			        msgIntent.putExtra("password", password);  
			        msgIntent.putExtra(PlayerIntentService.ActionType_Key, PlayerIntentService.ActionType_UserLogin);
			        startService(msgIntent); 
					return true;
			    }
			}
			
             
		} catch (Exception e) {
			Toast.makeText(AutoLoginActivity.this,"自动登录3 checkLoginInfoFromDB error"+e.getMessage(), Toast.LENGTH_SHORT).show();
			Log.d("checkLoginInfoFromDB error", e.getMessage());
			
		}
		return false;  
		
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 得到一个可读的SQLiteDatabase对象
		boolean isOk=checkLoginInfoFromDB();
		if(isOk==false){
			Toast.makeText(AutoLoginActivity.this,"自动登录4", Toast.LENGTH_SHORT).show();
			 Intent nextIntent = new Intent(AutoLoginActivity.this, LoginActivity.class); 
		     startActivity(nextIntent);  
		}
		
	}
}