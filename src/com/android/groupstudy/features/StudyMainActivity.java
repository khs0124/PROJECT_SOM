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
	private Button member;
	private Button bulletin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.study_main);
		
		timer = (Button)findViewById(R.id.timer);
		member = (Button)findViewById(R.id.member);
		bulletin = (Button)findViewById(R.id.bulletinboard);
		
		timer.setOnClickListener(this);
		member.setOnClickListener(this);
		bulletin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.timer){
			Intent timer = new Intent(getApplicationContext(),
					TimerActivity.class);
			timer.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(timer);
		}	
		else if(v.getId() == R.id.member){
			Intent member = new Intent(getApplicationContext(),
					MemberActivity.class);
			member.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(member);
		}
		else if(v.getId() == R.id.bulletinboard){
			Intent bulltein = new Intent(getApplicationContext(),
					BullteinActivity.class);
			bulltein.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(bulltein);
		}
	}
}
