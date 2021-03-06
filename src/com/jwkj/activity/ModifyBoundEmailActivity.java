package com.jwkj.activity;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.test.jpushServer.R;
import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.data.SharedPreferencesManager;
import com.jwkj.entity.Email;
import com.jwkj.global.Constants;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.P2PHandler;

public class ModifyBoundEmailActivity extends BaseActivity implements
		OnClickListener {
	private final static String TAG = "ModifyBoundEmailActivity";
	private final static int GET_TIMES = 5;
	Context mContext;
	Contact mContact;
	ImageView mBack;
	Button mSave;
	EditText mEmail, mSend, mPassword;
	NormalDialog dialog;
	private boolean isRegFilter = false;
	String email;
	String email_name;
	private TextView txSend, txPassword, txSendSelf;
	private CheckBox cbSendSelf;
	List<String> data_list;
	String sendEmail, emailRoot, emailPwd;
	private boolean isSendSelf = true;// 需求更改，值不会改变，一直为true
	private boolean isEmailLegal;
	private boolean isSurportSMTP;
	private boolean isEmailChecked;
	private LinearLayout llSendEmail, llPassword, llEmail, llEmailsmtp;
	private TextView txErrorTips;
	private TextView etSmtp;
	private int CheckedEmailTimes = 0;
	private Button btnClear, btnChecked;
	private boolean isNeedClearEmail = false;
	private boolean isProcessResult = false;// 是否处理结果

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_npc_bound_email);
		mContact = (Contact) getIntent().getSerializableExtra("contact");
		email_name = getIntent().getStringExtra("email");
		sendEmail = getIntent().getStringExtra("sendEmail");
		emailRoot = getIntent().getStringExtra("emailRoot");
		emailPwd = getIntent().getStringExtra("emailPwd");
		isEmailLegal = getIntent().getBooleanExtra("isEmailLegal", true);
		isSurportSMTP = getIntent().getBooleanExtra("isSurportSMTP", false);
		isEmailChecked = getIntent().getBooleanExtra("isEmailChecked", false);
		mContext = this;
		initCompent();
		initData();
		regFilter();
	}

	public void initCompent() {
		mBack = (ImageView) findViewById(R.id.back_btn);
		mSave = (Button) findViewById(R.id.save);
		mEmail = (EditText) findViewById(R.id.email);
		mSend = (EditText) findViewById(R.id.ed_sendemail);
		etSmtp = (TextView) findViewById(R.id.et_smtp);
		btnClear = (Button) findViewById(R.id.btn_clear);
		btnClear.setOnClickListener(this);
		btnChecked = (Button) findViewById(R.id.btn_checked);
		if (sendEmail.length() <= 0 || sendEmail.equals("0")
				|| sendEmail.split("@")[0].equals("0")) {
			if (!email_name.equals("Unbound") && (!email_name.equals("未绑定"))
					&& (!email_name.equals("未綁定"))) {
				mSend.setText(email_name.substring(0,
						email_name.lastIndexOf("@")));
				etSmtp.setText(email_name.substring(email_name.lastIndexOf("@")));
				isNeedClearEmail = false;
			} else {
				mSend.setText("");
				etSmtp.setText(Email.getInstence().getUIEmail(emailRoot));
				isNeedClearEmail = true;
			}
		} else {
			mSend.setText(sendEmail.substring(0, sendEmail.lastIndexOf("@")));
			isNeedClearEmail = true;
			if (emailRoot != null) {
				etSmtp.setText(email_name.substring(email_name.lastIndexOf("@")));
			}
		}
		mSend.setSelection(mSend.getText().length());
		mPassword = (EditText) findViewById(R.id.et_password);
		mPassword.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		mPassword.setText(emailPwd);
		txSend = (TextView) findViewById(R.id.tx_send);
		txPassword = (TextView) findViewById(R.id.tx_password);
		txSendSelf = (TextView) findViewById(R.id.tx_sen_self);
		cbSendSelf = (CheckBox) findViewById(R.id.cb_sen_self);
		txErrorTips = (TextView) findViewById(R.id.tx_error_tips);
		llEmailsmtp = (LinearLayout) findViewById(R.id.ll_emial_smtp);
		// isSendSelf = SharedPreferencesManager.getInstance()
		// .getIsSendemailToSelf(mContext);
		cbSendSelf.setChecked(isSendSelf);
		mBack.setOnClickListener(this);
		mSave.setOnClickListener(this);

		llSendEmail = (LinearLayout) findViewById(R.id.ll_sendemail);
		llPassword = (LinearLayout) findViewById(R.id.layout_password);
		llEmail = (LinearLayout) findViewById(R.id.layout_cNumber);
		if (isSurportSMTP) {
			llEmail.setVisibility(View.GONE);
			llPassword.setVisibility(View.VISIBLE);
			llSendEmail.setVisibility(View.VISIBLE);
			changeTextTips();
		} else {
			llEmail.setVisibility(View.VISIBLE);
			llPassword.setVisibility(View.GONE);
			llSendEmail.setVisibility(View.GONE);
		}

		if (!email_name.equals("Unbound") && (!email_name.equals("未绑定"))
				&& (!email_name.equals("未綁定"))) {
			mEmail.setText(email_name);
			mEmail.setSelection(email_name.length());
			isNeedClearEmail = true;
		} else {
			isNeedClearEmail = false;
		}

		initPopwindow();
		if (isNeedClearEmail) {
			btnClear.setVisibility(View.VISIBLE);
		} else {
			btnClear.setVisibility(View.GONE);
		}
	}

	void changeTextTips() {
		if (isEmailChecked) {
			if (isEmailLegal) {
				// 邮箱可用
				txErrorTips.setVisibility(View.GONE);
				btnChecked.setVisibility(View.GONE);
			} else {
				// 邮箱不可用
				txErrorTips.setText(R.string.email_error_tips);
				txErrorTips.setVisibility(View.VISIBLE);
				// btnChecked.setVisibility(View.VISIBLE);
			}
		} else {
			txErrorTips.setText(R.string.email_notcheck_smtp);
			txErrorTips.setVisibility(View.VISIBLE);
			// btnChecked.setVisibility(View.VISIBLE);
		}
	}

	void initPopwindow() {
		data_list = Email.getInstence().getUIEmailList();
		llEmailsmtp.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					showPopwindow(v, 0, 0, 0);
					break;
				default:
					break;
				}
				return false;
			}
		});
	}

	private PopupWindow popupWindow;
	private ListView lvEmail;
	private MySpinnerAdapter arr_adapter = new MySpinnerAdapter();

	void showPopwindow(View parent, int x, int y, int postion) {
		View popView = LayoutInflater.from(mContext).inflate(
				R.layout.pop_email_smtp, null);
		popupWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		popupWindow.setWidth(parent.getMeasuredWidth());
		popupWindow.setHeight((int) (parent.getMeasuredHeight() * 4));
		lvEmail = (ListView) popView.findViewById(R.id.lv_email_smtp);
		lvEmail.setAdapter(arr_adapter);
		lvEmail.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// if (position == data_list.size() - 1) {
				// // 是系统邮箱发件人不可编辑，发给自己按钮不可点击，并且在给自己按钮
				// isSendSelf = false;
				// cbSendSelf.setEnabled(false);
				// cbSendSelf.setChecked(false);
				// mSend.setText("");
				// mSend.setEnabled(false);
				// mPassword.setText("");
				// mPassword.setEnabled(false);
				// TextEnable(true);
				// } else {
				// 不是系统邮箱，在发给自己按钮选中时同步更改收件人
				cbSendSelf.setEnabled(true);
				mSend.setEnabled(true);
				mPassword.setEnabled(true);
				TextEnable(false);
				// }
				setReseveEmail();
				etSmtp.setText((String) (parent.getAdapter().getItem(position)));
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		// popupWindow.setAnimationStyle(R.style.PopupAnimation);
		// 设置popwindow显示位置
		// popupWindow.showAtLocation(parent, 0, x, y);
		popupWindow.showAsDropDown(parent);
		// 获取popwindow焦点
		popupWindow.setFocusable(true);
		// 设置popwindow如果点击外面区域，便关闭。
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
	}

	private void initData() {
		cbSendSelf.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				isSendSelf = isChecked;
				// 收件人邮箱要联动且设置为不可编辑
				setReseveEmail();
				mEmail.setEnabled(!isSendSelf);
			}
		});

		mSend.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!isEmailLegal) {
					if (s.length() >= 1) {
						txErrorTips.setVisibility(View.GONE);
					} else {
						txErrorTips.setVisibility(View.VISIBLE);
					}
				}
				setReseveEmail();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	void TextEnable(boolean isGray) {
		if (isGray) {
			txSend.setTextColor(Utils
					.getColorByResouse(R.color.text_color_gray));
			txPassword.setTextColor(Utils
					.getColorByResouse(R.color.text_color_gray));
			txSendSelf.setTextColor(Utils
					.getColorByResouse(R.color.text_color_gray));
		} else {
			txSend.setTextColor(Utils.getColorByResouse(R.color.black));
			txPassword.setTextColor(Utils.getColorByResouse(R.color.black));
			txSendSelf.setTextColor(Utils.getColorByResouse(R.color.black));
		}

	}

	void setReseveEmail() {
		if (isSendSelf) {
			Editable senemail = mSend.getText();
			mEmail.setText(senemail);
			mEmail.append(etSmtp.getText());
		} else {
			mEmail.setEnabled(true);
		}
	}

	class MySpinnerAdapter extends BaseAdapter {

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;
			if (null == convertView) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.spinner_item, null);
				// convertView = LayoutInflater.from(mContext).inflate(
				// android.R.layout.simple_spinner_item, null);
				holder = new ViewHolder();
				holder.txEmail = (TextView) convertView
						.findViewById(R.id.tx_emails);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.txEmail.setText(data_list.get(position));
			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data_list.size();
		}

		@Override
		public String getItem(int position) {
			// TODO Auto-generated method stub
			return data_list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (null == convertView) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.spinner_item, null);
				holder = new ViewHolder();
				holder.txEmail = (TextView) convertView
						.findViewById(R.id.tx_emails);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.txEmail.setText(data_list.get(position));
			return convertView;
		}

		class ViewHolder {
			public TextView txEmail;
		}

	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.P2P.ACK_RET_SET_ALARM_EMAIL);
		filter.addAction(Constants.P2P.RET_SET_ALARM_EMAIL);
		filter.addAction(Constants.P2P.RET_GET_ALARM_EMAIL_WITHSMTP);
		mContext.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (!isProcessResult) {
				return;
			}
			if (intent.getAction().equals(Constants.P2P.RET_SET_ALARM_EMAIL)) {
				int result = intent.getIntExtra("result", -1);
				Log.i("dxsemail", "result-->" + result);
				if ((result & (1 << 0)) == Constants.P2P_SET.ALARM_EMAIL_SET.SETTING_SUCCESS) {
					if (isSurportSMTP) {
						CheckedEmailTimes++;
						delayGetAlarmEmial();
					} else {
						if (null != dialog && dialog.isShowing()) {
							dialog.dismiss();
							dialog = null;
						}
						getSMTPMessage(result);
					}

				} else if (result == -1) {
					T.showShort(mContext, R.string.operator_error);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_SET_ALARM_EMAIL)) {
				int result = intent.getIntExtra("result", -1);
				
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					if (null != dialog && dialog.isShowing()) {
						dialog.dismiss();
					}
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					mContext.sendBroadcast(i);
					finish();
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					if (null != dialog && dialog.isShowing()) {
						dialog.dismiss();
					}
					T.showShort(mContext, R.string.net_error_operator_fault);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.RET_GET_ALARM_EMAIL_WITHSMTP)) {
				String contectid = intent.getStringExtra("contectid");
				if (contectid != null && contectid.equals(mContact.contactId)) {
					String email = intent.getStringExtra("email");
					String[] SmptMessage = intent
							.getStringArrayExtra("SmptMessage");
					int result = intent.getIntExtra("result", 0);
					getSMTPMessage(result);
				}
			}
		}
	};

	private void getSMTPMessage(int result) {
		if ((byte) ((result >> 1) & (0x1)) == 0) {
			isSurportSMTP = false;
			T.showShort(mContext, R.string.modify_success);
			SharedPreferencesManager.getInstance().putIsSendemailToSelf(
					mContext, isSendSelf);
			finish();
		} else {
			isSurportSMTP = true;
			if ((byte) ((result >> 4) & (0x1)) == 0) {// 验证通过
				isEmailChecked = true;
				if (null != dialog && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}
				if ((byte) ((result >> 2) & (0x1)) == 0) {
					isEmailLegal = false;
					Toast toast = Toast.makeText(mContext,
							R.string.email_error_tips, Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else {
					if ((byte) ((result >> 3) & (0x1)) == 0) {
						isEmailLegal = true;
						T.showShort(mContext, R.string.modify_success);
						SharedPreferencesManager.getInstance()
								.putIsSendemailToSelf(mContext, isSendSelf);
						finish();
					} else {// 格式错误
						T.showShort(mContext, R.string.email_format_error);
					}

				}
				changeTextTips();
			} else {
				isEmailChecked = false;
				if (CheckedEmailTimes < GET_TIMES) {
					CheckedEmailTimes++;
					delayGetAlarmEmial();
				} else {
					changeTextTips();
					if (null != dialog && dialog.isShowing()) {
						dialog.dismiss();
						dialog = null;
					}
					Toast toast = Toast.makeText(mContext,
							R.string.email_notcheck_smtp, Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}

		}
	}

	private Handler getAlarmEmailHandler = new Handler();
	private Runnable runable = new Runnable() {
		public void run() {
			P2PHandler.getInstance().getAlarmEmail(mContact.contactId,
					mContact.contactPassword);
		}
	};

	void delayGetAlarmEmial() {
		getAlarmEmailHandler.postDelayed(runable, 3000);
		Log.i("dxsSMTP", "第" + CheckedEmailTimes + "次请求");
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		isProcessResult = true;
		switch (view.getId()) {
		case R.id.back_btn:
			Utils.hindKeyBoard(view);
			finish();
			break;
		case R.id.btn_clear:
			// 清除邮箱
			showClearEmail();
			break;
		case R.id.save:
			CheckedEmailTimes = 0;
			Utils.hindKeyBoard(view);
			if (isSurportSMTP) {
				saveEmailWithEMTP();
			} else {
				saveEmail();
			}
			break;
		}
	}

	void showClearEmail() {
		NormalDialog dialog = new NormalDialog(mContext, mContext
				.getResources().getString(R.string.unbind), mContext
				.getResources().getString(R.string.unbind)
				+ " "
				+ email_name
				+ "?", mContext.getResources().getString(R.string.confirm),
				mContext.getResources().getString(R.string.cancel));
		dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

			@Override
			public void onClick() {
				ClearEmail();
			}
		});
		dialog.showDialog();
	}

	void ClearEmail() {
		if (null == dialog) {
			dialog = new NormalDialog(this, this.getResources().getString(
					R.string.verification), "", "", "");
			dialog.setStyle(NormalDialog.DIALOG_STYLE_LOADING);
		}
		dialog.setOnCancelListener(dialogCancelListner);
		dialog.showDialog();
		dialog.setCanceledOnTouchOutside(true);
		P2PHandler.getInstance().setAlarmEmailWithSMTP(mContact.contactId,
				mContact.contactPassword, (byte) 0, "0", 0, "", "", "", "", "",(byte)0,(byte)0,0);
	}

	private OnCancelListener dialogCancelListner = new OnCancelListener() {

		@Override
		public void onCancel(DialogInterface dialog) {
			CheckedEmailTimes = GET_TIMES;
			isProcessResult = false;
			if (getAlarmEmailHandler != null && runable != null) {
				getAlarmEmailHandler.removeCallbacks(runable);
			}
			Log.i("dxsSMTP", "CheckedEmailTimes-->" + CheckedEmailTimes);
		}
	};

	private void saveEmailWithEMTP() {
		String sendEmails = mSend.getText().toString();// 发件人
		String emailRoot = etSmtp.getText().toString();// 后缀
		String pwd = mPassword.getText().toString().trim();

		// if (sendEmails.trim().length() <= 0 && pwd.trim().length() <=
		// 0&&Email.getInstence().isSurportThisSMTP(emailRoot)) {
		// sendEmails = "0";
		// ClearEmail();
		// return;
		// }
		if (sendEmails.trim().length() <= 0) {
			T.show(mContext, R.string.input_email, 2000);
			return;
		}
		if (!Email.getInstence().isSurportThisSMTP(emailRoot)) {
			T.show(mContext, String.format(
					getString(R.string.email_notsurpport_smtp), emailRoot),
					2000);
			return;
		}
		if (("".equals(pwd.trim()))) {
			T.show(mContext, R.string.inputpassword, 2000);
			return;
		}
		if ((sendEmails + emailRoot).length() > 31
				|| (sendEmails + emailRoot).length() < 5) {
			T.showShort(this, R.string.email_too_long);
			return;
		}
		if (null == dialog) {
			dialog = new NormalDialog(this, this.getResources().getString(
					R.string.verification), "", "", "");
			dialog.setStyle(NormalDialog.DIALOG_STYLE_LOADING);
		}

		dialog.setOnCancelListener(dialogCancelListner);
		dialog.showDialog();
		dialog.setCanceledOnTouchOutside(true);
		String[] emailMessage = Email.getInstence().getEmailMessage(emailRoot);
		P2PHandler.getInstance().setAlarmEmailWithSMTP(mContact.contactId,
				mContact.contactPassword, (byte) 3,
				sendEmails + emailMessage[0],
				Integer.parseInt(emailMessage[2]), emailMessage[1],
				sendEmails + emailMessage[0], pwd, emailMessage[3],
				emailMessage[4],(byte)0,(byte)0,0);
	}

	private void saveEmail() {
		email = mEmail.getText().toString();
		if ("".equals(email.trim())) {
			ClearEmail();
			return;
		}
		if (!Utils.isEmial(email)) {
			T.showShort(this, R.string.email_format_error);
			return;
		}
		if (email.length() > 31 || email.length() < 5) {
			T.showShort(this, R.string.email_too_long);
			return;
		}
		if (null == dialog) {
			dialog = new NormalDialog(this, this.getResources().getString(
					R.string.verification), "", "", "");
			dialog.setStyle(NormalDialog.DIALOG_STYLE_LOADING);

		}
		dialog.setOnCancelListener(dialogCancelListner);
		dialog.showDialog();
		dialog.setCanceledOnTouchOutside(true);
		P2PHandler.getInstance().setAlarmEmailWithSMTP(mContact.contactId,
				mContact.contactPassword, (byte) 3, email, 0, "", "", "", "",
				"",(byte)0,(byte)0,1);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (isRegFilter) {
			mContext.unregisterReceiver(mReceiver);
			isRegFilter = false;
		}
	}

	@Override
	public int getActivityInfo() {
		return Constants.ActivityInfo.ACTIVITY_MODIFYBOUNDEMAILACTIVITY;
	}

}
