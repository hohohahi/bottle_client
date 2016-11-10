package com.liu.activity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.broadcastReceiver.MyBroadcastReceiver;
import com.service.PlayerIntentService;
import com.service.MySqlLiteDBHelper;
import com.service.PlayerDao;
import com.service.WithdrawService;
import com.util.CacheManager;

public class WithdrawActivity extends Activity {
	private EditText edit_text_withdraw_tel,edit_text_withdraw_currentAmount;
	private String tel="",amount="";
	private Spinner spinner;
	private List<String> data_list;
	private ArrayAdapter<String> arr_adapter;
	public class WithdrawRecieved  extends MyBroadcastReceiver{
		
		 //接收广播类
		/* (non-Javadoc)
		 * @see com.broadcastReceiver.MyBroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
		 */
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			 super.onReceive(arg0, arg1);
			 boolean succuss=false;
			 if(retJson!=null){
				 String msg=retJson.toString();
				 Log.d("ProfileActivity onReceive", msg);   
				 Toast.makeText(WithdrawActivity.this,msg, Toast.LENGTH_SHORT).show();
				 if(msg.contains("订单提交成功，等待充值")){
					 succuss=true;
				 }
			 }
			 if(succuss==true){	 
		    	  JSONObject userInfo= CacheManager.getInstance().getUserInfo();
		    	  Intent nextIntent = new Intent(WithdrawActivity.this, ProfileActivity.class);
		    	  Bundle bundle = new Bundle();
		    	  /*字符、字符串、布尔、字节数组、浮点数等等，都可以传*/
		    	  Toast.makeText(WithdrawActivity.this,"订单提交成功，请查收", Toast.LENGTH_SHORT).show();
		    	  bundle.putString("data", userInfo.toString());
		    	  nextIntent.putExtras(bundle);		    
		    	  startActivity(nextIntent);
		    	  
		    	  arg0.unregisterReceiver(this);
		    	   
		     }else{  
		    	  JSONObject userInfo= CacheManager.getInstance().getUserInfo();
		    	  Intent nextIntent = new Intent(WithdrawActivity.this, ProfileActivity.class);
		    	  Bundle bundle = new Bundle();
		    	  /*字符、字符串、布尔、字节数组、浮点数等等，都可以传*/
		    	  Toast.makeText(WithdrawActivity.this,"提现失败,请联系工作人员", Toast.LENGTH_SHORT).show();
		    	  bundle.putString("data", userInfo.toString());
		    	  nextIntent.putExtras(bundle);		
		    	  startActivity(nextIntent);
		    	 
		     }
		}
	}
	
	
	
	private void setValueByIntent(){
		
		 Bundle bundle = this.getIntent().getExtras();    	
	    	if(bundle !=null){
	    		 String data = bundle.getString("data");
	    		 
	    		 try {
	    			 if(data!=null){
	    				JSONObject dataJson =new JSONObject(data);
	    				if(dataJson!=null){    					
	    					
	    					Object telIntent=dataJson.get("tel");
	    					if(telIntent!=null && telIntent instanceof String){
	    						//Log.d("ProfileActivity phoneNumberLong", telIntent.toString());   
	    						this.tel=(String)telIntent;
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
	    				}else{
	    					Log.d("dataJson", "null");			
	    				}
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
	
	
	private void setSpinnerList(String currentAmount){
		
		
		
		 spinner = (Spinner) findViewById(R.id.spinner_withdraw_amount);
         //数据
//         data_list = new ArrayList<String>();
//         data_list.add("北京");
//         data_list.add("上海");
//         data_list.add("广州");
//         data_list.add("深圳");
         data_list= WithdrawService.getTelWithdrawListByCurrentAmount(Double.valueOf(currentAmount));
         //适配器
         arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
         //设置样式
         arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         //加载适配器
         spinner.setAdapter(arr_adapter);
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.withdraw);
		edit_text_withdraw_tel = (EditText)this.findViewById(R.id.edit_text_withdraw_tel); 
		edit_text_withdraw_currentAmount=(EditText)this.findViewById(R.id.edit_text_withdraw_currentAmount);
		
		setValueByIntent();
		
		
		double currentAmount =CacheManager.getInstance().getUserAmount();
		if(currentAmount!=-1){
			amount=String.valueOf(currentAmount);
		}
		setSpinnerList(String.valueOf(amount));
		edit_text_withdraw_currentAmount.setText(String.valueOf(amount));
		edit_text_withdraw_tel.setText(this.tel);
		
		
		
		
		Button withdrawConfirm = (Button) this.findViewById(R.id.btn_withdraw_confirm);
		
		
		
		
	    withdrawConfirm.setOnClickListener(new OnClickListener() {
	        	
			@Override
			public void onClick(View v) {
				Log.d(" withdrawConfirm click ", "yes");
				IntentFilter filter = new IntentFilter(PlayerIntentService.ACTION_RECV_MSG_AmountWithdraw);    
		        filter.addCategory(Intent.CATEGORY_DEFAULT);    
		        WithdrawRecieved receiver = new WithdrawRecieved();    
		        registerReceiver(receiver, filter); 
				
				Intent msgIntent = new Intent(WithdrawActivity.this, PlayerIntentService.class);  
				String telStr = edit_text_withdraw_tel.getText().toString();
				String amountStr = spinner.getSelectedItem().toString();
				Log.d(" withdrawConfirm click ", "telStr = "+telStr+",amountStr = "+amountStr);
				msgIntent.putExtra("tel", telStr);
				msgIntent.putExtra("amount", amountStr);
				msgIntent.putExtra("withdrawType", "1");
		        msgIntent.putExtra(PlayerIntentService.ActionType_Key, PlayerIntentService.ActionType_UserAmountWithdraw);
		        startService(msgIntent); 
			}
		}); 

	}
}