package com.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

public class HttpUtil {
  // 创建HttpClient对象
  public static HttpClient httpClient = new DefaultHttpClient();
  public static final String BASE_URL = "http://109.205.93.209:8585/Bottle_CloudServer/api/";
  //public static final String BASE_URL = "http://127.0.0.1:8080/Bottle_CloudServer/api/";
  private static final String APPLICATION_JSON = "application/json";
  private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

  /**
   * 
   * @param url
   *            发送请求的URL
   * @return 服务器响应字符串
   * @throws Exception
   */
  public static String getRequest(String url) throws Exception {
    // 创建HttpGet对象。
    HttpGet get = new HttpGet(url);
    // 发送GET请求
    HttpResponse httpResponse = httpClient.execute(get);
    // 如果服务器成功地返回响应
    if (httpResponse.getStatusLine().getStatusCode() == 200) {
      // 获取服务器响应字符串
      String result = EntityUtils.toString(httpResponse.getEntity());
      return result;
    } else {
      Log.d("服务器响应代码", (new Integer(httpResponse.getStatusLine()
          .getStatusCode())).toString());
      return null;
    }
  }

  /**
   * 
   * @param url
   *            发送请求的URL
   * @param params
   *            请求参数
   * @return 服务器响应字符串
   * @throws Exception
   */
  public static String postRequest(String url, Map<String, String> rawParams)
      throws Exception {
    // 创建HttpPost对象。
    HttpPost post = new HttpPost(url);
    // 如果传递参数个数比较多的话可以对传递的参数进行封装
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    for (String key : rawParams.keySet()) {
      // 封装请求参数
      params.add(new BasicNameValuePair(key, rawParams.get(key)));
    }
    // 设置请求参数
    post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
    // 发送POST请求
    HttpResponse httpResponse = httpClient.execute(post);
    post.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
    // 如果服务器成功地返回响应
    if (httpResponse.getStatusLine().getStatusCode() == 200) {
      // 获取服务器响应字符串
      String result = EntityUtils.toString(httpResponse.getEntity());
      return result;
    }
    return null;
  }
  
  
  
  public static JSONObject postRequestForJson(String url, JSONObject jsonObj) throws Exception {
		    
	    DefaultHttpClient httpClient = new DefaultHttpClient();
	    
        HttpPost httpPost = new HttpPost(url);               
        httpPost.setHeader("Accept", "application/json"); 
        
        StringEntity se = new StringEntity(jsonObj.toString());
        se.setContentEncoding("UTF-8");
        se.setContentType("application/json");
        
              
        
        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);        
        // 
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {        	
			String result = EntityUtils.toString(httpResponse.getEntity());// 返回json格式：
			JSONObject 	response = new JSONObject(result);
		    return response;
        }else{
        	JSONObject retJson=new JSONObject();
        	retJson.put("StatusCode", httpResponse.getStatusLine().getStatusCode());
        	return retJson;
        }
 }
        
	  
  
  public static void main(String[] args){
	  
		JSONObject jsonObj  =new JSONObject();		
		// 定义发送请求的URL
		String url = HttpUtil.BASE_URL + "player/smscode/application"; // GET方式
		try {
			jsonObj.put("phoneNumber", "13787210140");			
			System.out.println( HttpUtil.postRequestForJson(url, jsonObj)); // POST方式
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	 
  }
}