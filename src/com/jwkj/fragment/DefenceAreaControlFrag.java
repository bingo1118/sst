package com.jwkj.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.im.proto.ay;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.test.jpushServer.R;
import com.jwkj.activity.CutImageActivity;
import com.jwkj.activity.MainControlActivity;
import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.data.Defence;
import com.jwkj.global.Constants;
import com.jwkj.global.MyApp;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.volley.JsonArrayPostRequest;
import com.jwkj.widget.NormalDialog;
import com.jwkj.widget.NormalDialog.OnButtonCancelListener;
import com.p2p.core.P2PHandler;

public class DefenceAreaControlFrag extends BaseFragment implements OnClickListener{
	private Context mContext;
	private Contact contact;
	AlertDialog dialog;
	private String alarmType=null,channelName,yuzhiweiNum;
	private boolean isRegFilter = false;
	EditText editTextDialog;
	private int channelID,areaID,cID,aID;
	ImageButton btn,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8;
	TextView remote,hall,window,balcony,bedroom,kitchen,courtyard,door_lock,other,tView;
	RelativeLayout change_defence_area1,change_defence_area2,change_defence_area3,change_defence_area4,change_defence_area5,change_defence_area6,change_defence_area7,change_defence_area8,change_defence_area9;
	LinearLayout defence_area_content1,defence_area_content2,defence_area_content3,defence_area_content4,defence_area_content5,defence_area_content6,defence_area_content7,defence_area_content8,defence_area_content9;
	ProgressBar progressBar_defence_area1,progressBar_defence_area2,progressBar_defence_area3,progressBar_defence_area4,progressBar_defence_area5,progressBar_defence_area6,progressBar_defence_area7,progressBar_defence_area8,progressBar_defence_area9;
	RelativeLayout one1,one2,one3,one4,one5,one6,one7,one8;
	RelativeLayout two1,two2,two3,two4,two5,two6,two7,two8;
	RelativeLayout three1,three2,three3,three4,three5,three6,three7,three8;
	RelativeLayout four1,four2,four3,four4,four5,four6,four7,four8;
	RelativeLayout five1,five2,five3,five4,five5,five6,five7,five8;
	RelativeLayout six1,six2,six3,six4,six5,six6,six7,six8;
	RelativeLayout seven1,seven2,seven3,seven4,seven5,seven6,seven7,seven8;
	RelativeLayout eight1,eight2,eight3,eight4,eight5,eight6,eight7,eight8;
	RelativeLayout nine1,nine2,nine3,nine4,nine5,nine6,nine7,nine8;
	
	TextView text_view_one1,text_view_one2,text_view_one3,text_view_one4,text_view_one5,text_view_one6,text_view_one7,text_view_one8;
	TextView text_view_two1,text_view_two2,text_view_two3,text_view_two4,text_view_two5,text_view_two6,text_view_two7,text_view_two8;
	TextView text_view_three1,text_view_three2,text_view_three3,text_view_three4,text_view_three5,text_view_three6,text_view_three7,text_view_three8;
	TextView text_view_four1,text_view_four2,text_view_four3,text_view_four4,text_view_four5,text_view_four6,text_view_four7,text_view_four8;
	TextView text_view_five1,text_view_five2,text_view_five3,text_view_five4,text_view_five5,text_view_five6,text_view_five7,text_view_five8;
	TextView text_view_six1,text_view_six2,text_view_six3,text_view_six4,text_view_six5,text_view_six6,text_view_six7,text_view_six8;
	TextView text_view_seven1,text_view_seven2,text_view_seven3,text_view_seven4,text_view_seven5,text_view_seven6,text_view_seven7,text_view_seven8;
	TextView text_view_eigth1,text_view_eigth2,text_view_eigth3,text_view_eigth4,text_view_eigth5,text_view_eigth6,text_view_eigth7,text_view_eigth8;
	TextView text_view_nine1,text_view_nine2,text_view_nine3,text_view_nine4,text_view_nine5,text_view_nine6,text_view_nine7,text_view_nine8;
	
	ImageView switch_one1,switch_one2,switch_one3,switch_one4,switch_one5,switch_one6,switch_one7,switch_one8;
	ImageView switch_two1,switch_two2,switch_two3,switch_two4,switch_two5,switch_two6,switch_two7,switch_two8;
	ImageView switch_three1,switch_three2,switch_three3,switch_three4,switch_three5,switch_three6,switch_three7,switch_three8;
	ImageView switch_four1,switch_four2,switch_four3,switch_four4,switch_four5,switch_four6,switch_four7,switch_four8;
	ImageView switch_five1,switch_five2,switch_five3,switch_five4,switch_five5,switch_five6,switch_five7,switch_five8;
	ImageView switch_six1,switch_six2,switch_six3,switch_six4,switch_six5,switch_six6,switch_six7,switch_six8;
	ImageView switch_seven1,switch_seven2,switch_seven3,switch_seven4,switch_seven5,switch_seven6,switch_seven7,switch_seven8;
	ImageView switch_eight1,switch_eight2,switch_eight3,switch_eight4,switch_eight5,switch_eight6,switch_eight7,switch_eight8;
	ImageView switch_nine1,switch_nine2,switch_nine3,switch_nine4,switch_nine5,switch_nine6,switch_nine7,switch_nine8;
	
	ImageView clear_one1,clear_one2,clear_one3,clear_one4,clear_one5,clear_one6,clear_one7,clear_one8;
	ImageView clear_two1,clear_two2,clear_two3,clear_two4,clear_two5,clear_two6,clear_two7,clear_two8;
	ImageView clear_three1,clear_three2,clear_three3,clear_three4,clear_three5,clear_three6,clear_three7,clear_three8;
	ImageView clear_four1,clear_four2,clear_four3,clear_four4,clear_four5,clear_four6,clear_four7,clear_four8;
	ImageView clear_five1,clear_five2,clear_five3,clear_five4,clear_five5,clear_five6,clear_five7,clear_five8;
	ImageView clear_six1,clear_six2,clear_six3,clear_six4,clear_six5,clear_six6,clear_six7,clear_six8;
	ImageView clear_seven1,clear_seven2,clear_seven3,clear_seven4,clear_seven5,clear_seven6,clear_seven7,clear_seven8;
	ImageView clear_eight1,clear_eight2,clear_eight3,clear_eight4,clear_eight5,clear_eight6,clear_eight7,clear_eight8;
	ImageView clear_nine1,clear_nine2,clear_nine3,clear_nine4,clear_nine5,clear_nine6,clear_nine7,clear_nine8;
	
	
	ProgressBar pre_two1,pre_two2,pre_two3,pre_two4,pre_two5,pre_two6,pre_two7,pre_two8;
	ProgressBar pre_three1,pre_three2,pre_three3,pre_three4,pre_three5,pre_three6,pre_three7,pre_three8;
	ProgressBar pre_four1,pre_four2,pre_four3,pre_four4,pre_four5,pre_four6,pre_four7,pre_four8;
	ProgressBar pre_five1,pre_five2,pre_five3,pre_five4,pre_five5,pre_five6,pre_five7,pre_five8;
	ProgressBar pre_six1,pre_six2,pre_six3,pre_six4,pre_six5,pre_six6,pre_six7,pre_six8;
	ProgressBar pre_seven1,pre_seven2,pre_seven3,pre_seven4,pre_seven5,pre_seven6,pre_seven7,pre_seven8;
	ProgressBar pre_eight1,pre_eight2,pre_eight3,pre_eight4,pre_eight5,pre_eight6,pre_eight7,pre_eight8;
	ProgressBar pre_nine1,pre_nine2,pre_nine3,pre_nine4,pre_nine5,pre_nine6,pre_nine7,pre_nine8;
	NormalDialog dialog_loading;
	
	int current_group;
	int current_item;
	int current_type;
	
	boolean is_one_active = false;
	boolean is_two_active = false;
	boolean is_three_active = false;
	boolean is_four_active = false;
	boolean is_five_active = false;
	boolean is_six_active = false;
	boolean is_seven_active = false;
	boolean is_eight_active = false;
	boolean is_nine_active = false;
	private static final int EXPAND_OR_SHRINK = 0x11;
	private static final int END_EXPAND_OR_SHRINK = 0x12;
	int currentSwitch;
	int currentgroup;
	int currentitem;
	boolean isSurpport=true;
	private Map<String,String> mDefenceArea;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = MainControlActivity.mContext;
		contact = (Contact) getArguments().getSerializable("contact");
		View view = inflater.inflate(R.layout.fragment_defence_area_control, container, false);
		initComponent(view);
		regFilter();
		
		new MyTask().execute();
		
		P2PHandler.getInstance().getDefenceAreaAlarmSwitch(contact.contactId, contact.contactPassword);
		P2PHandler.getInstance().getDefenceArea(contact.contactId, contact.contactPassword);
		return view;
	}
	
	class MyTask extends AsyncTask<String, Integer, Object>{
		private String str0,str1,str2,str3,str4,str5,str6,str7,str8;
		@Override
		protected Object doInBackground(String... params) {
			boolean ifExit = DataManager.ifExitDefence(mContext, contact.activeUser+contact.contactId);
			if(ifExit == false){
				DataManager.insertDefence(mContext, contact.activeUser+contact.contactId);
			}else{
				mDefenceArea = DataManager.findDefenceByDefenceID(mContext, contact.activeUser+contact.contactId);
				String defenceID = contact.activeUser+contact.contactId;
				str0 = mDefenceArea.get(defenceID+"00");
				str1 = mDefenceArea.get(defenceID+"01");
				str2 = mDefenceArea.get(defenceID+"02");
				str3 = mDefenceArea.get(defenceID+"03");
				str4 = mDefenceArea.get(defenceID+"04");
				str5 = mDefenceArea.get(defenceID+"05");
				str6 = mDefenceArea.get(defenceID+"06");
				str7 = mDefenceArea.get(defenceID+"07");
				str8 = mDefenceArea.get(defenceID+"08");
				return null;
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			remote.setText(str0);
			hall.setText(str1);
			window.setText(str2);
			balcony.setText(str3);
			bedroom.setText(str4);
			kitchen.setText(str5);
			courtyard.setText(str6);
			door_lock.setText(str7);
			other.setText(str8);
		}
	}
	
	public void initComponent(View view){
		change_defence_area1 = (RelativeLayout) view.findViewById(R.id.change_defence_area1);
		defence_area_content1 = (LinearLayout) view.findViewById(R.id.defence_area_content1);
		progressBar_defence_area1 = (ProgressBar) view.findViewById(R.id.progressBar_defence_area1);
		
		change_defence_area2 = (RelativeLayout) view.findViewById(R.id.change_defence_area2);
		defence_area_content2 = (LinearLayout) view.findViewById(R.id.defence_area_content2);
		progressBar_defence_area2 = (ProgressBar) view.findViewById(R.id.progressBar_defence_area2);
		
		change_defence_area3 = (RelativeLayout) view.findViewById(R.id.change_defence_area3);
		defence_area_content3 = (LinearLayout) view.findViewById(R.id.defence_area_content3);
		progressBar_defence_area3 = (ProgressBar) view.findViewById(R.id.progressBar_defence_area3);
		
		change_defence_area4 = (RelativeLayout) view.findViewById(R.id.change_defence_area4);
		defence_area_content4 = (LinearLayout) view.findViewById(R.id.defence_area_content4);
		progressBar_defence_area4 = (ProgressBar) view.findViewById(R.id.progressBar_defence_area4);
		
		change_defence_area5 = (RelativeLayout) view.findViewById(R.id.change_defence_area5);
		defence_area_content5 = (LinearLayout) view.findViewById(R.id.defence_area_content5);
		progressBar_defence_area5 = (ProgressBar) view.findViewById(R.id.progressBar_defence_area5);
		
		change_defence_area6 = (RelativeLayout) view.findViewById(R.id.change_defence_area6);
		defence_area_content6 = (LinearLayout) view.findViewById(R.id.defence_area_content6);
		progressBar_defence_area6 = (ProgressBar) view.findViewById(R.id.progressBar_defence_area6);
		
		change_defence_area7 = (RelativeLayout) view.findViewById(R.id.change_defence_area7);
		defence_area_content7 = (LinearLayout) view.findViewById(R.id.defence_area_content7);
		progressBar_defence_area7 = (ProgressBar) view.findViewById(R.id.progressBar_defence_area7);
		
		change_defence_area8 = (RelativeLayout) view.findViewById(R.id.change_defence_area8);
		defence_area_content8 = (LinearLayout) view.findViewById(R.id.defence_area_content8);
		progressBar_defence_area8 = (ProgressBar) view.findViewById(R.id.progressBar_defence_area8);
		
		change_defence_area9 = (RelativeLayout) view.findViewById(R.id.change_defence_area9);
		defence_area_content9 = (LinearLayout) view.findViewById(R.id.defence_area_content9);
		progressBar_defence_area9 = (ProgressBar) view.findViewById(R.id.progressBar_defence_area9);
		
		btn = (ImageButton) view.findViewById(R.id.btn1);
		btn1 = (ImageButton) view.findViewById(R.id.btn2);
		btn2 = (ImageButton) view.findViewById(R.id.btn3);
		btn3 = (ImageButton) view.findViewById(R.id.btn4);
		btn4 = (ImageButton) view.findViewById(R.id.btn5);
		btn5 = (ImageButton) view.findViewById(R.id.btn6);
		btn6 = (ImageButton) view.findViewById(R.id.btn7);
		btn7 = (ImageButton) view.findViewById(R.id.btn8);
		btn8 = (ImageButton) view.findViewById(R.id.btn9);
		btn.setOnClickListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btn6.setOnClickListener(this);
		btn7.setOnClickListener(this);
		btn8.setOnClickListener(this);
		
		remote = (TextView) view.findViewById(R.id.remote);
		hall = (TextView) view.findViewById(R.id.hall);
		window = (TextView) view.findViewById(R.id.window);
		balcony = (TextView) view.findViewById(R.id.balcony);
		bedroom = (TextView) view.findViewById(R.id.bedroom);
		kitchen = (TextView) view.findViewById(R.id.kitchen);
		courtyard = (TextView) view.findViewById(R.id.courtyard);
		door_lock = (TextView) view.findViewById(R.id.door_lock);
		other = (TextView) view.findViewById(R.id.other);
		
		one1 = (RelativeLayout) view.findViewById(R.id.one1);
		one2 = (RelativeLayout) view.findViewById(R.id.one2);
		one3 = (RelativeLayout) view.findViewById(R.id.one3);
		one4 = (RelativeLayout) view.findViewById(R.id.one4);
		one5 = (RelativeLayout) view.findViewById(R.id.one5);
		one6 = (RelativeLayout) view.findViewById(R.id.one6);
		one7 = (RelativeLayout) view.findViewById(R.id.one7);
		one8 = (RelativeLayout) view.findViewById(R.id.one8);
		
		two1 = (RelativeLayout) view.findViewById(R.id.two1);
		two2 = (RelativeLayout) view.findViewById(R.id.two2);
		two3 = (RelativeLayout) view.findViewById(R.id.two3);
		two4 = (RelativeLayout) view.findViewById(R.id.two4);
		two5 = (RelativeLayout) view.findViewById(R.id.two5);
		two6 = (RelativeLayout) view.findViewById(R.id.two6);
		two7 = (RelativeLayout) view.findViewById(R.id.two7);
		two8 = (RelativeLayout) view.findViewById(R.id.two8);
		
		three1 = (RelativeLayout) view.findViewById(R.id.three1);
		three2 = (RelativeLayout) view.findViewById(R.id.three2);
		three3 = (RelativeLayout) view.findViewById(R.id.three3);
		three4 = (RelativeLayout) view.findViewById(R.id.three4);
		three5 = (RelativeLayout) view.findViewById(R.id.three5);
		three6 = (RelativeLayout) view.findViewById(R.id.three6);
		three7 = (RelativeLayout) view.findViewById(R.id.three7);
		three8 = (RelativeLayout) view.findViewById(R.id.three8);
		
		four1 = (RelativeLayout) view.findViewById(R.id.four1);
		four2 = (RelativeLayout) view.findViewById(R.id.four2);
		four3 = (RelativeLayout) view.findViewById(R.id.four3);
		four4 = (RelativeLayout) view.findViewById(R.id.four4);
		four5 = (RelativeLayout) view.findViewById(R.id.four5);
		four6 = (RelativeLayout) view.findViewById(R.id.four6);
		four7 = (RelativeLayout) view.findViewById(R.id.four7);
		four8 = (RelativeLayout) view.findViewById(R.id.four8);
		
		five1 = (RelativeLayout) view.findViewById(R.id.five1);
		five2 = (RelativeLayout) view.findViewById(R.id.five2);
		five3 = (RelativeLayout) view.findViewById(R.id.five3);
		five4 = (RelativeLayout) view.findViewById(R.id.five4);
		five5 = (RelativeLayout) view.findViewById(R.id.five5);
		five6 = (RelativeLayout) view.findViewById(R.id.five6);
		five7 = (RelativeLayout) view.findViewById(R.id.five7);
		five8 = (RelativeLayout) view.findViewById(R.id.five8);
		
		six1 = (RelativeLayout) view.findViewById(R.id.six1);
		six2 = (RelativeLayout) view.findViewById(R.id.six2);
		six3 = (RelativeLayout) view.findViewById(R.id.six3);
		six4 = (RelativeLayout) view.findViewById(R.id.six4);
		six5 = (RelativeLayout) view.findViewById(R.id.six5);
		six6 = (RelativeLayout) view.findViewById(R.id.six6);
		six7 = (RelativeLayout) view.findViewById(R.id.six7);
		six8 = (RelativeLayout) view.findViewById(R.id.six8);
		
		seven1 = (RelativeLayout) view.findViewById(R.id.seven1);
		seven2 = (RelativeLayout) view.findViewById(R.id.seven2);
		seven3 = (RelativeLayout) view.findViewById(R.id.seven3);
		seven4 = (RelativeLayout) view.findViewById(R.id.seven4);
		seven5 = (RelativeLayout) view.findViewById(R.id.seven5);
		seven6 = (RelativeLayout) view.findViewById(R.id.seven6);
		seven7 = (RelativeLayout) view.findViewById(R.id.seven7);
		seven8 = (RelativeLayout) view.findViewById(R.id.seven8);
		
		eight1 = (RelativeLayout) view.findViewById(R.id.eight1);
		eight2 = (RelativeLayout) view.findViewById(R.id.eight2);
		eight3 = (RelativeLayout) view.findViewById(R.id.eight3);
		eight4 = (RelativeLayout) view.findViewById(R.id.eight4);
		eight5 = (RelativeLayout) view.findViewById(R.id.eight5);
		eight6 = (RelativeLayout) view.findViewById(R.id.eight6);
		eight7 = (RelativeLayout) view.findViewById(R.id.eight7);
		eight8 = (RelativeLayout) view.findViewById(R.id.eight8);
		
		nine1 = (RelativeLayout) view.findViewById(R.id.nine1);
		nine2 = (RelativeLayout) view.findViewById(R.id.nine2);
		nine3 = (RelativeLayout) view.findViewById(R.id.nine3);
		nine4 = (RelativeLayout) view.findViewById(R.id.nine4);
		nine5 = (RelativeLayout) view.findViewById(R.id.nine5);
		nine6 = (RelativeLayout) view.findViewById(R.id.nine6);
		nine7 = (RelativeLayout) view.findViewById(R.id.nine7);
		nine8 = (RelativeLayout) view.findViewById(R.id.nine8);
		
		switch_one1=(ImageView)view.findViewById(R.id.switch_one1);
		switch_one2=(ImageView)view.findViewById(R.id.switch_one2);
		switch_one3=(ImageView)view.findViewById(R.id.switch_one3);
		switch_one4=(ImageView)view.findViewById(R.id.switch_one4);
		switch_one5=(ImageView)view.findViewById(R.id.switch_one5);
		switch_one6=(ImageView)view.findViewById(R.id.switch_one6);
		switch_one7=(ImageView)view.findViewById(R.id.switch_one7);
		switch_one8=(ImageView)view.findViewById(R.id.switch_one8);
		
		switch_two1=(ImageView)view.findViewById(R.id.switch_two1);
		switch_two2=(ImageView)view.findViewById(R.id.switch_two2);
		switch_two3=(ImageView)view.findViewById(R.id.switch_two3);
		switch_two4=(ImageView)view.findViewById(R.id.switch_two4);
		switch_two5=(ImageView)view.findViewById(R.id.switch_two5);
		switch_two6=(ImageView)view.findViewById(R.id.switch_two6);
		switch_two7=(ImageView)view.findViewById(R.id.switch_two7);
		switch_two8=(ImageView)view.findViewById(R.id.switch_two8);
		
		switch_three1=(ImageView)view.findViewById(R.id.switch_three1);
		switch_three2=(ImageView)view.findViewById(R.id.switch_three2);
		switch_three3=(ImageView)view.findViewById(R.id.switch_three3);
		switch_three4=(ImageView)view.findViewById(R.id.switch_three4);
		switch_three5=(ImageView)view.findViewById(R.id.switch_three5);
		switch_three6=(ImageView)view.findViewById(R.id.switch_three6);
		switch_three7=(ImageView)view.findViewById(R.id.switch_three7);
		switch_three8=(ImageView)view.findViewById(R.id.switch_three8);
		
		switch_four1=(ImageView)view.findViewById(R.id.switch_four1);
		switch_four2=(ImageView)view.findViewById(R.id.switch_four2);
		switch_four3=(ImageView)view.findViewById(R.id.switch_four3);
		switch_four4=(ImageView)view.findViewById(R.id.switch_four4);
		switch_four5=(ImageView)view.findViewById(R.id.switch_four5);
		switch_four6=(ImageView)view.findViewById(R.id.switch_four6);
		switch_four7=(ImageView)view.findViewById(R.id.switch_four7);
		switch_four8=(ImageView)view.findViewById(R.id.switch_four8);
		
		switch_five1=(ImageView)view.findViewById(R.id.switch_five1);
		switch_five2=(ImageView)view.findViewById(R.id.switch_five2);
		switch_five3=(ImageView)view.findViewById(R.id.switch_five3);
		switch_five4=(ImageView)view.findViewById(R.id.switch_five4);
		switch_five5=(ImageView)view.findViewById(R.id.switch_five5);
		switch_five6=(ImageView)view.findViewById(R.id.switch_five6);
		switch_five7=(ImageView)view.findViewById(R.id.switch_five7);
		switch_five8=(ImageView)view.findViewById(R.id.switch_five8);
		
		switch_six1=(ImageView)view.findViewById(R.id.switch_six1);
		switch_six2=(ImageView)view.findViewById(R.id.switch_six2);
		switch_six3=(ImageView)view.findViewById(R.id.switch_six3);
		switch_six4=(ImageView)view.findViewById(R.id.switch_six4);
		switch_six5=(ImageView)view.findViewById(R.id.switch_six5);
		switch_six6=(ImageView)view.findViewById(R.id.switch_six6);
		switch_six7=(ImageView)view.findViewById(R.id.switch_six7);
		switch_six8=(ImageView)view.findViewById(R.id.switch_six8);
		
		switch_seven1=(ImageView)view.findViewById(R.id.switch_seven1);
		switch_seven2=(ImageView)view.findViewById(R.id.switch_seven2);
		switch_seven3=(ImageView)view.findViewById(R.id.switch_seven3);
		switch_seven4=(ImageView)view.findViewById(R.id.switch_seven4);
		switch_seven5=(ImageView)view.findViewById(R.id.switch_seven5);
		switch_seven6=(ImageView)view.findViewById(R.id.switch_seven6);
		switch_seven7=(ImageView)view.findViewById(R.id.switch_seven7);
		switch_seven8=(ImageView)view.findViewById(R.id.switch_seven8);
		
		switch_eight1=(ImageView)view.findViewById(R.id.switch_eight1);
		switch_eight2=(ImageView)view.findViewById(R.id.switch_eight2);
		switch_eight3=(ImageView)view.findViewById(R.id.switch_eight3);
		switch_eight4=(ImageView)view.findViewById(R.id.switch_eight4);
		switch_eight5=(ImageView)view.findViewById(R.id.switch_eight5);
		switch_eight6=(ImageView)view.findViewById(R.id.switch_eight6);
		switch_eight7=(ImageView)view.findViewById(R.id.switch_eight7);
		switch_eight8=(ImageView)view.findViewById(R.id.switch_eight8);
		
		switch_nine1=(ImageView)view.findViewById(R.id.switch_nine1);
		switch_nine2=(ImageView)view.findViewById(R.id.switch_nine2);
		switch_nine3=(ImageView)view.findViewById(R.id.switch_nine3);
		switch_nine4=(ImageView)view.findViewById(R.id.switch_nine4);
		switch_nine5=(ImageView)view.findViewById(R.id.switch_nine5);
		switch_nine6=(ImageView)view.findViewById(R.id.switch_nine6);
		switch_nine7=(ImageView)view.findViewById(R.id.switch_nine7);
		switch_nine8=(ImageView)view.findViewById(R.id.switch_nine8);
		
		clear_one1=(ImageView)view.findViewById(R.id.clear_one1);
		clear_one2=(ImageView)view.findViewById(R.id.clear_one2);
		clear_one3=(ImageView)view.findViewById(R.id.clear_one3);
		clear_one4=(ImageView)view.findViewById(R.id.clear_one4);
		clear_one5=(ImageView)view.findViewById(R.id.clear_one5);
		clear_one6=(ImageView)view.findViewById(R.id.clear_one6);
		clear_one7=(ImageView)view.findViewById(R.id.clear_one7);
		clear_one8=(ImageView)view.findViewById(R.id.clear_one8);
		
		clear_two1=(ImageView)view.findViewById(R.id.clear_two1);
		clear_two2=(ImageView)view.findViewById(R.id.clear_two2);
		clear_two3=(ImageView)view.findViewById(R.id.clear_two3);
		clear_two4=(ImageView)view.findViewById(R.id.clear_two4);
		clear_two5=(ImageView)view.findViewById(R.id.clear_two5);
		clear_two6=(ImageView)view.findViewById(R.id.clear_two6);
		clear_two7=(ImageView)view.findViewById(R.id.clear_two7);
		clear_two8=(ImageView)view.findViewById(R.id.clear_two8);
		
		clear_three1=(ImageView)view.findViewById(R.id.clear_three1);
		clear_three2=(ImageView)view.findViewById(R.id.clear_three2);
		clear_three3=(ImageView)view.findViewById(R.id.clear_three3);
		clear_three4=(ImageView)view.findViewById(R.id.clear_three4);
		clear_three5=(ImageView)view.findViewById(R.id.clear_three5);
		clear_three6=(ImageView)view.findViewById(R.id.clear_three6);
		clear_three7=(ImageView)view.findViewById(R.id.clear_three7);
		clear_three8=(ImageView)view.findViewById(R.id.clear_three8);
		
		clear_four1=(ImageView)view.findViewById(R.id.clear_four1);
		clear_four2=(ImageView)view.findViewById(R.id.clear_four2);
		clear_four3=(ImageView)view.findViewById(R.id.clear_four3);
		clear_four4=(ImageView)view.findViewById(R.id.clear_four4);
		clear_four5=(ImageView)view.findViewById(R.id.clear_four5);
		clear_four6=(ImageView)view.findViewById(R.id.clear_four6);
		clear_four7=(ImageView)view.findViewById(R.id.clear_four7);
		clear_four8=(ImageView)view.findViewById(R.id.clear_four8);
		
		clear_five1=(ImageView)view.findViewById(R.id.clear_five1);
		clear_five2=(ImageView)view.findViewById(R.id.clear_five2);
		clear_five3=(ImageView)view.findViewById(R.id.clear_five3);
		clear_five4=(ImageView)view.findViewById(R.id.clear_five4);
		clear_five5=(ImageView)view.findViewById(R.id.clear_five5);
		clear_five6=(ImageView)view.findViewById(R.id.clear_five6);
		clear_five7=(ImageView)view.findViewById(R.id.clear_five7);
		clear_five8=(ImageView)view.findViewById(R.id.clear_five8);
		
		clear_six1=(ImageView)view.findViewById(R.id.clear_six1);
		clear_six2=(ImageView)view.findViewById(R.id.clear_six2);
		clear_six3=(ImageView)view.findViewById(R.id.clear_six3);
		clear_six4=(ImageView)view.findViewById(R.id.clear_six4);
		clear_six5=(ImageView)view.findViewById(R.id.clear_six5);
		clear_six6=(ImageView)view.findViewById(R.id.clear_six6);
		clear_six7=(ImageView)view.findViewById(R.id.clear_six7);
		clear_six8=(ImageView)view.findViewById(R.id.clear_six8);
		
		clear_seven1=(ImageView)view.findViewById(R.id.clear_seven1);
		clear_seven2=(ImageView)view.findViewById(R.id.clear_seven2);
		clear_seven3=(ImageView)view.findViewById(R.id.clear_seven3);
		clear_seven4=(ImageView)view.findViewById(R.id.clear_seven4);
		clear_seven5=(ImageView)view.findViewById(R.id.clear_seven5);
		clear_seven6=(ImageView)view.findViewById(R.id.clear_seven6);
		clear_seven7=(ImageView)view.findViewById(R.id.clear_seven7);
		clear_seven8=(ImageView)view.findViewById(R.id.clear_seven8);
		
		clear_eight1=(ImageView)view.findViewById(R.id.clear_eight1);
		clear_eight2=(ImageView)view.findViewById(R.id.clear_eight2);
		clear_eight3=(ImageView)view.findViewById(R.id.clear_eight3);
		clear_eight4=(ImageView)view.findViewById(R.id.clear_eight4);
		clear_eight5=(ImageView)view.findViewById(R.id.clear_eight5);
		clear_eight6=(ImageView)view.findViewById(R.id.clear_eight6);
		clear_eight7=(ImageView)view.findViewById(R.id.clear_eight7);
		clear_eight8=(ImageView)view.findViewById(R.id.clear_eight8);
		
		clear_nine1=(ImageView)view.findViewById(R.id.clear_nine1);
		clear_nine2=(ImageView)view.findViewById(R.id.clear_nine2);
		clear_nine3=(ImageView)view.findViewById(R.id.clear_nine3);
		clear_nine4=(ImageView)view.findViewById(R.id.clear_nine4);
		clear_nine5=(ImageView)view.findViewById(R.id.clear_nine5);
		clear_nine6=(ImageView)view.findViewById(R.id.clear_nine6);
		clear_nine7=(ImageView)view.findViewById(R.id.clear_nine7);
		clear_nine8=(ImageView)view.findViewById(R.id.clear_nine8);
		
		pre_two1=(ProgressBar)view.findViewById(R.id.pre_two1);
		pre_two2=(ProgressBar)view.findViewById(R.id.pre_two2);
		pre_two3=(ProgressBar)view.findViewById(R.id.pre_two3);
		pre_two4=(ProgressBar)view.findViewById(R.id.pre_two4);
		pre_two5=(ProgressBar)view.findViewById(R.id.pre_two5);
		pre_two6=(ProgressBar)view.findViewById(R.id.pre_two6);
		pre_two7=(ProgressBar)view.findViewById(R.id.pre_two7);
		pre_two8=(ProgressBar)view.findViewById(R.id.pre_two8);
		
		pre_three1=(ProgressBar)view.findViewById(R.id.pre_three1);
		pre_three2=(ProgressBar)view.findViewById(R.id.pre_three2);
		pre_three3=(ProgressBar)view.findViewById(R.id.pre_three3);
		pre_three4=(ProgressBar)view.findViewById(R.id.pre_three4);
		pre_three5=(ProgressBar)view.findViewById(R.id.pre_three5);
		pre_three6=(ProgressBar)view.findViewById(R.id.pre_three6);
		pre_three7=(ProgressBar)view.findViewById(R.id.pre_three7);
		pre_three8=(ProgressBar)view.findViewById(R.id.pre_three8);
		
		pre_four1=(ProgressBar)view.findViewById(R.id.pre_four1);
		pre_four2=(ProgressBar)view.findViewById(R.id.pre_four2);
		pre_four3=(ProgressBar)view.findViewById(R.id.pre_four3);
		pre_four4=(ProgressBar)view.findViewById(R.id.pre_four4);
		pre_four5=(ProgressBar)view.findViewById(R.id.pre_four5);
		pre_four6=(ProgressBar)view.findViewById(R.id.pre_four6);
		pre_four7=(ProgressBar)view.findViewById(R.id.pre_four7);
		pre_four8=(ProgressBar)view.findViewById(R.id.pre_four8);
		
		pre_five1=(ProgressBar)view.findViewById(R.id.pre_five1);
		pre_five2=(ProgressBar)view.findViewById(R.id.pre_five2);
		pre_five3=(ProgressBar)view.findViewById(R.id.pre_five3);
		pre_five4=(ProgressBar)view.findViewById(R.id.pre_five4);
		pre_five5=(ProgressBar)view.findViewById(R.id.pre_five5);
		pre_five6=(ProgressBar)view.findViewById(R.id.pre_five6);
		pre_five7=(ProgressBar)view.findViewById(R.id.pre_five7);
		pre_five8=(ProgressBar)view.findViewById(R.id.pre_five8);
		
		pre_six1=(ProgressBar)view.findViewById(R.id.pre_six1);
		pre_six2=(ProgressBar)view.findViewById(R.id.pre_six2);
		pre_six3=(ProgressBar)view.findViewById(R.id.pre_six3);
		pre_six4=(ProgressBar)view.findViewById(R.id.pre_six4);
		pre_six5=(ProgressBar)view.findViewById(R.id.pre_six5);
		pre_six6=(ProgressBar)view.findViewById(R.id.pre_six6);
		pre_six7=(ProgressBar)view.findViewById(R.id.pre_six7);
		pre_six8=(ProgressBar)view.findViewById(R.id.pre_six8);
		
		pre_seven1=(ProgressBar)view.findViewById(R.id.pre_seven1);
		pre_seven2=(ProgressBar)view.findViewById(R.id.pre_seven2);
		pre_seven3=(ProgressBar)view.findViewById(R.id.pre_seven3);
		pre_seven4=(ProgressBar)view.findViewById(R.id.pre_seven4);
		pre_seven5=(ProgressBar)view.findViewById(R.id.pre_seven5);
		pre_seven6=(ProgressBar)view.findViewById(R.id.pre_seven6);
		pre_seven7=(ProgressBar)view.findViewById(R.id.pre_seven7);
		pre_seven8=(ProgressBar)view.findViewById(R.id.pre_seven8);
		
		pre_eight1=(ProgressBar)view.findViewById(R.id.pre_eight1);
		pre_eight2=(ProgressBar)view.findViewById(R.id.pre_eight2);
		pre_eight3=(ProgressBar)view.findViewById(R.id.pre_eight3);
		pre_eight4=(ProgressBar)view.findViewById(R.id.pre_eight4);
		pre_eight5=(ProgressBar)view.findViewById(R.id.pre_eight5);
		pre_eight6=(ProgressBar)view.findViewById(R.id.pre_eight6);
		pre_eight7=(ProgressBar)view.findViewById(R.id.pre_eight7);
		pre_eight8=(ProgressBar)view.findViewById(R.id.pre_eight8);
		
		pre_nine1=(ProgressBar)view.findViewById(R.id.pre_nine1);
		pre_nine2=(ProgressBar)view.findViewById(R.id.pre_nine2);
		pre_nine3=(ProgressBar)view.findViewById(R.id.pre_nine3);
		pre_nine4=(ProgressBar)view.findViewById(R.id.pre_nine4);
		pre_nine5=(ProgressBar)view.findViewById(R.id.pre_nine5);
		pre_nine6=(ProgressBar)view.findViewById(R.id.pre_nine6);
		pre_nine7=(ProgressBar)view.findViewById(R.id.pre_nine7);
		pre_nine8=(ProgressBar)view.findViewById(R.id.pre_nine8);

		text_view_one1 = (TextView) view.findViewById(R.id.text_view_one1);
		text_view_one2 = (TextView) view.findViewById(R.id.text_view_one2);
		text_view_one3 = (TextView) view.findViewById(R.id.text_view_one3);
		text_view_one4 = (TextView) view.findViewById(R.id.text_view_one4);
		text_view_one5 = (TextView) view.findViewById(R.id.text_view_one5);
		text_view_one6 = (TextView) view.findViewById(R.id.text_view_one6);
		text_view_one7 = (TextView) view.findViewById(R.id.text_view_one7);
		text_view_one8 = (TextView) view.findViewById(R.id.text_view_one8);
		
		text_view_two1 = (TextView) view.findViewById(R.id.text_view_two1);
		text_view_two2 = (TextView) view.findViewById(R.id.text_view_two2);
		text_view_two3 = (TextView) view.findViewById(R.id.text_view_two3);
		text_view_two4 = (TextView) view.findViewById(R.id.text_view_two4);
		text_view_two5 = (TextView) view.findViewById(R.id.text_view_two5);
		text_view_two6 = (TextView) view.findViewById(R.id.text_view_two6);
		text_view_two7 = (TextView) view.findViewById(R.id.text_view_two7);
		text_view_two8 = (TextView) view.findViewById(R.id.text_view_two8);
		
		text_view_three1 = (TextView) view.findViewById(R.id.text_view_three1);
		text_view_three2 = (TextView) view.findViewById(R.id.text_view_three2);
		text_view_three3 = (TextView) view.findViewById(R.id.text_view_three3);
		text_view_three4 = (TextView) view.findViewById(R.id.text_view_three4);
		text_view_three5 = (TextView) view.findViewById(R.id.text_view_three5);
		text_view_three6 = (TextView) view.findViewById(R.id.text_view_three6);
		text_view_three7 = (TextView) view.findViewById(R.id.text_view_three7);
		text_view_three8 = (TextView) view.findViewById(R.id.text_view_three8);
		
		text_view_four1 = (TextView) view.findViewById(R.id.text_view_four1);
		text_view_four2 = (TextView) view.findViewById(R.id.text_view_four2);
		text_view_four3 = (TextView) view.findViewById(R.id.text_view_four3);
		text_view_four4 = (TextView) view.findViewById(R.id.text_view_four4);
		text_view_four5 = (TextView) view.findViewById(R.id.text_view_four5);
		text_view_four6 = (TextView) view.findViewById(R.id.text_view_four6);
		text_view_four7 = (TextView) view.findViewById(R.id.text_view_four7);
		text_view_four8 = (TextView) view.findViewById(R.id.text_view_four8);
		
		text_view_five1 = (TextView) view.findViewById(R.id.text_view_five1);
		text_view_five2 = (TextView) view.findViewById(R.id.text_view_five2);
		text_view_five3 = (TextView) view.findViewById(R.id.text_view_five3);
		text_view_five4 = (TextView) view.findViewById(R.id.text_view_five4);
		text_view_five5 = (TextView) view.findViewById(R.id.text_view_five5);
		text_view_five6 = (TextView) view.findViewById(R.id.text_view_five6);
		text_view_five7 = (TextView) view.findViewById(R.id.text_view_five7);
		text_view_five8 = (TextView) view.findViewById(R.id.text_view_five8);
		
		text_view_six1 = (TextView) view.findViewById(R.id.text_view_six1);
		text_view_six2 = (TextView) view.findViewById(R.id.text_view_six2);
		text_view_six3 = (TextView) view.findViewById(R.id.text_view_six3);
		text_view_six4 = (TextView) view.findViewById(R.id.text_view_six4);
		text_view_six5 = (TextView) view.findViewById(R.id.text_view_six5);
		text_view_six6 = (TextView) view.findViewById(R.id.text_view_six6);
		text_view_six7 = (TextView) view.findViewById(R.id.text_view_six7);
		text_view_six8 = (TextView) view.findViewById(R.id.text_view_six8);
		
		text_view_seven1 = (TextView) view.findViewById(R.id.text_view_seven1);
		text_view_seven2 = (TextView) view.findViewById(R.id.text_view_seven2);
		text_view_seven3 = (TextView) view.findViewById(R.id.text_view_seven3);
		text_view_seven4 = (TextView) view.findViewById(R.id.text_view_seven4);
		text_view_seven5 = (TextView) view.findViewById(R.id.text_view_seven5);
		text_view_seven6 = (TextView) view.findViewById(R.id.text_view_seven6);
		text_view_seven7 = (TextView) view.findViewById(R.id.text_view_seven7);
		text_view_seven8 = (TextView) view.findViewById(R.id.text_view_seven8);
		
		text_view_eigth1 = (TextView) view.findViewById(R.id.text_view_eigth1);
		text_view_eigth2 = (TextView) view.findViewById(R.id.text_view_eigth2);
		text_view_eigth3 = (TextView) view.findViewById(R.id.text_view_eigth3);
		text_view_eigth4 = (TextView) view.findViewById(R.id.text_view_eigth4);
		text_view_eigth5 = (TextView) view.findViewById(R.id.text_view_eigth5);
		text_view_eigth6 = (TextView) view.findViewById(R.id.text_view_eigth6);
		text_view_eigth7 = (TextView) view.findViewById(R.id.text_view_eigth7);
		text_view_eigth8 = (TextView) view.findViewById(R.id.text_view_eigth8);
		
		text_view_nine1 = (TextView) view.findViewById(R.id.text_view_nine1);
		text_view_nine2 = (TextView) view.findViewById(R.id.text_view_nine2);
		text_view_nine3 = (TextView) view.findViewById(R.id.text_view_nine3);
		text_view_nine4 = (TextView) view.findViewById(R.id.text_view_nine4);
		text_view_nine5 = (TextView) view.findViewById(R.id.text_view_nine5);
		text_view_nine6 = (TextView) view.findViewById(R.id.text_view_nine6);
		text_view_nine7 = (TextView) view.findViewById(R.id.text_view_nine7);
		text_view_nine8 = (TextView) view.findViewById(R.id.text_view_nine8);
	}
	
	public void regFilter(){
		IntentFilter filter = new IntentFilter();
		
		filter.addAction(Constants.P2P.ACK_RET_SET_DEFENCE_AREA);
		filter.addAction(Constants.P2P.ACK_RET_GET_DEFENCE_AREA);
		filter.addAction(Constants.P2P.ACK_RET_CLEAR_DEFENCE_AREA);
		filter.addAction(Constants.P2P.RET_CLEAR_DEFENCE_AREA);
		filter.addAction(Constants.P2P.RET_SET_DEFENCE_AREA);
		filter.addAction(Constants.P2P.RET_GET_DEFENCE_AREA);
		filter.addAction(Constants.P2P.RET_DEVICE_NOT_SUPPORT);
		filter.addAction(Constants.P2P.ACK_RET_GET_SENSOR_SWITCH);
		filter.addAction(Constants.P2P.ACK_RET_SET_SENSOR_SWITCH);
		filter.addAction(Constants.P2P.RET_GET_SENSOR_SWITCH);
		filter.addAction(Constants.P2P.RET_SET_SENSOR_SWITCH);
		
		mContext.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}
	
	private void setViewVisibility(){
		btn.setVisibility(View.VISIBLE);
		btn1.setVisibility(View.VISIBLE);
		btn2.setVisibility(View.VISIBLE);
		btn3.setVisibility(View.VISIBLE);
		btn4.setVisibility(View.VISIBLE);
		btn5.setVisibility(View.VISIBLE);
		btn6.setVisibility(View.VISIBLE);
		btn7.setVisibility(View.VISIBLE);
		btn8.setVisibility(View.VISIBLE);
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if(intent.getAction().equals(Constants.P2P.RET_GET_DEFENCE_AREA)){
				ArrayList<int[]> data = (ArrayList<int[]>) intent.getSerializableExtra("data");
				initData(data);
				showDefence_area1();
				setViewVisibility();
			}else if(intent.getAction().equals(Constants.P2P.RET_SET_DEFENCE_AREA)){
				if(null!=dialog_loading){
					dialog_loading.dismiss();
					dialog_loading = null;
				}
				int result = intent.getIntExtra("result", -1);
				
				if(result==Constants.P2P_SET.DEFENCE_AREA_SET.SETTING_SUCCESS){
					String defenceID = "0"+areaID+""+"00"+channelID+"";
					if(current_type == Constants.P2P_SET.DEFENCE_AREA_SET.DEFENCE_AREA_TYPE_CLEAR){
						grayButton(current_group,current_item);
						T.showShort(mContext, R.string.clear_success);
						System.out.println("clear_success1.....1");
						getDefence(contact.activeUser,contact.contactId,"0"+aID+"00"+cID,"",-1);
					}else{
						lightButton(current_group,current_item);
						T.showShort(mContext, R.string.learning_success);
						//学习成功返回值，在此处处理数据库
						getDefence(contact.activeUser,contact.contactId,defenceID,alarmType,-1);
						alarmType = null;
						if(null!=yuzhiweiNum&&yuzhiweiNum.length()>0&&!yuzhiweiNum.equals("none")){
							byte bPresetNum = (byte) (Integer.parseInt(yuzhiweiNum)-1);
							byte[] datas = new byte[7];
							datas[0] = 90;
							datas[1] = 0;
							datas[2] = 1;
							datas[3] = 0;
							datas[4] = (byte) (areaID-1);
							datas[5] = (byte) channelID;
							datas[6] = bPresetNum;
							P2PHandler.getInstance().sMesgSetAlarmPresetMotorPos(contact.contactId,contact.contactPassword,datas);
							yuzhiweiNum = null;
						}
					}
				}else if(result==30){
					grayButton(current_group,current_item);
					T.showShort(mContext, R.string.clear_success);
				}else if(result==32){
					int group = intent.getIntExtra("group", -1);
					int item = intent.getIntExtra("item", -1);
					Log.e("my","group:"+group+" item:"+item);
					System.out.println("group="+group);
					System.out.println("item="+item);
					T.showShort(mContext,
							Utils.getDefenceAreaByGroup(mContext, group)+
							":"+(item+1)+" "+
							mContext.getResources().getString(R.string.channel)+" "+
							mContext.getResources().getString(R.string.has_been_learning)
							);
				}else if(result==41){
					Intent back = new Intent();
					back.setAction(Constants.Action.REPLACE_MAIN_CONTROL);
					mContext.sendBroadcast(back);
					T.showShort(mContext, R.string.device_unsupport_defence_area);
				}else{
					T.showShort(mContext, R.string.operator_error);
				}
			}else if(intent.getAction().equals(Constants.P2P.RET_CLEAR_DEFENCE_AREA)){
				if(null!=dialog_loading){
					dialog_loading.dismiss();
					dialog_loading = null;
				}
				int result = intent.getIntExtra("result", -1);
				if(result==0){
					grayButton(current_group,0);
					grayButton(current_group,1);
					grayButton(current_group,2);
					grayButton(current_group,3);
					grayButton(current_group,4);
					grayButton(current_group,5);
					grayButton(current_group,6);
					grayButton(current_group,7);
					T.showShort(mContext, R.string.clear_success);
					String defenceId = "0"+current_group+"";
					System.out.println("clear_success1.....2");
					getDefence(contact.activeUser,contact.contactId,defenceId,"",current_group);
				}else{
					T.showShort(mContext, R.string.operator_error);
				}
			}else if(intent.getAction().equals(Constants.P2P.RET_DEVICE_NOT_SUPPORT)){
				if(null!=dialog_loading){
					dialog_loading.dismiss();
					dialog_loading = null;
				}
				isSurpport=false;
//				T.showShort(mContext, R.string.not_surpport_sensor);
			}else if(intent.getAction().equals(Constants.P2P.ACK_RET_GET_DEFENCE_AREA)){
				int result = intent.getIntExtra("result", -1);
				if(result==Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR){
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					mContext.sendBroadcast(i);
				}else if(result==Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR){
					Log.e("my","net error resend:get defence area");
					P2PHandler.getInstance().getDefenceArea(contact.contactId, contact.contactPassword);
				}
			}else if(intent.getAction().equals(Constants.P2P.ACK_RET_SET_DEFENCE_AREA)){
				int result = intent.getIntExtra("result", -1);
				if(result==Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR){
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					mContext.sendBroadcast(i);
				}else if(result==Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR){
					Log.e("my","net error resend:set defence area");
					P2PHandler.getInstance().setDefenceAreaState(contact.contactId, contact.contactPassword, current_group, current_item ,current_type);
				}
			}else if(intent.getAction().equals(Constants.P2P.ACK_RET_CLEAR_DEFENCE_AREA)){
				int result = intent.getIntExtra("result", -1);
				if(result==Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR){
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					mContext.sendBroadcast(i);
				}else if(result==Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR){
					Log.e("my","net error resend:clear defence area");
					P2PHandler.getInstance().clearDefenceAreaState(contact.contactId, contact.contactPassword, current_group);
				}
			}else if(intent.getAction().equals(Constants.P2P.ACK_RET_GET_SENSOR_SWITCH)){
				int result=intent.getIntExtra("result", -1);
				if(result==Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR){
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					mContext.sendBroadcast(i);
				}else if(result==Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR){
					Log.e("my","net error resend:set defence area");
					P2PHandler.getInstance().getDefenceAreaAlarmSwitch(contact.contactId, contact.contactPassword);
				}
			}else if(intent.getAction().equals(Constants.P2P.ACK_RET_SET_SENSOR_SWITCH)){
				int result=intent.getIntExtra("result", -1);
				if(result==Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR){
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					mContext.sendBroadcast(i);
				}else if(result==Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR){
					Log.e("my","net error resend:set defence area");
					P2PHandler.getInstance().setDefenceAreaAlarmSwitch(contact.contactId, contact.contactPassword, currentSwitch,currentgroup, currentitem);
				}
			}else if(intent.getAction().equals(Constants.P2P.RET_GET_SENSOR_SWITCH)){
				int result=intent.getIntExtra("result", -1);
				ArrayList<int[]> sensors=(ArrayList<int[]>) intent.getSerializableExtra("data");
				if(result==1){
					initSensorSwitch(sensors);
				}else if(result==41){
//					T.showShort(mContext, R.string.device_unsupport_defence_area);
				}
			}else if(intent.getAction().equals(Constants.P2P.RET_SET_SENSOR_SWITCH)){
				int result=intent.getIntExtra("result", -1);
				if(result==0){
					setgraySwitch(currentgroup+1, currentitem, currentSwitch);
				}else if(result==41){
//					T.showShort(mContext, R.string.device_unsupport_defence_area);
				}else{
					
				}
			}
		}
	};
	
	public void initData(ArrayList<int[]> data){
		System.out.println("data==="+data.size());
		for(int i=0;i<data.size();i++){
			int[] status = data.get(i);
			for(int j=0;j<status.length;j++){
				
				if(status[j]==1){
					grayButton(i,j);
				}else{
					System.out.println("iii=="+i);
					System.out.println("jjj=="+j);
					lightButton(i,j);
				}
			}
		}
	}

	public void lightButton(final int i,final int j){
		RelativeLayout item = this.getKeyBoard(i, j);
		ImageView clear_item=this.getClear(i, j);
		ImageView  switch_item=this.getSwitch(i, j);
		final TextView txtView = this.getTextView(i,j);
		if(null!=item){
//			item.setLongClickable(true);
//			item.setOnLongClickListener(new OnLongClickListener() {
//				
//				@Override
//				public boolean onLongClick(View v) {
//					String str = txtView.getText().toString().trim();
//					updateDefenceChannel(mContext,j,contact.activeUser,contact.contactId,str,i+"",txtView);
//					return true;
//				}
//			});
//			item.setClickable(false);
		    clear_item.setVisibility(ImageView.VISIBLE);
			if(i!=0&&isSurpport==true){
				switch_item.setVisibility(ImageView.VISIBLE);
			}
		    clear_item.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					clear(i,j,txtView);
				}
			});
//			item.setBackgroundResource(R.drawable.button_bg_dialog_ok);
//			item.setTextColor(mContext.getResources().getColor(R.color.text_color_black));
		}
	}
	
	public void grayButton(final int i,final int j){
		RelativeLayout item = this.getKeyBoard(i, j);
		ImageView clear_item=this.getClear(i, j);
		final ImageView switch_item=this.getSwitch(i, j);
		final TextView txtView = this.getTextView(i,j);
		if(null!=item){
			item.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					System.out.println("i="+i+"jj="+j);
					channelID = j;
					areaID = i;
					channelName = txtView.getText().toString().trim();
					study(i,j,txtView);
					
//					P2PHandler.getInstance().getDefenceAreaAlarmSwitch(contact.contactId, contact.contactPassword);
				}
			});
			
			item.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					String str = txtView.getText().toString().trim();
					updateDefenceChannel(mContext,j,contact.activeUser,contact.contactId,str,i+"",txtView);
					return true;
				}
			});
			item.setClickable(true);
			if(i!=0){
				switch_item.setVisibility(ImageView.INVISIBLE);
			}
			clear_item.setVisibility(ImageView.GONE);	
//			item.setBackgroundResource(R.drawable.button_bg_dialog_cancel);
//			item.setTextColor(mContext.getResources().getColor(R.color.text_color_gray));
		}
	}
	
	private void updateDefenceChannel(final Context context,final int channelID,final String userID,final String cameraID,String textStr,final String areaID,final TextView mTextView){
		LayoutInflater layoutInflater = LayoutInflater.from(context);   
        View myLoginView = layoutInflater.inflate(R.layout.defence_alertdialog, null);      
        editTextDialog = (EditText) myLoginView.findViewById(R.id.edit_dialog);
        editTextDialog.setText(textStr);
        Dialog alertDialog = new AlertDialog.Builder(context)
        		.setView(myLoginView)
        		.setPositiveButton("修改", new DialogInterface.OnClickListener() {   
                    @Override   
                    public void onClick(DialogInterface dialog, int which) {   
                    	String dialogStr = editTextDialog.getText().toString().trim();
                    	String defenceID = "0"+areaID+"00"+channelID+"";
                    	updateDefenceName(userID,cameraID,mContext,defenceID,dialogStr);
                        mTextView.setText(dialogStr);
                    }   
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {   
   
                    @Override   
                    public void onClick(DialogInterface dialog, int which) {     
                    }   
                }).   
                create();   
        alertDialog.show();   
				 
	}
	
	public Handler mHandler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			
			switch(msg.what){
			case EXPAND_OR_SHRINK:
				int group1 = msg.arg1;
				int length = msg.arg2;
				LinearLayout item = getContent(group1);
				LinearLayout.LayoutParams params = (LayoutParams) item.getLayoutParams();
				params.height = length;
				item.setLayoutParams(params);
				break;
			case END_EXPAND_OR_SHRINK:
				int group2 = msg.arg1;
				if(group2==8){
					RelativeLayout bar = getBar(group2);
					bar.setBackgroundResource(R.drawable.tiao_bg_bottom);
				}
//				
				break;
			}
			return false;
		}
	});
	
	public void shrinkItem(final int i){
		if(this.getIsActive(i)){
			return;
		}else{
			setActive(i,true);
		}
		new Thread(){
			public void run(){
				int length = (int) mContext.getResources().getDimension(R.dimen.defen_area_expand_view_height);
				while(length>0){
					length = length-20;
					Message msg = new Message();
					msg.what = EXPAND_OR_SHRINK;
					msg.arg1 = i;
					msg.arg2 = length;
					mHandler.sendMessage(msg);
					Utils.sleepThread(20);
				}
				
				Message end = new Message();
				end.what = END_EXPAND_OR_SHRINK;
				end.arg1 = i;
				mHandler.sendMessage(end);
				setActive(i,false);
				RelativeLayout item = getBar(i);
				item.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						expandItem(i);
					}
					
				});
				
			}
		}.start();
	}
	
	public void expandItem(final int i){
		if(this.getIsActive(i)){
			return;
		}else{
			this.setActive(i, true);
		}
		
		final RelativeLayout item = getBar(i);
		if(i==8){
			item.setBackgroundResource(R.drawable.tiao_bg_center);
		}
		
		new Thread(){
			public void run(){
				int length = 0;
				
				int total = (int) mContext.getResources().getDimension(R.dimen.defen_area_expand_view_height);
				while(length<total){
					length = length+20;
					Message msg = new Message();
					msg.what = EXPAND_OR_SHRINK;
					msg.arg1 = i;
					msg.arg2 = length;
					mHandler.sendMessage(msg);
					Utils.sleepThread(20);
				}
				setActive(i, false);
				item.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						shrinkItem(i);
					}
					
				});
				
			}
		}.start();
	}
	
	public void showDefence_area1(){
		progressBar_defence_area1.setVisibility(RelativeLayout.GONE);
		progressBar_defence_area2.setVisibility(RelativeLayout.GONE);
		progressBar_defence_area3.setVisibility(RelativeLayout.GONE);
		progressBar_defence_area4.setVisibility(RelativeLayout.GONE);
		progressBar_defence_area5.setVisibility(RelativeLayout.GONE);
		progressBar_defence_area6.setVisibility(RelativeLayout.GONE);
		progressBar_defence_area7.setVisibility(RelativeLayout.GONE);
		progressBar_defence_area8.setVisibility(RelativeLayout.GONE);
		progressBar_defence_area9.setVisibility(RelativeLayout.GONE);
		for(int i=0;i<9;i++){
			RelativeLayout item = this.getBar(i);
			final int group = i;
			item.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String defenceID = contact.activeUser+contact.contactId;
					refreash(group,defenceID);
					expandItem(group);
				}
			});
			item.setOnLongClickListener(new OnLongClickListener(){

				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					
					NormalDialog dialog = new NormalDialog(
							mContext,
							mContext.getResources().getString(R.string.clear_code),
							mContext.getResources().getString(R.string.clear_code_prompt),
							mContext.getResources().getString(R.string.ensure),
							mContext.getResources().getString(R.string.cancel)
							);
					
					dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {
						
						@Override
						public void onClick() {
							// TODO Auto-generated method stub
							if(null==dialog_loading){
								dialog_loading = new NormalDialog(mContext,
										mContext.getResources().getString(R.string.clearing),
										"","","");
								dialog_loading.setStyle(NormalDialog.DIALOG_STYLE_LOADING);
							}
							dialog_loading.showDialog();
							current_group = group;
							current_type = Constants.P2P_SET.DEFENCE_AREA_SET.DEFENCE_AREA_TYPE_CLEAR_GROUP;
							P2PHandler.getInstance().clearDefenceAreaState(contact.contactId, contact.contactPassword, group);		
						}
					});
					
					dialog.showNormalDialog();
					dialog.setCanceledOnTouchOutside(false);
					
					return false;
				}
				
			});
		}
	}
	
	private void refreash(int group,String defenceID){
		Map<String,String> map = DataManager.findDefenceByDefenceID(mContext,defenceID);
		switch (group) {
		case 0:
			text_view_one1.setText(map.get(defenceID+"00000"));
			text_view_one2.setText(map.get(defenceID+"00001"));
			text_view_one3.setText(map.get(defenceID+"00002"));
			text_view_one4.setText(map.get(defenceID+"00003"));
			text_view_one5.setText(map.get(defenceID+"00004"));
			text_view_one6.setText(map.get(defenceID+"00005"));
			text_view_one7.setText(map.get(defenceID+"00006"));
			text_view_one8.setText(map.get(defenceID+"00007"));
			break;
		case 1:
			text_view_two1.setText(map.get(defenceID+"01000"));
			text_view_two2.setText(map.get(defenceID+"01001"));
			text_view_two3.setText(map.get(defenceID+"01002"));
			text_view_two4.setText(map.get(defenceID+"01003"));
			text_view_two5.setText(map.get(defenceID+"01004"));
			text_view_two6.setText(map.get(defenceID+"01005"));
			text_view_two7.setText(map.get(defenceID+"01006"));
			text_view_two8.setText(map.get(defenceID+"01007"));
			break;
		case 2:
			text_view_three1.setText(map.get(defenceID+"02000"));
			text_view_three2.setText(map.get(defenceID+"02001"));
			text_view_three3.setText(map.get(defenceID+"02002"));
			text_view_three4.setText(map.get(defenceID+"02003"));
			text_view_three5.setText(map.get(defenceID+"02004"));
			text_view_three6.setText(map.get(defenceID+"02005"));
			text_view_three7.setText(map.get(defenceID+"02006"));
			text_view_three8.setText(map.get(defenceID+"02007"));
			break;
		case 3:
			text_view_four1.setText(map.get(defenceID+"03000"));
			text_view_four2.setText(map.get(defenceID+"03001"));
			text_view_four3.setText(map.get(defenceID+"03002"));
			text_view_four4.setText(map.get(defenceID+"03003"));
			text_view_four5.setText(map.get(defenceID+"03004"));
			text_view_four6.setText(map.get(defenceID+"03005"));
			text_view_four7.setText(map.get(defenceID+"03006"));
			text_view_four8.setText(map.get(defenceID+"03007"));
			break;
		case 4:
			text_view_five1.setText(map.get(defenceID+"04000"));
			text_view_five2.setText(map.get(defenceID+"04001"));
			text_view_five3.setText(map.get(defenceID+"04002"));
			text_view_five4.setText(map.get(defenceID+"04003"));
			text_view_five5.setText(map.get(defenceID+"04004"));
			text_view_five6.setText(map.get(defenceID+"04005"));
			text_view_five7.setText(map.get(defenceID+"04006"));
			text_view_five8.setText(map.get(defenceID+"04007"));
			break;
		case 5:
			text_view_six1.setText(map.get(defenceID+"05000"));
			text_view_six2.setText(map.get(defenceID+"05001"));
			text_view_six3.setText(map.get(defenceID+"05002"));
			text_view_six4.setText(map.get(defenceID+"05003"));
			text_view_six5.setText(map.get(defenceID+"05004"));
			text_view_six6.setText(map.get(defenceID+"05005"));
			text_view_six7.setText(map.get(defenceID+"05006"));
			text_view_six8.setText(map.get(defenceID+"05007"));
			break;
		case 6:
			text_view_seven1.setText(map.get(defenceID+"06000"));
			text_view_seven2.setText(map.get(defenceID+"06001"));
			text_view_seven3.setText(map.get(defenceID+"06002"));
			text_view_seven4.setText(map.get(defenceID+"06003"));
			text_view_seven5.setText(map.get(defenceID+"06004"));
			text_view_seven6.setText(map.get(defenceID+"06005"));
			text_view_seven7.setText(map.get(defenceID+"06006"));
			text_view_seven8.setText(map.get(defenceID+"06007"));

			break;
		case 7:
			text_view_eigth1.setText(map.get(defenceID+"07000"));
			text_view_eigth2.setText(map.get(defenceID+"07001"));
			text_view_eigth3.setText(map.get(defenceID+"07002"));
			text_view_eigth4.setText(map.get(defenceID+"07003"));
			text_view_eigth5.setText(map.get(defenceID+"07004"));
			text_view_eigth6.setText(map.get(defenceID+"07005"));
			text_view_eigth7.setText(map.get(defenceID+"07006"));
			text_view_eigth8.setText(map.get(defenceID+"07007"));

			break;
		case 8:
			text_view_nine1.setText(map.get(defenceID+"08000"));
			text_view_nine2.setText(map.get(defenceID+"08001"));
			text_view_nine3.setText(map.get(defenceID+"08002"));
			text_view_nine4.setText(map.get(defenceID+"08003"));
			text_view_nine5.setText(map.get(defenceID+"08004"));
			text_view_nine6.setText(map.get(defenceID+"08005"));
			text_view_nine7.setText(map.get(defenceID+"08006"));
			text_view_nine8.setText(map.get(defenceID+"08007"));
			break;

		default:
			break;
		}
	}
	
	private void updateDefenceArea(final Context context,final int id,final String userID,final String cameraID,String textStr,final TextView mTextView){
		LayoutInflater layoutInflater = LayoutInflater.from(context);   
        View myLoginView = layoutInflater.inflate(R.layout.defence_alertdialog, null);      
        editTextDialog = (EditText) myLoginView.findViewById(R.id.edit_dialog);
        editTextDialog.setText(textStr);
        Dialog alertDialog = new AlertDialog.Builder(context)
        		.setView(myLoginView)
        		.setPositiveButton("修改", new DialogInterface.OnClickListener() {   
                    @Override   
                    public void onClick(DialogInterface dialog, int which) {   
                    	String dialogStr = editTextDialog.getText().toString().trim();
                    	String defenceID = "0"+id+"";
                    	updateDefenceName(userID,cameraID,mContext,defenceID,dialogStr);
                        
                        mTextView.setText(dialogStr);
                    }   
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {   
   
                    @Override   
                    public void onClick(DialogInterface dialog, int which) {     
                    }   
                }).   
                create();   
        alertDialog.show();   
				 
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub remote,hall,window,balcony,bedroom,kitchen,courtyard,door_lock,other;
		switch(v.getId()){
			case R.id.btn1:
				String remoteStr = remote.getText().toString().trim();
				updateDefenceArea(getActivity(),0,contact.activeUser,contact.contactId,remoteStr,remote);
				break;
			case R.id.btn2:
				String hallStr = hall.getText().toString().trim();
				updateDefenceArea(getActivity(),1,contact.activeUser,contact.contactId,hallStr,hall);
				break;
			case R.id.btn3:
				String windowStr = window.getText().toString().trim();
				updateDefenceArea(getActivity(),2,contact.activeUser,contact.contactId,windowStr,window);
				break;
			case R.id.btn4:
				String balconyStr = balcony.getText().toString().trim();
				updateDefenceArea(getActivity(),3,contact.activeUser,contact.contactId,balconyStr,balcony);
				break;
			case R.id.btn5:
				String bedroomStr = bedroom.getText().toString().trim();
				updateDefenceArea(getActivity(),4,contact.activeUser,contact.contactId,bedroomStr,bedroom);
				break;
			case R.id.btn6:
				String kitchenStr = kitchen.getText().toString().trim();
				updateDefenceArea(getActivity(),5,contact.activeUser,contact.contactId,kitchenStr,kitchen);
				break;
			case R.id.btn7:
				String courtyardStr = courtyard.getText().toString().trim();
				updateDefenceArea(getActivity(),6,contact.activeUser,contact.contactId,courtyardStr,courtyard);
				break;
			case R.id.btn8:
				String door_lockStr = door_lock.getText().toString().trim();
				updateDefenceArea(getActivity(),7,contact.activeUser,contact.contactId,door_lockStr,door_lock);
				break;
			case R.id.btn9:
				String otherStr = other.getText().toString().trim();
				updateDefenceArea(getActivity(),8,contact.activeUser,contact.contactId,otherStr,other);
				break;
		}
	}
	
	BroadcastReceiver studyBroadcastReceiver = new BroadcastReceiver(){
		private static final String STUDYCODE="STUDYCODE_ACTION";
		private static final String YZWCODE="YZWCODE_ACTION";
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(STUDYCODE.equals(intent.getAction())){
				alarmType = intent.getExtras().getString("alarmType");
			}
			if(YZWCODE.equals(intent.getAction())){
				yuzhiweiNum = intent.getExtras().getString("yuzhiweiNum");
				System.out.println("yuzhiweiNum=--"+yuzhiweiNum);
			}
		}
	};
	
	public void studyBroadcastReceiverFilter(){
		IntentFilter filter = new IntentFilter();
		filter.addAction("STUDYCODE_ACTION");
		filter.addAction("YZWCODE_ACTION");
		mContext.registerReceiver(studyBroadcastReceiver, filter);
		isRegFilter = true;
	}
	
	
	//学习防区
	public void study(final int group,final int item,final TextView textV){
		final NormalDialog dialog = new NormalDialog(
				mContext,
				mContext.getResources().getString(R.string.learing_code),
				mContext.getResources().getString(R.string.learing_code_prompt),
				"学习",
				mContext.getResources().getString(R.string.cancel)
				);
		studyBroadcastReceiverFilter();
		dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {
			
			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				System.out.println("alarmTypealarmType=="+alarmType);
				if(null==alarmType||alarmType.length()<=0){
					Toast.makeText(mContext, "请选择需要学习的设备类型", 1).show();
				}else{
					if(null==dialog_loading){
						dialog_loading = new NormalDialog(mContext,
								mContext.getResources().getString(R.string.studying),
								"","","");
						dialog_loading.setStyle(NormalDialog.DIALOG_STYLE_LOADING);
					}
					dialog_loading.showDialog();
					current_type = Constants.P2P_SET.DEFENCE_AREA_SET.DEFENCE_AREA_TYPE_LEARN;
					current_group = group;
					current_item = item;
					tView = textV;
					P2PHandler.getInstance().setDefenceAreaState(contact.contactId, contact.contactPassword, group, item ,Constants.P2P_SET.DEFENCE_AREA_SET.DEFENCE_AREA_TYPE_LEARN);
					dialog.cancel();
					
				}
			}
		});
		dialog.setOnButtonCancelListener(new NormalDialog.OnButtonCancelListener() {
			
			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				alarmType = null;
				yuzhiweiNum = null;
			}
		});
		dialog.showSelectDialog(group);
		//dialog.setCanceledOnTouchOutside(false);
	}
	
	public void clear(final int group,final int item,final TextView tv){
		
		NormalDialog dialog = new NormalDialog(
				mContext,
				mContext.getResources().getString(R.string.clear_code),
				mContext.getResources().getString(R.string.clear_code_prompt),
				mContext.getResources().getString(R.string.ensure),
				mContext.getResources().getString(R.string.cancel)
				);
		
		dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {
			
			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				if(null==dialog_loading){
					dialog_loading = new NormalDialog(mContext,
							mContext.getResources().getString(R.string.clearing),
							"","","");
					dialog_loading.setStyle(NormalDialog.DIALOG_STYLE_LOADING);
				}
				dialog_loading.showDialog();
				current_type = Constants.P2P_SET.DEFENCE_AREA_SET.DEFENCE_AREA_TYPE_CLEAR;
				current_group = group;
				current_item = item;
				aID = group;
				cID = item;
				tView = tv;
				P2PHandler.getInstance().setDefenceAreaState(contact.contactId, contact.contactPassword, group, item ,Constants.P2P_SET.DEFENCE_AREA_SET.DEFENCE_AREA_TYPE_CLEAR);		
			}
		});
		
		dialog.showNormalDialog();
		dialog.setCanceledOnTouchOutside(false);
	}
	
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		if(isRegFilter){
			mContext.unregisterReceiver(mReceiver);
			isRegFilter = false;
		}
	}
	
	
	public RelativeLayout getBar(int group){
		switch(group){
		case 0:
			return this.change_defence_area1;
		case 1:
			return this.change_defence_area2;
		case 2:
			return this.change_defence_area3;
		case 3:
			return this.change_defence_area4;
		case 4:
			return this.change_defence_area5;
		case 5:
			return this.change_defence_area6;
		case 6:
			return this.change_defence_area7;
		case 7:
			return this.change_defence_area8;
		case 8:
			return this.change_defence_area9;
		}
		return null;
	}
	
	public LinearLayout getContent(int group){
		switch(group){
		case 0:
			return this.defence_area_content1;
		case 1:
			return this.defence_area_content2;
		case 2:
			return this.defence_area_content3;
		case 3:
			return this.defence_area_content4;
		case 4:
			return this.defence_area_content5;
		case 5:
			return this.defence_area_content6;
		case 6:
			return this.defence_area_content7;
		case 7:
			return this.defence_area_content8;
		case 8:
			return this.defence_area_content9;
		}
		return null;
	}
	
	public TextView getTextView(int group,int item){
		switch(group){
		case 0:
			if(item==0){
				return this.text_view_one1;
			}else if(item==1){
				return this.text_view_one2;
			}else if(item==2){
				return this.text_view_one3;
			}else if(item==3){
				return this.text_view_one4;
			}else if(item==4){
				return this.text_view_one5;
			}else if(item==5){
				return this.text_view_one6;
			}else if(item==6){
				return this.text_view_one7;
			}else if(item==7){
				return this.text_view_one8;
			}
			break;
		case 1:
			if(item==0){
				return this.text_view_two1;
			}else if(item==1){
				return this.text_view_two2;
			}else if(item==2){
				return this.text_view_two3;
			}else if(item==3){
				return this.text_view_two4;
			}else if(item==4){
				return this.text_view_two5;
			}else if(item==5){
				return this.text_view_two6;
			}else if(item==6){
				return this.text_view_two7;
			}else if(item==7){
				return this.text_view_two8;
			}
			break;
		case 2:
			if(item==0){
				return this.text_view_three1;
			}else if(item==1){
				return this.text_view_three2;
			}else if(item==2){
				return this.text_view_three3;
			}else if(item==3){
				return this.text_view_three4;
			}else if(item==4){
				return this.text_view_three5;
			}else if(item==5){
				return this.text_view_three6;
			}else if(item==6){
				return this.text_view_three7;
			}else if(item==7){
				return this.text_view_three8;
			}
			break;
		case 3:
			if(item==0){
				return this.text_view_four1;
			}else if(item==1){
				return this.text_view_four2;
			}else if(item==2){
				return this.text_view_four3;
			}else if(item==3){
				return this.text_view_four4;
			}else if(item==4){
				return this.text_view_four5;
			}else if(item==5){
				return this.text_view_four6;
			}else if(item==6){
				return this.text_view_four7;
			}else if(item==7){
				return this.text_view_four8;
			}
			break;
		case 4:
			if(item==0){
				return this.text_view_five1;
			}else if(item==1){
				return this.text_view_five2;
			}else if(item==2){
				return this.text_view_five3;
			}else if(item==3){
				return this.text_view_five4;
			}else if(item==4){
				return this.text_view_five5;
			}else if(item==5){
				return this.text_view_five6;
			}else if(item==6){
				return this.text_view_five7;
			}else if(item==7){
				return this.text_view_five8;
			}
			break;
		case 5:
			if(item==0){
				return this.text_view_six1;
			}else if(item==1){
				return this.text_view_six2;
			}else if(item==2){
				return this.text_view_six3;
			}else if(item==3){
				return this.text_view_six4;
			}else if(item==4){
				return this.text_view_six5;
			}else if(item==5){
				return this.text_view_six6;
			}else if(item==6){
				return this.text_view_six7;
			}else if(item==7){
				return this.text_view_six8;
			}
			break;
		case 6:
			if(item==0){
				return this.text_view_seven1;
			}else if(item==1){
				return this.text_view_seven2;
			}else if(item==2){
				return this.text_view_seven3;
			}else if(item==3){
				return this.text_view_seven4;
			}else if(item==4){
				return this.text_view_seven5;
			}else if(item==5){
				return this.text_view_seven6;
			}else if(item==6){
				return this.text_view_seven7;
			}else if(item==7){
				return this.text_view_seven8;
			}
			break;
		case 7:
			if(item==0){
				return this.text_view_eigth1;
			}else if(item==1){
				return this.text_view_eigth2;
			}else if(item==2){
				return this.text_view_eigth3;
			}else if(item==3){
				return this.text_view_eigth4;
			}else if(item==4){
				return this.text_view_eigth5;
			}else if(item==5){
				return this.text_view_eigth6;
			}else if(item==6){
				return this.text_view_eigth7;
			}else if(item==7){
				return this.text_view_eigth8;
			}
			break;
		case 8:
			if(item==0){
				return this.text_view_nine1;
			}else if(item==1){
				return this.text_view_nine2;
			}else if(item==2){
				return this.text_view_nine3;
			}else if(item==3){
				return this.text_view_nine4;
			}else if(item==4){
				return this.text_view_nine5;
			}else if(item==5){
				return this.text_view_nine6;
			}else if(item==6){
				return this.text_view_nine7;
			}else if(item==7){
				return this.text_view_nine8;
			}
			break;
		}
		return null;
	}
	
	public RelativeLayout getKeyBoard(int group,int item){
		switch(group){
		case 0:
			if(item==0){
				return this.one1;
			}else if(item==1){
				return this.one2;
			}else if(item==2){
				return this.one3;
			}else if(item==3){
				return this.one4;
			}else if(item==4){
				return this.one5;
			}else if(item==5){
				return this.one6;
			}else if(item==6){
				return this.one7;
			}else if(item==7){
				return this.one8;
			}
			break;
		case 1:
			if(item==0){
				return this.two1;
			}else if(item==1){
				return this.two2;
			}else if(item==2){
				return this.two3;
			}else if(item==3){
				return this.two4;
			}else if(item==4){
				return this.two5;
			}else if(item==5){
				return this.two6;
			}else if(item==6){
				return this.two7;
			}else if(item==7){
				return this.two8;
			}
			break;
		case 2:
			if(item==0){
				return this.three1;
			}else if(item==1){
				return this.three2;
			}else if(item==2){
				return this.three3;
			}else if(item==3){
				return this.three4;
			}else if(item==4){
				return this.three5;
			}else if(item==5){
				return this.three6;
			}else if(item==6){
				return this.three7;
			}else if(item==7){
				return this.three8;
			}
			break;
		case 3:
			if(item==0){
				return this.four1;
			}else if(item==1){
				return this.four2;
			}else if(item==2){
				return this.four3;
			}else if(item==3){
				return this.four4;
			}else if(item==4){
				return this.four5;
			}else if(item==5){
				return this.four6;
			}else if(item==6){
				return this.four7;
			}else if(item==7){
				return this.four8;
			}
			break;
		case 4:
			if(item==0){
				return this.five1;
			}else if(item==1){
				return this.five2;
			}else if(item==2){
				return this.five3;
			}else if(item==3){
				return this.five4;
			}else if(item==4){
				return this.five5;
			}else if(item==5){
				return this.five6;
			}else if(item==6){
				return this.five7;
			}else if(item==7){
				return this.five8;
			}
			break;
		case 5:
			if(item==0){
				return this.six1;
			}else if(item==1){
				return this.six2;
			}else if(item==2){
				return this.six3;
			}else if(item==3){
				return this.six4;
			}else if(item==4){
				return this.six5;
			}else if(item==5){
				return this.six6;
			}else if(item==6){
				return this.six7;
			}else if(item==7){
				return this.six8;
			}
			break;
		case 6:
			if(item==0){
				return this.seven1;
			}else if(item==1){
				return this.seven2;
			}else if(item==2){
				return this.seven3;
			}else if(item==3){
				return this.seven4;
			}else if(item==4){
				return this.seven5;
			}else if(item==5){
				return this.seven6;
			}else if(item==6){
				return this.seven7;
			}else if(item==7){
				return this.seven8;
			}
			break;
		case 7:
			if(item==0){
				return this.eight1;
			}else if(item==1){
				return this.eight2;
			}else if(item==2){
				return this.eight3;
			}else if(item==3){
				return this.eight4;
			}else if(item==4){
				return this.eight5;
			}else if(item==5){
				return this.eight6;
			}else if(item==6){
				return this.eight7;
			}else if(item==7){
				return this.eight8;
			}
			break;
		case 8:
			if(item==0){
				return this.nine1;
			}else if(item==1){
				return this.nine2;
			}else if(item==2){
				return this.nine3;
			}else if(item==3){
				return this.nine4;
			}else if(item==4){
				return this.nine5;
			}else if(item==5){
				return this.nine6;
			}else if(item==6){
				return this.nine7;
			}else if(item==7){
				return this.nine8;
			}
			break;
		}
		return null;
	}

	public boolean getIsActive(int group){
		switch(group){
		case 0:
			return this.is_one_active;
		case 1:
			return this.is_two_active;
		case 2:
			return this.is_three_active;
		case 3:
			return this.is_four_active;
		case 4:
			return this.is_five_active;
		case 5:
			return this.is_six_active;
		case 6:
			return this.is_seven_active;
		case 7:
			return this.is_eight_active;
		case 8:
			return this.is_nine_active;
		}
		return true;
	}
	
	public void setActive(int group,boolean bool){
		switch(group){
		case 0:
			this.is_one_active = bool;
			break;
		case 1:
			this.is_two_active = bool;
			break;
		case 2:
			this.is_three_active = bool;
			break;
		case 3:
			this.is_four_active = bool;
			break;
		case 4:
			this.is_five_active = bool;
			break;
		case 5:
			this.is_six_active = bool;
			break;
		case 6:
			this.is_seven_active = bool;
			break;
		case 7:
			this.is_eight_active = bool;
			break;
		case 8:
			this.is_nine_active = bool;
			break;
		}
	}
	public ImageView getClear(int group,int item){
		switch(group){
		case 0:
			if(item==0){
				return this.clear_one1;
			}else if(item==1){
				return this.clear_one2;
			}else if(item==2){
				return this.clear_one3;
			}else if(item==3){
				return this.clear_one4;
			}else if(item==4){
				return this.clear_one5;
			}else if(item==5){
				return this.clear_one6;
			}else if(item==6){
				return this.clear_one7;
			}else if(item==7){
				return this.clear_one8;
			}
			break;
		case 1:
			if(item==0){
				return this.clear_two1;
			}else if(item==1){
				return this.clear_two2;
			}else if(item==2){
				return this.clear_two3;
			}else if(item==3){
				return this.clear_two4;
			}else if(item==4){
				return this.clear_two5;
			}else if(item==5){
				return this.clear_two6;
			}else if(item==6){
				return this.clear_two7;
			}else if(item==7){
				return this.clear_two8;
			}
			break;
		case 2:
			if(item==0){
				return this.clear_three1;
			}else if(item==1){
				return this.clear_three2;
			}else if(item==2){
				return this.clear_three3;
			}else if(item==3){
				return this.clear_three4;
			}else if(item==4){
				return this.clear_three5;
			}else if(item==5){
				return this.clear_three6;
			}else if(item==6){
				return this.clear_three7;
			}else if(item==7){
				return this.clear_three8;
			}
			break;
		case 3:
			if(item==0){
				return this.clear_four1;
			}else if(item==1){
				return this.clear_four2;
			}else if(item==2){
				return this.clear_four3;
			}else if(item==3){
				return this.clear_four4;
			}else if(item==4){
				return this.clear_four5;
			}else if(item==5){
				return this.clear_four6;
			}else if(item==6){
				return this.clear_four7;
			}else if(item==7){
				return this.clear_four8;
			}
			break;
		case 4:
			if(item==0){
				return this.clear_five1;
			}else if(item==1){
				return this.clear_five2;
			}else if(item==2){
				return this.clear_five3;
			}else if(item==3){
				return this.clear_five4;
			}else if(item==4){
				return this.clear_five5;
			}else if(item==5){
				return this.clear_five6;
			}else if(item==6){
				return this.clear_five7;
			}else if(item==7){
				return this.clear_five8;
			}
			break;
		case 5:
			if(item==0){
				return this.clear_six1;
			}else if(item==1){
				return this.clear_six2;
			}else if(item==2){
				return this.clear_six3;
			}else if(item==3){
				return this.clear_six4;
			}else if(item==4){
				return this.clear_six5;
			}else if(item==5){
				return this.clear_six6;
			}else if(item==6){
				return this.clear_six7;
			}else if(item==7){
				return this.clear_six8;
			}
			break;
		case 6:
			if(item==0){
				return this.clear_seven1;
			}else if(item==1){
				return this.clear_seven2;
			}else if(item==2){
				return this.clear_seven3;
			}else if(item==3){
				return this.clear_seven4;
			}else if(item==4){
				return this.clear_seven5;
			}else if(item==5){
				return this.clear_seven6;
			}else if(item==6){
				return this.clear_seven7;
			}else if(item==7){
				return this.clear_seven8;
			}
			break;
		case 7:
			if(item==0){
				return this.clear_eight1;
			}else if(item==1){
				return this.clear_eight2;
			}else if(item==2){
				return this.clear_eight3;
			}else if(item==3){
				return this.clear_eight4;
			}else if(item==4){
				return this.clear_eight5;
			}else if(item==5){
				return this.clear_eight6;
			}else if(item==6){
				return this.clear_eight7;
			}else if(item==7){
				return this.clear_eight8;
			}
			break;
		case 8:
			if(item==0){
				return this.clear_nine1;
			}else if(item==1){
				return this.clear_nine2;
			}else if(item==2){
				return this.clear_nine3;
			}else if(item==3){
				return this.clear_nine4;
			}else if(item==4){
				return this.clear_nine5;
			}else if(item==5){
				return this.clear_nine6;
			}else if(item==6){
				return this.clear_nine7;
			}else if(item==7){
				return this.clear_nine8;
			}
			break;
		}
		return null;
		
	}
	public ImageView getSwitch(int group,int item){
		switch(group){
		case 0:
			if(item==0){
				return this.switch_one1;
			}else if(item==1){
				return this.switch_one2;
			}else if(item==2){
				return this.switch_one3;
			}else if(item==3){
				return this.switch_one4;
			}else if(item==4){
				return this.switch_one5;
			}else if(item==5){
				return this.switch_one6;
			}else if(item==6){
				return this.switch_one7;
			}else if(item==7){
				return this.switch_one8;
			}
			break;
		case 1:
			if(item==0){
				return this.switch_two1;
			}else if(item==1){
				return this.switch_two2;
			}else if(item==2){
				return this.switch_two3;
			}else if(item==3){
				return this.switch_two4;
			}else if(item==4){
				return this.switch_two5;
			}else if(item==5){
				return this.switch_two6;
			}else if(item==6){
				return this.switch_two7;
			}else if(item==7){
				return this.switch_two8;
			}
			break;
		case 2:
			if(item==0){
				return this.switch_three1;
			}else if(item==1){
				return this.switch_three2;
			}else if(item==2){
				return this.switch_three3;
			}else if(item==3){
				return this.switch_three4;
			}else if(item==4){
				return this.switch_three5;
			}else if(item==5){
				return this.switch_three6;
			}else if(item==6){
				return this.switch_three7;
			}else if(item==7){
				return this.switch_three8;
			}
			break;
		case 3:
			if(item==0){
				return this.switch_four1;
			}else if(item==1){
				return this.switch_four2;
			}else if(item==2){
				return this.switch_four3;
			}else if(item==3){
				return this.switch_four4;
			}else if(item==4){
				return this.switch_four5;
			}else if(item==5){
				return this.switch_four6;
			}else if(item==6){
				return this.switch_four7;
			}else if(item==7){
				return this.switch_four8;
			}
			break;
		case 4:
			if(item==0){
				return this.switch_five1;
			}else if(item==1){
				return this.switch_five2;
			}else if(item==2){
				return this.switch_five3;
			}else if(item==3){
				return this.switch_five4;
			}else if(item==4){
				return this.switch_five5;
			}else if(item==5){
				return this.switch_five6;
			}else if(item==6){
				return this.switch_five7;
			}else if(item==7){
				return this.switch_five8;
			}
			break;
		case 5:
			if(item==0){
				return this.switch_six1;
			}else if(item==1){
				return this.switch_six2;
			}else if(item==2){
				return this.switch_six3;
			}else if(item==3){
				return this.switch_six4;
			}else if(item==4){
				return this.switch_six5;
			}else if(item==5){
				return this.switch_six6;
			}else if(item==6){
				return this.switch_six7;
			}else if(item==7){
				return this.switch_six8;
			}
			break;
		case 6:
			if(item==0){
				return this.switch_seven1;
			}else if(item==1){
				return this.switch_seven2;
			}else if(item==2){
				return this.switch_seven3;
			}else if(item==3){
				return this.switch_seven4;
			}else if(item==4){
				return this.switch_seven5;
			}else if(item==5){
				return this.switch_seven6;
			}else if(item==6){
				return this.switch_seven7;
			}else if(item==7){
				return this.switch_seven8;
			}
			break;
		case 7:
			if(item==0){
				return this.switch_eight1;
			}else if(item==1){
				return this.switch_eight2;
			}else if(item==2){
				return this.switch_eight3;
			}else if(item==3){
				return this.switch_eight4;
			}else if(item==4){
				return this.switch_eight5;
			}else if(item==5){
				return this.switch_eight6;
			}else if(item==6){
				return this.switch_eight7;
			}else if(item==7){
				return this.switch_eight8;
			}
			break;
		case 8:
			if(item==0){
				return this.switch_nine1;
			}else if(item==1){
				return this.switch_nine2;
			}else if(item==2){
				return this.switch_nine3;
			}else if(item==3){
				return this.switch_nine4;
			}else if(item==4){
				return this.switch_nine5;
			}else if(item==5){
				return this.switch_nine6;
			}else if(item==6){
				return this.switch_nine7;
			}else if(item==7){
				return this.switch_nine8;
			}
			break;
		}
		return null;
	}
	public ProgressBar getProgress(int group,int item){
		switch(group){
		case 1:
			if(item==0){
				return this.pre_two1;
			}else if(item==1){
				return this.pre_two2;
			}else if(item==2){
				return this.pre_two3;
			}else if(item==3){
				return this.pre_two4;
			}else if(item==4){
				return this.pre_two5;
			}else if(item==5){
				return this.pre_two6;
			}else if(item==6){
				return this.pre_two7;
			}else if(item==7){
				return this.pre_two8;
			}
			break;
		case 2:
			if(item==0){
				return this.pre_three1;
			}else if(item==1){
				return this.pre_three2;
			}else if(item==2){
				return this.pre_three3;
			}else if(item==3){
				return this.pre_three4;
			}else if(item==4){
				return this.pre_three5;
			}else if(item==5){
				return this.pre_three6;
			}else if(item==6){
				return this.pre_three7;
			}else if(item==7){
				return this.pre_three8;
			}
			break;
		case 3:
			if(item==0){
				return this.pre_four1;
			}else if(item==1){
				return this.pre_four2;
			}else if(item==2){
				return this.pre_four3;
			}else if(item==3){
				return this.pre_four4;
			}else if(item==4){
				return this.pre_four5;
			}else if(item==5){
				return this.pre_four6;
			}else if(item==6){
				return this.pre_four7;
			}else if(item==7){
				return this.pre_four8;
			}
			break;
		case 4:
			if(item==0){
				return this.pre_five1;
			}else if(item==1){
				return this.pre_five2;
			}else if(item==2){
				return this.pre_five3;
			}else if(item==3){
				return this.pre_five4;
			}else if(item==4){
				return this.pre_five5;
			}else if(item==5){
				return this.pre_five6;
			}else if(item==6){
				return this.pre_five7;
			}else if(item==7){
				return this.pre_five8;
			}
			break;
		case 5:
			if(item==0){
				return this.pre_six1;
			}else if(item==1){
				return this.pre_six2;
			}else if(item==2){
				return this.pre_six3;
			}else if(item==3){
				return this.pre_six4;
			}else if(item==4){
				return this.pre_six5;
			}else if(item==5){
				return this.pre_six6;
			}else if(item==6){
				return this.pre_six7;
			}else if(item==7){
				return this.pre_six8;
			}
			break;
		case 6:
			if(item==0){
				return this.pre_seven1;
			}else if(item==1){
				return this.pre_seven2;
			}else if(item==2){
				return this.pre_seven3;
			}else if(item==3){
				return this.pre_seven4;
			}else if(item==4){
				return this.pre_seven5;
			}else if(item==5){
				return this.pre_seven6;
			}else if(item==6){
				return this.pre_seven7;
			}else if(item==7){
				return this.pre_seven8;
			}
			break;
		case 7:
			if(item==0){
				return this.pre_eight1;
			}else if(item==1){
				return this.pre_eight2;
			}else if(item==2){
				return this.pre_eight3;
			}else if(item==3){
				return this.pre_eight4;
			}else if(item==4){
				return this.pre_eight5;
			}else if(item==5){
				return this.pre_eight6;
			}else if(item==6){
				return this.pre_eight7;
			}else if(item==7){
				return this.pre_eight8;
			}
			break;
		case 8:
			if(item==0){
				return this.pre_nine1;
			}else if(item==1){
				return this.pre_nine2;
			}else if(item==2){
				return this.pre_nine3;
			}else if(item==3){
				return this.pre_nine4;
			}else if(item==4){
				return this.pre_nine5;
			}else if(item==5){
				return this.pre_nine6;
			}else if(item==6){
				return this.pre_nine7;
			}else if(item==7){
				return this.pre_nine8;
			}
			break;
		}
		return null;
	}
	public void initSensorSwitch(ArrayList<int[]> sensors){
		for(int i=0;i<sensors.size();i++){
			int [] sensor=sensors.get(i);
			for(int j=0;j<sensor.length;j++){
				graySwitch(i+1,j,sensor[7-j]);
			}
		}
	}
	public void graySwitch(final int i,final int j,final int isOpen){
		final ImageView item=getSwitch(i,j);
		final ProgressBar pre_item=getProgress(i, j);
		if(pre_item!=null){
			pre_item.setVisibility(ProgressBar.GONE);
		}
		if(isOpen==1){
			item.setImageResource(R.drawable.check_on);
		}else{
			item.setImageResource(R.drawable.check_off);
		}
		item.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(pre_item!=null){
					pre_item.setVisibility(ProgressBar.VISIBLE);		
				}
				item.setVisibility(ImageView.INVISIBLE);
				if(isOpen==1){
					currentSwitch=0;
					currentgroup=i-1;
					currentitem=j;
					P2PHandler.getInstance().setDefenceAreaAlarmSwitch(contact.contactId, contact.contactPassword, currentSwitch, currentgroup,currentitem);
				}else{
					currentSwitch=1;
					currentgroup=i-1;
					currentitem=j;
					P2PHandler.getInstance().setDefenceAreaAlarmSwitch(contact.contactId, contact.contactPassword, currentSwitch, currentgroup,currentitem);
				}
			}
		});
	}
	public void setgraySwitch(final int i,final int j,final int isOpen){
		final ImageView item=getSwitch(i,j);
		final ProgressBar pre_item=getProgress(i, j);
		if(pre_item!=null){
			pre_item.setVisibility(ProgressBar.GONE);
		}
		if(isOpen==1){
			item.setImageResource(R.drawable.check_on);
		}else{
			item.setImageResource(R.drawable.check_off);
		}
		item.setVisibility(ImageView.VISIBLE);
		item.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(pre_item!=null){
					pre_item.setVisibility(ProgressBar.VISIBLE);		
				}
				item.setVisibility(ImageView.INVISIBLE);
				if(isOpen==1){
					currentSwitch=0;
					currentgroup=i-1;
					currentitem=j;
					P2PHandler.getInstance().setDefenceAreaAlarmSwitch(contact.contactId, contact.contactPassword, currentSwitch, currentgroup,currentitem);
				}else{
					currentSwitch=1;
					currentgroup=i-1;
					currentitem=j;
					P2PHandler.getInstance().setDefenceAreaAlarmSwitch(contact.contactId, contact.contactPassword, currentSwitch, currentgroup,currentitem);
				}
			}
		});
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		Intent it=new Intent();
		it.setAction(Constants.Action.CONTROL_BACK);
		mContext.sendBroadcast(it);
	}

	private void getDefence(String userID,final String cameraID,final String defenceId,final String alarmType,final int group){
		RequestQueue mQueue = Volley.newRequestQueue(mContext);
		String str =null;
		try {
			str = new String(alarmType.getBytes("gbk"),"iso-8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("userID", userID);
		map.put("cameraID", cameraID);
		map.put("defenceId", defenceId);
		map.put("alarmType", str);
		JsonArrayPostRequest mJsonRequest = new JsonArrayPostRequest(
				Constants.UPDATE_DEFENCE_ALARMTYPE_URL, 
				new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray response) {
						DataManager.updateDefenceAlarmType(mContext,alarmType, cameraID+defenceId);
						if(null!=alarmType&&alarmType.length()>0&&null!=tView){
							String str = tView.getText().toString().trim();
							tView.setText(str+"     "+alarmType);
							tView=null;
						}
						if("".equals(alarmType)&&null!=tView){
							String str = tView.getText().toString();
							String[] ss = str.split("     ");
							tView.setText(ss[0]);
							tView=null;
						}
						if(null==tView){
							String defenceID = contact.activeUser+contact.contactId;
							refreash(group,defenceID);
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
		mQueue.add(mJsonRequest);
	}
	
	private void updateDefenceName(final String userID,final String cameraID,final Context context,final String defenceID,final String defenceName){
		RequestQueue mQueue = Volley.newRequestQueue(context);
		String str =null;
		try {
			str = new String(defenceName.getBytes("gbk"),"iso-8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("userID", userID);
		map.put("cameraID", cameraID);
		map.put("defenceID", defenceID);
		map.put("defenceName", str);
		JsonArrayPostRequest mJsonRequest = new JsonArrayPostRequest(
				Constants.UPDATE_DEFENCE_NAME_URL, 
				new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray response) {

						DataManager.updateDefence(context,defenceName,userID+cameraID+defenceID);
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

}
