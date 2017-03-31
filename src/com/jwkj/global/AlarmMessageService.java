package com.jwkj.global;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.jwkj.data.AlarmRecord;
import com.jwkj.data.DataManager;
import com.jwkj.volley.JsonArrayPostRequest;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

public class AlarmMessageService extends Service{
	private JsonArrayPostRequest mJsonArrayPostRequest;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
		String userID = intent.getExtras().getString("userID");
		if(null!=userID&&userID.length()>0){
			new MyTask().execute(userID);
		}else{
			stopSelf();
		}
	}
	
	class MyTask extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... arg0) {
			String str = arg0[0];
			getAlarmMessageInfo(str);
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			stopSelf();
		}
		
	}
	
	private void getAlarmMessageInfo(final String userID){
		RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
		Map<String,String> map = new HashMap<String,String>();
		map.put("userID", userID);
		mJsonArrayPostRequest = new JsonArrayPostRequest(
				Constants.FINDALARMMESSAGE_BY_TIME,
				new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray response) {
						try {
							for(int i = 0 ;i<response.length();i++){
								JSONObject obj = (JSONObject) response.get(i);
								AlarmRecord mAlarmRecord = new AlarmRecord();
								mAlarmRecord.deviceId = obj.getString("deviceID");
								DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
								long alarmTime = df.parse(obj.getString("recordTime")).getTime();
								mAlarmRecord.alarmTime = alarmTime+"";
								mAlarmRecord.activeUser = userID;
								mAlarmRecord.alarmType = Integer.parseInt(obj.getString("alarmType"));
								mAlarmRecord.group = Integer.parseInt(obj.getString("alarmArea"));
								int alarmChannel = Integer.parseInt(obj.getString("alarmChannel"));
								if(alarmChannel==0){
									mAlarmRecord.item = alarmChannel;
								}else{
									mAlarmRecord.item = alarmChannel-1;
								}
								String alarmInfo = null;
								DataManager.insertAlarmRecord(getApplicationContext(), mAlarmRecord,alarmInfo);
							}
							
						} catch (Exception e) {
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
		mQueue.add(mJsonArrayPostRequest);
	}

}
