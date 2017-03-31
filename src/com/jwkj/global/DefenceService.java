package com.jwkj.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.jwkj.data.DataManager;
import com.jwkj.volley.JsonArrayPostRequest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DefenceService extends Service{
	private String listStr=null;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		listStr = intent.getExtras().getString("userID");
		getDefence(listStr);
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
	}
	
	private void getDefence(String defenceID){
		RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
		Map<String,String> map = new HashMap<String,String>();
		map.put("userID", defenceID);
		JsonArrayPostRequest mJsonRequest = new JsonArrayPostRequest(
				Constants.DEFENCE_FINDALL_URL, 
				new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray response) {
						try {
							for(int i = 0 ;i<response.length();i++){
								JSONObject obj = (JSONObject) response.get(i);
								String defenceID = obj.getString("mDefenceID");
								String defenceName = obj.getString("mDefenceName");
								String defenceAlarmType = obj.getString("alarmType");
								DataManager.updateDefenceFromServer(MyApp.app, defenceID, defenceAlarmType, defenceName);
							}
							stopSelf();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							stopSelf();
						}
					}
				}, 
				new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						System.out.println("error="+error);
						stopSelf();
					}
				}, 
				map);
		mQueue.add(mJsonRequest);
	}
}
