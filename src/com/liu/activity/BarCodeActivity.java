package com.liu.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.broadcastReceiver.MyBroadcastReceiver;
import com.google.zxing.WriterException;
import com.service.PlayerIntentService;
import com.zxing.activity.CaptureActivity;
import com.zxing.encoding.EncodingHandler;

public class BarCodeActivity extends Activity {
    /** Called when the activity is first created. */
	private TextView resultTextView;
	private EditText qrStrEditText;
	private ImageView qrImgImageView;
	private Long tel;
	private String userName;
	
	public class MountRemoteClientRecieved  extends MyBroadcastReceiver{
		
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
		    	  Toast.makeText(BarCodeActivity.this,getData().toString(), Toast.LENGTH_SHORT).show();
		    	  Intent nextIntent = new Intent(BarCodeActivity.this, ProfileActivity.class);
		    	  
		    	  
		    	  startActivity(nextIntent);		   
		    	  finish();  		    	 
		    	  arg0.unregisterReceiver(this);  
		     }else{  
		    	 String errorMsg=getErrorMsg();
		    	 Toast.makeText(BarCodeActivity.this,errorMsg, Toast.LENGTH_SHORT).show();
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
	    					
	    					Object phoneNumber=dataJson.get("phoneNumber");
	    					if(phoneNumber!=null && phoneNumber instanceof Long){
	    						Log.d("phoneNumberLong", phoneNumber.toString());   
	    						tel=(Long)phoneNumber;
	    					}
	    					
	    					
	    					Object name=dataJson.get("name");
	    					if(name!=null && name instanceof String){
	    						Log.d("name", name.toString());   
	    						userName=name.toString();
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
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.camera_main);        
        resultTextView = (TextView) this.findViewById(R.id.tv_scan_result);
        qrStrEditText = (EditText) this.findViewById(R.id.et_qr_string);
        qrImgImageView = (ImageView) this.findViewById(R.id.iv_qr_image);
       
        
        this.setValueByIntent();
        resultTextView.setText(this.userName);       
        
        Button scanBarCodeButton = (Button) this.findViewById(R.id.btn_scan_barcode);
        scanBarCodeButton.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View v) {
				
					Intent openCameraIntent = new Intent(BarCodeActivity.this,CaptureActivity.class);
					startActivityForResult(openCameraIntent, 0);
			}
		});        
        
        
        Button generateQRCodeButton = (Button) this.findViewById(R.id.btn_add_qrcode);
        generateQRCodeButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				try {
					String contentString = qrStrEditText.getText().toString();
					if (!contentString.equals("")) {
						//根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
						Bitmap qrCodeBitmap = EncodingHandler.createQRCode(contentString, 500);
						qrImgImageView.setImageBitmap(qrCodeBitmap);
					}else {
						Toast.makeText(BarCodeActivity.this, "Text can not be empty", Toast.LENGTH_SHORT).show();
					}
					
				} catch (WriterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
					    
		
			Intent msgIntent = new Intent(BarCodeActivity.this, PlayerIntentService.class);  
			msgIntent.putExtra("barCode", scanResult);
			msgIntent.putExtra("tel", tel.toString());
	        msgIntent.putExtra(PlayerIntentService.ActionType_Key, PlayerIntentService.ActionType_MountRemoteClient);
	        
	        
	        startService(msgIntent); 
	        
		}
		
			
	}
}