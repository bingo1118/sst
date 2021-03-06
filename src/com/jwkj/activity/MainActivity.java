package com.jwkj.activity;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.example.jpushdemo.ExampleUtil;
import com.example.jpushdemo.PushSetActivity;
import com.example.jpushdemo.MainActivity.MessageReceiver;
import com.test.jpushServer.R;
import com.jwkj.P2PListener;
import com.jwkj.SettingListener;
import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.entity.Account;
import com.jwkj.fragment.ContactFrag;
import com.jwkj.fragment.KeyboardFrag;
import com.jwkj.fragment.SettingFrag;
import com.jwkj.fragment.UtilsFrag;
import com.jwkj.global.AccountPersist;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.MyApp;
import com.jwkj.global.NpcCommon;
import com.jwkj.global.NpcCommon.NETWORK_TYPE;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.P2PHandler;
import com.p2p.core.network.GetAccountInfoResult;
import com.p2p.core.network.NetManager;
import com.p2p.core.update.UpdateManager;

public class MainActivity extends BaseActivity implements OnClickListener {
	public static Context mContext;

	private ImageView dials_img, contact_img, recent_img, settings_img,
			discover_img;
	private RelativeLayout contact, dials, recent, settings, discover;
	boolean isRegFilter = false;

	private int currFrag = 0;
	private AlertDialog dialog_update;
	private AlertDialog dialog_downapk;
	private ProgressBar downApkBar;
	private TextView tv_contact, tv_message, tv_image, tv_more;

	private String[] fragTags = new String[] { "contactFrag", "keyboardFrag",
			"nearlyTellFrag", "utilsFrag", "settingFrag" };
	private SettingFrag settingFrag;
	private KeyboardFrag keyboardFrag;
	private ContactFrag contactFrag;
	private UtilsFrag utilsFrag;
	//Jpush
	public static boolean isForeground = false;
	private static final String TAG = "JPush";
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		Log.e("life", "MainActivity->>onCreate");
		super.onCreate(arg0);
		mContext = this;
		DataManager.findAlarmMaskByActiveUser(mContext, "");
		NpcCommon.verifyNetwork(mContext);
		regFilter();
		JpushBegin();
		if (!verifyLogin()) {
			Intent login = new Intent(mContext, LoginActivity.class);
			startActivity(login);
			finish();
		} else {
			initComponent();
			new FList();
			P2PHandler.getInstance().p2pInit(this, new P2PListener(),
					new SettingListener());
			connect();
			if (null == contactFrag) {
				contactFrag = new ContactFrag();
			}
			currFrag = 0;
			if (null == contactFrag) {
				contactFrag = new ContactFrag();
			}
			replaceFragment(R.id.fragContainer, contactFrag, fragTags[0]);
			changeIconShow();
			if (!NpcCommon.mThreeNum.equals("517400")) {
				new GetAccountInfoTask().execute();
			}

		}

	}

	public void initComponent() {
		setContentView(R.layout.activity_main);
		dials = (RelativeLayout) findViewById(R.id.icon_keyboard);
		dials_img = (ImageView) findViewById(R.id.icon_keyboard_img);
		contact = (RelativeLayout) findViewById(R.id.icon_contact);
		contact_img = (ImageView) findViewById(R.id.icon_contact_img);
		recent = (RelativeLayout) findViewById(R.id.icon_nearlytell);
		recent_img = (ImageView) findViewById(R.id.icon_nearlytell_img);
		settings = (RelativeLayout) findViewById(R.id.icon_setting);
		settings_img = (ImageView) findViewById(R.id.icon_setting_img);
		discover = (RelativeLayout) findViewById(R.id.icon_discover);
		discover_img = (ImageView) findViewById(R.id.icon_discover_img);
		tv_contact = (TextView) findViewById(R.id.tv_contact);
		tv_message = (TextView) findViewById(R.id.tv_message);
		tv_image = (TextView) findViewById(R.id.tv_image);
		tv_more = (TextView) findViewById(R.id.tv_more);

		dials.setOnClickListener(this);
		contact.setOnClickListener(this);
		recent.setOnClickListener(this);
		settings.setOnClickListener(this);
		discover.setOnClickListener(this);
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action.ACTION_NETWORK_CHANGE);
		filter.addAction(Constants.Action.ACTION_SWITCH_USER);
		filter.addAction(Constants.Action.ACTION_EXIT);
		filter.addAction(Constants.Action.RECEIVE_MSG);
		filter.addAction(Constants.Action.RECEIVE_SYS_MSG);
		filter.addAction(Intent.ACTION_LOCALE_CHANGED);
		filter.addAction(Constants.Action.ACTION_UPDATE);
		filter.addAction(Constants.Action.SESSION_ID_ERROR);
		this.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	Handler handler = new Handler() {
		long last_time;

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int value = msg.arg1;
			switch (msg.what) {
			case UpdateManager.HANDLE_MSG_DOWNING:
				if ((System.currentTimeMillis() - last_time) > 1000) {
					MyApp.app.showDownNotification(
							UpdateManager.HANDLE_MSG_DOWNING, value);
					last_time = System.currentTimeMillis();
				}
				break;
			case UpdateManager.HANDLE_MSG_DOWN_SUCCESS:
				// MyApp.app.showDownNotification(UpdateManager.HANDLE_MSG_DOWN_SUCCESS,0);
				MyApp.app.hideDownNotification();
				// T.showShort(mContext, R.string.down_success);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				File file = new File(Environment.getExternalStorageDirectory()
						+ "/" + Constants.Update.SAVE_PATH + "/"
						+ Constants.Update.FILE_NAME);
				if (!file.exists()) {
					return;
				}
				intent.setDataAndType(Uri.fromFile(file),
						Constants.Update.INSTALL_APK);
				mContext.startActivity(intent);
				break;
			case UpdateManager.HANDLE_MSG_DOWN_FAULT:

				MyApp.app.showDownNotification(
						UpdateManager.HANDLE_MSG_DOWN_FAULT, value);
				T.showShort(mContext, R.string.down_fault);
				break;
			}
		}
	};
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(
					Constants.Action.ACTION_NETWORK_CHANGE)) {
				boolean isNetConnect = false;
				ConnectivityManager connectivityManager = (ConnectivityManager) mContext
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo activeNetInfo = connectivityManager
						.getActiveNetworkInfo();
				if (activeNetInfo != null) {
					if (activeNetInfo.isConnected()) {
						isNetConnect = true;
						T.showShort(mContext,
								getString(R.string.message_net_connect)
										+ activeNetInfo.getTypeName());
					} else {
						T.showShort(mContext, getString(R.string.network_error)
								+ " " + activeNetInfo.getTypeName());
					}

					if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
						NpcCommon.mNetWorkType = NETWORK_TYPE.NETWORK_WIFI;
					} else {
						NpcCommon.mNetWorkType = NETWORK_TYPE.NETWORK_2GOR3G;
					}
				} else {
					Toast.makeText(mContext, getString(R.string.network_error),
							Toast.LENGTH_SHORT).show();
				}

				NpcCommon.setNetWorkState(isNetConnect);

				Intent intentNew = new Intent();
				intentNew.setAction(Constants.Action.NET_WORK_TYPE_CHANGE);
				mContext.sendBroadcast(intentNew);
			} else if (intent.getAction().equals(
					Constants.Action.ACTION_SWITCH_USER)) {
				Account account = AccountPersist.getInstance()
						.getActiveAccountInfo(mContext);
				new ExitTask(account).execute();
				AccountPersist.getInstance().setActiveAccount(mContext,
						new Account());
				NpcCommon.mThreeNum = "";
				Intent i = new Intent(MyApp.MAIN_SERVICE_START);
				stopService(i);
				Intent login = new Intent(mContext, LoginActivity.class);
				startActivity(login);
				finish();
			} else if (intent.getAction().equals(
					Constants.Action.SESSION_ID_ERROR)) {
				Account account = AccountPersist.getInstance()
						.getActiveAccountInfo(mContext);
				new ExitTask(account).execute();
				AccountPersist.getInstance().setActiveAccount(mContext,
						new Account());
				Intent i = new Intent(MyApp.MAIN_SERVICE_START);
				stopService(i);
				Intent login = new Intent(mContext, LoginActivity.class);
				startActivity(login);
				T.showShort(mContext, R.string.session_id_error);
				finish();
			} else if (intent.getAction().equals(Constants.Action.ACTION_EXIT)) {
				NormalDialog dialog = new NormalDialog(mContext, mContext
						.getResources().getString(R.string.exit), mContext
						.getResources().getString(R.string.confirm_exit),
						mContext.getResources().getString(R.string.exit),
						mContext.getResources().getString(R.string.cancel));
				dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

					@Override
					public void onClick() {
						// TODO Auto-generated method stub
						Intent i = new Intent(MyApp.MAIN_SERVICE_START);
						stopService(i);
						isGoExit(true);
						finish();
					}
				});
				dialog.showNormalDialog();
			} else if (intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {

			} else if (intent.getAction().equals(Constants.Action.RECEIVE_MSG)) {
				int result = intent.getIntExtra("result", -1);
				String msgFlag = intent.getStringExtra("msgFlag");

				if (result == Constants.P2P_SET.ACK_RESULT.ACK_SUCCESS) {
					DataManager.updateMessageStateByFlag(mContext, msgFlag,
							Constants.MessageType.SEND_SUCCESS);
				} else {
					DataManager.updateMessageStateByFlag(mContext, msgFlag,
							Constants.MessageType.SEND_FAULT);
				}

			} else if (intent.getAction().equals(
					Constants.Action.RECEIVE_SYS_MSG)) {

			} else if (intent.getAction()
					.equals(Constants.Action.ACTION_UPDATE)) {
				if (null != dialog_update && dialog_update.isShowing()) {
					Log.e("my", "isShowing");
					return;
				}

				View view = LayoutInflater.from(mContext).inflate(
						R.layout.dialog_update, null);
				TextView title = (TextView) view.findViewById(R.id.title_text);
				WebView content = (WebView) view
						.findViewById(R.id.content_text);
				TextView button1 = (TextView) view
						.findViewById(R.id.button1_text);
				TextView button2 = (TextView) view
						.findViewById(R.id.button2_text);

				title.setText(R.string.update);
				content.setBackgroundColor(Color.WHITE); // 设置背景色
				content.getBackground().setAlpha(100); // 设置填充透明度 范围：0-255
				String data = intent.getStringExtra("message");
				final String downloadPath = intent.getStringExtra("url");
				content.loadDataWithBaseURL(null, data, "text/html", "utf-8",
						null);
				button1.setText(R.string.update_now);
				button2.setText(R.string.next_time);
				button1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (null != dialog_update) {
							dialog_update.dismiss();
							dialog_update = null;
						}
						if (UpdateManager.getInstance().getIsDowning()) {
							return;
						}
						MyApp.app.showDownNotification(
								UpdateManager.HANDLE_MSG_DOWNING, 0);
						T.showShort(mContext, R.string.start_down);
						new Thread() {
							public void run() {
								UpdateManager.getInstance().downloadApk(
										handler, Constants.Update.SAVE_PATH,
										Constants.Update.FILE_NAME,downloadPath);
							}
						}.start();
					}
				});
				button2.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (null != dialog_update) {
							dialog_update.cancel();
						}
					}
				});

				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				dialog_update = builder.create();
				dialog_update.show();
				dialog_update.setContentView(view);
				FrameLayout.LayoutParams layout = (LayoutParams) view
						.getLayoutParams();
				layout.width = (int) mContext.getResources().getDimension(
						R.dimen.update_dialog_width);
				view.setLayoutParams(layout);
				dialog_update.setCanceledOnTouchOutside(false);
				Window window = dialog_update.getWindow();
				window.setWindowAnimations(R.style.dialog_normal);
			} else if (intent.getAction().equals(
					Constants.Action.SETTING_WIFI_SUCCESS)) {
				currFrag = 0;
				if (null == contactFrag) {
					contactFrag = new ContactFrag();
				}
				replaceFragment(R.id.fragContainer, contactFrag, fragTags[0]);
				changeIconShow();
			}
		}

	};

	private void connect() {
		Intent service = new Intent(MyApp.MAIN_SERVICE_START);
		startService(service);
	}

	private boolean verifyLogin() {
		Account activeUser = AccountPersist.getInstance().getActiveAccountInfo(
				mContext);

		if (activeUser != null) {
			NpcCommon.mThreeNum = activeUser.three_number;
			return true;
		}

		return false;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.icon_contact:
			currFrag = 0;
			if (null == contactFrag) {
				contactFrag = new ContactFrag();
			}
			replaceFragment(R.id.fragContainer, contactFrag, fragTags[0]);
			changeIconShow();
			break;
		case R.id.icon_keyboard:
			ContactFrag.isHideAdd = true;
			currFrag = 1;
			if (null == keyboardFrag) {
				keyboardFrag = new KeyboardFrag();
			}
			replaceFragment(R.id.fragContainer, keyboardFrag, fragTags[1]);
			changeIconShow();
			break;
		case R.id.icon_nearlytell:
			break;

		case R.id.icon_setting:
			ContactFrag.isHideAdd = true;
			currFrag = 3;
			if (null == settingFrag) {
				settingFrag = new SettingFrag();
			}
			replaceFragment(R.id.fragContainer, settingFrag, fragTags[3]);
			changeIconShow();
			break;
		case R.id.icon_discover:
			ContactFrag.isHideAdd = true;
			currFrag = 4;
			if (null == utilsFrag) {
				utilsFrag = new UtilsFrag();
			}
			replaceFragment(R.id.fragContainer, utilsFrag, fragTags[4]);
			changeIconShow();
			break;
		}

	}

	public void changeIconShow() {
		switch (currFrag) {
		case 0:
			contact_img.setImageResource(R.drawable.contact_p);
			dials_img.setImageResource(R.drawable.keyboard);
			recent_img.setImageResource(R.drawable.recent);
			settings_img.setImageResource(R.drawable.setting);
			discover_img.setImageResource(R.drawable.toolbox);
			tv_contact.setTextColor(getResources().getColor(
					R.color.color_local_device_bar));
			tv_message.setTextColor(getResources().getColor(
					R.color.text_color_white));
			tv_image.setTextColor(getResources().getColor(
					R.color.text_color_white));
			tv_more.setTextColor(getResources().getColor(
					R.color.text_color_white));
			contact.setSelected(true);
			dials.setSelected(false);
			recent.setSelected(false);
			settings.setSelected(false);
			discover.setSelected(false);
			break;
		case 1:
			contact_img.setImageResource(R.drawable.contact);
			dials_img.setImageResource(R.drawable.keyboard_p);
			recent_img.setImageResource(R.drawable.recent);
			settings_img.setImageResource(R.drawable.setting);
			discover_img.setImageResource(R.drawable.toolbox);
			tv_contact.setTextColor(getResources().getColor(
					R.color.text_color_white));
			tv_message.setTextColor(getResources().getColor(
					R.color.color_local_device_bar));
			tv_image.setTextColor(getResources().getColor(
					R.color.text_color_white));
			tv_more.setTextColor(getResources().getColor(
					R.color.text_color_white));
			contact.setSelected(false);
			dials.setSelected(true);
			recent.setSelected(false);
			settings.setSelected(false);
			discover.setSelected(false);
			break;
		case 2:
			contact_img.setImageResource(R.drawable.contact);
			dials_img.setImageResource(R.drawable.keyboard);
			recent_img.setImageResource(R.drawable.recent_p);
			settings_img.setImageResource(R.drawable.setting);
			discover_img.setImageResource(R.drawable.toolbox);
			contact.setSelected(false);
			dials.setSelected(false);
			recent.setSelected(true);
			settings.setSelected(false);
			discover.setSelected(false);
			break;
		case 3:
			contact_img.setImageResource(R.drawable.contact);
			dials_img.setImageResource(R.drawable.keyboard);
			recent_img.setImageResource(R.drawable.recent);
			settings_img.setImageResource(R.drawable.setting_p);
			discover_img.setImageResource(R.drawable.toolbox);
			tv_contact.setTextColor(getResources().getColor(
					R.color.text_color_white));
			tv_message.setTextColor(getResources().getColor(
					R.color.text_color_white));
			tv_image.setTextColor(getResources().getColor(
					R.color.text_color_white));
			tv_more.setTextColor(getResources().getColor(
					R.color.color_local_device_bar));
			contact.setSelected(false);
			dials.setSelected(false);
			recent.setSelected(false);
			settings.setSelected(true);
			discover.setSelected(false);
			break;
		case 4:
			contact_img.setImageResource(R.drawable.contact);
			dials_img.setImageResource(R.drawable.keyboard);
			recent_img.setImageResource(R.drawable.recent);
			settings_img.setImageResource(R.drawable.setting);
			discover_img.setImageResource(R.drawable.toolbox_p);
			tv_contact.setTextColor(getResources().getColor(
					R.color.text_color_white));
			tv_message.setTextColor(getResources().getColor(
					R.color.text_color_white));
			tv_image.setTextColor(getResources().getColor(
					R.color.color_local_device_bar));
			tv_more.setTextColor(getResources().getColor(
					R.color.text_color_white));
			contact.setSelected(false);
			dials.setSelected(false);
			recent.setSelected(false);
			settings.setSelected(false);
			discover.setSelected(true);
			break;
		}
	}

	public void replaceFragment(int container, Fragment fragment, String tag) {
		try {
			FragmentManager manager = getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			// transaction.setCustomAnimations(android.R.anim.fade_in,
			// android.R.anim.fade_out);
			transaction.replace(R.id.fragContainer, fragment, tag);
			transaction.commit();
			manager.executePendingTransactions();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("my", "replaceFrag error--main");
		}
	}

	@Override
	public void onPause() {
		Log.e("life", "MainActivity->>onPause");
		isForeground = false;
		super.onPause();
	}

	@Override
	public void onResume() {
		Log.e("life", "MainActivity->>onResume");
		isForeground = true;
		super.onResume();
	}

	@Override
	public void onStart() {
		Log.e("life", "MainActivity->>onStart");
		super.onStart();
	}

	@Override
	public void onStop() {
		Log.e("life", "MainActivity->>onStop");
		super.onStop();
	}

	@Override
	public void onDestroy() {
		Log.e("life", "MainActivity->>onDestroy");
		unregisterReceiver(mMessageReceiver);
		super.onDestroy();
		if (isRegFilter) {
			isRegFilter = false;
			this.unregisterReceiver(mReceiver);
		}
	}

	@Override
	public void onBackPressed() {
		Log.e("my", "onBackPressed");
		if (null != keyboardFrag && currFrag == 1) {
			// if(keyboardFrag.IsInputDialogShowing()){
			// Intent close_input_dialog = new Intent();
			// close_input_dialog.setAction(Constants.Action.CLOSE_INPUT_DIALOG);
			// mContext.sendBroadcast(close_input_dialog);
			// return;
			// }
		}
		Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
		mHomeIntent.addCategory(Intent.CATEGORY_HOME);
		mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		mContext.startActivity(mHomeIntent);
	}

	class GetAccountInfoTask extends AsyncTask {

		public GetAccountInfoTask() {

		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			Utils.sleepThread(1000);
			
			Account account = AccountPersist.getInstance()
					.getActiveAccountInfo(mContext);
			if(null==NpcCommon.mThreeNum||null==account.sessionId){
				return null;
			}else{
				return NetManager.getInstance(mContext).getAccountInfo(
						NpcCommon.mThreeNum, account.sessionId);
			}
			
		}

		@Override
		protected void onPostExecute(Object object) {
			// TODO Auto-generated method stub
			if(null==object){
				Intent intet = new Intent(MainActivity.this,LoginActivity.class);
				startActivity(intet);
				finish();
			}else{
				GetAccountInfoResult result = NetManager
						.createGetAccountInfoResult((JSONObject) object);
				switch (Integer.parseInt(result.error_code)) {
				case NetManager.SESSION_ID_ERROR:
					Intent i = new Intent();
					i.setAction(Constants.Action.SESSION_ID_ERROR);
					MyApp.app.sendBroadcast(i);
					break;
				case NetManager.CONNECT_CHANGE:
					new GetAccountInfoTask().execute();
					return;
				case NetManager.GET_ACCOUNT_SUCCESS:
					try {
						String email = result.email;
						String phone = result.phone;
						Account account = AccountPersist.getInstance()
								.getActiveAccountInfo(mContext);
						if (null == account) {
							account = new Account();
						}
						account.email = email;
						account.phone = phone;
						AccountPersist.getInstance().setActiveAccount(mContext,
								account);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			}
			
		}

	}

	class ExitTask extends AsyncTask {
		Account account;

		public ExitTask(Account account) {
			this.account = account;
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			return NetManager.getInstance(mContext).exit_application(
					account.three_number, account.sessionId);
		}

		@Override
		protected void onPostExecute(Object object) {
			// TODO Auto-generated method stub
			int result = (Integer) object;
			switch (result) {
			case NetManager.CONNECT_CHANGE:
				new ExitTask(account).execute();
				return;
			default:
				break;
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_MAINACTIVITY;
	}
	
	public void JpushBegin(){
		registerMessageReceiver();  // used for receive msg
		JPushInterface.init(getApplicationContext());
	}
	
	//for receive customer msg from jpush server
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	
	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
              String messge = intent.getStringExtra(KEY_MESSAGE);
              String extras = intent.getStringExtra(KEY_EXTRAS);
              StringBuilder showMsg = new StringBuilder();
              showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
              if (!ExampleUtil.isEmpty(extras)) {
            	  showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
              }
			}
		}
	}

}
