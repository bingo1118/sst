package com.jwkj.activity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.test.jpushServer.R;
import com.jwkj.data.Contact;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.ImageUtils;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.HeaderView;
import com.jwkj.widget.MyPassLinearLayout;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;

public class ModifyContactActivity extends BaseActivity implements OnClickListener{
	private static final int RESULT_GETIMG_FROM_CAMERA = 0x11;
	private static final int RESULT_GETIMG_FROM_GALLERY = 0x12;
	private static final int RESULT_CUT_IMAGE= 0x13;
	private TextView mSave;
	private ImageView mBack;
	HeaderView header_img;
	Context mContext;
	EditText contactName,contactPwd;
	LinearLayout layout_device_pwd;
	TextView contactId;
	Contact mModifyContact;
	NormalDialog dialog;
	RelativeLayout modify_header;
	private Bitmap tempHead;
	private MyPassLinearLayout llPass;
	private RequestQueue mQueue;
	private String input_name;
	private String input_pwd;
	String[] last_bind_data;
	int max_alarm_count;
	boolean isReceiveAlarm = true;
	String[] new_data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_contact);
		mModifyContact = (Contact) getIntent().getSerializableExtra("contact");
		mContext = this;
		initCompent();
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
					P2PHandler.getInstance().setBindAlarmId(mModifyContact.contactId,
							mModifyContact.contactPassword, new_data.length, new_data);
				}
			}
		}
	};

	public void initCompent() {
		contactId = (TextView) findViewById(R.id.contactId);
		contactName = (EditText) findViewById(R.id.contactName);
		contactPwd = (EditText) findViewById(R.id.contactPwd);
		contactPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
		layout_device_pwd = (LinearLayout) findViewById(R.id.layout_device_pwd);
		header_img = (HeaderView) findViewById(R.id.header_img);
		
		header_img.updateImage(mModifyContact.contactId,false);
		
		mBack=(ImageView)findViewById(R.id.back_btn);
		mSave=(TextView)findViewById(R.id.save);
		modify_header = (RelativeLayout) findViewById(R.id.modify_header);
		if(mModifyContact.contactType!=P2PValue.DeviceType.PHONE){
			layout_device_pwd.setVisibility(RelativeLayout.VISIBLE);
		}else{
			layout_device_pwd.setVisibility(RelativeLayout.GONE);
		}
		llPass=(MyPassLinearLayout) findViewById(R.id.ll_p);
		llPass.setEditextListener(contactPwd);
		if(mModifyContact!=null){
			contactId.setText(mModifyContact.contactId);
			contactName.setText(mModifyContact.contactName);
			contactPwd.setText(mModifyContact.contactPassword);
		}
		
		modify_header.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mSave.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==RESULT_GETIMG_FROM_CAMERA){
			try{
				Bundle extras = data.getExtras();
				tempHead = (Bitmap) extras.get("data");
				Log.e("my",tempHead.getWidth()+":"+tempHead.getHeight());
				ImageUtils.saveImg(tempHead,Constants.Image.USER_HEADER_PATH, Constants.Image.USER_HEADER_TEMP_FILE_NAME);
				
				Intent cutImage = new Intent(mContext,CutImageActivity.class);
				cutImage.putExtra("contact", mModifyContact);
				startActivityForResult(cutImage,RESULT_CUT_IMAGE);
			}catch(NullPointerException e){
				Log.e("my","用户终止..");
			}
		}else if(requestCode==RESULT_GETIMG_FROM_GALLERY){
			
			try {
				Uri uri = data.getData();
				tempHead = ImageUtils.getBitmap(ImageUtils.getAbsPath(mContext, uri), Constants.USER_HEADER_WIDTH_HEIGHT, Constants.USER_HEADER_WIDTH_HEIGHT);
				ImageUtils.saveImg(tempHead,Constants.Image.USER_HEADER_PATH, Constants.Image.USER_HEADER_TEMP_FILE_NAME);
				
				Intent cutImage = new Intent(mContext,CutImageActivity.class);
				cutImage.putExtra("contact", mModifyContact);
				startActivityForResult(cutImage,RESULT_CUT_IMAGE);
				
			} catch(NullPointerException e){
				Log.e("my","用户终止..");
			}
		}else if(requestCode==RESULT_CUT_IMAGE){
			Log.e("my",resultCode+"");
			try{
				if(resultCode==1){
					header_img.updateImage(mModifyContact.contactId,false);
					Intent refreshContans = new Intent();
					refreshContans.setAction(Constants.Action.REFRESH_CONTANTS);
					refreshContans.putExtra("contact", mModifyContact);
					mContext.sendBroadcast(refreshContans);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void destroyTempHead(){
		if(tempHead!=null&&!tempHead.isRecycled()){
			tempHead.recycle();
			tempHead = null;
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_btn:
			this.finish();
			break;
		case R.id.save:
			save();
			break;
		case R.id.modify_header:
//			dialog = new NormalDialog(mContext);
//			dialog.setTitle(R.string.operator);
//			String[] data = new String[]{
//					mContext.getResources().getString(R.string.gallery),
//					mContext.getResources().getString(R.string.camara)
//					};
//			dialog.setListData(data);
//			dialog.setOnItemClickListener(new OnItemClickListener(){
//
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1,
//						int position, long arg3) {
//					// TODO Auto-generated method stub
//					switch(position){
//					case 0:
//						if(null!=dialog){
//							dialog.dismiss();
//							dialog = null;
//						}
//						Intent gallery = new Intent();
//						gallery.setAction(Intent.ACTION_GET_CONTENT);
//						gallery.setType("image/*");
//						startActivityForResult(gallery,RESULT_GETIMG_FROM_GALLERY);
//						
//						break;
//					case 1:
//						if(null!=dialog){
//							dialog.dismiss();
//							dialog = null;
//						}
//						Intent camera = new Intent();
//						camera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//						startActivityForResult(camera,RESULT_GETIMG_FROM_CAMERA);
//						break;
//					}
//				}
//				
//			});
//			dialog.showSelectorDialog();
			break;
		default:
			break;
		}
	}	
	
	void save(){
		input_name = contactName.getText().toString();
		input_pwd = contactPwd.getText().toString();
		
		if(input_name!=null&&input_name.trim().equals("")){
			T.showShort(mContext,  R.string.input_contact_name);
			return;
		}
		
		if(mModifyContact.contactType!=P2PValue.DeviceType.PHONE){
			if(input_pwd!=null&&input_pwd.trim().equals("")){
				T.showShort(mContext,  R.string.input_contact_pwd);
				return;
			}
			if(!Utils.isNumeric(input_pwd)||input_pwd.charAt(0)=='0'||input_pwd.length()>10){
				T.showShort(mContext,  R.string.device_password_invalid);
				return;
			}
		}
		mQueue = Volley.newRequestQueue(mContext); 
		StringRequest stringRequest = new StringRequest(Method.POST, Constants.UPDATE_CAMERA_INFO_URL,  
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						mModifyContact.contactName = input_name;
						mModifyContact.contactPassword = input_pwd;
						FList.getInstance().update(mModifyContact);
						Intent refreshContans = new Intent();
						refreshContans.setAction(Constants.Action.REFRESH_CONTANTS);
						refreshContans.putExtra("contact", mModifyContact);
						mContext.sendBroadcast(refreshContans);
						P2PHandler.getInstance().getBindAlarmId(mModifyContact.contactId,mModifyContact.contactPassword);
						T.showShort(mContext, R.string.modify_success);
						finish();
					}
		} , 
		new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(mContext, "修改失败，请重新修改", 1).show();
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				String str =null;
				try {
					str = new String(input_name.getBytes("gbk"),"iso-8859-1");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("mUserID", mModifyContact.activeUser);
				map.put("mCameraID", mModifyContact.contactId);
				map.put("mCameraName", str);
				map.put("mCameraPassword", input_pwd);
				return map;
			}
		};
		mQueue.add(stringRequest);
	}

	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		destroyTempHead();
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_MODIFYCONTACTACTIVITY;
	}
}
