package com.jwkj.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.jwkj.CallActivity;
import com.jwkj.P2PConnect;
import com.jwkj.data.AlarmMask;
import com.jwkj.data.AlarmRecord;
import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.data.SharedPreferencesManager;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.MyApp;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.MusicManger;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.volley.JsonArrayPostRequest;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.P2PValue;
import com.test.jpushServer.R;

public class AlarmActivity extends Activity implements OnClickListener {

	Context mContext;
	int alarm_id, alarm_type, group, item;
	boolean isSupport;
	boolean hasContact = false;
	private int TIME_OUT = 20 * 1000;
	boolean isRegFilter = false;
	LinearLayout layout_area_chanel;
	ImageView alarm_fk_img;
	boolean isAlarm;
	private TextView monitor_fk_btn;
	private TextView alarm_type_fk_text;
	private TextView tv_fk_type;
	private TextView ignore_fk_btn,alarm_id_fk_text;
	private TextView shield_fk_btn;
	private TextView alarm_type_fk_time;
	private String areaName,channelName;
	private String retStrFormatNowDate;
	private String cameraName;
	private String str;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		if (P2PConnect.isPlaying()) {
			if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		}

		Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		mContext = this;
		alarm_id = getIntent().getIntExtra("alarm_id", 0);
		alarm_type = getIntent().getIntExtra("alarm_type", 0);
		isSupport = getIntent().getBooleanExtra("isSupport", false);
		group = getIntent().getIntExtra("group", 0);
		item = getIntent().getIntExtra("item", 0);

		if (P2PConnect.isPlaying()) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setContentView(R.layout.activity_firelink_alarm2);
		}else{
			setContentView(R.layout.activity_firelink_alarm);
		}
		excuteTimeOutTimer();
		initView();
		insertAlarmRecord();
		
		alarmInit();
		
	}
	
	private void initView(){
		monitor_fk_btn = (TextView) findViewById(R.id.monitor_fk_btn);
		alarm_fk_img = (ImageView) findViewById(R.id.alarm_fk_img);
		tv_fk_type = (TextView) findViewById(R.id.tv_fk_type);
		alarm_type_fk_text = (TextView) findViewById(R.id.alarm_type_fk_text);
		ignore_fk_btn = (TextView) findViewById(R.id.ignore_fk_btn);
		shield_fk_btn = (TextView) findViewById(R.id.shield_fk_btn);
		alarm_type_fk_time = (TextView) findViewById(R.id.alarm_type_fk_time);
		alarm_id_fk_text = (TextView) findViewById(R.id.alarm_id_fk_text);
	}

	public void insertAlarmRecord() {
		AlarmRecord alarmRecord = new AlarmRecord();
		long timeStr = System.currentTimeMillis();
		alarmRecord.alarmTime = String.valueOf(timeStr);
		Date nowTime = new Date(timeStr);
	    SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
	    retStrFormatNowDate = sdFormatter.format(nowTime);
		alarmRecord.deviceId = String.valueOf(alarm_id);
		alarmRecord.alarmType = alarm_type;
		alarmRecord.activeUser = NpcCommon.mThreeNum;
		if ((alarm_type == P2PValue.AlarmType.EXTERNAL_ALARM || alarm_type == P2PValue.AlarmType.LOW_VOL_ALARM)
				&& isSupport) {
			alarmRecord.group = group;
			alarmRecord.item = item;
		} else {
			alarmRecord.group = -1;
			alarmRecord.item = -1;
		}
		//DataManager.insertAlarmRecord(mContext, alarmRecord);
		String defenceId = NpcCommon.mThreeNum+alarmRecord.deviceId+"0"+group;
		areaName = DataManager.findDefence(mContext, defenceId);
		getDefenceAlarmType(alarmRecord.deviceId+"0"+group+"00"+item+"");
		
		Map<String,String> map = DataManager.findDefenceMap(mContext, defenceId+"00"+item+"");
		channelName = map.get("defenceName");
		cameraName = DataManager.findContactByContactId(mContext, alarmRecord.deviceId).contactName;
		//忽略报警
		ignore_fk_btn.setOnClickListener(this);
		//查看报警摄像头
		monitor_fk_btn.setOnClickListener(this);
		//停止报警
		shield_fk_btn.setOnClickListener(this);
	}

	private void alarmInit(){
		final AnimationDrawable anim = (AnimationDrawable) alarm_fk_img
				.getDrawable();
		OnPreDrawListener opdl = new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				anim.start();
				return true;
			}

		};
		alarm_fk_img.getViewTreeObserver().addOnPreDrawListener(opdl);
	}
	
	boolean viewed = false;
	Timer timeOutTimer;
	public static final int USER_HASNOTVIEWED = 3;

	private Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case USER_HASNOTVIEWED:
				finish();
				break;

			default:
				break;
			}
			return false;
		}
	});

	// 超时计时器
	public void excuteTimeOutTimer() {
		timeOutTimer = new Timer();
		TimerTask mTask = new TimerTask() {
			@Override
			public void run() {
				// 弹出一个对话框
				if (!viewed) {
					Message message = new Message();
					message.what = USER_HASNOTVIEWED;
					mHandler.sendMessage(message);
				}
			}
		};
		timeOutTimer.schedule(mTask, TIME_OUT);

	}

	//报警声音
	public void loadMusicAndVibrate() {
		isAlarm = true;
		int a_muteState = SharedPreferencesManager.getInstance().getAMuteState(
				MyApp.app);
		System.out.println("a_muteState="+a_muteState);
		if (a_muteState == 1) {
			MusicManger.getInstance().playAlarmMusic(getApplicationContext());
		}

		int a_vibrateState = SharedPreferencesManager.getInstance()
				.getAVibrateState(MyApp.app);
		if (a_vibrateState == 1) {
			new Thread() {
				public void run() {
					while (isAlarm) {
						MusicManger.getInstance().Vibrate();
						Utils.sleepThread(100);
					}
					MusicManger.getInstance().stopVibrate();
				}
			}.start();
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		SharedPreferencesManager.getInstance().putIgnoreAlarmTime(mContext,
				System.currentTimeMillis());
		isAlarm = false;
		P2PConnect.vEndAllarm();
		Log.e("alarmActivity", "---onStop");
	}

	@Override
	protected void onResume() {
		super.onResume();
		P2PConnect.setAlarm(true);
		loadMusicAndVibrate();
		Log.e("alarmActivity", "+++onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		MusicManger.getInstance().stop();
		Log.e("alarmActivity", "---onPause");
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.e("alarmActivity", "+++onStart");
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		P2PConnect.setAlarm(false);
		if (timeOutTimer != null) {
			timeOutTimer.cancel();
		}
		Log.e("alarmActivity", "---onDestroy");
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.e("alarmActivity", "+++onRestart");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ignore_fk_btn:
			finish();
			break;
		case R.id.monitor_fk_btn:
			Contact camera = DataManager.findContactByContactId(mContext,String.valueOf(alarm_id));
			Intent monitor = new Intent();
			monitor.setClass(mContext, CallActivity.class);
			monitor.putExtra("callId", camera.contactId);
			monitor.putExtra("contactName", camera.contactName);
			monitor.putExtra("password",camera.contactPassword);
			monitor.putExtra("isOutCall", true);
			monitor.putExtra("type",
					Constants.P2P_TYPE.P2P_TYPE_MONITOR);
			monitor.putExtra("contactType", camera.contactType);
			mContext.startActivity(monitor);
			finish();
			break;
		case R.id.shield_fk_btn:
			break;
		default:
			break;
		}
	}
	
	private void getDefenceAlarmType(String defenceID){
		RequestQueue mQueue = Volley.newRequestQueue(mContext);
		Map<String,String> map = new HashMap<String,String>();
		map.put("defenceID", defenceID);
		JsonArrayPostRequest mJsonRequest = new JsonArrayPostRequest(
				Constants.GET_ALARM_TYPE_URL, 
				new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray response) {
						try {
							if(response.length()>0){
								str = response.getString(0);
								if(str.equals("null")){
									str="移动侦测";
									tv_fk_type.setText(cameraName+"发出"+str+"报警消息");
									alarm_type_fk_text.setVisibility(View.GONE);
									alarm_id_fk_text.setVisibility(View.GONE);
									alarm_type_fk_time.setText(retStrFormatNowDate);
								}else{
									tv_fk_type.setText(cameraName+"发出"+str+"报警消息");
									alarm_type_fk_text.setText(areaName+"防区,"+channelName+"通道");
									alarm_type_fk_time.setText(retStrFormatNowDate);
								}
								
							}
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}, 
				new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						System.out.println("error="+error);
					}
				}, 
				map);
		mQueue.add(mJsonRequest);
	}
}
