package com.example.com.android.groupstudy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.groupstudy.features.StudyMainActivity;
import com.android.groupstudy.lib.JSONParser;

public class GroupListActivity extends Activity implements OnClickListener, OnItemClickListener, OnItemLongClickListener {
	
	Button btnGroupAdd;
	SimpleAdapter list;
	String []gid;
	String gname;
	int position;
	
	ArrayList<HashMap<String, String>> groupList;
	private ProgressDialog pDialog;
	
	JSONParser jsonParser = new JSONParser();
	
	private static final String delete_grouplist_url = "http://192.168.0.43/android_login_api/delete_grouplist.php";
	
	private static final String KEY_SUCCESS = "success";
	private static final String KEY_GID = "gid";
	private static final String KEY_GNAME = "gname";
	private static final String KEY_GROUPLISTS = "grouplists";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_list);
		
		btnGroupAdd = (Button) findViewById(R.id.group_add);
		btnGroupAdd.setOnClickListener(this);
		
		Intent intent = getIntent();
		//gname = intent.getStringExtra("gname");
		groupList = (ArrayList<HashMap<String, String>>) intent.getSerializableExtra("groupList");
		
		ListView listview = (ListView) findViewById(R.id.listview);
		
		String[] from = new String[] {"gname"};
		int[] to = new int[] {R.id.text1};
		
		list = new SimpleAdapter(this, groupList, R.layout.study_list_font, from, to);
		listview.setAdapter(list);
		
		listview.setDividerHeight(3);
	    listview.setOnItemClickListener(this);
	    registerForContextMenu(listview);
	    
	    int grouplistNum = groupList.size();
		gid = new String[grouplistNum];
		
		for(int i=0; i<grouplistNum; i++){
			gid[i] = groupList.get(i).get(KEY_GID);
		}
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
		Intent registerGroup = new Intent(getApplicationContext(), GroupRegisterActivity.class);
		startActivity(registerGroup);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent studymain = new Intent(getApplicationContext(), StudyMainActivity.class);
		startActivity(studymain);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.grouplist_contextmenu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    
	    switch (item.getItemId()) {
	        case R.id.group_enter:
	        	Intent studymain = new Intent(getApplicationContext(), StudyMainActivity.class);
	    		startActivity(studymain);
	            return true;
	        case R.id.group_exit:
	        	position = info.position;
	        	groupList.remove(position);
	        	list.notifyDataSetChanged();
	        	new DeleteGroupList().execute();
	        	return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	@Override
	public void onBackPressed() {
		finish();
		// super.onBackPressed();
	}
	
	class DeleteGroupList extends AsyncTask<String, String, String>{
		
		@Override
		protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GroupListActivity.this);
            pDialog.setMessage("삭제중입니다...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				Log.d("gid : ", gid[position]);
				params.add(new BasicNameValuePair("gid", gid[position]));
				
				JSONObject json = jsonParser.getJSONFromUrl(delete_grouplist_url, "POST", params);
				
				Log.d("Delete Grouplist", json.toString());
				
				if( json.getString(KEY_SUCCESS) != null){
					String res = json.getString(KEY_SUCCESS);
					if(Integer.parseInt(res) == 1){
						pDialog.dismiss();
					}
				}
			}
			
			catch (JSONException e){
				e.printStackTrace();
				
			}
			return null;
		}
		
		
	}

}
