package com.jwkj.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jpushdemo.ExampleUtil;
import com.test.jpushServer.R;
import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.data.Defence;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.NpcCommon;
import com.jwkj.thread.MainThread;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.volley.JsonArrayPostRequest;
import com.jwkj.widget.MyPassLinearLayout;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;

public class AddContactActivity extends BaseActivity implements OnClickListener {
	private TextView mNext;
	private ImageView mBack;
	Context mContext;
	EditText contactId;
	Contact mContact;
	Button ensure;
	NormalDialog dialog;
	EditText input_device_id, input_device_name, input_device_password;
	Contact saveContact = new Contact();
	private MyPassLinearLayout llPass;
	private String phoneUUID;
	String[] last_bind_data;
	int max_alarm_count;
	boolean isReceiveAlarm = true;
	String[] new_data;
	private List<Defence> listDefenceID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		mContact = (Contact) getIntent().getSerializableExtra("contact");
		mContext = this;
		phoneUUID = getMyUUID();
		initCompent();
		String path = Environment.getExternalStorageDirectory().getPath()
				+ "/DCIM/Camera";
		File dirFile = new File(path);
		if (dirFile.exists()) {
			Log.e("file", "------");
		}
		regFilter();
    	
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
				System.out.println("data="+data.length);
				int max_count = intent.getIntExtra("max_count", 0);
				last_bind_data = data;
				max_alarm_count = max_count;
				int count = 0;
				for (int i = 0; i < data.length; i++) {
					System.out.println("data[i]="+data[i]);
					if (data[i].equals(NpcCommon.mThreeNum)) {
						isReceiveAlarm = false;
						//已设置
						System.out.println("false");
						count = count + 1;
						return;
					}
				}
				if (count == 0) {
					//设置
					System.out.println("true");
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
					P2PHandler.getInstance().setBindAlarmId(saveContact.contactId,
							saveContact.contactPassword, new_data.length, new_data);
				}
			}
		}
	};
	
	public void initCompent() {
		// contactId = (EditText) findViewById(R.id.contactId);
		mBack = (ImageView) findViewById(R.id.back_btn);
		mNext = (TextView) findViewById(R.id.next);
		ensure = (Button) findViewById(R.id.bt_ensure);
		input_device_id = (EditText) findViewById(R.id.input_device_id);
		input_device_name = (EditText) findViewById(R.id.input_contact_name);
		input_device_password = (EditText) findViewById(R.id.input_contact_pwd);
		
		// if(null!=mContact){
		// contactId.setText(mContact.contactId);
		// }

		input_device_password
				.setTransformationMethod(PasswordTransformationMethod
						.getInstance());
		mBack.setOnClickListener(this);
		mNext.setOnClickListener(this);
		ensure.setOnClickListener(this);
		llPass = (MyPassLinearLayout) findViewById(R.id.ll_p);
		llPass.setEditextListener(input_device_password);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_btn:
			this.finish();
			break;
		case R.id.next:
			dialog = new NormalDialog(mContext);
			dialog.setTitle(mContext.getResources().getString(
					R.string.add_contacts));
			dialog.showLoadingDialog();
			dialog.setCanceledOnTouchOutside(false);
			next();
			break;
		case R.id.bt_ensure:
			dialog = new NormalDialog(mContext);
			dialog.setTitle(mContext.getResources().getString(
					R.string.add_contacts));
			dialog.showLoadingDialog();
			dialog.setCanceledOnTouchOutside(false);
			next();
			break;
		default:
			break;
		}
	}

	public void next() {
		String input_id = input_device_id.getText().toString();
		String input_name = input_device_name.getText().toString();
		String input_pwd = input_device_password.getText().toString();
		if (input_id != null && input_id.trim().equals("")) {
			T.showShort(mContext, R.string.input_contact_id);
			return;
		}

		if (null != FList.getInstance().isContact(input_id)) {
			T.showShort(mContext, R.string.contact_already_exist);
			return;
		}

		int type;
		if (input_id.charAt(0) == '0') {
			type = P2PValue.DeviceType.PHONE;
		} else {
			type = P2PValue.DeviceType.UNKNOWN;
		}
		if (input_name != null && input_name.trim().equals("")) {
			T.showShort(mContext, R.string.input_contact_name);
			return;
		}
		saveContact.contactId = input_id;
		saveContact.contactType = type;
		saveContact.activeUser = NpcCommon.mThreeNum;
		saveContact.messageCount = 0;

		List<Contact> lists = DataManager.findContactByActiveUser(mContext,
				NpcCommon.mThreeNum);
		for (Contact c : lists) {
			if (c.contactName.equals(input_name)) {
				T.showShort(mContext, R.string.device_name_exist);
				return;
			}
		}
		if (input_pwd == null || input_pwd.trim().equals("")) {
			T.showShort(this, R.string.input_password);
			return;
		}
		if (saveContact.contactType != P2PValue.DeviceType.PHONE) {
			if (input_pwd != null && !input_pwd.trim().equals("")) {
				if (input_pwd.length() > 10 || input_pwd.charAt(0) == '0') {
					T.showShort(mContext, R.string.device_password_invalid);
					return;
				}
			}
		}

		List<Contact> contactlist = DataManager.findContactByActiveUser(
				mContext, NpcCommon.mThreeNum);
		for (Contact contact : contactlist) {
			if (contact.contactId.equals(saveContact.contactId)) {
				T.showShort(mContext, R.string.contact_already_exist);
				return;
			}
		}
		saveContact.contactName = input_name;
		saveContact.contactPassword = input_pwd;
		String pwd=P2PHandler.getInstance().EntryPassword(input_pwd);
		P2PHandler.getInstance().getBindAlarmId(saveContact.contactId,pwd);
		//保存新增摄像头信息到服务器
		saveCamera();
	}
	
	private void saveCamera(){
		RequestQueue mQueue = Volley.newRequestQueue(mContext);
		String str =null;
		try {
			str = new String(saveContact.contactName.getBytes("gbk"),"iso-8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("cameraID", saveContact.contactId);
		map.put("userID", saveContact.activeUser);
		map.put("cameraPassword", saveContact.contactPassword);
		map.put("cameraName", str);
		map.put("cameraType", saveContact.contactType+"");
		map.put("messageCount", saveContact.messageCount+"");
		map.put("phoneUUID", saveContact.activeUser);

		JsonArrayPostRequest mJsonRequest = new JsonArrayPostRequest(
				Constants.ADD_CAMERA_URL, 
				new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray response) {
						System.out.println("response.length()="+response.length());
						if(response.length()>0){
							try {
								listDefenceID = new ArrayList<Defence>();
								for(int j = 0 ;j<response.length();j++){
										JSONObject obj = (JSONObject) response.get(j);
										Defence mDefence = new Defence();
										mDefence.mDefenceID = obj.getString("cameraDefenceID");
										mDefence.alarmType = obj.getString("deviceType");
										System.out.println("mDefence.mDefenceID="+mDefence.mDefenceID);
										System.out.println("mDefence.alarmType="+mDefence.alarmType);
										listDefenceID.add(mDefence);
								}
								
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						FList.getInstance().insert(saveContact);
						FList.getInstance().updateLocalDeviceWithLocalFriends();
						
						new MyTask().execute();
						sendSuccessBroadcast();
						finish();
					}
				}, 
				new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Toast.makeText(mContext, "添加摄像头失败，请重新添加", 1).show();
					}
				}, 
				map);
		mQueue.add(mJsonRequest);
	}
	
	class MyTask extends AsyncTask<String, Integer, Object>{

		@Override
		protected Object doInBackground(String... params) {
			String defenceID = saveContact.activeUser+saveContact.contactId;
			boolean ifExit = DataManager.ifExitDefence(mContext, defenceID);
			if(ifExit == false){
				DataManager.insertDefence(mContext, defenceID);
			}
			if(null!=listDefenceID&&listDefenceID.size()>0){
				DataManager.updateAlarmType(mContext, listDefenceID);
			}
			try {
				new Thread().sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (null != dialog) {
				dialog.dismiss();
				dialog = null;
			}
			return null;
		}
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_ADDCONTACTACTIVITY;
	}

	public void sendSuccessBroadcast() {
		Intent refreshContans = new Intent();
		refreshContans.setAction(Constants.Action.REFRESH_CONTANTS);
		refreshContans.putExtra("contact", saveContact);
		mContext.sendBroadcast(refreshContans);

		Intent createPwdSuccess = new Intent();
		createPwdSuccess.setAction(Constants.Action.UPDATE_DEVICE_FALG);
		createPwdSuccess.putExtra("threeNum", saveContact.contactId);
		mContext.sendBroadcast(createPwdSuccess);

		Intent add_success = new Intent();
		add_success.setAction(Constants.Action.ADD_CONTACT_SUCCESS);
		add_success.putExtra("contact", saveContact);
		mContext.sendBroadcast(add_success);

		Intent refreshNearlyTell = new Intent();
		refreshNearlyTell
				.setAction(Constants.Action.ACTION_REFRESH_NEARLY_TELL);
		mContext.sendBroadcast(refreshNearlyTell);
		T.showShort(mContext, R.string.add_success);
	}
	
	//生成mei
	private String getMyUUID(){
		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);   
		final String tmDevice;   
		tmDevice = "" + tm.getDeviceId();  
		System.out.println("tmDevice="+tmDevice);
		return tmDevice;
	}
}
