package com.android.groupstudy.features;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.groupstudy.lib.JSONParser;
import com.example.com.android.groupstudy.R;

public class ScheduleUpdateActivity extends Activity implements OnClickListener {


	String title;
	String content;
	String sid;
	
	Button btnCancel;
	Button btnUpdate;
	
	EditText inputTitle;
	EditText inputContent;
	
	private static String KEY_SUCCESS = "success";
	
	JSONParser jsonParser = new JSONParser();
	
	private static final String schedule_update_url = "http://192.168.0.43/android_login_api/schedule_update.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_schedule);
		
		SharedPreferences schedule = getSharedPreferences("schedule", MODE_PRIVATE);
		title = schedule.getString("title", "");
		content = schedule.getString("content", "");
		sid = schedule.getString("sid", "");

		btnCancel = (Button) findViewById(R.id.cancel);
		btnUpdate = (Button) findViewById(R.id.update);
		
		inputTitle = (EditText) findViewById(R.id.schedule_title);
		inputContent = (EditText) findViewById(R.id.schedule_content);
		
		if(!title.equals("")){
			inputTitle.setText(title);
			inputContent.setText(content);
		}

		btnCancel.setOnClickListener(this);
		btnUpdate.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.cancel) {
			cancelDialog();
		} else if (v.getId() == R.id.update) {
			UpdateSchedule();
		}

	}

	private void UpdateSchedule() {
		// TODO Auto-generated method stub
		String title = inputTitle.getText().toString();
		String content = inputContent.getText().toString();
		
		if(title.equals("")){
			Toast.makeText(this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
			return;
		}
		else if(content.equals("")){
			Toast.makeText(this, "내용을 입력하세요", Toast.LENGTH_SHORT).show();
			return;
		}
		
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("sid", sid));
			params.add(new BasicNameValuePair("title", title));
			params.add(new BasicNameValuePair("content", content));
			
			Log.d("params : ", params.toString());
			JSONObject json = jsonParser.getJSONFromUrl(
					schedule_update_url, "POST", params);

			if (json.getString(KEY_SUCCESS) != null) {
				String res = json.getString(KEY_SUCCESS);
				if (Integer.parseInt(res) == 1) {
					
					Toast.makeText(this, "일정수정 완료", Toast.LENGTH_SHORT).show();
					
					Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);

					finish();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void onBackPressed() {
		cancelDialog();
		// super.onBackPressed();
	}

	private void cancelDialog() {
		AlertDialog.Builder altCancel = new AlertDialog.Builder(this);
		altCancel
				.setMessage("일정등록을 취소하시겠습니까 ?")
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
		alert.setTitle("일정등록");
		alert.show();
	}

}