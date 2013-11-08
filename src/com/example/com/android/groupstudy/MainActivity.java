package com.example.com.android.groupstudy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.groupstudy.lib.DatabaseHandler;
import com.android.groupstudy.lib.JSONParser;
import com.android.groupstudy.lib.UserFunctions;

public class MainActivity extends Activity implements OnClickListener {

	Button btnRegister;
	Button btnLogin;
	EditText loginEmail;
	EditText loginPassword;
	
	String email;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_UID = "uid";
	private static String KEY_EMAIL = "email";
	private static String KEY_NAME = "name";
	private static String KEY_BIRTH = "birth";
	private static String KEY_PHONENUM = "phonenum";
	private static String KEY_CREATED_AT = "created_at";
	
	private static String KEY_GID = "gid";
	private static String KEY_GNAME = "gname";
	private static String KEY_GROUPLISTS = "grouplists";
	
	private ProgressDialog pDialog;
	private static String get_grouplist_url = "http://192.168.0.43/android_login_api/grouplist.php";
	
	ArrayList<HashMap<String, String>> groupList;
	JSONParser jsonParser = new JSONParser();
	
	JSONArray groupLists = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		StrictMode.enableDefaults();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		startActivity(new Intent(this, SplashActivity.class));
		
		groupList = new ArrayList<HashMap<String, String>>();

		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnRegister = (Button) findViewById(R.id.btnRegister);

		loginEmail = (EditText) findViewById(R.id.loginEmail);
		loginPassword = (EditText) findViewById(R.id.loginPassword);

		btnLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnLogin) {
			LoginProcess();

		} else if (v.getId() == R.id.btnRegister) {
			Intent register = new Intent(getApplicationContext(),
					AgreeRegisterActivity.class);
			startActivity(register);
		}
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder altCancel = new AlertDialog.Builder(this);
		altCancel
				.setMessage("프로그램을 종료하시겠습니까 ?")
				.setCancelable(false)
				.setPositiveButton("예", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				})
				.setNegativeButton("아니오",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});

		AlertDialog alert = altCancel.create();
		alert.setTitle("종료");
		alert.show();
		// super.onBackPressed();
	}

	private void LoginProcess() {
		// TODO Auto-generated method stub
		email = loginEmail.getText().toString();
		String password = loginPassword.getText().toString();
		
		if(email.equals("")){
			Toast.makeText(this, "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
			return;
		}
		else if(password.equals("")){
			Toast.makeText(this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Log.d("Button", "Login");
		UserFunctions userFunction = new UserFunctions();
		JSONObject json = userFunction.loginUser(email, password);
		
		try{
			if( json.getString(KEY_SUCCESS) != null){
				String res = json.getString(KEY_SUCCESS);
				if(Integer.parseInt(res) == 1){
					// user successfully logged in
					// Store user details in SQLite Database
					DatabaseHandler db = new DatabaseHandler(getApplicationContext());
					JSONObject json_user = json.getJSONObject("user");
					
					// Clear all previous data in database
					userFunction.logoutUser(getApplicationContext());
					db.addUser(json_user.getString(KEY_EMAIL), json_user.getString(KEY_NAME), json_user.getString(KEY_BIRTH), 
							json_user.getString(KEY_PHONENUM), json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));
					
					new LoadGroupList().execute();
					
				}
			}
		} catch (JSONException e){
			e.printStackTrace();
		}
	}
	
	class LoadGroupList extends AsyncTask<String, String, String>{

		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("로그인중입니다...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
		}
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("members", email));
			JSONObject json = jsonParser.getJSONFromUrl(get_grouplist_url, "GET", params);
			
			Log.d("GroupLists: ", json.toString());
			try{
				if( json.getString(KEY_SUCCESS) != null){
					String res = json.getString(KEY_SUCCESS);
					if(Integer.parseInt(res) == 1){
						groupLists = json.getJSONArray(KEY_GROUPLISTS);
						
						for(int i=0; i<groupLists.length(); i++){
							JSONObject g = groupLists.getJSONObject(i);
							
							String gid = g.getString(KEY_GID);
							String gname = g.getString(KEY_GNAME);
							
							HashMap<String, String> map = new HashMap<String, String>();
							
							map.put(KEY_GID, gid);
							map.put(KEY_GNAME, gname);
							
							groupList.add(map);
						}
					}
				}	else{
					
				}
			}
			catch (JSONException e){
				e.printStackTrace();
			}
		
			return null;
		}
		
		
		protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            
            Intent grouplist = new Intent(getApplicationContext(), GroupListActivity.class);
			grouplist.putExtra("groupList", groupList);
			grouplist.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(grouplist);
			
			finish();
            
        }
	}
}
