package com.jwkj.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.MyPassLinearLayout;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.P2PHandler;

public class ModifyNpcPasswordActivity extends BaseActivity implements
		OnClickListener {
	Context mContext;
	Contact mContact;
	ImageView mBack;
	Button mSave;
	EditText old_pwd, new_pwd, re_new_pwd;
	NormalDialog dialog;
	private boolean isRegFilter = false;
	String password_old, password_new, password_re_new;
	private MyPassLinearLayout llPass;
	private RequestQueue mQueue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_npc_pwd);

		mContact = (Contact) getIntent().getSerializableExtra("contact");
		mContext = this;
		initCompent();
		regFilter();
	}

	public void initCompent() {
		mBack = (ImageView) findViewById(R.id.back_btn);
		mSave = (Button) findViewById(R.id.save);
		old_pwd = (EditText) findViewById(R.id.old_pwd);
		new_pwd = (EditText) findViewById(R.id.new_pwd);
		re_new_pwd = (EditText) findViewById(R.id.re_new_pwd);

		old_pwd.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		new_pwd.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		re_new_pwd.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		llPass = (MyPassLinearLayout) findViewById(R.id.ll_p);
		llPass.setEditextListener(new_pwd);
		mBack.setOnClickListener(this);
		mSave.setOnClickListener(this);
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.P2P.ACK_RET_SET_DEVICE_PASSWORD);
		filter.addAction(Constants.P2P.RET_SET_DEVICE_PASSWORD);
		filter.addAction(Constants.P2P.RET_DEVICE_NOT_SUPPORT);
		mContext.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals(Constants.P2P.RET_SET_DEVICE_PASSWORD)) {
				int result = intent.getIntExtra("result", -1);
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				if (result == Constants.P2P_SET.DEVICE_PASSWORD_SET.SETTING_SUCCESS) {
					mContact.contactPassword = password_new;
					FList.getInstance().update(mContact);
					Intent refreshContans = new Intent();
					refreshContans.setAction(Constants.Action.REFRESH_CONTANTS);
					refreshContans.putExtra("contact", mContact);
					mContext.sendBroadcast(refreshContans);

					T.showShort(mContext, R.string.modify_success);
					finish();
				} else {
					T.showShort(mContext, R.string.operator_error);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_SET_DEVICE_PASSWORD)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					if (dialog != null) {
						dialog.dismiss();
						dialog = null;
					}
					T.showShort(mContext, R.string.old_pwd_error);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					T.showShort(mContext, R.string.net_error_operator_fault);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.RET_DEVICE_NOT_SUPPORT)) {
				finish();
				T.showShort(mContext, R.string.not_support);
			}
		}
	};

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.back_btn:
			finish();
			break;
		case R.id.save:
			password_old = old_pwd.getText().toString();
			password_new = new_pwd.getText().toString();
			password_re_new = re_new_pwd.getText().toString();
			if ("".equals(password_old.trim())) {
				T.showShort(mContext, R.string.input_old_device_pwd);
				return;
			}

			if (password_old.length() >10) {
				T.showShort(mContext, R.string.old_pwd_too_long);
				return;
			}

			if ("".equals(password_new.trim())) {
				T.showShort(mContext, R.string.input_new_device_pwd);
				return;
			}

			if (password_new.length() >10) {
				T.showShort(mContext, R.string.new_pwd_too_long);
				return;
			}

			/*if (!Utils.isNumeric(password_new) || password_new.charAt(0) == '0') {
				T.showShort(mContext, R.string.device_password_invalid);
				return;
			}*/

			if ("".equals(password_re_new.trim())) {
				T.showShort(mContext, R.string.input_re_new_device_pwd);
				return;
			}

			if (!password_re_new.equals(password_new)) {
				T.showShort(mContext, R.string.pwd_inconsistence);
				return;
			}
			if (llPass.isWeakpassword()) {
				if (password_new.length() < 6) {
					T.showShort(mContext, R.string.simple_password);
				} else {
					T.showShort(mContext, R.string.simple_password);
				}
				return;
			}

			if (null == dialog) {
				dialog = new NormalDialog(this, this.getResources().getString(
						R.string.verification), "", "", "");
				dialog.setStyle(NormalDialog.DIALOG_STYLE_LOADING);
			}
			dialog.showDialog();
			String pwd=P2PHandler.getInstance().EntryPassword(password_new);
			P2PHandler.getInstance().setDevicePassword(mContact.contactId,
					password_old, pwd);
			//修改摄像头密码保存在服务器
			mQueue = Volley.newRequestQueue(mContext); 
			StringRequest stringRequest = new StringRequest(Method.POST, Constants.UPDATE_CAMERA_PWD_URL,  
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
						}
			} , 
			new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
				}
			}){
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					Map<String, String> map = new HashMap<String, String>();
					map.put("mCameraID", mContact.contactId);
					map.put("mCameraPassword", password_new);
					return map;
				}
			};
			mQueue.add(stringRequest);
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (isRegFilter) {
			mContext.unregisterReceiver(mReceiver);
			isRegFilter = false;
		}
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_MODIFNPCPASSWORDACTIVITY;
	}
}
