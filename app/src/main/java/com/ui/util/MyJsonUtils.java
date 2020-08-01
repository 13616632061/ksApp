/* vim: set expandtab sw=4 ts=4 sts=4: */
/**
 * @author:          eddie
 * @last modified:   2012-07-07 10:45:27
 * @filename:        MyJsonUtils.java
 * @description:     
 */
package com.ui.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyJsonUtils {

	public static String getJosnStr(String preJsonStr) {

		String jsonStr = "";

		jsonStr = "{jsonStr:" + preJsonStr + "}";

		return jsonStr;
	}
	

	public static JSONObject resolveJson(String str, Context ctx) {
		JSONObject jsonObject = null;

		try {
			jsonObject = new JSONObject(str);

			//jsonObject = jsonObject.getJSONObject("jsonStr");

		} catch (JSONException e) {
			e.printStackTrace();
			
			SysUtils.showNetworkError();
		}
		return jsonObject;
	}

	public static JSONObject resolveJson(String str) {
		JSONObject jsonObject = null;

		try {
			jsonObject = new JSONObject(str);

			//jsonObject = jsonObject.getJSONObject("jsonStr");

		} catch (JSONException e) {
			e.printStackTrace();
			
			

		}
		return jsonObject;
	}

	public static ArrayList<JSONObject> resolveJsonArray(JSONArray jsonArray) {
		ArrayList<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject tempJsonObject = null;
			try {
				tempJsonObject = jsonArray.getJSONObject(i);
				jsonObjectList.add(tempJsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return jsonObjectList;
	}

}
