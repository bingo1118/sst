package com.jwkj.widget;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.test.jpushServer.R;
import com.jwkj.activity.QRcodeActivity;
import com.jwkj.adapter.AlarmTypeAdapter;
import com.jwkj.adapter.SelectorDialogAdapter;
import com.jwkj.adapter.YzwAdapter;
import com.jwkj.data.DataManager;
import com.jwkj.global.Constants;
import com.jwkj.global.MyApp;
import com.jwkj.volley.JsonArrayPostRequest;
import com.jwkj.widget.MyInputPassDialog.OnCustomDialogListener;

public class NormalDialog {
	Context context;
	String[] list_data = new String[] {};
	String title_str, content_str, btn1_str, btn2_str;
	AlertDialog dialog;
	private OnButtonOkListener onButtonOkListener;
	private OnButtonCancelListener onButtonCancelListener;
	private OnCancelListener onCancelListener;
	private OnItemClickListener onItemClickListener;
	private int style = 999;
	public Spinner alarm_status;
	private ArrayAdapter<String> alarmStatusAdapter;
	private String yuzhiweiNum;
	private ListView listrView;
	private ListView alarm_statusList;

	public static final int DIALOG_STYLE_NORMAL = 1;
	public static final int DIALOG_STYLE_LOADING = 2;
	public static final int DIALOG_STYLE_UPDATE = 3;
	public static final int DIALOG_STYLE_DOWNLOAD = 4;
	public static final int DIALOG_STYLE_PROMPT = 5;

	public NormalDialog(Context context, String title, String content,
			String btn1, String btn2) {
		this.context = context;
		this.title_str = title;
		this.content_str = content;
		this.btn1_str = btn1;
		this.btn2_str = btn2;
	}
	
	public NormalDialog(Context context, String title,
			String btn1, String btn2) {
		this.context = context;
		this.title_str = title;
		this.btn1_str = btn1;
		this.btn2_str = btn2;
	}

	public NormalDialog(Context context) {
		this.context = context;
		this.title_str = "";
		this.content_str = "";
		this.btn1_str = "";
		this.btn2_str = "";
	}

	public void showDialog() {
		switch (style) {
		case DIALOG_STYLE_NORMAL:
			showNormalDialog();
			break;
		case DIALOG_STYLE_PROMPT:
			showPromptDialog();
			break;
		case DIALOG_STYLE_LOADING:
			showLoadingDialog();
			break;
		default:
			showNormalDialog();
			break;
		}
	}

	public void showLoadingDialog() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_loading, null);
		TextView title = (TextView) view.findViewById(R.id.title_text);
		title.setText(title_str);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		dialog.show();
		dialog.setContentView(view);
		FrameLayout.LayoutParams layout = (LayoutParams) view.getLayoutParams();
		layout.width = (int) context.getResources().getDimension(
				R.dimen.Loading_dialog_width);
		view.setLayoutParams(layout);
		dialog.setOnCancelListener(onCancelListener);
		dialog.setCanceledOnTouchOutside(false);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_normal);
	}
	

	public void showAboutDialog() {
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_about,
				null);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (null != dialog) {
					dialog.dismiss();
				}
			}

		});
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		dialog.show();
		dialog.setContentView(view);
		FrameLayout.LayoutParams layout = (LayoutParams) view.getLayoutParams();
		layout.width = (int) context.getResources().getDimension(
				R.dimen.about_dialog_width);
		view.setLayoutParams(layout);
		dialog.setOnCancelListener(onCancelListener);
		dialog.setCanceledOnTouchOutside(true);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_normal);
	}

	public void showDeviceInfoDialog(String curversion, String uBootVersion,
			String kernelVersion, String rootfsVersion) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_device_info, null);
		TextView text_curversion = (TextView) view
				.findViewById(R.id.text_curversion);
		TextView text_uBootVersion = (TextView) view
				.findViewById(R.id.text_uBootVersion);
		TextView text_kernelVersion = (TextView) view
				.findViewById(R.id.text_kernelVersion);
		TextView text_rootfsVersion = (TextView) view
				.findViewById(R.id.text_rootfsVersion);
		text_curversion.setText(curversion);
		text_uBootVersion.setText(uBootVersion);
		text_kernelVersion.setText(kernelVersion);
		text_rootfsVersion.setText(rootfsVersion);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (null != dialog) {
					dialog.dismiss();
				}
			}

		});
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		dialog.show();
		dialog.setContentView(view);
		FrameLayout.LayoutParams layout = (LayoutParams) view.getLayoutParams();
		layout.width = (int) context.getResources().getDimension(
				R.dimen.device_info_dialog_width);
		view.setLayoutParams(layout);
		dialog.setOnCancelListener(onCancelListener);
		dialog.setCanceledOnTouchOutside(true);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_normal);
	}

	public void showLoadingDialog2() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_loading2, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		dialog.show();
		dialog.setContentView(view);
		FrameLayout.LayoutParams layout = (LayoutParams) view.getLayoutParams();
		layout.width = (int) context.getResources().getDimension(
				R.dimen.Loading_dialog2_width);
		view.setLayoutParams(layout);
		dialog.setOnCancelListener(onCancelListener);
		dialog.setCanceledOnTouchOutside(false);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_normal);
	}

	public void showNormalDialog() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_normal, null);
		TextView title = (TextView) view.findViewById(R.id.title_text);
		TextView content = (TextView) view.findViewById(R.id.content_text);
		TextView button1 = (TextView) view.findViewById(R.id.button1_text);
		TextView button2 = (TextView) view.findViewById(R.id.button2_text);
		title.setText(title_str);
		content.setText(content_str);
		button1.setText(btn1_str);
		button2.setText(btn2_str);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != dialog) {
					dialog.dismiss();
				}

				onButtonOkListener.onClick();
			}
		});
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null == onButtonCancelListener) {
					if (null != dialog) {
						dialog.cancel();
					}
				} else {
					onButtonCancelListener.onClick();
				}
			}
		});
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		dialog.show();
		dialog.setContentView(view);
		FrameLayout.LayoutParams layout = (LayoutParams) view.getLayoutParams();
		layout.width = (int) context.getResources().getDimension(
				R.dimen.normal_dialog_width);

		view.setLayoutParams(layout);
		dialog.setCanceledOnTouchOutside(true);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_normal);
	}
	
	OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
            {
             return true;
            }
            else
            {
             return false;
            }
        }
    } ;

	public void showSelectDialog(int item) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_list, null);
		TextView button1 = (TextView) view.findViewById(R.id.calarm_button1_text);
		TextView button2 = (TextView) view.findViewById(R.id.calarm_button2_text);
		listrView = (ListView) view.findViewById(R.id.calarm_type_list);
		alarm_statusList = (ListView) view.findViewById(R.id.alarm_status);
		TextView yzw_tv = (TextView) view.findViewById(R.id.ywz_tv);
		if(item>1){
			alarm_statusList.setVisibility(View.GONE);
			yzw_tv.setVisibility(View.GONE);
			DisplayMetrics dMetrics = new DisplayMetrics();
			((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
			listrView.getLayoutParams().width=dMetrics.widthPixels;
		}
		findAllAlarmType(view);
		button1.setText(btn1_str);
		button2.setText(btn2_str);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onButtonOkListener.onClick();
			}
		});
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null == onButtonCancelListener) {
					if (null != dialog) {
						dialog.cancel();
					}
				} else {
					onButtonCancelListener.onClick();
					if (null != dialog) {
						dialog.cancel();
					}
				}
			}
		});
		
	}
	
	private void findAllAlarmType(final View view){
		RequestQueue mQueue = Volley.newRequestQueue(context);

		JsonArrayPostRequest mJsonRequest = new JsonArrayPostRequest(
				Constants.FIND_ALL_ALARM_TYPE, 
				new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray response) {
						List<String> lt = new ArrayList<String>();
						if(response.length()>0){
							for(int i=0;i<response.length();i++){
								try {
									lt.add(response.getString(i));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}else{
							lt.add("烟感");
							lt.add("门磁");
							lt.add("燃气探测器");
							lt.add("温感");
							lt.add("红外");
							lt.add("防火门检测器");
						}
						AlarmTypeAdapter mAlarmTypeAdapter = new AlarmTypeAdapter(context,lt);
						listrView.setAdapter(mAlarmTypeAdapter);
						YzwAdapter mYzwAdapter = new YzwAdapter(context,Constants.STATUS);
						alarm_statusList.setAdapter(mYzwAdapter);
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						dialog = builder.create();
						dialog.setOnKeyListener(keylistener);
						dialog.setCancelable(false);
						dialog.show();
						dialog.setContentView(view);
						FrameLayout.LayoutParams layout = (LayoutParams) view.getLayoutParams();
						layout.width = (int) context.getResources().getDimension(
								R.dimen.normal_dialog_width);
						view.setLayoutParams(layout);
						dialog.setCanceledOnTouchOutside(true);
						Window window = dialog.getWindow();
						window.setWindowAnimations(R.style.dialog_normal);
					}
				}, 
				new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						List<String> lt = new ArrayList<String>();
						lt.add("烟感");
						lt.add("门磁");
						lt.add("燃气探测器");
						lt.add("温感");
						lt.add("红外");
						lt.add("防火门检测器");
						AlarmTypeAdapter mAlarmTypeAdapter = new AlarmTypeAdapter(context,lt);
						listrView.setAdapter(mAlarmTypeAdapter);
						YzwAdapter mYzwAdapter = new YzwAdapter(context,Constants.STATUS);
						alarm_statusList.setAdapter(mYzwAdapter);
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						dialog = builder.create();
						dialog.setOnKeyListener(keylistener);
						dialog.setCancelable(false);
						dialog.show();
						dialog.setContentView(view);
						FrameLayout.LayoutParams layout = (LayoutParams) view.getLayoutParams();
						layout.width = (int) context.getResources().getDimension(
								R.dimen.normal_dialog_width);
						view.setLayoutParams(layout);
						dialog.setCanceledOnTouchOutside(true);
						Window window = dialog.getWindow();
						window.setWindowAnimations(R.style.dialog_normal);
					}
				}, 
				null);
		mQueue.add(mJsonRequest);
	}

	public void showSelectorDialog() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_selector, null);
		TextView title = (TextView) view.findViewById(R.id.title_text);
		title.setText(title_str);

		ListView content = (ListView) view.findViewById(R.id.content_text);

		SelectorDialogAdapter adapter = new SelectorDialogAdapter(context,
				list_data);
		content.setAdapter(adapter);
		content.setOnItemClickListener(onItemClickListener);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		dialog.show();
		dialog.setContentView(view);

		int itemHeight = (int) context.getResources().getDimension(
				R.dimen.selector_dialog_item_height);
		int margin = (int) context.getResources().getDimension(
				R.dimen.selector_dialog_margin);
		int separatorHeight = (int) context.getResources().getDimension(
				R.dimen.selector_dialog_separator_height);

		FrameLayout.LayoutParams layout = (LayoutParams) view.getLayoutParams();
		layout.width = (int) context.getResources().getDimension(
				R.dimen.selector_dialog_width);
		layout.height = itemHeight * list_data.length + margin * 2
				+ (list_data.length - 1) * separatorHeight;
		view.setLayoutParams(layout);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_normal);
	}

	public void showPromptDialog() {

		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_prompt, null);
		TextView content = (TextView) view.findViewById(R.id.content_text);
		TextView title = (TextView) view.findViewById(R.id.title_text);
		TextView button2 = (TextView) view.findViewById(R.id.button2_text);
		content.setText(content_str);
		title.setText(title_str);
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null == onButtonCancelListener) {
					if (null != dialog) {
						dialog.dismiss();
					}
				} else {
					onButtonCancelListener.onClick();
				}
			}
		});
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		dialog.show();
		dialog.setContentView(view);
		FrameLayout.LayoutParams layout = (LayoutParams) view.getLayoutParams();
		layout.width = (int) context.getResources().getDimension(
				R.dimen.normal_dialog_width);

		view.setLayoutParams(layout);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_normal);
	}

	public void showPromoptDiaglog() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_promopt_box2, null);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (null != dialog) {
					dialog.dismiss();
				}
			}

		});
		Button bt1 = (Button) view.findViewById(R.id.bt_determine);
		bt1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (null != dialog) {
					dialog.dismiss();
				}
			}
		});
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		dialog.show();
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(true);
		FrameLayout.LayoutParams layout = (LayoutParams) view.getLayoutParams();
		layout.width = (int) context.getResources().getDimension(
				R.dimen.dialog_promopt_width);
		layout.height = (int) context.getResources().getDimension(
				R.dimen.dialog_promopt_height);
		view.setLayoutParams(layout);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_normal);
	}

	public void showWaitConnectionDialog() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_wait_connection, null);
		ImageView anim_load = (ImageView) view.findViewById(R.id.anim_wait);
		AnimationDrawable animationdrawable = (AnimationDrawable) anim_load
				.getDrawable();
		animationdrawable.start();
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		dialog.show();
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		FrameLayout.LayoutParams layout = (LayoutParams) view.getLayoutParams();
		layout.width = (int) context.getResources().getDimension(
				R.dimen.dialog_remind_width);
		layout.height = (int) context.getResources().getDimension(
				R.dimen.dialog_reming_height);
		view.setLayoutParams(layout);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_normal);
	}

	public void showQRcodehelp() {
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_help,
				null);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (null != dialog) {
					dialog.dismiss();
				}
			}

		});
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		dialog.show();
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(true);
		FrameLayout.LayoutParams layout = (LayoutParams) view.getLayoutParams();
		layout.width = (int) context.getResources().getDimension(
				R.dimen.dialog_remind_width);
		layout.height = (int) context.getResources().getDimension(
				R.dimen.dialog_reming_height);
		view.setLayoutParams(layout);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_normal);
	}

	public void successDialog() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_success, null);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (null != dialog) {
					dialog.dismiss();
				}
			}

		});
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		dialog.show();
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(true);
		FrameLayout.LayoutParams layout = (LayoutParams) view.getLayoutParams();
		layout.width = (int) context.getResources().getDimension(
				R.dimen.dialog_success_width);
		layout.height = (int) context.getResources().getDimension(
				R.dimen.dialog_success_height);
		view.setLayoutParams(layout);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_normal);
	}

	public void faildDialog() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_prompt_box1, null);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (null != dialog) {
					dialog.dismiss();
				}
			}

		});
		Button bt1 = (Button) view.findViewById(R.id.bt_determine);
		bt1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}

			}
		});
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		dialog.show();
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(true);
		FrameLayout.LayoutParams layout = (LayoutParams) view.getLayoutParams();
		layout.width = (int) context.getResources().getDimension(
				R.dimen.dialog_promopt_width);
		layout.height = (int) context.getResources().getDimension(
				R.dimen.dialog_promopt_height);
		view.setLayoutParams(layout);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_normal);
	}

	public void showConnectFail() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_connect_failed, null);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// if (null!=dialog) {
				// dialog.dismiss();
				// }
			}

		});
		Button try_again = (Button) view.findViewById(R.id.try_again);
		Button use_qrecode = (Button) view.findViewById(R.id.try_qrecode);
		try_again.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (null != dialog) {
					dialog.dismiss();
				}
			}
		});
		use_qrecode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 跳转到二维码添加
				if (null != dialog) {
					dialog.dismiss();
					 onButtonCancelListener.onClick();
				}
				context.startActivity(new Intent(context, QRcodeActivity.class));
			}
		});
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		dialog.show();
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		FrameLayout.LayoutParams layout = (LayoutParams) view.getLayoutParams();
		layout.width = (int) context.getResources().getDimension(
				R.dimen.dialog_remind_width);
		layout.height = (int) context.getResources().getDimension(
				R.dimen.dialog_reming_height);
		view.setLayoutParams(layout);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_normal);

	}

	public void setTitle(String title) {
		this.title_str = title;
	}

	public void setTitle(int id) {
		this.title_str = context.getResources().getString(id);
	}

	public void setListData(String[] data) {
		this.list_data = data;
	}

	public void setCanceledOnTouchOutside(boolean bool) {
		dialog.setCanceledOnTouchOutside(bool);
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public void setCancelable(boolean bool) {
		dialog.setCancelable(bool);
	}

	public void cancel() {
		dialog.cancel();
	}

	public void dismiss() {
		dialog.dismiss();
	}

	public boolean isShowing() {
		return dialog.isShowing();
	}

	public void setBtnListener(TextView btn1, TextView btn2) {

	}

	public void setStyle(int style) {
		this.style = style;
	}

	public interface OnButtonOkListener {
		public void onClick();
	}

	public interface OnButtonCancelListener {
		public void onClick();
	}

	public void setOnButtonOkListener(OnButtonOkListener onButtonOkListener) {
		this.onButtonOkListener = onButtonOkListener;
	}

	public void setOnButtonCancelListener(
			OnButtonCancelListener onButtonCancelListener) {
		this.onButtonCancelListener = onButtonCancelListener;
	}

	public void setOnCancelListener(OnCancelListener onCancelListener) {
		this.onCancelListener = onCancelListener;
		Log.i("dxsSMTP", "setlistener");
	}
}
