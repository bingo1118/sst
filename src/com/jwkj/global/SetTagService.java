package com.jwkj.global;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.example.jpushdemo.ExampleUtil;
import com.jwkj.data.SharedPreferencesManager;
import com.jwkj.entity.Account;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

public class SetTagService extends Service{
	private static final String TAG = "SetTagService";
	private Context mContext;
	private static String strName;
	private Timer mTimer;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mContext = getApplicationContext();
		Account account = AccountPersist.getInstance()
				.getActiveAccountInfo(mContext);
		strName = account.three_number;
		mTimer = new Timer();
		setTimerdoAction(doActionHandler,mTimer);
		
	}
	
	//jpush
	private void setTag(String tag){
        // 检查 tag 的有效性
		if (TextUtils.isEmpty(tag)) {
			return;
		}
		
		// ","隔开的多个 转换成 Set
		String[] sArray = tag.split(",");
		Set<String> tagSet = new LinkedHashSet<String>();
		for (String sTagItme : sArray) {
			if (!ExampleUtil.isValidTagAndAlias(sTagItme)) {
				return;
			}
			tagSet.add(sTagItme);
		}
		
		//调用JPush API设置Tag
		jpushHandler.sendMessage(jpushHandler.obtainMessage(MSG_SET_TAGS, tagSet));
	} 
	
	private static final int MSG_SET_TAGS = 1002;
	private final Handler jpushHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case MSG_SET_TAGS:
                Log.d(TAG, "Set tags in handler.");
                JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                break;
                
            default:
                Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };
    
    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
            case 0:
                logs = "Set tag and alias success";
                Log.i(TAG, logs);
                if(null!=mTimer){
                	mTimer.cancel();
                }
                stopSelf();
                break;
                
            case 6002:
                logs = "Failed to set alias and tags due to timeout. Try again after 6s.";
                Log.i(TAG, logs);
                if (ExampleUtil.isConnected(getApplicationContext())) {
                	jpushHandler.sendMessageDelayed(jpushHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 6);
                } else {
                	Log.i(TAG, "No network");
                }
                break;
            
            default:
                logs = "Failed with errorCode = " + code;
                Log.e(TAG, logs);
            }
            //ExampleUtil.showToast(logs, getApplicationContext());
        } 
    };
    
    private void setTimerdoAction(final Handler oj,Timer t) { 
        t.schedule(new TimerTask() {  
            @Override  
            public void run() {
	    		Message message = new Message();	
				message = oj.obtainMessage();
				message.what = 2; 
	    		oj.sendMessage(message);
            }  
        }, 1000, 7000/*表示1000毫秒之後，每隔1000毫秒執行一次 */);  
    } 
    
    private Handler doActionHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 2:
				setTag(strName);
				break;
			default:
				break;
			}
		}
	};
}
