package com.android.groupstudy.features;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.groupstudy.lib.JSONParser;
import com.example.com.android.groupstudy.R;

public class FineActivity extends ListActivity implements OnClickListener {

	private Button addFine;

	String members;
	String gname;
	int totalfine = 0;

	JSONArray fineInfoList = null;
	final static int MEMBER_NUM = 6;

	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	private static final String get_fineinfo_url = "http://192.168.0.43/android_login_api/get_fine.php";

	private static final String KEY_SUCCESS = "success";
	private static final String KEY_NAME = "name";
	private static final String KEY_FINE = "fine";
	private static final String KEY_CONTENT = "fine_content";
	private static final String KEY_DATE = "created_at";
	
	TextView total;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fine);

		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		members = pref.getString("email", "");
		gname = pref.getString("gname", "");

		addFine = (Button) findViewById(R.id.fine_add);
		total = (TextView) findViewById(R.id.totalfine);

		addFine.setOnClickListener(this);

		new GetMembersInfo().execute();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.fine_add) {
			Intent fineAdd = new Intent(getApplicationContext(),
					FineAddActivity.class);
			fineAdd.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(fineAdd);
		}
	}

	class GetMembersInfo extends AsyncTask<String, String, String> {

		ArrayList<fineInfo> fine_order = new ArrayList<fineInfo>();
		fineInfo f;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(FineActivity.this);
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
						get_fineinfo_url, "GET", params);

				Log.d("fineinfo : ", json.toString());
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						fineInfoList = json.getJSONArray("finesinfo");

						for (int i = 0; i < fineInfoList.length(); i++) {
							JSONObject g = fineInfoList.getJSONObject(i);

							if (!(g.getString(KEY_FINE).equals(""))) {
								f = new fineInfo(g.getString(KEY_DATE),
										g.getString(KEY_NAME),
										g.getString(KEY_CONTENT),
										g.getString(KEY_FINE));
									
								totalfine += Integer.parseInt(g.getString(KEY_FINE));
								fine_order.add(f);
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
					FineInfoAdapter f_adapter = new FineInfoAdapter(
							FineActivity.this, R.layout.fine_list_font,
							fine_order); 
					setListAdapter(f_adapter);
					total.setText("총 금액 : " + totalfine);
				}

			});

		}

	}

	private class FineInfoAdapter extends ArrayAdapter<fineInfo> {

		private ArrayList<fineInfo> items;

		public FineInfoAdapter(Context context, int textViewResourceId,
				ArrayList<fineInfo> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.fine_list_font, null);
			}
			fineInfo p = items.get(position);
			if (p != null) {
				TextView ft1 = (TextView) v.findViewById(R.id.fineText1);
				TextView ft2 = (TextView) v.findViewById(R.id.fineText2);
				TextView ft3 = (TextView) v.findViewById(R.id.fineText3);
				TextView ft4 = (TextView) v.findViewById(R.id.fineText4);
				if (ft1 != null) {
					ft1.setText(p.getDate());
				}
				if (ft2 != null) {
					ft2.setText(p.getName());
				}
				if (ft3 != null) {
					ft3.setText(p.getFineContent());
				}
				if (ft4 != null) {
					ft4.setText(p.getFine());
				}
			}
			return v;
		}
	}

	class fineInfo {

		private String name;
		private String fine;
		private String fine_content;
		private String created_at;

		public fineInfo(String _Date, String _Name, String _Content,
				String _Fine) {
			this.created_at = _Date;
			this.name = _Name;
			this.fine_content = _Content;
			this.fine = _Fine;
		}

		public String getName() {
			return name;
		}

		public String getFine() {
			return fine;
		}

		public String getFineContent() {
			return fine_content;
		}

		public String getDate() {
			return created_at;
		}

	}
}
