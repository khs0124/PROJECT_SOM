package com.android.groupstudy.features;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.android.groupstudy.R;

public class TimerActivity extends Activity implements OnClickListener {

	Button UpstartButton;
	Button UppauseButton;
	Button UPresetButton;
	
	Button DownStartButton;
	Button DownPauseButton;
	Button DownResetButton;
	
	EditText setHour;
	EditText setMin;
	EditText setSec;

	TextView UptimerValue;
	TextView DowntimerValue;

	long startTime = 0L;

	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;

	Handler customHandler = new Handler();
	
	long totalTimeCountInMilliseconds;
	long leftedTimeCountInMilliseconds;
	
	final static long seconds_in_millies = 1000L;
	final static long minutes_in_millies = seconds_in_millies * 60;
	final static long hours_in_millies = minutes_in_millies * 60;
	
	CountDownTimer countDownTimer;
	CountDownTimer resumeTimer;
	
	boolean ontickStarted = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timer);

		TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
		tabHost.setup();

		TabHost.TabSpec spec = tabHost.newTabSpec("tag1");
		spec.setContent(R.id.tab1);
		spec.setIndicator("Count-up");
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("tag2");
		spec.setContent(R.id.tab2);
		spec.setIndicator("Count-down");
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);

		UptimerValue = (TextView) findViewById(R.id.up_timertext);
		UpstartButton = (Button) findViewById(R.id.up_btnStart);
		UppauseButton = (Button) findViewById(R.id.up_btnPause);
		UPresetButton = (Button) findViewById(R.id.up_btnReset);

		UpstartButton.setOnClickListener(this);
		UppauseButton.setOnClickListener(this);
		UPresetButton.setOnClickListener(this);
		
		DowntimerValue = (TextView) findViewById(R.id.down_timertext);
		DowntimerValue.setVisibility(View.INVISIBLE);
		
		setHour = (EditText) findViewById(R.id.setHour);
		setMin = (EditText) findViewById(R.id.setMin);
		setSec = (EditText) findViewById(R.id.setSec);
		
		DownStartButton = (Button) findViewById(R.id.down_btnStart);
		DownPauseButton = (Button) findViewById(R.id.down_btnPause);
		DownResetButton = (Button) findViewById(R.id.down_btnReset);
				
		DownStartButton.setOnClickListener(this);
		DownPauseButton.setOnClickListener(this);
		DownResetButton.setOnClickListener(this);
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
		if(v.getId() == R.id.up_btnStart) {
			startTime = SystemClock.uptimeMillis();
			customHandler.postDelayed(updateTimerThread, 0);
		}else if(v.getId() == R.id.up_btnPause) {
			timeSwapBuff += timeInMilliseconds;
			customHandler.removeCallbacks(updateTimerThread);
		}
		else if(v.getId() == R.id.up_btnReset){
			UptimerValue.setText("00:00:00");
			startTime = 0L;
			timeInMilliseconds = 0L;
			timeSwapBuff = 0L;
			updatedTime = 0L;
		}
		else if(v.getId() == R.id.down_btnStart){
			setTimer();
			startTimer();
		}
		else if(v.getId() == R.id.down_btnPause){
			countDownTimer.cancel();		
		}
		else if(v.getId() == R.id.down_btnReset){
			countDownTimer.cancel();
			
			DowntimerValue.setVisibility(View.INVISIBLE);
			setHour.setVisibility(View.VISIBLE);
			setMin.setVisibility(View.VISIBLE);
			setSec.setVisibility(View.VISIBLE);
			
			setHour.setText("00");
			setMin.setText("00");
			setSec.setText("00");
			
			totalTimeCountInMilliseconds = 0L;
		}
	}

	private Runnable updateTimerThread = new Runnable() {

		public void run() {

			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

			updatedTime = timeSwapBuff + timeInMilliseconds;

			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			int hours = mins / 60;
			secs = secs % 60;
			UptimerValue.setText("" + String.format("%02d", hours) + ":" + String.format("%02d", mins) 
					+ ":" + String.format("%02d", secs));
			customHandler.postDelayed(this, 0);
		}

	};
	
	private void setTimer() {
		// TODO Auto-generated method stub
		int hour = 0, min = 0, sec = 0;
		
		if((setHour.length() == 0) && (setMin.length() == 0) &&
				(setSec.length() == 0)){
			Toast.makeText(getApplicationContext(), "시간을 설정해주세요...", Toast.LENGTH_SHORT).show();
			return;
		}
		else{
			if(setHour.getText().toString().equals(""))
				setHour.setText("00");
			if(setMin.getText().toString().equals(""))
				setMin.setText("00");
			if(setSec.getText().toString().equals(""))
				setSec.setText("00");
			
			hour = Integer.parseInt(setHour.getText().toString());
			min = Integer.parseInt(setMin.getText().toString());
			sec = Integer.parseInt(setSec.getText().toString());
			
			totalTimeCountInMilliseconds = hour * 60 * 60 * 1000 + min * 60 * 1000 + sec * 1000;
			
//			String total = Long.toString(totalTimeCountInMilliseconds);
//			Toast.makeText(getApplicationContext(), total, Toast.LENGTH_SHORT).show();
		}
		
		DowntimerValue.setVisibility(View.VISIBLE);
		setHour.setVisibility(View.INVISIBLE);
		setMin.setVisibility(View.INVISIBLE);
		setSec.setVisibility(View.INVISIBLE);
	}
	
	private void startTimer() {
		// TODO Auto-generated method stub
		countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				long hour = 0L;
				long min = 0L;
				long sec = 0L;
				
				hour = millisUntilFinished / hours_in_millies;
				millisUntilFinished %= hours_in_millies;
				min = millisUntilFinished / minutes_in_millies;
				millisUntilFinished %= minutes_in_millies;
				sec = millisUntilFinished / seconds_in_millies;
				
				DowntimerValue.setText("" + String.format("%02d", hour) + ":" + String.format("%02d", min) 
						+ ":" + String.format("%02d", sec));
				
				ontickStarted = true;
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				if(ontickStarted){
					Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					vibrator.vibrate(2000);
					
					ontickStarted = false;
				}
				
				DowntimerValue.setVisibility(View.INVISIBLE);
				setHour.setVisibility(View.VISIBLE);
				setMin.setVisibility(View.VISIBLE);
				setSec.setVisibility(View.VISIBLE);
				
				totalTimeCountInMilliseconds = 0L;
			}
		}.start();
	}
}
