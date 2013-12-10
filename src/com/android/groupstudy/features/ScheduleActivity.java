package com.android.groupstudy.features;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.groupstudy.lib.JSONParser;
import com.example.com.android.groupstudy.R;

public class ScheduleActivity extends ListActivity implements OnClickListener {

	private Button addSchedule;

	String members;
	String gname;

	JSONArray scheduleList = null;
	scheduleInfo p;

	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	private static final String get_scheduleinfo_url = "http://192.168.0.43/android_login_api/get_schedule.php";

	private static final String KEY_SUCCESS = "success";
	private static final String KEY_SID = "sid";
	private static final String KEY_TITLE = "title";
	private static final String KEY_CONTENT = "content";
	private static final String KEY_DATE = "created_at";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule);

		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		members = pref.getString("email", "");
		gname = pref.getString("gname", "");

		addSchedule = (Button) findViewById(R.id.schedule_add);

		addSchedule.setOnClickListener(this);

		new GetScheduleInfo().execute();

		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				SharedPreferences schedule = getSharedPreferences("schedule", MODE_PRIVATE);
				SharedPreferences.Editor editor = schedule.edit();
			
				editor.putString("sid", p.getSid());
				editor.putString("title", p.getTitle());
				editor.putString("content", p.getContent());
				editor.commit();
				Intent intent = new Intent(getApplicationContext(),
						ScheduleUpdateActivity.class);
				startActivity(intent);

			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.schedule_add) {
			SharedPreferences schedule = getSharedPreferences("schedule", MODE_PRIVATE);
			SharedPreferences.Editor editor = schedule.edit();
			editor.putString("title", "");
			editor.putString("content", "");
			editor.commit();
			
			Intent scheduleAdd = new Intent(getApplicationContext(),
					ScheduleAddActivity.class);
			scheduleAdd.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(scheduleAdd);
		}
	}

	class GetScheduleInfo extends AsyncTask<String, String, String> {

		ArrayList<scheduleInfo> schedule_order = new ArrayList<scheduleInfo>();
		scheduleInfo s;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ScheduleActivity.this);
			pDialog.setMessage("로딩중입니다...");
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
						get_scheduleinfo_url, "GET", params);

				Log.d("scheduleInfo : ", json.toString());
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						scheduleList = json.getJSONArray("schedulesinfo");

						for (int i = 0; i < scheduleList.length(); i++) {
							JSONObject g = scheduleList.getJSONObject(i);

							if (!(g.getString(KEY_TITLE).equals(""))) {
								s = new scheduleInfo(g.getString(KEY_SID), g.getString(KEY_DATE),
										g.getString(KEY_TITLE),
										g.getString(KEY_CONTENT));

								schedule_order.add(s);
							}
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
			// Log.d("name : ", name[0]);
			// Log.d("name : ", name[1]);
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					ScheduleInfoAdapter s_adapter = new ScheduleInfoAdapter(
							ScheduleActivity.this, R.layout.schedule_list_font,
							schedule_order);
					setListAdapter(s_adapter);
				}

			});

		}

	}

	private class ScheduleInfoAdapter extends ArrayAdapter<scheduleInfo> {

		private ArrayList<scheduleInfo> items;

		public ScheduleInfoAdapter(Context context, int textViewResourceId,
				ArrayList<scheduleInfo> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.schedule_list_font, null);
			}
			p = items.get(position);
			if (p != null) {
				TextView ft1 = (TextView) v.findViewById(R.id.fineText1);
				TextView ft2 = (TextView) v.findViewById(R.id.fineText2);
				if (ft1 != null) {
					ft1.setText(p.getDate());
				}
				if (ft2 != null) {
					ft2.setText(p.getTitle());
				}

			}
			return v;
		}
	}

	class scheduleInfo {

		private String sid;
		private String title;
		private String content;
		private String created_at;

		public scheduleInfo(String _Sid, String _Date, String _Title, String _Content) {
			this.sid = _Sid;
			this.created_at = _Date;
			this.title = _Title;
			this.content = _Content;
		}

		public String getSid() {
			return sid;
		}
		public String getTitle() {
			return title;
		}

		public String getContent() {
			return content;
		}

		public String getDate() {
			return created_at;
		}

	}

}