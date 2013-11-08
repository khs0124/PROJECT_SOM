
package com.android.groupstudy.lib;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class UserFunctions {
	
	private JSONParser jsonParser;
	
	private static String loginURL = "http://192.168.0.43/android_login_api/";
	private static String registerURL = "http://192.168.0.43/android_login_api/";
	
	private static String login_tag = "login";
	private static String register_tag = "register";
	private static String group_register_tag = "group_register";
	
	// constructor
	public UserFunctions(){
		jsonParser = new JSONParser();
	}
	
	/**
	 * function make Login Request
	 * @param email
	 * @param password
	 * */	
	public JSONObject loginUser(String email, String password){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, "POST", params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}
	
	/**
	 * function make Login Request
	 * @param name
	 * @param email
	 * @param password
	 * */
	public JSONObject registerUser(String email, String password, String name, String birth, String phonenum){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("birth", birth));
		params.add(new BasicNameValuePair("phonenum", phonenum));
		
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, "POST", params);
		// return json
		return json;
	}
	
	public JSONObject registerGroup(String gname, String members){
		
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", group_register_tag));
		params.add(new BasicNameValuePair("gname", gname));
		params.add(new BasicNameValuePair("members", members));
		
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, "POST", params);
		// return json
		return json;
	}
	
	/*public JSONObject getGroupList(){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, "POST", params);
		
		return json;
	}*/
	
	/**
	 * Function get Login status
	 * */
	public boolean isUserLoggedIn(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		int count = db.getRowCount();
		if(count > 0){
			// user logged in
			return true;
		}
		return false;
	}
	
	/**
	 * Function to logout user
	 * Reset Database
	 * */
	public boolean logoutUser(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		db.resetTables();
		return true;
	}
	
	public boolean clearGroup(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		db.resetGroupTables();
		return true;
	}
	
	
}
