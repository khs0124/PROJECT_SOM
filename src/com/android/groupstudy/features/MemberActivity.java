package com.android.groupstudy.features;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.android.groupstudy.lib.JSONParser;
import com.example.com.android.groupstudy.R;

public class MemberActivity extends Activity {

	String members;
	String gname;
	String[] name;
	String[] birth;
	String[] phonenum;
	int member_count = 0;

	//TextView member1;
	LinearLayout layout;
	TextView member[];

	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	private static final String get_membersinfo_url = "http://192.168.0.217/android_login_api/get_members.php";

	private static final String KEY_SUCCESS = "success";
	private static final String KEY_NAME = "name";
	private static final String KEY_BIRTH = "birth";
	private static final String KEY_PHONENUM = "phonenum";

	final static int MEMBER_NUM = 6;

	JSONArray memberInfoList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member);
		
		layout = (LinearLayout) findViewById(R.id.member_layout);
		member = new TextView[MEMBER_NUM];

		name = new String[MEMBER_NUM];
		birth = new String[MEMBER_NUM];
		phonenum = new String[MEMBER_NUM];
		
		init();

		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		members = pref.getString("email", "");
		gname = pref.getString("gname", "");

		new GetMembersInfo().execute();

		//member1 = (TextView) findViewById(R.id.Text01);
	}
	
	void init(){
		for (int i = 0; i < MEMBER_NUM; i++) {
			name[i] = "";
			birth[i] = "";
			phonenum[i] = "";
		}
	}

	class GetMembersInfo extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MemberActivity.this);
			pDialog.setMessage("가져오는중입니다...");
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

				Log.d("params : ", params.toString());
				JSONObject json = jsonParser.getJSONFromUrl(
						get_membersinfo_url, "GET", params);

				Log.d("Members : ", json.toString());
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						memberInfoList = json.getJSONArray("membersinfo");

						for (int i = 0; i < memberInfoList.length(); i++) {
							JSONObject g = memberInfoList.getJSONObject(i);

							name[i] = g.getString(KEY_NAME);
							birth[i] = g.getString(KEY_BIRTH);
							phonenum[i] = g.getString(KEY_PHONENUM);
						
							member_count++;
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
//			Log.d("name : ", name[0]);
//			Log.d("name : ", name[1]);
			pDialog.dismiss();
			Log.d("memberCount : ", String.valueOf(Integer.toString(member_count)));;
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					for (int j = 0; j < member_count; j++) {
						if(!name[j].equals("")){
							member[j] = new TextView(MemberActivity.this);
							String textColor="#000000";
							
							member[j].setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
							member[j].setBackgroundResource(R.drawable.member_empty);
							member[j].setGravity(Gravity.CENTER);
							member[j].setText(String.valueOf("이름: " + name[j]) + "\n" + 
									"생년월일: " + String.valueOf(birth[j]) + "\n" + "핸드폰번호: " + String.valueOf(phonenum[j]));
							member[j].setTextSize(17);
							member[j].setTextColor(Color.parseColor(textColor));
							layout.addView(member[j]);
							
							if(j==0){
								ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(member[j].getLayoutParams());
								margin.setMargins(0, 100, 0, 0);
								member[j].setLayoutParams(new LinearLayout.LayoutParams(margin));
							}
							else{
								ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(member[j].getLayoutParams());
								margin.setMargins(0, 10, 0, 0);
								member[j].setLayoutParams(new LinearLayout.LayoutParams(margin));
							}
						}
					}
				}
			});

		}
	}
}
