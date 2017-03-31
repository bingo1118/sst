package com.jwkj.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.test.jpushServer.R;
import com.jwkj.data.AlarmRecord;
import com.jwkj.data.DataManager;
import com.jwkj.global.Constants;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.Utils;
import com.jwkj.widget.NormalDialog;

public class AlarmRecordAdapter extends BaseAdapter{
	List<AlarmRecord> list;
	Context mContext;
	private String areaName,channelName;
	
	public AlarmRecordAdapter(Context context,List<AlarmRecord> list){
		this.mContext = context;
		this.list = list;
	}

	class ViewHolder{
		private TextView robotId;
		private TextView allarmType;
		private TextView allarmTime;
		private LinearLayout layout_extern;
		private TextView text_group;
		private TextView text_item;
		private TextView text_type;
		private ImageView image_one;
		
		
		public TextView getText_type() {
			return text_type;
		}
		public void setText_type(TextView text_type) {
			this.text_type = text_type;
		}
		public LinearLayout getLayout_extern() {
			return layout_extern;
		}
		public void setLayout_extern(LinearLayout layout_extern) {
			this.layout_extern = layout_extern;
		}
		public TextView getText_group() {
			return text_group;
		}
		public void setText_group(TextView text_group) {
			this.text_group = text_group;
		}
		public TextView getText_item() {
			return text_item;
		}
		public void setText_item(TextView text_item) {
			this.text_item = text_item;
		}
		public TextView getRobotId() {
			return robotId;
		}
		public void setRobotId(TextView robotId) {
			this.robotId = robotId;
		}
		public TextView getAllarmType() {
			return allarmType;
		}
		public void setAllarmType(TextView allarmType) {
			this.allarmType = allarmType;
		}
		public TextView getAllarmTime() {
			return allarmTime;
		}
		public void setAllarmTime(TextView allarmTime) {
			this.allarmTime = allarmTime;
		}
		public ImageView getImage_one() {
			return image_one;
		}
		public void setImage_one(ImageView image_one) {
			this.image_one = image_one;
		}
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view = arg1;
		ViewHolder holder;
		if(null==view){
			view = LayoutInflater.from(mContext).inflate(R.layout.list_alarm_record_item, null);
			holder = new ViewHolder();
			holder.setRobotId((TextView) view.findViewById(R.id.robot_id));
			holder.setAllarmType((TextView) view.findViewById(R.id.allarm_type));
			holder.setAllarmTime((TextView) view.findViewById(R.id.allarm_time));
			holder.setLayout_extern((LinearLayout)view.findViewById(R.id.layout_extern));
			holder.setText_group((TextView) view.findViewById(R.id.text_group));
			holder.setText_item((TextView) view.findViewById(R.id.text_item));
			holder.setText_type((TextView)view.findViewById(R.id.tv_type));
			holder.setImage_one((ImageView)view.findViewById(R.id.image_one));
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		
		final AlarmRecord ar = list.get(arg0);
		holder.getRobotId().setText(Utils.getDeviceName(ar.deviceId));
		holder.getAllarmTime().setText(Utils.getFormatTellDate(mContext, ar.alarmTime));
		
		imageLoad(holder.getImage_one());
		
		holder.getLayout_extern().setVisibility(RelativeLayout.GONE);
		switch(ar.alarmType){
		case 1:
			holder.getText_type().setText(R.string.allarm_type);
			holder.getAllarmType().setText(ar.alarmInfo);
			if(ar.group>=0&&ar.item>=0){
				String defenceID = NpcCommon.mThreeNum+ar.deviceId+"0"+ar.group+"";
				areaName = DataManager.findDefence(mContext, defenceID);
				Map<String,String> map= DataManager.findDefenceMap(mContext, defenceID+"00"+(ar.item+1)+"");
				channelName = map.get("defenceName");
				holder.getLayout_extern().setVisibility(RelativeLayout.VISIBLE);
				holder.getText_group().setText(
						mContext.getResources().getString(R.string.area)+
						":"+areaName);
				holder.getText_item().setText(
						mContext.getResources().getString(R.string.channel)+
						":"+channelName);
			}
			break;
		case 2:
			holder.getText_type().setText(R.string.allarm_type);
			holder.getAllarmType().setText(R.string.allarm_type2);
			break;
		case 3:
			holder.getText_type().setText(R.string.allarm_type);
			holder.getAllarmType().setText(R.string.allarm_type3);
			break;
//		case 4:
//			holder.getAllarmType().setText(R.string.allarm_type4);
//			break;
		case 5:
			holder.getText_type().setText(R.string.allarm_type);
			holder.getAllarmType().setText(R.string.allarm_type5);
			break;
		case 6:
			holder.getText_type().setText(R.string.allarm_type);
			holder.getAllarmType().setText(ar.alarmInfo);
			if(ar.group>=0&&ar.item>=0){
				String defenceID = NpcCommon.mThreeNum+ar.deviceId+"0"+ar.group+"";
				areaName = DataManager.findDefence(mContext, defenceID);
				Map<String,String> map = DataManager.findDefenceMap(mContext, defenceID+"00"+(ar.item+1)+"");
				channelName = map.get("channelName");
				holder.getLayout_extern().setVisibility(RelativeLayout.VISIBLE);
				holder.getText_group().setText(
						mContext.getResources().getString(R.string.area)+
						":"+areaName);
				holder.getText_item().setText(
						mContext.getResources().getString(R.string.channel)+
						":"+channelName);
			}
			break;
		case 7:
			holder.getText_type().setText(R.string.allarm_type);
			holder.getAllarmType().setText(R.string.allarm_type4);
			break;
		case 8:
			holder.getText_type().setText(R.string.allarm_type);
			holder.getAllarmType().setText(R.string.defence);
			break;
		case 9:
			holder.getText_type().setText(R.string.allarm_type);
			holder.getAllarmType().setText(R.string.no_defence);
			break;
		case 10:
			holder.getText_type().setText(R.string.allarm_type);
			holder.getAllarmType().setText(R.string.battery_low_alarm);
			break;
		case 13:
			holder.getText_type().setText(R.string.allarm_type);
			holder.getAllarmType().setText(R.string.alarm_type);
			break;
		case 15:
			holder.getText_type().setText(R.string.allarm_type);
			holder.getAllarmType().setText(R.string.record_failed);
			break;
		case 16:
			holder.getText_type().setText(R.string.allarm_type);
			holder.getAllarmType().setText("烟感报警");
			break;
	    default:
	    	holder.getText_type().setText(R.string.not_know);
	    	holder.getAllarmType().setText(String.valueOf(ar.alarmType));
	    	break;
		}
		
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 Intent intent = new Intent();  
		         intent.setAction("CLOSED_ACTION");
		         mContext.sendBroadcast(intent);
			}
		});
		
		view.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				NormalDialog dialog = new NormalDialog(
						mContext,
						mContext.getResources().getString(R.string.delete_alarm_records),
						mContext.getResources().getString(R.string.are_you_sure_delete)+" "+ar.deviceId+"?",
						mContext.getResources().getString(R.string.delete),
						mContext.getResources().getString(R.string.cancel)
						);
				dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {
					
					@Override
					public void onClick() {
						// TODO Auto-generated method stub
						
						DataManager.deleteAlarmRecordById(mContext, ar.id);
						Intent refreshAlarm=new Intent();
						refreshAlarm.setAction(Constants.Action.REFRESH_ALARM_RECORD);
						mContext.sendBroadcast(refreshAlarm);
					}
				});
				dialog.showDialog();
				return true;
			}
			
		});
		return view;
	} 
	
	public void updateData(){
		this.list = DataManager.findAlarmRecordByActiveUser(mContext, NpcCommon.mThreeNum);
	}
	
	public void imageLoad(final ImageView v){
		RequestQueue mQueue = Volley.newRequestQueue(mContext);  
		ImageRequest imageRequest = new ImageRequest(  
		        "http://119.29.155.148/images/224713621.jpg",  
		        new Response.Listener<Bitmap>() {  
		            @Override  
		            public void onResponse(Bitmap response) {  
		            	v.setImageBitmap(response);  
		            }  
		        }, 0, 0, Config.RGB_565, new Response.ErrorListener() {  
		            @Override  
		            public void onErrorResponse(VolleyError error) {  
		            	v.setImageResource(R.drawable.a1);  
		            } 
		        }); 
		mQueue.add(imageRequest);
	}
	
	
}
