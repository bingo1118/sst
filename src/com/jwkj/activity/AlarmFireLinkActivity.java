package com.jwkj.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;

import com.jwkj.CallActivity;
import com.jwkj.P2PConnect;
import com.jwkj.data.AlarmRecord;
import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.data.SharedPreferencesManager;
import com.jwkj.global.Constants;
import com.jwkj.global.MyApp;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.MusicManger;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;
import com.test.jpushServer.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmFireLinkActivity extends Activity implements OnClickListener{
	Context mContext;
	private int TIME_OUT = 20 * 1000;
	boolean isRegFilter = false;
	LinearLayout layout_area_chanel;
	ImageView alarm_fk_img;
	boolean isAlarm;
	private TextView monitor_fk_btn;
	private TextView alarm_type_fk_text;
	private TextView tv_fk_type;
	private TextView ignore_fk_btn;
	private TextView shield_fk_btn;
	private TextView alarm_type_fk_time;
	private String cameraId;
	private AlarmRecord alarmRecord;
	private String cameraName;
	String[] last_bind_data;
	int max_alarm_count;
	boolean isReceiveAlarm = true;
	String[] new_data;
	private Contact camera;
	private String str;
	private LinearLayout alarm_weizhi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int mCurrentOri = getResources().getConfiguration().orientation;
		System.out.println("mCurrentOri="+mCurrentOri);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		mContext = this;
		
		System.out.println("P2PConnect.isPlaying()="+P2PConnect.isPlaying());
		if (P2PConnect.isPlaying()) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setContentView(R.layout.activity_firelink_alarm2);
		}else{
			setContentView(R.layout.activity_firelink_alarm);
		}
		
		String insertStr = getIntent().getStringExtra("insert");
		Bundle bundle = getIntent().getExtras();
		String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
        String[] contentStr = content.split(",");
        int areaID = Integer.parseInt(contentStr[0]);
        int channelID = Integer.parseInt(contentStr[1]);
        String timeStr = contentStr[2];
        
        String[] cameraIdStr = title.split("\\D");
        String[] alarmContent = title.split(cameraIdStr[0]);

        
        cameraId=cameraIdStr[0];

        SharedPreferences share = getApplicationContext().getSharedPreferences("account_shared",Context.MODE_PRIVATE);
        String name = share.getString("account_info_3c", null);
        alarmRecord = new AlarmRecord();
        alarmRecord.deviceId = cameraId;

        alarmRecord.activeUser = name;
        alarmRecord.alarmTime = System.currentTimeMillis()+"";
        alarmRecord.group = areaID;
        alarmRecord.item = channelID-1;

		initView();
		
		regFilter();
		String defenceID = name+cameraId+"0"+areaID+"";
		String areaName = DataManager.findDefence(mContext,defenceID);
		Map<String,String> map = DataManager.findDefenceMap(mContext, defenceID+"00"+channelID+"");
		String channelName = map.get("defenceName");
		cameraName = DataManager.findContactByContactId(mContext, cameraId).contactName;
		if("发出移动侦测报警消息".equals(alarmContent[1])){
			alarm_weizhi.setVisibility(View.GONE);
		}
		//设置报警信息
		tv_fk_type.setText(cameraName+alarmContent[1]);
		
		
		alarm_type_fk_text.setText(areaName+"防区,"+channelName+"通道");
		
		alarm_type_fk_time.setText(timeStr.replace("时间为：", ""));
		//忽略报警
		ignore_fk_btn.setOnClickListener(this);
		//查看报警摄像头
		monitor_fk_btn.setOnClickListener(this);
		//停止报警
		shield_fk_btn.setOnClickListener(this);
		camera = DataManager.findContactByContactId(mContext,cameraId);

		alarmInit();
		excuteTimeOutTimer();
		//P2PHandler.getInstance().getBindAlarmId(camera.contactId,camera.contactPassword);
	}
	
	public void regFilter(){
        IntentFilter filter=new IntentFilter();
        filter.addAction(Constants.P2P.RET_GET_BIND_ALARM_ID);
        mContext.registerReceiver(br, filter);
    }
	
	BroadcastReceiver br=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context arg0, Intent intent) {
			if(intent.getAction().equals(Constants.P2P.RET_GET_BIND_ALARM_ID)){
				String[] data = intent.getStringArrayExtra("data");
				int max_count = intent.getIntExtra("max_count", 0);
				last_bind_data = data;
				max_alarm_count = max_count;
				int count = 0;
				for (int i = 0; i < data.length; i++) {
					if (data[i].equals(NpcCommon.mThreeNum)) {
						isReceiveAlarm = false;
						//已设置
						count = count + 1;
						return;
					}
				}
				if (count == 0) {
					//设置
					isReceiveAlarm = true;
					if (last_bind_data.length >= max_alarm_count) {
						T.showShort(mContext, R.string.alarm_push_limit);
						return;
					}
					new_data = new String[last_bind_data.length + 1];
					for (int i = 0; i < last_bind_data.length; i++) {
						new_data[i] = last_bind_data[i];
					}
					new_data[new_data.length - 1] = NpcCommon.mThreeNum;
					// last_bind_data=new_data;
					P2PHandler.getInstance().setBindAlarmId(camera.contactId,
							camera.contactPassword, new_data.length, new_data);
				}
			}
		}
	};
	
	private void initView(){
		monitor_fk_btn = (TextView) findViewById(R.id.monitor_fk_btn);
		alarm_fk_img = (ImageView) findViewById(R.id.alarm_fk_img);
		tv_fk_type = (TextView) findViewById(R.id.tv_fk_type);
		alarm_type_fk_text = (TextView) findViewById(R.id.alarm_type_fk_text);
		ignore_fk_btn = (TextView) findViewById(R.id.ignore_fk_btn);
		shield_fk_btn = (TextView) findViewById(R.id.shield_fk_btn);
		alarm_type_fk_time = (TextView) findViewById(R.id.alarm_type_fk_time);
		alarm_weizhi = (LinearLayout) findViewById(R.id.alarm_weizhi);
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
			if(null==camera.contactId||camera.contactId.length()<=0){
				Toast.makeText(mContext, "查看的摄像头不存在", 1).show();
			}else{
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
			}
			
			break;
		case R.id.shield_fk_btn:
			break;
		default:
			break;
		}
		
	}
}
