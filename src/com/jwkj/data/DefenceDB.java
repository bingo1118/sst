package com.jwkj.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

public class DefenceDB {
	public static final String TABLE_NAME = "defence";
	public static final String COLUMN_DEFENCEID = "defenceID";
	public static final String COLUMN_DEFENCEID_DATA_TYPE = "varchar PRIMARY KEY";

	public static final String COLUMN_DEFENCENAME = "defenceName";
	public static final String COLUMN_DEFENCENAME_DATA_TYPE = "varchar";

	public static final String COLUMN_ALARMTYPE = "alarmType";
	public static final String COLUMN_ALARMTYPE_DATA_TYPE = "varchar";
	
	private SQLiteDatabase myDatabase;

	public DefenceDB(SQLiteDatabase myDatabase) {
		this.myDatabase = myDatabase;
	}

	public static String getDeleteTableSQLString() {
		return SqlHelper.formDeleteTableSqlString(TABLE_NAME);
	}

	public static String getCreateTableString() {
		HashMap<String, String> columnNameAndType = new HashMap<String, String>();
		columnNameAndType.put(COLUMN_DEFENCEID, COLUMN_DEFENCEID_DATA_TYPE);
		columnNameAndType.put(COLUMN_DEFENCENAME, COLUMN_DEFENCENAME_DATA_TYPE);
		columnNameAndType.put(COLUMN_ALARMTYPE, COLUMN_ALARMTYPE_DATA_TYPE);
		String mSQLCreateWeiboInfoTable = SqlHelper.formCreateTableSqlString(
				TABLE_NAME, columnNameAndType);
		return mSQLCreateWeiboInfoTable;
	}
	
	public long insert(String defenceID) {
		long isResut = -1;
		String[] defenceNamestr = {"遥控","大厅","窗户","阳台","卧室","厨房","庭院","门锁","其它"};
		String[] namestr = {"1","2","3","4","5","6","7","8"};
		if (defenceID != null) {
			myDatabase.beginTransaction();
			for(int i=0;i<9;i++){
				ContentValues values = new ContentValues();
				values.put(COLUMN_DEFENCEID, defenceID+"0"+i+"");
				values.put(COLUMN_DEFENCENAME, defenceNamestr[i]);
				values.put(COLUMN_ALARMTYPE, "");
				try {
					isResut = myDatabase.insertOrThrow(TABLE_NAME, null, values);
				} catch (SQLiteConstraintException e) {
					e.printStackTrace();
				}
				for(int j=0;j<8;j++){
					ContentValues values1 = new ContentValues();
					values1.put(COLUMN_DEFENCEID, defenceID+"0"+i+""+"00"+j+"");
					values1.put(COLUMN_DEFENCENAME, namestr[j]);
					values1.put(COLUMN_ALARMTYPE, "");
					try {
						isResut = myDatabase.insertOrThrow(TABLE_NAME, null, values1);
					} catch (SQLiteConstraintException e) {
						e.printStackTrace();
					}
				}
			}
			myDatabase.setTransactionSuccessful();
			myDatabase.endTransaction();
		}
		return isResut;
	}
	
	public Map<String,String> findDefenceByDefenceID(String defenceID) {
		Cursor cursor = null;
		Map<String,String> map = new HashMap<String,String>();
		cursor = myDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
				+ COLUMN_DEFENCEID + " like ?", new String[] { defenceID+"%" });
		if (cursor != null) {
			while (cursor.moveToNext()) {
				String defenceIDStr = cursor.getString(cursor
						.getColumnIndex(COLUMN_DEFENCEID));
				String defenceName = cursor.getString(cursor
						.getColumnIndex(COLUMN_DEFENCENAME))+"     "+cursor.getString(cursor
								.getColumnIndex(COLUMN_ALARMTYPE));
				map.put(defenceIDStr, defenceName);
			}
			cursor.close();
		}
		return map;
	}
	
	public List<String> findDefenceAlarmType(String defenceID) {
		Cursor cursor = null;
		List<String> list = new ArrayList<String>();
		cursor = myDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
				+ COLUMN_ALARMTYPE + " IS NOT NULL AND "
				+ COLUMN_ALARMTYPE + " <>'' AND "
				+ COLUMN_DEFENCEID + " like ?", new String[] { defenceID+"%" });
		if (cursor != null) {
			while (cursor.moveToNext()) {
				String defenceIDStr = cursor.getString(cursor
						.getColumnIndex(COLUMN_DEFENCEID));
				list.add(defenceIDStr);
			}
			cursor.close();
		}
		return list;
	}
	
	public boolean findDefenceExit(String defenceID) {
		Cursor cursor = null;
		cursor = myDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
				+ COLUMN_DEFENCEID + " like ?", new String[] { defenceID+"%" });
		if(cursor.moveToNext()){
			return true;
		}
		return false;
	}
	
	public void updateDefence(String defenceID,String defenceName) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_DEFENCENAME, defenceName);
		try {
			myDatabase.update(TABLE_NAME, values, COLUMN_DEFENCEID + "=?", new String[] {defenceID});
		} catch (SQLiteConstraintException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteDefence(String defenceID) {
		try {
			myDatabase.delete(TABLE_NAME, COLUMN_DEFENCEID + " like ?", new String[] {defenceID+"%"});
		} catch (SQLiteConstraintException e) {
			e.printStackTrace();
		}
	}

	public void updateDefenceAlarmType(String defenceID,String alarmType) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_ALARMTYPE, alarmType);
		try {
			myDatabase.update(TABLE_NAME, values, COLUMN_DEFENCEID + " like ?",
					new String[] { "%"+defenceID+"%"});
		} catch (SQLiteConstraintException e) {
			e.printStackTrace();
		}
	}
	
	public void updateListDefenceAlarmType(List<String> defenceID,String alarmType) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_ALARMTYPE, alarmType);
		try {
			for(String s : defenceID){
				myDatabase.update(TABLE_NAME, values, COLUMN_DEFENCEID + " = ?",
						new String[] { s});
			}
			
		} catch (SQLiteConstraintException e) {
			e.printStackTrace();
		}
	}
	
	public void updateDefenceFromServer(String defenceID,String alarmType,String defenceName) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_ALARMTYPE, alarmType);
		values.put(COLUMN_DEFENCENAME, defenceName);
		try {
			myDatabase.update(TABLE_NAME, values, COLUMN_DEFENCEID + " =? ",
					new String[] { defenceID});
		} catch (SQLiteConstraintException e) {
			e.printStackTrace();
		}
	}
	
	public String findDefence(String defenceID) {
		Cursor cursor = null;
		String defenceName = null;
		cursor = myDatabase.rawQuery("SELECT defenceName FROM " + TABLE_NAME + " WHERE "
				+ COLUMN_DEFENCEID + "=?", new String[] { defenceID});
		if(cursor.moveToNext()){
			defenceName = cursor.getString(cursor.getColumnIndex(COLUMN_DEFENCENAME));
		}
		
		if(null!=defenceName){
			cursor.close();
			return defenceName;
		}else{
			cursor.close();
			return null;
		}
	}
	
	public Map<String,String> findDefenceMap(String defenceID) {
		Cursor cursor = null;
		cursor = myDatabase.rawQuery("SELECT defenceName,alarmType FROM " + TABLE_NAME + " WHERE "
				+ COLUMN_DEFENCEID + "=?", new String[] { defenceID });
		if(null!=cursor&&cursor.moveToNext()){
			String defenceName = cursor.getString(0);
			String alarmType = cursor.getString(1);
			Map<String,String> map = new HashMap<String,String>();
			map.put("defenceName", defenceName);
			map.put("alarmType", alarmType);
			cursor.close();
			return map;
		}else{
			if(null!=cursor){
				cursor.close();
			}
			return null;
		}
	}
	
	public boolean findExit(String defenceID) {
		Cursor cursor = null;
		cursor = myDatabase.rawQuery("SELECT alarmType FROM " + TABLE_NAME + " WHERE "
				+ COLUMN_DEFENCEID + " =?", new String[] { defenceID });
		if(null!=cursor&&cursor.moveToNext()){
			String str = cursor.getString(0);
			if(null!=str&&str.length()>0){
				return true;
			}
			return false;
		}else{
			if(null!=cursor){
				cursor.close();
			}
			return false;
		}
	}
	
	public void updateAlarmType(List<Defence> defence) {

		try {
			for(Defence mDefence : defence){
				ContentValues values = new ContentValues();
				values.put(COLUMN_ALARMTYPE, mDefence.alarmType);
				int I = myDatabase.update(TABLE_NAME, values, COLUMN_DEFENCEID + " like ?",
						new String[] { "%"+mDefence.mDefenceID});
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateCameraAlarmType(String defenceID) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_ALARMTYPE, "");
		try {
			myDatabase.update(TABLE_NAME, values, COLUMN_DEFENCEID + " like ?",
					new String[] { defenceID+"%"});
		} catch (SQLiteConstraintException e) {
			e.printStackTrace();
		}
	}
}
