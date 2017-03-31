package com.jwkj.fragment;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.jwkj.activity.MainActivity;
import com.jwkj.adapter.VolleyImageViewListAdapter;
import com.jwkj.data.AlarmMessage;
import com.jwkj.data.AlarmRecord;
import com.jwkj.data.DataManager;
import com.jwkj.global.Constants;
import com.jwkj.global.NpcCommon;
import com.jwkj.volley.JsonArrayPostRequest;
import com.jwkj.wheelutil.OnWheelScrollListener;
import com.jwkj.wheelutil.StrericWheelAdapter;
import com.jwkj.wheelutil.WheelView;
import com.jwkj.widget.NormalDialog;
import com.lib.pullToRefresh.PullToRefreshBase;
import com.lib.pullToRefresh.PullToRefreshBase.OnRefreshListener2;
import com.lib.pullToRefresh.PullToRefreshBase.Mode;
import com.lib.pullToRefresh.PullToRefreshListView;
import com.test.jpushServer.R;

public class KeyboardFrag extends BaseFragment implements OnClickListener,OnFocusChangeListener{
	private boolean isRegFilter = false;
	private Context mContext;
	private PullToRefreshListView list_alarm;
	private List<AlarmRecord> list;
	private VolleyImageViewListAdapter adapter;
	private ImageView clear;
	private LinearLayout layout_menu;
	Animation animation_out, animation_in;
	public static boolean isHideAdd = true;
	private RelativeLayout radar_menu,manually_menu;
	boolean isDpShow = false;
	LinearLayout pick_date;
	RecordListFragment rlFrag;
	int selected_Date;
	public static final int START_TIME = 0;
	public static final int END_TIME = 1;
	private Button cancel_date,search_button;
	private EditText time_end,time_start;
	private WheelView yearWheel,monthWheel,dayWheel,hourWheel,minuteWheel,secondWheel;
	public static String[] yearContent=null;
	public static String[] monthContent=null;
	public static String[] dayContent=null;
	public static String[] dayContent1=null;
	public static String[] dayContent2=null;
	public static String[] dayContent3=null;
	public static String[] hourContent = null;
	public static String[] minuteContent=null;
	public static String[] secondContent=null;
	public List<AlarmMessage> listAlarmMessage;
	private int countPage=1;
	private String userId;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_keyboard, container, false);
		mContext = MainActivity.mContext;
		initComponent(view);
		userId = NpcCommon.mThreeNum;
		Map<String,String> map = new HashMap<String,String>();
		map.put("userID", "04345945");
		map.put("pageNo", countPage+"");
		findAlarmInfo(Constants.FINDALARMMESSAGE_BY_PAGE,map,"find");
		regFilter();
		return view;
	}
	public void initComponent(final View view) {
		clear=(ImageView)view.findViewById(R.id.clear);
		list_alarm=(PullToRefreshListView)view.findViewById(R.id.list_allarm);
		layout_menu = (LinearLayout) view.findViewById(R.id.layout_menu);
		radar_menu = (RelativeLayout) view.findViewById(R.id.radar_menu);
		manually_menu = (RelativeLayout) view.findViewById(R.id.manually_menu);
		pick_date = (LinearLayout) view.findViewById(R.id.pick_date); 
		cancel_date = (Button) view.findViewById(R.id.cancel_date); 
		search_button = (Button) view.findViewById(R.id.search_button); 
		time_end = (EditText) view.findViewById(R.id.time_end);
		time_start = (EditText) view.findViewById(R.id.time_start);
		time_end.setInputType(InputType.TYPE_NULL);
		time_start.setInputType(InputType.TYPE_NULL);
		time_end.setOnClickListener(this);
		time_start.setOnClickListener(this);
		time_start.setOnFocusChangeListener(this);
		time_end.setOnFocusChangeListener(this);
		cancel_date.setOnClickListener(this);
		search_button.setOnClickListener(this);
		pick_date.getBackground().setAlpha(230);
		initContent();
		list_alarm.setMode(Mode.BOTH);
		list_alarm.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub 
				countPage=1;
				Map<String,String> map = new HashMap<String,String>();
				map.put("userID", userId);
				map.put("pageNo", countPage+"");
				findAlarmInfo(Constants.FINDALARMMESSAGE_BY_PAGE,map,"downReflesh");
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				countPage++;
				Map<String,String> map = new HashMap<String,String>();
				map.put("userID", userId);
				map.put("pageNo", countPage+"");
				reFreshAlarmInfo(Constants.FINDALARMMESSAGE_BY_PAGE,map);
				
			}
		});

		animation_out = AnimationUtils.loadAnimation(mContext,
				R.anim.scale_amplify);
		animation_in = AnimationUtils.loadAnimation(mContext,
				R.anim.scale_narrow);
		//查询
		manually_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDatePick(view);
				hideAdd();
			}
		});
		//删除
		radar_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NormalDialog dialog = new NormalDialog(mContext, mContext
						.getResources().getString(R.string.delete_alarm_records),
						mContext.getResources().getString(R.string.confirm_clear),
						mContext.getResources().getString(R.string.clear), mContext
								.getResources().getString(R.string.cancel));
				dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

					@Override
					public void onClick() {
						// TODO Auto-generated method stub
						DataManager.clearAlarmRecord(mContext, NpcCommon.mThreeNum);
						//adapter.updateData();
						adapter.notifyDataSetChanged();
					}
				});
				dialog.showDialog();
				hideAdd();
			}
		});
		
		clear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				if (isHideAdd == true) {
//					showAdd();
//				} else {
//					hideAdd();
//				}
//				list_alarm.setFocusable(false);
				showDatePick(view);
			}
		});
		list_alarm.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (isHideAdd == false) {
					hideAdd();
				}
				return false;
			}
		});
	}
	public void regFilter(){
		IntentFilter filter = new IntentFilter();
		filter.addAction("CLOSED_ACTION");
		mContext.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals("CLOSED_ACTION")){
				if (isHideAdd == false) {
					hideAdd();
				}
			}
		}
	};
	
	public void hideAdd() {
		clear.setImageResource(R.drawable.menu_right);
		layout_menu.startAnimation(animation_in);
		layout_menu.setVisibility(LinearLayout.GONE);
		isHideAdd = true;
	}

	public void showAdd() {
		clear.setImageResource(R.drawable.b_down);
		layout_menu.setVisibility(LinearLayout.VISIBLE);
		layout_menu.startAnimation(animation_out);
		isHideAdd = false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(isRegFilter){
			isRegFilter = false;
			mContext.unregisterReceiver(mReceiver);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		//clearEditText();
	}
	
	public void showDatePick(View v) {
		isDpShow = true;
		pick_date.setVisibility(RelativeLayout.VISIBLE);
		Animation anim = AnimationUtils.loadAnimation(mContext,
				R.anim.slide_in_bottom);
		pick_date.startAnimation(anim);
		buttonClick(v);
	}
	
	public void buttonClick(View v)
    {	
		Calendar calendar = Calendar.getInstance();
	    int curYear = calendar.get(Calendar.YEAR);
        int curMonth= calendar.get(Calendar.MONTH)+1;
        int curDay = calendar.get(Calendar.DAY_OF_MONTH);
        
        int curHour = calendar.get(Calendar.HOUR_OF_DAY);
        int curMinute = calendar.get(Calendar.MINUTE);
        int curSecond = calendar.get(Calendar.SECOND);
 	    
	    yearWheel = (WheelView)v.findViewById(R.id.yearwheel);
	    monthWheel = (WheelView)v.findViewById(R.id.monthwheel);
	    dayWheel = (WheelView)v.findViewById(R.id.daywheel);
	    hourWheel = (WheelView)v.findViewById(R.id.hourwheel);
	    minuteWheel = (WheelView)v.findViewById(R.id.minutewheel);
	    secondWheel = (WheelView)v.findViewById(R.id.secondWheel);
	    
        yearWheel.setAdapter(new StrericWheelAdapter(yearContent));
	 	yearWheel.setCurrentItem(curYear-2014);
	 	yearWheel.addScrollingListener(scrolledListener);
	    yearWheel.setCyclic(true);
	    yearWheel.setInterpolator(new AnticipateOvershootInterpolator());
        monthWheel.setAdapter(new StrericWheelAdapter(monthContent));
        monthWheel.setCurrentItem(curMonth-1);
        monthWheel.addScrollingListener(scrolledListener);
        monthWheel.setCyclic(true);
        monthWheel.setInterpolator(new AnticipateOvershootInterpolator());
        
        dayWheel.setAdapter(new StrericWheelAdapter(dayContent));
        dayWheel.setCurrentItem(curDay-1);
        dayWheel.setCyclic(true);
        dayWheel.addScrollingListener(scrolledListener);
        dayWheel.setInterpolator(new AnticipateOvershootInterpolator());
        
        hourWheel.setAdapter(new StrericWheelAdapter(hourContent));
        hourWheel.setCurrentItem(curHour);
        hourWheel.setCyclic(true);
        hourWheel.addScrollingListener(scrolledListener);
        hourWheel.setInterpolator(new AnticipateOvershootInterpolator());
        
        minuteWheel.setAdapter(new StrericWheelAdapter(minuteContent));
        minuteWheel.setCurrentItem(curMinute);
        minuteWheel.setCyclic(true);
        minuteWheel.addScrollingListener(scrolledListener);
        minuteWheel.setInterpolator(new AnticipateOvershootInterpolator());

        secondWheel.setAdapter(new StrericWheelAdapter(secondContent));
        secondWheel.setCurrentItem(curSecond);
        secondWheel.setCyclic(true);
        secondWheel.addScrollingListener(scrolledListener);
        secondWheel.setInterpolator(new AnticipateOvershootInterpolator());
	}
	
	public void initContent()
	{
		yearContent = new String[50];
		for(int i=0;i<50;i++){
			yearContent[i] = String.valueOf(i+2014);
		}
		
		monthContent = new String[12];
		for(int i=0;i<12;i++)
		{
			monthContent[i]= String.valueOf(i+1);
			if(monthContent[i].length()<2)
	        {
				monthContent[i] = "0"+monthContent[i];
	        }
		}
			
		dayContent = new String[31];
		for(int i=0;i<31;i++)
		{
			dayContent[i]=String.valueOf(i+1);
			if(dayContent[i].length()<2)
	        {
				dayContent[i] = "0"+dayContent[i];
	        }
		}
		
		dayContent1 = new String[29];
		for(int i=0;i<29;i++)
		{
			dayContent1[i]=String.valueOf(i+1);
			if(dayContent1[i].length()<2)
	        {
				dayContent1[i] = "0"+dayContent1[i];
	        }
		}	
		
		dayContent2 = new String[28];
		for(int i=0;i<28;i++)
		{
			dayContent2[i]=String.valueOf(i+1);
			if(dayContent2[i].length()<2)
	        {
				dayContent2[i] = "0"+dayContent2[i];
	        }
		}	
		
		dayContent3 = new String[30];
		for(int i=0;i<30;i++)
		{
			dayContent3[i]=String.valueOf(i+1);
			if(dayContent3[i].length()<2)
	        {
				dayContent3[i] = "0"+dayContent3[i];
	        }
		}	
		
		hourContent = new String[24];
		for(int i=0;i<24;i++)
		{
			hourContent[i]= String.valueOf(i);
			if(hourContent[i].length()<2)
	        {
				hourContent[i] = "0"+hourContent[i];
	        }
		}
			
		minuteContent = new String[60];
		for(int i=0;i<60;i++)
		{
			minuteContent[i]=String.valueOf(i);
			if(minuteContent[i].length()<2)
	        {
				minuteContent[i] = "0"+minuteContent[i];
	        }
		}
		secondContent = new String[60];
		for(int i=0;i<60;i++)
		{
			secondContent[i]=String.valueOf(i);
			if(secondContent[i].length()<2)
	        {
				secondContent[i] = "0"+secondContent[i];
	        }
		}
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.cancel_date:
			pick_date.setVisibility(RelativeLayout.GONE);
			break;
		case R.id.search_button:
			String startStr = time_start.getText().toString();
			String endStr = time_end.getText().toString();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				if(null!=startStr&&null!=endStr&&endStr.length()>0&&startStr.length()>0){
					long startLong =  df.parse(startStr).getTime();
					long endLong =  df.parse(endStr).getTime();
					if(startLong<=endLong){
						Map<String,String> map = new HashMap<String,String>();
						map.put("userID", userId);
						map.put("startTime", startStr);
						map.put("endTime", endStr);
						findAlarmInfo(Constants.FINDALARMMESSAGE_BY_TIME,map,"find");
						pick_date.setVisibility(RelativeLayout.GONE);
					}else{
						Toast.makeText(mContext, "开始时间不能大于结束时间", 1).show();
					}
				}else{
					Toast.makeText(mContext, "请选择开始时间和结束时间", 1).show();
				}
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}
	@Override
	public void onFocusChange(View arg0, boolean arg1) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.time_end:
			selected_Date = END_TIME;
			time_start.setTextColor(Color.BLACK);
			time_start.setHintTextColor(Color.BLACK);
			time_end.setTextColor(Color.BLUE);
			time_end.setHintTextColor(Color.BLUE);
			break;
		case R.id.time_start:
			selected_Date = START_TIME;
			time_start.setTextColor(Color.BLUE);
			time_start.setHintTextColor(Color.BLUE);
			time_end.setTextColor(Color.BLACK);
			time_end.setHintTextColor(Color.BLACK);
			break;
		default:
			break;
		}
	}
	
	private boolean wheelScrolled = false;
	OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
		public void onScrollingStarted(WheelView wheel) {
			wheelScrolled = true;
			updateStatus();
			updateSearch();
		}

		public void onScrollingFinished(WheelView wheel) {
			wheelScrolled = false;
			updateStatus();
			updateSearch();
		}
	};
	
	public void updateStatus() {
		//yearWheel,monthWheel,dayWheel,hourWheel,minuteWheel;
		int year = yearWheel.getCurrentItem() + 2014;
		int month = monthWheel.getCurrentItem() + 1;

		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12) {
			dayWheel.setAdapter(new StrericWheelAdapter(dayContent));
		} else if (month == 2) {

			boolean isLeapYear = false;
			if (year % 100 == 0) {
				if (year % 400 == 0) {
					isLeapYear = true;
				} else {
					isLeapYear = false;
				}
			} else {
				if (year % 4 == 0) {
					isLeapYear = true;
				} else {
					isLeapYear = false;
				}
			}
			if (isLeapYear) {
				if (dayWheel.getCurrentItem() > 28) {
					dayWheel.scroll(30, 2000);
				}
				dayWheel.setAdapter(new StrericWheelAdapter(dayContent1));
			} else {
				if (dayWheel.getCurrentItem() > 27) {
					dayWheel.scroll(30, 2000);
				}
				dayWheel.setAdapter(new StrericWheelAdapter(dayContent2));
			}

		} else {
			if (dayWheel.getCurrentItem() > 29) {
				dayWheel.scroll(30, 2000);
			}
			dayWheel.setAdapter(new StrericWheelAdapter(dayContent3));
		}

	}
	
	public void updateSearch() {
		//yearWheel,monthWheel,dayWheel,hourWheel,minuteWheel;
		Timestamp now = new Timestamp(System.currentTimeMillis());
		int year = yearWheel.getCurrentItem() + 2014;
		int month = monthWheel.getCurrentItem() + 1;
		int day = dayWheel.getCurrentItem() + 1;
		int hour = hourWheel.getCurrentItem();
		int minute = minuteWheel.getCurrentItem();
		int second = secondWheel.getCurrentItem();
		StringBuilder sb = new StringBuilder();
		sb.append(year + "-");

		if (month < 10) {
			sb.append("0" + month + "-");
		} else {
			sb.append(month + "-");
		}

		if (day < 10) {
			sb.append("0" + day + " ");
		} else {
			sb.append(day + " ");
		}

		if (hour < 10) {
			sb.append("0" + hour + ":");
		} else {
			sb.append(hour + ":");
		}

		if (minute < 10) {
			sb.append("0" + minute+":");
		} else {
			sb.append(minute + ":");
		}
		
		if (second < 10) {
			sb.append("0" + second);
		} else {
			sb.append("" + second);
		}

		if (selected_Date == START_TIME) {
			time_start.setText(sb.toString());
		} else {
			time_end.setText(sb.toString());
		}
	}
	
	private void findAlarmInfo(String url, Map<String,String> map,final String flagReflesh){
		RequestQueue mQueue = Volley.newRequestQueue(mContext);
		
		JsonArrayPostRequest mJsonRequest = new JsonArrayPostRequest(
				url, 
				new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray response) {
						if(listAlarmMessage!=null){
							listAlarmMessage.clear();
						}
						if(response.length()>0){
							listAlarmMessage = new ArrayList<AlarmMessage>();
							String strResult;
							try {
								strResult = response.getString(0);
								if("pageisall".equals(strResult)){
									Toast.makeText(mContext, "无最新数据", 1).show();
								}else{
									for(int i=0;i<response.length();i++){
										AlarmMessage mAlarmMessage = new AlarmMessage();
										JSONObject obj = (JSONObject) response.get(i);
										mAlarmMessage.setDeiceId(obj.getString("deviceID"));
										mAlarmMessage.setAlarmType(obj.getString("alarmType"));
										mAlarmMessage.setAlarmArea(obj.getString("alarmArea"));
										mAlarmMessage.setAlarmChannel(obj.getString("alarmChannel"));
										mAlarmMessage.setDataType(obj.getString("dataType"));
										mAlarmMessage.setRecordTime(obj.getString("recordTime"));
										JSONObject str7= obj.getJSONObject("mAlarmImage");
										mAlarmMessage.setImageNameOne(str7.getString("imageNameOne"));
										mAlarmMessage.setImageNameTwo(str7.getString("imageNameTwo"));
										mAlarmMessage.setImageNameThree(str7.getString("imageNameThree"));
										listAlarmMessage.add(mAlarmMessage);
									}
									if("find".equals(flagReflesh)){
										adapter=new VolleyImageViewListAdapter(mContext,listAlarmMessage);
										list_alarm.setAdapter(adapter);
									}else if("downReflesh".equals(flagReflesh)){
										adapter=new VolleyImageViewListAdapter(mContext,listAlarmMessage);
										//ListView actualListView = list_alarm.getRefreshableView();
										registerForContextMenu(list_alarm);
										list_alarm.setAdapter(adapter);
										adapter.notifyDataSetChanged();
										list_alarm.getRefreshableView().setSelection((countPage-1)*3);
										list_alarm.onRefreshComplete();
									}
									
								}
							}catch(Exception e){
								
							}
						}else{
							Toast.makeText(mContext, "无最新数据", 1).show();
						}
						
					}
				}, 
				new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
					}
				}, 
				map);
		mQueue.add(mJsonRequest);
	}
	
	private void reFreshAlarmInfo(String url, Map<String,String> map){
		RequestQueue mQueue = Volley.newRequestQueue(mContext);
		
		JsonArrayPostRequest mJsonRequest = new JsonArrayPostRequest(
				url, 
				new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray response) {
						
						if(response.length()>0){
							String strResult;
							try {
								strResult = response.getString(0);
								if("pageisall".equals(strResult)){
									Toast.makeText(mContext, "已无最新数据", 1).show();
								}else{
									for(int i=0;i<response.length();i++){
										AlarmMessage mAlarmMessage = new AlarmMessage();
										JSONObject obj = (JSONObject) response.get(i);
										mAlarmMessage.setDeiceId(obj.getString("deviceID"));
										mAlarmMessage.setAlarmType(obj.getString("alarmType"));
										mAlarmMessage.setAlarmArea(obj.getString("alarmArea"));
										mAlarmMessage.setAlarmChannel(obj.getString("alarmChannel"));
										mAlarmMessage.setDataType(obj.getString("dataType"));
										mAlarmMessage.setRecordTime(obj.getString("recordTime"));
										JSONObject str7= obj.getJSONObject("mAlarmImage");
										mAlarmMessage.setImageNameOne(str7.getString("imageNameOne"));
										mAlarmMessage.setImageNameTwo(str7.getString("imageNameTwo"));
										mAlarmMessage.setImageNameThree(str7.getString("imageNameThree"));
										listAlarmMessage.add(mAlarmMessage);
									}

								}
								adapter=new VolleyImageViewListAdapter(mContext,listAlarmMessage);
								//ListView actualListView = list_alarm.getRefreshableView();
								registerForContextMenu(list_alarm);
								list_alarm.setAdapter(adapter);
								adapter.notifyDataSetChanged();
								list_alarm.getRefreshableView().setSelection((countPage-1)*3);
								list_alarm.onRefreshComplete();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
						}
					}
				}, 
				new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
					}
				}, 
				map);
		mQueue.add(mJsonRequest);
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		countPage=1;
	}
	
	
}
