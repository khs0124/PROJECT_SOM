package com.android.groupstudy.features;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.example.com.android.groupstudy.AgreeRegisterActivity;
import com.example.com.android.groupstudy.R;

public class StudyMainActivity extends Activity implements OnClickListener{
	
	private Button timer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.study_main);
		
		timer = (Button)findViewById(R.id.timer);
		
		timer.setOnClickListener(this);
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
		if(v.getId() == R.id.timer){
			Intent timer = new Intent(getApplicationContext(),
					TimerActivity.class);
			startActivity(timer);
		}		
	}
}
