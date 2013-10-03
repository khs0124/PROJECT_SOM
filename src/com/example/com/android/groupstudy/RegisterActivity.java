package com.example.com.android.groupstudy;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.groupstudy.lib.DatabaseHandler;
import com.android.groupstudy.lib.UserFunctions;


public class RegisterActivity extends Activity implements OnClickListener {

	Button btnRegister;
	Button btnCancel;
	EditText inputEmail;
	EditText inputPassword;
	EditText inputName;
	EditText inputBirth;
	EditText inputPhonenum;
	
	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_UID = "uid";
	private static String KEY_EMAIL = "email";
	private static String KEY_PASSWORD = "password";
	private static String KEY_NAME = "name";
	private static String KEY_BIRTH = "birth";
	private static String KEY_PHONENUM = "phonenum";
	private static String KEY_CREATED_AT = "created_at";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_2);

		btnRegister = (Button) findViewById(R.id.register);
		btnCancel = (Button) findViewById(R.id.register_cancel);

		inputEmail = (EditText) findViewById(R.id.input_email);
		inputPassword = (EditText) findViewById(R.id.input_password);
		inputName = (EditText) findViewById(R.id.input_name);
		inputBirth = (EditText) findViewById(R.id.input_birth);
		inputPhonenum = (EditText) findViewById(R.id.input_phoneNum);

		btnRegister.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.register) {
			RegisterMember();
		} else if (v.getId() == R.id.register_cancel) {
			cancelDialog();
		}
	}

	@Override
	public void onBackPressed(){
		cancelDialog();
		//super.onBackPressed();
	}
	
	private void RegisterMember() {
		// TODO Auto-generated method stub
		String email = inputEmail.getText().toString();
		String password = inputPassword.getText().toString();
		String name = inputName.getText().toString();
		String birth = inputBirth.getText().toString();
		String phonenum = inputPhonenum.getText().toString();
		
		// input exception
		if(email.equals("")){
			Toast.makeText(this, "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
			return;
		}
		else if(password.equals("")){
			Toast.makeText(this, "패스워드를 입력하세요", Toast.LENGTH_SHORT).show();
			return;
		}
		else if(name.equals("")){
			Toast.makeText(this, "이름을 입력하세요", Toast.LENGTH_SHORT).show();
			return;
		}
		else if(birth.equals("")){
			Toast.makeText(this, "생년월일을 입력하세요", Toast.LENGTH_SHORT).show();
			return;
		}
		else if(phonenum.equals("")){
			Toast.makeText(this, "폰번호를 입력하세요", Toast.LENGTH_SHORT).show();
			return;
		}
		
		UserFunctions userFunction = new UserFunctions();
		JSONObject json = userFunction.registerUser(email, password, name, birth, phonenum);
		
		// check for login response
		try{
			if(json.getString(KEY_SUCCESS) != null){
				String res = json.getString(KEY_SUCCESS);
				if(Integer.parseInt(res) == 1){			// user successfully registered
					
					// Store user details in SQLite Database
					DatabaseHandler db = new DatabaseHandler(getApplicationContext());
					JSONObject json_user = json.getJSONObject("user");
					
					// Clear all previous data in database
					userFunction.logoutUser(getApplicationContext());
					db.addUser(json_user.getString(KEY_EMAIL), json_user.getString(KEY_NAME), 
							json_user.getString(KEY_BIRTH), json_user.getString(KEY_PHONENUM), json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));
					
					Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show();
					
					Intent main = new Intent(getApplicationContext(), MainActivity.class);
					main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(main);
					
					finish();
				}
			}
		} catch (JSONException e){
			e.printStackTrace();
		}
	}

	public void cancelDialog(){
		AlertDialog.Builder altCancel = new AlertDialog.Builder(this);
		altCancel.setMessage("회원가입을 취소하시겠습니까 ?").setCancelable(false).setPositiveButton("예", 
			new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					finish();
				}
			}).setNegativeButton("아니오", 
			new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
		
		AlertDialog alert = altCancel.create();
		alert.setTitle("회원가입");
		alert.show();
	}
	
}