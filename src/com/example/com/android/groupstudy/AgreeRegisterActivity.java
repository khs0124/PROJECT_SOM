package com.example.com.android.groupstudy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class AgreeRegisterActivity extends Activity implements OnClickListener {
	
	Button btnContinue;
	Button btnAgreement1;
	Button btnAgreement2;
	
	Boolean unchecked1 = true;
	Boolean unchecked2 = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_1);
		
		btnContinue = (Button) findViewById(R.id.continue_btn);
		btnAgreement1 = (Button) findViewById(R.id.agreement_check1);
		btnAgreement2 = (Button) findViewById(R.id.agreement_check2);
		
		btnContinue.setOnClickListener(this);
		btnAgreement1.setOnClickListener(this);
		btnAgreement2.setOnClickListener(this);
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
		if(v.getId() == R.id.continue_btn){
			if( (unchecked1 == false) && unchecked2 == false){
				Intent register = new Intent(getApplicationContext(), RegisterActivity.class);
				startActivity(register);
			}
			else{
				Toast.makeText(this, "약관에 모두 동의하셔야 합니다.", Toast.LENGTH_SHORT).show();
			}
		}
		else if(v.getId() == R.id.agreement_check1){
			if(unchecked1){
				btnAgreement1.setSelected(true);
				unchecked1 = false;
			}
			else{
				btnAgreement1.setSelected(false);
				unchecked1 = true;
			}
		}
		else if(v.getId() == R.id.agreement_check2){
			if(unchecked2){
				btnAgreement2.setSelected(true);
				unchecked2 = false;
			}
			else{
				btnAgreement2.setSelected(false);
				unchecked2 = true;
			}
		}
	}

}
