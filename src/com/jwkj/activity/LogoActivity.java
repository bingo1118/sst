package com.jwkj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import cn.jpush.android.api.JPushInterface;

import com.test.jpushServer.R;
import com.jwkj.global.Constants;

public class LogoActivity extends BaseActivity{
	Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logo);
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				Intent i = new Intent(LogoActivity.this,MainActivity.class);
				startActivity(i);
				finish();
			}
			
		};
		Message msg = new Message();
		msg.what = 0x11;
		handler.sendMessageDelayed(msg, 2000);
	}
	
	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_LOGOACTIVITY;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(getApplicationContext());
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JPushInterface.onPause(getApplicationContext());
	}

}
