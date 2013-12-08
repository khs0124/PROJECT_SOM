package com.android.groupstudy.features;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.android.groupstudy.lib.JSONParser;
import com.example.com.android.groupstudy.R;

public class StudyMainActivity extends Activity implements OnClickListener {

	private Button timer;
	private Button member;
	private Button bulletin;
	private Button fine;

	String members;
	String gname;
	
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	
	private static String KEY_SUCCESS = "success";
	private static final String join_users_and_groups_url = "http://192.168.0.43/android_login_api/join_users_and_groups.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.study_main);

		timer = (Button) findViewById(R.id.timer);
		member = (Button) findViewById(R.id.member);
		bulletin = (Button) findViewById(R.id.bulletinboard);
		fine = (Button) findViewById(R.id.fine);

		timer.setOnClickListener(this);
		member.setOnClickListener(this);
		bulletin.setOnClickListener(this);
		fine.setOnClickListener(this);

		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		members = pref.getString("email", "");
		gname = pref.getString("gname", "");
		
		new JoinUsersAndGroups().execute(); 
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.timer) {
			Intent timer = new Intent(getApplicationContext(),
					TimerActivity.class);
			timer.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(timer);
		} else if (v.getId() == R.id.member) {
			Intent member = new Intent(getApplicationContext(),
					MemberActivity.class);
			member.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(member);
		} else if (v.getId() == R.id.bulletinboard) {
			Intent bulltein = new Intent(getApplicationContext(),
					BullteinActivity.class);
			bulltein.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(bulltein);
		} else if (v.getId() == R.id.fine) {
			Intent fine = new Intent(getApplicationContext(),
					FineActivity.class);
			fine.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(fine);
		}

	}

	class JoinUsersAndGroups extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(StudyMainActivity.this);
			pDialog.setMessage("로딩중입니다....");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub

			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("gname", gname));
				params.add(new BasicNameValuePair("members", members));

				JSONObject json = jsonParser.getJSONFromUrl(
						join_users_and_groups_url, "GET", params);

				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						pDialog.dismiss();
					}
				}
			}

			catch (JSONException e) {
				e.printStackTrace();

			}
			return null;
		}
		
		//protected void onPostExecute(String file_url) {
		//}
	}
}
