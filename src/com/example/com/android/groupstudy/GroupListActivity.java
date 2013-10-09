package com.example.com.android.groupstudy;

import com.android.groupstudy.features.StudyMainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class GroupListActivity extends Activity implements OnClickListener, OnItemClickListener {
	
	Button btnGroupAdd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_list);
		
		btnGroupAdd = (Button) findViewById(R.id.group_add);
		btnGroupAdd.setOnClickListener(this);
		
		ListView listview = (ListView) findViewById(R.id.listview);
	    String[] values = new String[] { "취업 스터디", "영어 스터디" };
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	        R.layout.study_list_font, values);
	    
	    
	    listview.setDividerHeight(3);
	    listview.setAdapter(adapter);
	    listview.setOnItemClickListener(this);
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
	

}
