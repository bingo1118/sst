package com.jwkj.global;

import cn.jpush.android.service.PushService;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimeBootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
	    if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {  
		    //检查Service状态 
	    	boolean mainFlag=false;
	    	boolean pushFlag=false;
		    ActivityManager manager = (ActivityManager) context.getApplicationContext()
		    		.getSystemService(Context.ACTIVITY_SERVICE);
		    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) { 
			    if("com.jwkj.global.MainService".equals(service.service.getClassName())){ 
			    	mainFlag=true;
			    } 
			    if("cn.jpush.android.service.PushService".equals(service.service.getClassName())){
			    	pushFlag=true;
			    }
		    } 
		    
		    if(!mainFlag){
		    	Intent i = new Intent(context, MainService.class); 
		    	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    context.startService(i);  
		    }
		    if(!pushFlag){
		    	Intent j = new Intent(context, PushService.class);
		    	j.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    context.startService(j); 
		    }
	    } 
	}

}
