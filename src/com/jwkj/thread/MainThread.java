package com.jwkj.thread;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Message;
import android.provider.Contacts;
import android.util.Log;
import android.util.Xml;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.data.Defence;
import com.jwkj.data.SharedPreferencesManager;
import com.jwkj.entity.UpdateInfo;
import com.jwkj.global.Constants;
import com.jwkj.global.Constants.Action;
import com.jwkj.global.AccountPersist;
import com.jwkj.global.FList;
import com.jwkj.global.MyApp;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.Utils;
import com.jwkj.volley.JsonArrayPostRequest;
import com.p2p.core.P2PHandler;
import com.p2p.core.update.UpdateManager;

public class MainThread {
	static MainThread manager;
	boolean isRun;
	private String version;
	private int serVersion;
	private static final long SYSTEM_MSG_INTERVAL = 60 * 60 * 1000;
	long lastSysmsgTime;
	private Main main;
	Context context;
	private String userID,cameraID,defenceID;
	private static boolean isOpenThread;
	private List<String> listDefence;

	public MainThread(Context context) {
		manager = this;
		this.context = context;
	}

	public static MainThread getInstance() {
		return manager;
	}

	class Main extends Thread {
		@Override
		public void run() {
			isRun = true;
			Utils.sleepThread(3000);
			
			//userID = AccountPersist.getInstance()
					//.getActiveAccountInfo(context).three_number;
			while (isRun) {
				if (isOpenThread == true) {
					checkUpdate();
					Log.e("my", "updateOnlineState");
					try {
						FList.getInstance().updateOnlineState();
						FList.getInstance().searchLocalDevice();
						//getDefenceInfo();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Utils.sleepThread(50 * 1000);
				}else{
					Utils.sleepThread(10 * 1000);
				}
			}
		}
	};
	
	public void getDefenceInfo(){
		List<Contact> list = DataManager.findContactByActiveUser(context, userID);
		if(list.size()>0){
			listDefence = new ArrayList<String>();
			for(int i = 0;i<list.size();i++){
				Contact mContact = list.get(i);
				regFilter();
				cameraID = mContact.contactId;
				P2PHandler.getInstance().getDefenceArea(mContact.contactId,mContact.contactPassword);
				Utils.sleepThread(1000);
			}
			Utils.sleepThread(3 * 1000);
			System.out.println("listDefence.size="+listDefence.size());
			List<String> listStr = DataManager.findDefenceAlarmType(context,userID);
			List<String> li = new ArrayList<String>();
			if(listDefence.size()>0){
				getDefenceAlarm(listDefence);
			}
			for(String s: listStr){
				for(String ss : listDefence){
					if(!s.equals(ss)){
						li.add(s);
					}
				}
			}
//			if(li.size()>0){
//				DataManager.updateListDefenceAlarmType(context,li,"");
//			}
		}
	}
	
	private void getDefenceAlarm(final List<String> list){
		RequestQueue mQueue = Volley.newRequestQueue(context);
		Map<String,String> map = new HashMap<String,String>();
		map.put("defenceIDSize", list.size()+"");
		for(int i=0;i<list.size();i++){
			map.put("defenceID["+i+"]", list.get(i));
		}
		JsonArrayPostRequest mJsonRequest = new JsonArrayPostRequest(
				Constants.FIND_DEFENCE_ALARMTYPE_URL, 
				new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray response) {
						List<Defence> listDefenceID = new ArrayList<Defence>();
						for(int j = 0 ;j<response.length();j++){
							try {
								JSONObject obj = (JSONObject) response.get(j);
								Defence mDefence = new Defence();
								mDefence.mDefenceID = obj.getString("mDefenceID");
								mDefence.alarmType = obj.getString("alarmType");
								System.out.println("mDefenceID="+obj.getString("mDefenceID"));
								System.out.println("alarmType="+obj.getString("alarmType"));
								listDefenceID.add(mDefence);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						DataManager.updateAlarmType(context, listDefenceID);
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

	public void regFilter(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.P2P.RET_GET_DEFENCE_AREA);
		context.registerReceiver(mReceiver, filter);
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context mContext, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(Constants.P2P.RET_GET_DEFENCE_AREA)){
				ArrayList<int[]> data = (ArrayList<int[]>) intent.getSerializableExtra("data");
				initData(data);
			}
		}
		
	};
	
	public void initData(ArrayList<int[]> data){
		System.out.println("data==="+data.size());
		for(int i=0;i<data.size();i++){
			int[] status = data.get(i);
			for(int j=0;j<status.length;j++){
				
				if(status[j]==1){

				}else{
					defenceID = cameraID+"0"+i+"00"+j+"";
					boolean result = DataManager.findExit(context, userID+defenceID);
					if(false==result){
						listDefence.add(defenceID);
					}
				}
			}
		}
	}

	public void go() {

		if (null == main || !main.isAlive()) {
			main = new Main();
			main.start();
		}
	}

	public void kill() {

		isRun = false;
		main = null;
	}

	
	public static void setOpenThread(boolean isOpenThread) {
		MainThread.isOpenThread = isOpenThread;
	}

	/*public void checkUpdate() {
		try {
			long last_check_update_time = SharedPreferencesManager
					.getInstance().getLastAutoCheckUpdateTime(MyApp.app);
			long now_time = System.currentTimeMillis();

			if ((now_time - last_check_update_time) > 1000 * 60 * 60 * 12) {
				SharedPreferencesManager.getInstance()
						.putLastAutoCheckUpdateTime(now_time, MyApp.app);
				Log.e("my", "后台检查更新");
				if (UpdateManager.getInstance().checkUpdate()) {
					String data = "";
					if (Utils.isZh(MyApp.app)) {
						data = UpdateManager.getInstance()
								.getUpdateDescription();
					} else {
						data = UpdateManager.getInstance()
								.getUpdateDescription_en();
					}
					Intent i = new Intent(Constants.Action.ACTION_UPDATE);
					i.putExtra("updateDescription", data);
					MyApp.app.sendBroadcast(i);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("my", "后台检查更新失败");
		}
	}*/
	private UpdateInfo mUpdateInfo = new UpdateInfo();
	public void checkUpdate() {
		try {
			long last_check_update_time = SharedPreferencesManager
					.getInstance().getLastAutoCheckUpdateTime(MyApp.app);
			long now_time = System.currentTimeMillis();

			if ((now_time - last_check_update_time) > 1000 * 60 * 60 * 12) {
				SharedPreferencesManager.getInstance()
						.putLastAutoCheckUpdateTime(now_time, MyApp.app);
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
						Intent i = new Intent(Constants.Action.ACTION_UPDATE);
						i.putExtra("url", mUpdateInfo.url);
						i.putExtra("message", mUpdateInfo.message);
						MyApp.app.sendBroadcast(i);
					} 
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public int getlocalVersion(){
 		int localversion = 0;
 		try {
 			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
 			localversion = info.versionCode;
 		} catch (NameNotFoundException e) {
 			e.printStackTrace();
 		}
 		return localversion;
 	}


}
