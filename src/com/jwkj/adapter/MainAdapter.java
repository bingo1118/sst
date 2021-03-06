package com.jwkj.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.Inflater;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jpushdemo.ExampleUtil;
import com.test.jpushServer.R;
import com.jwkj.CallActivity;
import com.jwkj.activity.AddContactNextActivity;
import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.data.SharedPreferencesManager;
import com.jwkj.entity.LocalDevice;
import com.jwkj.fragment.ContactFrag;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.MyApp;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.EmailPopwindow.onSelected;
import com.jwkj.widget.HeaderView;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;

public class MainAdapter extends BaseAdapter {
	Context context;
	private ContactFrag cf;
	private RequestQueue mQueue;
	private String [] last_bind_data;
	
	public MainAdapter(Context context, ContactFrag cf) {
		this.context = context;

		this.cf = cf;
	}

	class ViewHolder {
		private HeaderView head;
		private TextView name;
		private TextView online_state;
		private ImageView login_type;
		private TextView msgCount;
		private ImageView header_icon_play;

		private RelativeLayout layout_defence_btn;
		private ImageView image_defence_state;
		private ProgressBar progress_defence;
		public ImageView ivWeakPassWord;

		public TextView getMsgCount() {
			return msgCount;
		}

		public void setMsgCount(TextView msgCount) {
			this.msgCount = msgCount;
		}

		public ImageView getLogin_type() {
			return login_type;
		}

		public void setLogin_type(ImageView login_type) {
			this.login_type = login_type;
		}

		public TextView getOnline_state() {
			return online_state;
		}

		public void setOnline_state(TextView online_state) {
			this.online_state = online_state;
		}

		public HeaderView getHead() {
			return head;
		}

		public void setHead(HeaderView head) {
			this.head = head;
		}

		public TextView getName() {
			return name;
		}

		public void setName(TextView name) {
			this.name = name;
		}

		public ImageView getHeader_icon_play() {
			return header_icon_play;
		}

		public void setHeader_icon_play(ImageView header_icon_play) {
			this.header_icon_play = header_icon_play;
		}

		public RelativeLayout getLayout_defence_btn() {
			return layout_defence_btn;
		}

		public void setLayout_defence_btn(RelativeLayout layout_defence_btn) {
			this.layout_defence_btn = layout_defence_btn;
		}

		public ImageView getImage_defence_state() {
			return image_defence_state;
		}

		public void setImage_defence_state(ImageView image_defence_state) {
			this.image_defence_state = image_defence_state;
		}

		public ProgressBar getProgress_defence() {
			return progress_defence;
		}

		public void setProgress_defence(ProgressBar progress_defence) {
			this.progress_defence = progress_defence;
		}

	}

	class ViewHolder2 {
		public TextView name;
		public ImageView device_type;

		public TextView getName() {
			return name;
		}

		public void setName(TextView name) {
			this.name = name;
		}

		public ImageView getDevice_type() {
			return device_type;
		}

		public void setDevice_type(ImageView device_type) {
			this.device_type = device_type;
		}

	}

	@Override
	public int getCount() {
		// int size = FList.getInstance().getUnsetPasswordLocalDevices().size();
		// return FList.getInstance().size() + size;
		return FList.getInstance().size();
	}

	@Override
	public Contact getItem(int position) {
		return FList.getInstance().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		if (position >= FList.getInstance().size()) {
			return 0;
		} else {
			return 1;
		}

	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	public void regFilter(){
        IntentFilter filter=new IntentFilter();
        filter.addAction(Constants.P2P.RET_GET_BIND_ALARM_ID);
        context.registerReceiver(br, filter);
	 }
	 
	 BroadcastReceiver br=new BroadcastReceiver() {
			
		@Override
		public void onReceive(Context arg0, Intent intent) {
			if(intent.getAction().equals(Constants.P2P.RET_GET_BIND_ALARM_ID)){
				String[] data = intent.getStringArrayExtra("data");
				last_bind_data=data;
			}
		}
	};

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		int size1 = FList.getInstance().list().size();
		// 显示所有已经添加的设备
		// if (position < size1) {
		View view = convertView;
		final ViewHolder holder;
		if (null == view) {
			view = LayoutInflater.from(context).inflate(
					R.layout.list_contact_item, null);
			holder = new ViewHolder();
			HeaderView head = (HeaderView) view.findViewById(R.id.user_icon);
			holder.setHead(head);
			TextView name = (TextView) view.findViewById(R.id.user_name);
			holder.setName(name);
			TextView onlineState = (TextView) view
					.findViewById(R.id.online_state);
			holder.setOnline_state(onlineState);
			ImageView loginType = (ImageView) view
					.findViewById(R.id.login_type);
			holder.setLogin_type(loginType);
			TextView msgCount = (TextView) view.findViewById(R.id.msgCount);
			holder.setMsgCount(msgCount);
			ImageView headerIconPlay = (ImageView) view
					.findViewById(R.id.header_icon_play);
			holder.setHeader_icon_play(headerIconPlay);

			RelativeLayout layout_defence_btn = (RelativeLayout) view
					.findViewById(R.id.layout_defence_btn);
			holder.setLayout_defence_btn(layout_defence_btn);
			ImageView image_defence_state = (ImageView) view
					.findViewById(R.id.image_defence_state);
			holder.setImage_defence_state(image_defence_state);
			ProgressBar progress_defence = (ProgressBar) view
					.findViewById(R.id.progress_defence);
			holder.setProgress_defence(progress_defence);
			holder.ivWeakPassWord = (ImageView) view
					.findViewById(R.id.iv_weakpassword);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		final Contact contact = FList.getInstance().get(position);
		int deviceType = contact.contactType;
		if (contact.onLineState == Constants.DeviceState.ONLINE) {
			System.out.println("dxsmonitordefence");
			holder.getHead().updateImage(contact.contactId, false);
			holder.getOnline_state().setText(R.string.online_state);
			holder.getOnline_state().setTextColor(
					context.getResources().getColor(R.color.text_color_blue));
			if (contact.contactType == P2PValue.DeviceType.UNKNOWN
					|| contact.contactType == P2PValue.DeviceType.PHONE) {
				holder.getLayout_defence_btn().setVisibility(
						RelativeLayout.GONE);
			} else {
				holder.getLayout_defence_btn().setVisibility(
						RelativeLayout.VISIBLE);
				Log.i("dxsmonitordefence", "id-->"+contact.contactId+"contact.defenceState-->"+contact.defenceState);
				if (contact.defenceState == Constants.DefenceState.DEFENCE_STATE_LOADING) {
					holder.getProgress_defence().setVisibility(
							RelativeLayout.VISIBLE);
					holder.getImage_defence_state().setVisibility(
							RelativeLayout.GONE);
				} else if (contact.defenceState == Constants.DefenceState.DEFENCE_STATE_ON) {
					holder.getProgress_defence().setVisibility(
							RelativeLayout.GONE);
					holder.getImage_defence_state().setVisibility(
							RelativeLayout.VISIBLE);
					holder.getImage_defence_state().setImageResource(
							R.drawable.ic_defence_on);

				} else if (contact.defenceState == Constants.DefenceState.DEFENCE_STATE_OFF) {
					holder.getProgress_defence().setVisibility(
							RelativeLayout.GONE);
					holder.getImage_defence_state().setVisibility(
							RelativeLayout.VISIBLE);
					holder.getImage_defence_state().setImageResource(
							R.drawable.ic_defence_off);

				} else if (contact.defenceState == Constants.DefenceState.DEFENCE_STATE_WARNING_NET) {
					holder.getProgress_defence().setVisibility(
							RelativeLayout.GONE);
					holder.getImage_defence_state().setVisibility(
							RelativeLayout.VISIBLE);
					holder.getImage_defence_state().setImageResource(
							R.drawable.ic_defence_warning);
				} else if (contact.defenceState == Constants.DefenceState.DEFENCE_STATE_WARNING_PWD) {
					holder.getProgress_defence().setVisibility(
							RelativeLayout.GONE);
					holder.getImage_defence_state().setVisibility(
							RelativeLayout.VISIBLE);
					holder.getImage_defence_state().setImageResource(
							R.drawable.ic_defence_warning);
				} else if (contact.defenceState == Constants.DefenceState.DEFENCE_NO_PERMISSION) {
					holder.getProgress_defence().setVisibility(
							RelativeLayout.GONE);
					holder.getImage_defence_state().setVisibility(
							RelativeLayout.VISIBLE);
					holder.getImage_defence_state().setImageResource(
							R.drawable.limit);
				}
			}
			// 如果是门铃且不是访客密码则获取报警推送账号并判断自己在不在其中，如不在则添加(只执行一次)
			if (deviceType == P2PValue.DeviceType.DOORBELL
					&& contact.defenceState != Constants.DefenceState.DEFENCE_NO_PERMISSION) {
				if (!getIsDoorBellBind(contact.contactId)) {
					getBindAlarmId(contact.contactId, contact.contactPassword);
				} else {

				}
			}

		} else {
			holder.getHead().updateImage(contact.contactId, true);
			holder.getOnline_state().setText(R.string.offline_state);
			holder.getOnline_state().setTextColor(
					context.getResources().getColor(R.color.text_color_gray));
			holder.getLayout_defence_btn().setVisibility(RelativeLayout.GONE);
		}

		// 获得布防状态之后判断弱密码
		if (contact.onLineState == Constants.DeviceState.ONLINE
				&& (contact.defenceState == Constants.DefenceState.DEFENCE_STATE_ON || contact.defenceState == Constants.DefenceState.DEFENCE_STATE_OFF)) {
			if (Utils.isWeakPassword(contact.contactPassword)) {
				holder.ivWeakPassWord.setVisibility(View.VISIBLE);
			} else {
				holder.ivWeakPassWord.setVisibility(View.GONE);
			}
		} else {
			holder.ivWeakPassWord.setVisibility(View.GONE);
		}

		switch (deviceType) {
		case P2PValue.DeviceType.NPC:
			holder.getLogin_type().setImageResource(
					R.drawable.ic_device_type_npc);
			break;
		case P2PValue.DeviceType.IPC:
			holder.getLogin_type().setImageResource(
					R.drawable.ic_device_type_ipc);
			break;
		case P2PValue.DeviceType.PHONE:
			holder.getLogin_type().setImageResource(
					R.drawable.ic_device_type_phone);
			break;
		case P2PValue.DeviceType.DOORBELL:
			holder.getLogin_type().setImageResource(
					R.drawable.ic_device_type_door_bell);
			break;
		case P2PValue.DeviceType.UNKNOWN:
			holder.getLogin_type().setImageResource(
					R.drawable.ic_device_type_unknown);
			break;
		default:
			holder.getLogin_type().setImageResource(
					R.drawable.ic_device_type_unknown);
			break;
		}
		if (contact.messageCount > 0) {
			TextView msgCount = holder.getMsgCount();
			msgCount.setVisibility(RelativeLayout.VISIBLE);
			if (contact.messageCount > 10) {
				msgCount.setText("10+");
			} else {
				msgCount.setText(contact.messageCount + "");
			}

		} else {
			holder.getMsgCount().setVisibility(RelativeLayout.GONE);
		}

		holder.getName().setText(contact.contactName);
		
		if (deviceType == P2PValue.DeviceType.NPC
				|| deviceType == P2PValue.DeviceType.IPC
				|| deviceType == P2PValue.DeviceType.DOORBELL) {
			holder.getHead().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					if (null != FList.getInstance().isContactUnSetPassword(
							contact.contactId)) {
						return;
					}
					if (contact.contactId == null
							|| contact.contactId.equals("")) {
						T.showShort(context, R.string.username_error);
						return;
					}
					if (contact.contactPassword == null
							|| contact.contactPassword.equals("")) {
						
						T.showShort(context, R.string.password_error);
						return;
					}
					Intent monitor = new Intent();
					monitor.setClass(context, CallActivity.class);
					monitor.putExtra("callId", contact.contactId);
					monitor.putExtra("contactName", contact.contactName);
					monitor.putExtra("password", contact.contactPassword);
					monitor.putExtra("isOutCall", true);
					monitor.putExtra("type",
							Constants.P2P_TYPE.P2P_TYPE_MONITOR);
					monitor.putExtra("contactType", contact.contactType);
					context.startActivity(monitor);
				}

			});
			holder.getHeader_icon_play().setVisibility(RelativeLayout.VISIBLE);
		} else if (deviceType == P2PValue.DeviceType.PHONE) {
			holder.getHead().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					if (contact.contactId == null
							|| contact.contactId.equals("")) {
						T.showShort(context, R.string.username_error);
						return;
					}

					Intent call = new Intent();
					call.setClass(context, CallActivity.class);
					call.putExtra("callId", contact.contactId);
					call.putExtra("isOutCall", true);
					call.putExtra("type", Constants.P2P_TYPE.P2P_TYPE_CALL);
					context.startActivity(call);
				}

			});

			holder.getHeader_icon_play().setVisibility(RelativeLayout.VISIBLE);
		} else {
			if (Integer.parseInt(contact.contactId) < 256) {
				
				holder.getHead().setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
						Intent monitor = new Intent();
						monitor.setClass(context, CallActivity.class);
						monitor.putExtra("callId", contact.contactId);
						monitor.putExtra("contactName", contact.contactName);
						monitor.putExtra("password", contact.contactPassword);
						monitor.putExtra("isOutCall", true);
						monitor.putExtra("type",
								Constants.P2P_TYPE.P2P_TYPE_MONITOR);
						context.startActivity(monitor);
					}
				});
			} else {
				holder.getHead().setOnClickListener(null);
				holder.getHeader_icon_play().setVisibility(RelativeLayout.GONE);
			}

		}

		holder.getLayout_defence_btn().setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (contact.defenceState == Constants.DefenceState.DEFENCE_STATE_WARNING_NET
								|| contact.defenceState == Constants.DefenceState.DEFENCE_STATE_WARNING_PWD) {
							holder.getProgress_defence().setVisibility(
									RelativeLayout.VISIBLE);
							holder.getImage_defence_state().setVisibility(
									RelativeLayout.GONE);
							String pwd=P2PHandler.getInstance().EntryPassword(contact.contactPassword);
							P2PHandler.getInstance().getDefenceStates(
									contact.contactId, pwd);
							FList.getInstance().setIsClickGetDefenceState(
									contact.contactId, true);
							FList.getInstance().updateOnlineState();
						} else if (contact.defenceState == Constants.DefenceState.DEFENCE_STATE_ON) {
							Dialog alertDialog = new AlertDialog.Builder(context)
								.setTitle("确定取消布防吗？")
								.setMessage("取消布防后，摄像头将不会发出报警消息")
								.setPositiveButton("是", new DialogInterface.OnClickListener() {   
							    @Override   
							    public void onClick(DialogInterface dialog, int which) {   
							    	holder.getProgress_defence().setVisibility(
											RelativeLayout.VISIBLE);
									holder.getImage_defence_state().setVisibility(
											RelativeLayout.GONE);
									String pwd=P2PHandler.getInstance().EntryPassword(contact.contactPassword);
									P2PHandler
											.getInstance()
											.setRemoteDefence(
													contact.contactId,
													pwd,
													Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_OFF);
									FList.getInstance().setIsClickGetDefenceState(
											contact.contactId, true);
									FList.getInstance().updateOnlineState();
							    }   
							})
							.setNegativeButton("否", null)
							    .create();   
							alertDialog.show();   
							
						} else if (contact.defenceState == Constants.DefenceState.DEFENCE_STATE_OFF) {
							holder.getProgress_defence().setVisibility(
									RelativeLayout.VISIBLE);
							holder.getImage_defence_state().setVisibility(
									RelativeLayout.GONE);
							String pwd=P2PHandler.getInstance().EntryPassword(contact.contactPassword);
							P2PHandler
									.getInstance()
									.setRemoteDefence(
											contact.contactId,
											pwd,
											Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_ON);
							FList.getInstance().setIsClickGetDefenceState(
									contact.contactId, true);
							FList.getInstance().updateOnlineState();
						}
					}

				});
		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setAction(Constants.Action.DIAPPEAR_ADD);
				context.sendBroadcast(it);
				return false;
			}
		});
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				LocalDevice localDevice = FList.getInstance()
						.isContactUnSetPassword(contact.contactId);
				if (null != localDevice) {
					System.out.println("dddddddd");
					Contact saveContact = new Contact();
					saveContact.contactId = localDevice.contactId;
					saveContact.contactType = localDevice.type;
					saveContact.messageCount = 0;
					saveContact.activeUser = NpcCommon.mThreeNum;

					Intent modify = new Intent();
					modify.setClass(context, AddContactNextActivity.class);
					modify.putExtra("isCreatePassword", true);
					modify.putExtra("contact", saveContact);
					String mark = localDevice.address.getHostAddress();
					modify.putExtra(
							"ipFlag",
							mark.substring(mark.lastIndexOf(".") + 1,
									mark.length()));
					context.startActivity(modify);
					return;
				}

				cf.showQuickActionBar(arg0, contact);
			}

		});

		view.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				regFilter();
				P2PHandler.getInstance().getBindAlarmId(contact.contactId,contact.contactPassword);
				NormalDialog dialog = new NormalDialog(context, context
						.getResources().getString(R.string.delete_contact),
						context.getResources().getString(
								R.string.are_you_sure_delete)
								+ " " + contact.contactId + "?", context
								.getResources().getString(R.string.delete),
						context.getResources().getString(R.string.cancel));
				dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

					@Override
					public void onClick() {
						// TODO Auto-generated method stub
						//删除摄像头
						mQueue = Volley.newRequestQueue(context);  
						StringRequest stringRequest = new StringRequest(Method.POST, Constants.DEL_CAMERA_URL,  
								new Response.Listener<String>() {
									@Override
									public void onResponse(String response) {
										FList.getInstance().delete(contact, position, handler);
										File file = new File(Constants.Image.USER_HEADER_PATH
												+ NpcCommon.mThreeNum + "/" + contact.contactId);
										Utils.deleteFile(file);
										if (position == 0 && FList.getInstance().size() == 0) {
											Intent it = new Intent();
											it.setAction(Constants.Action.DELETE_DEVICE_ALL);
											MyApp.app.sendBroadcast(it);
										}
										
										int pos=0;
										if(null!=last_bind_data&&last_bind_data.length>0){
											for(int i=0;i<last_bind_data.length;i++){
												if(NpcCommon.mThreeNum.equalsIgnoreCase(last_bind_data[i])){
													pos=i;
													String[] data = Utils.getDeleteAlarmIdArray(
															last_bind_data, pos);
													P2PHandler.getInstance().setBindAlarmId(
															contact.contactId,contact.contactPassword,
															data.length, data);
												}
											}
										}
										String defenceID = NpcCommon.mThreeNum+contact.contactId;
										DataManager.deleteDefence(context, defenceID);
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
								map.put("mCameraID", contact.contactId);
								map.put("mUserID", NpcCommon.mThreeNum);
								return map;
							}
						};
						mQueue.add(stringRequest);
					}
				});
				dialog.showDialog();
				return true;
			}

		});
		return view;
		// } else {
		// // 显示没有设置密码的的设备
		// View view = convertView;
		// final ViewHolder2 holder2;
		// if (view == null) {
		// view = LayoutInflater.from(context).inflate(
		// R.layout.list_contact_item2, null);
		// holder2 = new ViewHolder2();
		// TextView name = (TextView) view.findViewById(R.id.user_name);
		// holder2.setName(name);
		// ImageView typeImg = (ImageView) view
		// .findViewById(R.id.login_type);
		// holder2.setDevice_type(typeImg);
		// view.setTag(holder2);
		// } else {
		// holder2 = (ViewHolder2) view.getTag();
		// }
		// final LocalDevice localDevice =
		// FList.getInstance().getUnsetPasswordLocalDevices().get(position -
		// size1);
		// holder2.name.setText(localDevice.getContactId());
		// switch (localDevice.getType()) {
		// case P2PValue.DeviceType.NPC:
		// holder2.device_type
		// .setImageResource(R.drawable.ic_device_type_npc);
		// break;
		// case P2PValue.DeviceType.IPC:
		// holder2.device_type
		// .setImageResource(R.drawable.ic_device_type_ipc);
		// break;
		// case P2PValue.DeviceType.DOORBELL:
		// holder2.device_type
		// .setImageResource(R.drawable.ic_device_type_door_bell);
		// break;
		// case P2PValue.DeviceType.UNKNOWN:
		// holder2.device_type
		// .setImageResource(R.drawable.ic_device_type_unknown);
		// break;
		// default:
		// holder2.device_type
		// .setImageResource(R.drawable.ic_device_type_unknown);
		// break;
		// }
		// view.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // 跳转到设置密码页面
		// Contact saveContact = new Contact();
		// saveContact.contactId = localDevice.contactId;
		// saveContact.contactType = localDevice.type;
		// saveContact.messageCount = 0;
		// saveContact.activeUser = NpcCommon.mThreeNum;
		// Intent modify = new Intent();
		// modify.setClass(context, AddContactNextActivity.class);
		// modify.putExtra("isCreatePassword", true);
		// modify.putExtra("contact", saveContact);
		// String mark = localDevice.address.getHostAddress();
		// modify.putExtra("ipFlag", mark.substring(mark.lastIndexOf(".")+1,
		// mark.length()));
		// context.startActivity(modify);
		//
		// }
		// });
		// return view;
		// }
	}

	Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			notifyDataSetChanged();
			return true;
		}
	});

	List<String> doorbells = new ArrayList<String>();
	Map<String, String[]> idMaps = new HashMap<String, String[]>();

	private void getBindAlarmId(String id, String password) {
		if (!doorbells.contains(id)) {
			doorbells.add(id);
		}
		String pwd=P2PHandler.getInstance().EntryPassword(password);
		P2PHandler.getInstance().getBindAlarmId(id, pwd);
		Log.e("dxsdoorbell", "获得报警列表id-->" + id + "password" + password
				+ "doorbells-->" + doorbells.size());
	}

	public void getAllBindAlarmId() {
		for (String ss : doorbells) {
			getBindAlarmId(ss);
		}
	}

	private int count = 0;// 总请求数计数器
	private int SumCount = 20;// 总请求次数上限

	public void getBindAlarmId(String id) {

		Contact contact = DataManager.findContactByActiveUserAndContactId(
				context, NpcCommon.mThreeNum, id);
		if (contact != null && count <= SumCount) {
			// 获取绑定id列表
			String pwd=P2PHandler.getInstance().EntryPassword(contact.contactPassword);
			P2PHandler.getInstance().getBindAlarmId(contact.contactId,
					pwd);
			Log.e("dxsdoorbell", "获得报警列表id-->" + id);
			count++;
		}
	}

	public void setBindAlarmId(String id, String[] ids) {
		int ss = 0;
		String[] new_data;
		for (int i = 0; i < ids.length; i++) {
			if (!NpcCommon.mThreeNum.equals(ids[i])) {
				ss++;
			}
		}
		if (ss == ids.length) {
			// 不包含则设置
			new_data = new String[ids.length + 1];
			for (int i = 0; i < ids.length; i++) {
				new_data[i] = ids[i];
			}
			new_data[new_data.length - 1] = NpcCommon.mThreeNum;
			Contact contact = DataManager.findContactByActiveUserAndContactId(
					context, NpcCommon.mThreeNum, id);
			String pwd=P2PHandler.getInstance().EntryPassword(contact.contactPassword);
			P2PHandler.getInstance().setBindAlarmId(contact.contactId,
					pwd, new_data.length, new_data);
			Log.e("dxsdoorbell", "设置报警列表id-->" + id + "ids大小-->" + ids.length);
		} else {
			new_data = ids;
		}
		idMaps.put(id, new_data);

	}

	public void setBindAlarmId(String Id) {
		Contact contact = DataManager.findContactByActiveUserAndContactId(
				context, NpcCommon.mThreeNum, Id);
		if (contact != null && (!idMaps.isEmpty())) {
			String[] new_data = idMaps.get(Id);
			String pwd=P2PHandler.getInstance().EntryPassword(contact.contactPassword);
			P2PHandler.getInstance().setBindAlarmId(contact.contactId,
					pwd, new_data.length, new_data);
			Log.e("dxsdoorbell", "设置报警列表id-->" + Id + "ids大小-->"
					+ new_data.length);
		}

	}

	public void setBindAlarmIdSuccess(String doorbellid) {
		SharedPreferencesManager.getInstance().putIsDoorbellBind(doorbellid,
				true, context);
	}

	private boolean getIsDoorBellBind(String doorbellid) {
		return SharedPreferencesManager.getInstance().getIsDoorbellBind(
				context, doorbellid);
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

}
