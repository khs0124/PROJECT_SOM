package com.android.groupstudy.features;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.example.com.android.groupstudy.R;

public class BullteinActivity extends Activity implements OnClickListener {
	
	private Button jobkorea;
	private Button saramin;
	private Button joberum;
	private Button hackers;
	private Button pagoda;
	private Button ybm;
	private Button toeic;
	private Button opic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bulletin_board);
		
		jobkorea = (Button) findViewById(R.id.jobkorea);
		saramin = (Button) findViewById(R.id.saramin);
		joberum = (Button) findViewById(R.id.joberum);
		hackers = (Button) findViewById(R.id.hackers);
		pagoda = (Button) findViewById(R.id.pagoda);
		ybm = (Button) findViewById(R.id.ybm);
		toeic = (Button) findViewById(R.id.toeic);
		opic = (Button) findViewById(R.id.opic);
		
		jobkorea.setOnClickListener(this);
		saramin.setOnClickListener(this);
		joberum.setOnClickListener(this);
		hackers.setOnClickListener(this);
		pagoda.setOnClickListener(this);
		ybm.setOnClickListener(this);
		toeic.setOnClickListener(this);
		opic.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.jobkorea){
			Intent jobkorea = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://m.jobkorea.co.kr/"));
			startActivity(jobkorea);
		}
		else if(v.getId() == R.id.saramin){
			Intent saramin = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://m.saramin.co.kr/"));
			startActivity(saramin);
		}
		else if(v.getId() == R.id.joberum){
			Intent joberum = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.joberum.com/"));
			startActivity(joberum);
		}
		else if(v.getId() == R.id.hackers){
			Intent hackers = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://m.hackers.co.kr/"));
			startActivity(hackers);
		}
		else if(v.getId() == R.id.pagoda){
			Intent pagoda = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://m.pagoda21.com/"));
			startActivity(pagoda);
		}
		else if(v.getId() == R.id.ybm){
			Intent ybm = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.ybmsisa.com/"));
			startActivity(ybm);
		}
		else if(v.getId() == R.id.toeic){
			Intent toeic = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://exam.ybmsisa.com/"));
			startActivity(toeic);
		}
		else if(v.getId() == R.id.opic){
			Intent opic = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.opic.or.kr/"));
			startActivity(opic);
		}
	}
}
