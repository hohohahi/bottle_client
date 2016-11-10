package com.service;

import com.util.UserVo;

import constances.DatabaseFeild;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PlayerDao {
	public static  void insertIntoDB(MySqlLiteDBHelper dbHelper,String tel,String password){			
		try {
			// 得到一个可写的数据库
			SQLiteDatabase db = dbHelper.getWritableDatabase();	
			// 生成ContentValues对象 //key:列名，value:想插入的值
			ContentValues cv = new ContentValues();
			// 往ContentValues对象存放数据，键-值对模式		
			cv.put(DatabaseFeild.Field_User_Tel, tel);
			cv.put(DatabaseFeild.Field_User_Password, password);		
			// 调用insert方法，将数据插入数据库
			db.insert(DatabaseFeild.Table_User, null, cv);
			// 关闭数据库
			
		} catch (Exception e) {
			Log.d("insertIntoDB error", e.getMessage());
			
		}
	}
	
	public static String  updateUserAfterLoginSuccess(MySqlLiteDBHelper dbHelper,String tel,String password){		
		StringBuilder sb=new StringBuilder();
		try {
			
			Log.d("updateUserAfterLoginSuccess ready to check isUserExist", "");
			if (isUserExist(dbHelper, tel) == false) {
				sb.append("updateUserAfterLoginSuccess ready to insert");
				
				String ms = "tel = " + tel + ",password = " + password;
				Log.d("updateUserAfterLoginSuccess ready to insert", ms);
				insertIntoDB(dbHelper, tel, password);
			}else {
				String ms = "tel = " + tel + ",password = " + password;
				Log.d("updateUserAfterLoginSuccess ready to delete and insert", ms);
//				deleteUser(dbHelper, tel);
//				Log.d("updateUserAfterLoginSuccess ready  delete ", "done");
//				insertIntoDB(dbHelper, tel, password);
				//Log.d("updateUserAfterLoginSuccess ready  insert ", "done");
				sb.append("updateUserAfterLoginSuccess ready to delete and insert");
				updateUserFromDB(dbHelper, tel, password);
			}
		} catch (Exception e) {
			sb.append("updateUserAfterLoginSuccess Exception"+ e.getMessage());
			Log.d("updateUserAfterLoginSuccess Exception", e.getMessage());
		}
		return sb.toString();
	}
	
	public  static boolean isUserExist(MySqlLiteDBHelper dbHelper,String tel){
		UserVo user=queryUserVo(dbHelper,tel);
		if(user==null){
			return false;
		}else{
			return true;
		}
	} 
	
	
	
	
	public static  void updateUserFromDB(MySqlLiteDBHelper dbHelper,String tel,String password){
		
		// 得到一个可写的数据库
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(DatabaseFeild.Field_User_Password, password);
		// where 子句 "?"是占位符号，对应后面的"1",
		String whereClause = "tel = ?";
		String[] whereArgs = { String.valueOf(tel) };
		// 参数1 是要更新的表名
		// 参数2 是一个ContentValeus对象
		// 参数3 是where子句
		db.update(DatabaseFeild.Table_User, cv, whereClause, whereArgs);
	
	}

	public static void deleteUser(MySqlLiteDBHelper dbHelper,String tel){
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String whereClauses = "tel=?";
		String[] whereArgs = { tel };
		db.delete(DatabaseFeild.Table_User, whereClauses, whereArgs);
		
	}
	
	public static UserVo queryUserVo(MySqlLiteDBHelper dbHelper,String tel){
    	//updateDB(dbHelper,"13787210140","123");
    	SQLiteDatabase database = dbHelper.getReadableDatabase();      	
        //  Cursor cursor = database.query("bottle_user", new String[] {"tel"}, null, null, null, null,null);  
         
		String[] columns = new String[] { "tel"};  
		String selection = "tel=?";  
		String[] selectionArgs = { tel};  
		String groupBy = null;  
		String having = null;  
		String orderBy = null;  
		Cursor cursor =database.query(DatabaseFeild.Table_User, columns, selection,selectionArgs, groupBy, having, orderBy);
				
		while (cursor.moveToNext()) {  
			UserVo vo=new UserVo();
			vo.setTel(cursor.getString(cursor.getColumnIndex(DatabaseFeild.Field_User_Tel)));  
		    vo.setPassword(cursor.getString(cursor.getColumnIndex(DatabaseFeild.Field_User_Password)));
		    return vo;
		}    
		return null;
	}
}
