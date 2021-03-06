package com.jwkj.data;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jwkj.entity.Account;
import com.jwkj.global.AccountPersist;
import com.jwkj.global.Constants;
import com.jwkj.global.MyApp;
import com.jwkj.global.NpcCommon;

public class DBHelper extends SQLiteOpenHelper {
	private SQLiteDatabase mDB = null;
	private Context context;
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		
		if (null == mDB) {
			mDB = db;
		}
		
		try{
			db.execSQL(MessageDB.getCreateTableString());
		}catch(Exception e){
			Log.e("my","db existed");
		}
		
		try{
			db.execSQL(SysMessageDB.getCreateTableString());
		}catch(Exception e){
			Log.e("my","db existed");
		}
		
		try{
			db.execSQL(AlarmMaskDB.getCreateTableString());
		}catch(Exception e){
			Log.e("my","db existed");
		}
		
		try{
			db.execSQL(AlarmRecordDB.getCreateTableString());
		}catch(Exception e){
			Log.e("my","db existed");
		}
		
		try{
			db.execSQL(NearlyTellDB.getCreateTableString());
		}catch(Exception e){
			Log.e("my","db existed");
		}
		
		try{
			db.execSQL(ContactDB.getCreateTableString());
		}catch(Exception e){
			Log.e("my","db existed");
		}
		
		try{
			db.execSQL(DefenceDB.getCreateTableString());
		}catch(Exception e){
			Log.e("my","db existed");
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("oldVersion="+oldVersion);
		System.out.println("newVersion=="+newVersion);
		if(oldVersion<10){
			String recentName = SharedPreferencesManager.getInstance().getData(MyApp.app, SharedPreferencesManager.SP_FILE_GWELL, SharedPreferencesManager.KEY_RECENTNAME);
			if(!recentName.equals("")){
				if(recentName.charAt(0)!='0'){
					SharedPreferencesManager.getInstance().putData(MyApp.app, SharedPreferencesManager.SP_FILE_GWELL, SharedPreferencesManager.KEY_RECENTNAME_EMAIL, "0"+recentName);
				}else{
					SharedPreferencesManager.getInstance().putData(MyApp.app, SharedPreferencesManager.SP_FILE_GWELL, SharedPreferencesManager.KEY_RECENTNAME_EMAIL, recentName);
				}
			}
			
			SharedPreferencesManager.getInstance().putData(MyApp.app, SharedPreferencesManager.SP_FILE_GWELL, SharedPreferencesManager.KEY_RECENTNAME, "");
			SharedPreferencesManager.getInstance().putRecentLoginType(MyApp.app, Constants.LoginType.EMAIL);
			
		}
		
		if(oldVersion<13){
			Account account = AccountPersist.getInstance().getActiveAccountInfo(MyApp.app);
			if(null!=account){
				account.three_number = "0"+account.three_number;
				AccountPersist.getInstance().setActiveAccount(MyApp.app, account);
				NpcCommon.mThreeNum = AccountPersist.getInstance().getActiveAccountInfo(MyApp.app).three_number;
			}
		}
		
		if(oldVersion<21){
			db.execSQL("DROP TABLE IF EXISTS message");
			db.execSQL(MessageDB.getCreateTableString());
			
			db.execSQL("DROP TABLE IF EXISTS sysMsg");
			db.execSQL(SysMessageDB.getCreateTableString());
			
			db.execSQL("DROP TABLE IF EXISTS allarm_mask");
			db.execSQL(AlarmMaskDB.getCreateTableString());
			
			db.execSQL("DROP TABLE IF EXISTS alarm_record");
			db.execSQL(AlarmRecordDB.getCreateTableString());
			
			db.execSQL("DROP TABLE IF EXISTS nearly_tell");
			db.execSQL(NearlyTellDB.getCreateTableString());
			
			
			Cursor cursor = db.rawQuery("SELECT * FROM contant_friends", null);
			List<Contact> lists = new ArrayList<Contact>();
			if(cursor!=null){
	    		while(cursor.moveToNext()){
	    			String contactName = cursor.getString(cursor.getColumnIndex("name"));
	    			String contactId = cursor.getString(cursor.getColumnIndex("threeAccount"));
	    			String contactPassword = cursor.getString(cursor.getColumnIndex("threePwd"));
	    			int contactType = cursor.getInt(cursor.getColumnIndex("device_type"));
	    			int messageCount = cursor.getInt(cursor.getColumnIndex("msgCount"));
	    			String activeUser = cursor.getString(cursor.getColumnIndex("uId"));
	    			Contact data = new Contact();
	    			data.contactName = contactName;
	    			data.contactId = contactId;
	    			data.contactPassword = contactPassword;
	    			data.contactType = contactType;
	    			data.messageCount = messageCount;
	    			if(activeUser.charAt(0)!='0'){
	    				activeUser = "0" + activeUser;
	    			}
	    			data.activeUser = activeUser;
	    			lists.add(data);
	    		}
	    		cursor.close();
	    	}
			db.execSQL("DROP TABLE IF EXISTS contant_friends");
			db.execSQL(ContactDB.getCreateTableString());
			
			for(Contact contact : lists){
				
				ContactDB contactDB = new ContactDB(db);
		    	contactDB.insert(contact);
			}
			
		}
		
		if(oldVersion<23){
			db.beginTransaction();
			try {
				db.execSQL("ALTER TABLE defence_channel ADD COLUMN alarmType varchar");
				db.setTransactionSuccessful();
			} catch (Throwable ex) {
				 ex.printStackTrace();
			} finally {
				 db.endTransaction();
			}
		}
		if(oldVersion<25){
			db.beginTransaction();
			try {
				db.execSQL("ALTER TABLE alarm_record ADD COLUMN alarmInfo varchar");
				db.setTransactionSuccessful();
			} catch (Throwable ex) {
				 ex.printStackTrace();
			} finally {
				 db.endTransaction();
			}
		}
		onCreate(db);
	}
}

