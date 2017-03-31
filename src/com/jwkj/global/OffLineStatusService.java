package com.jwkj.global;

import java.util.Timer;
import java.util.TimerTask;

import com.jwkj.entity.Account;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class OffLineStatusService extends Service{
	
	private FList flist;
	private Timer mTimer;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		mTimer = new Timer();
		setTimerdoAction(doActionHandler,mTimer);
	}
	
	private void setTimerdoAction(final Handler oj,Timer t) { 
        t.schedule(new TimerTask() {  
            @Override  
            public void run() {
            	flist = FList.getInstance();
            	if(null!=flist){
            		Message message = new Message();	
    				message = oj.obtainMessage();
    				message.what = 2; 
    	    		oj.sendMessage(message);
            	}
	    		
            }  
        }, 1000, 30000/* 表示1000毫秒之後，每隔1000毫秒執行一次 */);  
    } 
	
	private Handler doActionHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 2:
				flist.updateOnlineState();
				for(int i=0;i<flist.size();i++){
					System.out.println("flist.get"+i+"="+flist.get(i).onLineState);
				}
				
				
				break;
			default:
				break;
			}
		}
	};

}
