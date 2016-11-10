package com.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class WithdrawService {
	static Double[] telWithdrawDefaultArray = { 10d, 20d, 30d, 50d, 100d };

	public static List<String> getTelWithdrawListByCurrentAmount(
			double currentAmount) {

		List<String> amountList = new ArrayList<String>();
		for (Double amount : telWithdrawDefaultArray) {
			if ((amount - currentAmount) <= 0) {
				String msg = amount.toString();
				amountList.add(msg);
			}
		}
		return amountList;
	}

	public static boolean checkWithdrawisSucess(JSONObject resturnJson) {

		if (resturnJson != null) {
			Object errorCodeObj;
			Log.d("checkWithdrawisSucess  ",resturnJson.toString());	
			try {
				errorCodeObj = resturnJson.get("error_code");
				if (errorCodeObj instanceof Integer) {
					int erroCode = (Integer) errorCodeObj;
					if (erroCode == 0) {
						return true;
					}
				} else if (errorCodeObj instanceof Long) {
					long erroCode = (Long) errorCodeObj;
					if (erroCode == 0) {
						return true;
					}
				} else if (errorCodeObj instanceof String) {
					String erroCode = (String) errorCodeObj;
					if ("0".equals(erroCode)) {
						return true;
					}
				} else {
					return false;
				}

			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			Log.d("checkWithdrawisSucess  ","is null");	
			return false;
		}
		return false;
	}
}
