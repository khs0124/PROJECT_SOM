package com.android.groupstudy.features;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;

import com.example.com.android.groupstudy.R;

public class StudyMainActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.study_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
