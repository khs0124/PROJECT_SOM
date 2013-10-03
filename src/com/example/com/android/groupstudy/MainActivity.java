package com.example.com.android.groupstudy;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.android.groupstudy.lib.DatabaseHandler;
import com.android.groupstudy.lib.UserFunctions;

public class MainActivity extends Activity implements OnClickListener {

	Button btnRegister;
	Button btnLogin;
	EditText loginEmail;
	EditText loginPassword;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
		String email = loginEmail.getText().toString();
		String password = loginPassword.getText().toString();
		
		UserFunctions userFunction = new UserFunctions();
		Log.d("Button", "Login");
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
					
					Intent grouplist = new Intent(getApplicationContext(), GroupListActivity.class);
					
					grouplist.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(grouplist);
					
					finish();
				}
			}
		} catch (JSONException e){
			e.printStackTrace();
		}
	}

}
