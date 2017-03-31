package com.jwkj.fragment;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.p2p.core.utils.MyUtils;
import com.test.jpushServer.R;
import com.jwkj.activity.AccountInfoActivity;
import com.jwkj.activity.AlarmSetActivity;
import com.jwkj.activity.ImageBrowser;
import com.jwkj.activity.MainActivity;
import com.jwkj.activity.QRcodeActivity;
import com.jwkj.activity.SettingSystemActivity;
import com.jwkj.activity.SysMsgActivity;
import com.jwkj.data.DataManager;
import com.jwkj.data.SysMessage;
import com.jwkj.entity.UpdateInfo;
import com.jwkj.global.Constants;
import com.jwkj.global.NpcCommon;
import com.jwkj.global.UpdateService;
import com.jwkj.thread.MainThread;
import com.jwkj.thread.UpdateCheckVersionThread;
import com.jwkj.widget.NormalDialog;

public class SettingFrag extends BaseFragment implements OnClickListener{
	private Context mContext;
	private RelativeLayout mCheckUpdateTextView = null;
	private RelativeLayout mLogOut,mExit,center_about,account_set,sys_set,sys_msg,qr_code_set_wifi,alarm_set;
	private TextView mName;
	boolean isRegFilter = false;
	//update add 
	private Handler handler;
	private boolean isCancelCheck = false;
	private NormalDialog dialog;
	ImageView sysMsg_notify_img,network_type;
	//end
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_setting, container, false);
		Log.e("my","createSettingFrag");
		mContext = MainActivity.mContext;
		initComponent(view);
		regFilter();
		updateSysMsg();
		return view;
	}
	

	public void initComponent(View view) {
		mCheckUpdateTextView = (RelativeLayout)view.findViewById(R.id.center_t);
		mName=(TextView)view.findViewById(R.id.mailAdr);
		if(NpcCommon.mThreeNum.equals("517400")){
			mName.setText(R.string.no_name);	
		}else{
			mName.setText(String.valueOf(NpcCommon.mThreeNum));				
		}
		mLogOut=(RelativeLayout)view.findViewById(R.id.logout_layout);
		account_set=(RelativeLayout)view.findViewById(R.id.account_set); 
		sys_set=(RelativeLayout)view.findViewById(R.id.system_set);
		mExit=(RelativeLayout)view.findViewById(R.id.exit_layout);
		center_about=(RelativeLayout) view.findViewById(R.id.center_about);
		sys_msg = (RelativeLayout) view.findViewById(R.id.system_message);
		qr_code_set_wifi=(RelativeLayout)view.findViewById(R.id.qr_code_layout);
		alarm_set=(RelativeLayout)view.findViewById(R.id.alarm_set_btn);
		
		sysMsg_notify_img = (ImageView) view.findViewById(R.id.sysMsg_notify_img);
		
		network_type = (ImageView) view.findViewById(R.id.network_type);
		
//		if(NpcCommon.mNetWorkType == NpcCommon.NETWORK_TYPE.NETWORK_WIFI){
//			network_type.setImageResource(R.drawable.wifi);
//		}else{
//			network_type.setImageResource(R.drawable.wifi);
//		}
		
		mLogOut.setOnClickListener(this);
		account_set.setOnClickListener(this);
		sys_msg.setOnClickListener(this);
		mExit.setOnClickListener(this);
		sys_set.setOnClickListener(this);
		center_about.setOnClickListener(this);
		mCheckUpdateTextView.setOnClickListener(this);
		qr_code_set_wifi.setOnClickListener(this);
		alarm_set.setOnClickListener(this);
		handler = new MyHandler();
		if(NpcCommon.mThreeNum.equals("517400")){
			account_set.setVisibility(RelativeLayout.GONE);
			sys_set.setBackgroundResource(R.drawable.tiao_bg_up);
		}
	}
	
	public void regFilter(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action.RECEIVE_SYS_MSG);
		filter.addAction(Constants.Action.NET_WORK_TYPE_CHANGE);
		mContext.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Constants.Action.RECEIVE_SYS_MSG)) {
				updateSysMsg();
			}else if(intent.getAction().equals(Constants.Action.NET_WORK_TYPE_CHANGE)){
//				if(NpcCommon.mNetWorkType == NpcCommon.NETWORK_TYPE.NETWORK_WIFI){
//					network_type.setImageResource(R.drawable.wifi);
//				}else{
//					network_type.setImageResource(R.drawable.wifi);
//				}
			} 
		}
	};
	@Override
	public void onResume() {  
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.logout_layout:
			Intent canel=new Intent();
			canel.setAction(Constants.Action.ACTION_SWITCH_USER);
			mContext.sendBroadcast(canel);
			//JPushInterface.stopPush(mContext);
			break;
			
		case R.id.exit_layout:
			Intent exit=new Intent();
			exit.setAction(Constants.Action.ACTION_EXIT);
			mContext.sendBroadcast(exit);
			break;
		case R.id.center_t:
			executeUpdateProcess();
			break;
		case R.id.center_about:
			//Intent i = new Intent(mContext,AboutActivity.class);
			//mContext.startActivity(i);
			
			NormalDialog about = new NormalDialog(mContext);
			about.showAboutDialog();
			break;
		case R.id.account_set:
			Intent goAccount_set = new Intent();
			goAccount_set.setClass(mContext, AccountInfoActivity.class);
			startActivity(goAccount_set);
			break;
		case R.id.system_set:
			Intent goSys_set = new Intent();
			goSys_set.setClass(mContext, SettingSystemActivity.class);
			startActivity(goSys_set);
			break;
		case R.id.system_message:
			Intent goSysMsg = new Intent(mContext,SysMsgActivity.class);
			startActivity(goSysMsg);
			break;
		case R.id.qr_code_layout:
			Intent go_qr = new Intent(mContext,QRcodeActivity.class);
			mContext.startActivity(go_qr);
		    break;
		case R.id.alarm_set_btn:
			Intent go_alarm_set = new Intent(mContext,AlarmSetActivity.class);
			startActivity(go_alarm_set);
			break;
		default:
			break;
		}
	}
	
	
	class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case Constants.Update.CHECK_UPDATE_HANDLE_FALSE:
				Log.e("my","diss");
				if(null!=dialog){
					Log.e("my","diss ok");
					dialog.dismiss();
					dialog = null;
				}
				if(isCancelCheck){
					return;
				}
				
				dialog = new NormalDialog(
						mContext,
						mContext.getResources().getString(R.string.update_prompt_title),
						mContext.getResources().getString(R.string.update_check_false),
						"",
						""
						);
				dialog.setStyle(NormalDialog.DIALOG_STYLE_PROMPT);
				dialog.showDialog();
				break;
			case Constants.Update.CHECK_UPDATE_HANDLE_TRUE:
				if(null!=dialog){
					dialog.dismiss();
					dialog = null;
				}
				if(isCancelCheck){
					return;
				}
				Intent i = new Intent(Constants.Action.ACTION_UPDATE);
				i.putExtra("updateDescription", (String)msg.obj);
				mContext.sendBroadcast(i);
				break;
			}
		}
	}
	
	public void updateSysMsg(){
		List<SysMessage> sysmessages = DataManager.findSysMessageByActiveUser(mContext, NpcCommon.mThreeNum);
		boolean isNewSysMsg = false;
		for(SysMessage msg : sysmessages){
			if(msg.msgState==SysMessage.MESSAGE_STATE_NO_READ){
				isNewSysMsg = true;
			}
		}
		
		if(isNewSysMsg){
			sysMsg_notify_img.setVisibility(RelativeLayout.VISIBLE);
		}else{
			sysMsg_notify_img.setVisibility(RelativeLayout.GONE);
		}
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(isRegFilter){
			isRegFilter = false;
			mContext.unregisterReceiver(mReceiver);
		}
	}
	
   private UpdateInfo mUpdateInfo = new UpdateInfo();
   public void executeUpdateProcess() {
		new Thread() {
			public void run() {
				try {
					Looper.prepare();
					
					// 创建地址对象
					URL url = new URL(Constants.UPDATE_URL);
					// 打开链接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					// 参数
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					if (conn.getResponseCode() == 200) {
						InputStream input = conn.getInputStream();
						// 解析 xml pull
						//创建解PullParser析器
						XmlPullParser parser = Xml.newPullParser();
						//设置数据
						parser.setInput(input, "GBK");
						int eventType = XmlPullParser.START_DOCUMENT;
						// 逐行
						while (eventType != XmlPullParser.END_DOCUMENT) {
							switch (eventType) {
							case XmlPullParser.START_TAG:// 标签开始
								if ("versionCode".equals(parser.getName()//获取标签名
										)) {
									mUpdateInfo.versionCode = parser.nextText();//获取文本值
								} else if ("versionName".equals(parser.getName())) {
									mUpdateInfo.versionName = parser.nextText();
								} else if ("message".equals(parser.getName())) {
									mUpdateInfo.message = parser.nextText();
								} else if ("url".equals(parser.getName())) {
									mUpdateInfo.url = parser.nextText();
								}
								break;
							}
							// 执行下一行
							eventType = parser.next();
						}
						// 更新
						int serverCode = Integer.parseInt(mUpdateInfo.versionCode);
						if (serverCode > getlocalVersion()) {
							Message message = hanlder.obtainMessage();//建议使用
							message.what = 1;// 打开对话框
							hanlder.sendMessage(message);
						} else {
							Message message = hanlder.obtainMessage();//建议使用
							message.what = 2;// 打开对话框
							hanlder.sendMessage(message);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					Message message = new Message();
					message.what = -1;//
					hanlder.sendMessage(message);
					System.out.println(e.getMessage());
				}

			};
		}.start();
	  }
    
 // 更新界面
 	  private Handler hanlder = new Handler() {
 		// 回调函数:条件:sendMessage
 		public void handleMessage(android.os.Message msg) {
 			switch (msg.what) {
 			case 1:// 打话对话框
 				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
 				builder.setTitle("检查到新版本");
	 				builder.setMessage(mUpdateInfo.message);
	 				builder.setNegativeButton("下次再说", null);
	 				builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(mContext, UpdateService.class);
							intent.putExtra("url", mUpdateInfo.url);
							System.out.println("mUpdateInfo.url="+mUpdateInfo.url);
							mContext.startService(intent);
						}
					});
					builder.create().show();
 				
 				break;
 			case -1:// 异常
 				// View.inflate(getBaseContext(), resource, root)
 				Toast.makeText(mContext, "更新失败", Toast.LENGTH_LONG).show();
 				break;
 			case 2:// 更新进度
 				AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
 				alertBuilder.setTitle("无新版本");
 				alertBuilder.setMessage("已是最新版本");
 				alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
 				alertBuilder.create().show();
 				break;
 			}

 		};
 	  };
 	  
 	 public int getlocalVersion(){
 		int localversion = 0;
 		try {
 			PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
 			localversion = info.versionCode;
 		} catch (NameNotFoundException e) {
 			e.printStackTrace();
 		}
 		return localversion;
 	}
}
