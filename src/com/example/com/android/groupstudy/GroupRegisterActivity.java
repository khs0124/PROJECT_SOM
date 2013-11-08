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
import android.widget.Toast;

import com.android.groupstudy.lib.DatabaseHandler;
import com.android.groupstudy.lib.UserFunctions;

public class GroupRegisterActivity extends Activity implements OnClickListener {

	Button btnGroupRegister;
	Button btnGroupCancel;

	EditText inputGroupname;
	EditText inputMember1;
	EditText inputMember2;
	EditText inputMember3;
	EditText inputMember4;
	EditText inputMember5;
	EditText inputMember6;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_GID = "gid";
	private static String KEY_GNAME = "gname";
	private static String KEY_MEMBERS = "members";
	private static String KEY_CREATED_AT = "created_at";
	
	final static int MEMBER_NUM = 6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_group);

		btnGroupRegister = (Button) findViewById(R.id.register);
		btnGroupCancel = (Button) findViewById(R.id.register_cancel);

		inputGroupname = (EditText) findViewById(R.id.group_name);

		inputMember1 = (EditText) findViewById(R.id.member1);
		inputMember2 = (EditText) findViewById(R.id.member2);
		inputMember3 = (EditText) findViewById(R.id.member3);
		inputMember4 = (EditText) findViewById(R.id.member4);
		inputMember5 = (EditText) findViewById(R.id.member5);
		inputMember6 = (EditText) findViewById(R.id.member6);

		btnGroupRegister.setOnClickListener(this);
		btnGroupCancel.setOnClickListener(this);
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
		if (v.getId() == R.id.register) {
			RegisterGroup();
		} else if (v.getId() == R.id.register_cancel) {
			cancelDialog();
		}
	}

	public void onBackPressed() {
		cancelDialog();
		// super.onBackPressed();
	}

	private void RegisterGroup() {		
		String gname = inputGroupname.getText().toString();
		String member[] = new String[MEMBER_NUM];
		
		member[0] = inputMember1.getText().toString();
		member[1] = inputMember2.getText().toString();
		member[2] = inputMember3.getText().toString();
		member[3] = inputMember4.getText().toString();
		member[4] = inputMember5.getText().toString();
		member[5] = inputMember6.getText().toString();
		
		if (gname.equals("")) {
			Toast.makeText(this, "그룹명을 입력하세요", Toast.LENGTH_SHORT).show();
			return;
		} else if (member[0].equals("") && member[1].equals("")
				&& member[2].equals("") && member[3].equals("")
				&& member[4].equals("") && member[5].equals("")) {
			Toast.makeText(this, "구성원 email(ID)을 입력하세요", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		
		UserFunctions userFunction = new UserFunctions();
		
		for(int i=0; i<MEMBER_NUM; i++){
			if(member[i].equals(""))
				break;
			JSONObject json = userFunction.registerGroup(gname, member[i]);
		}
		
		Toast.makeText(this, "그룹생성 완료", Toast.LENGTH_SHORT).show();
		
		Intent main = new Intent(getApplicationContext(), MainActivity.class);
		main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//main.putExtra("gname", gname);
		startActivity(main);

		finish();

		/*try {
			if (json.getString(KEY_SUCCESS) != null) {
				String res = json.getString(KEY_SUCCESS);
				if (Integer.parseInt(res) == 1) {
					DatabaseHandler db = new DatabaseHandler(getApplicationContext());
					JSONObject json_group = json.getJSONObject("group");
					
					Log.d(tag, "!!!");
					userFunction.clearGroup(getApplicationContext());
					db.addGroup(json_group.getString(KEY_GNAME),
							json_group.getString(KEY_MEMBERS),
							json.getString(KEY_GID),
							json_group.getString(KEY_CREATED_AT));
				
					Toast.makeText(this, "그룹생성 완료", Toast.LENGTH_SHORT).show();
					
					Intent main = new Intent(getApplicationContext(), GroupListActivity.class);
					main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(main);

					finish();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}*/
		
	}

	private void cancelDialog() {
		AlertDialog.Builder altCancel = new AlertDialog.Builder(this);
		altCancel
				.setMessage("그룹등록을 취소하시겠습니까 ?")
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
		alert.setTitle("그룹등록");
		alert.show();
	}
}
