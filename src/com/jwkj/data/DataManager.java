package com.jwkj.data;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataManager {
	public static final String TAG = "NpcData";
	public static final String DataBaseName = "NpcDatabase.db";
	public static final int DataBaseVersion = 25;

	/*
	 * MessageDB
	 */

	// 查找消息 parameter: 1.Context 2.当前登录用户 3. 对方ID
	public static synchronized List<Message> findMessageByActiveUserAndChatId(
			Context context, String activeUserId, String chatId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			MessageDB messageDB = new MessageDB(db);
			List<Message> list = messageDB.findMessageByActiveUserAndChatId(
					activeUserId, chatId);
			db.close();
			return list;
		}
	}

	// 插入消息 parameter: 1.Context 2.Message
	public static synchronized void insertMessage(Context context, Message msg) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			MessageDB messageDB = new MessageDB(db);
			messageDB.insert(msg);
			db.close();
		}
	}

	// 清空消息 parameter: 1.Context 2.当前登录用户 3.对方ID
	public static synchronized void clearMessageByActiveUserAndChatId(
			Context context, String activeUserId, String chatId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			MessageDB messageDB = new MessageDB(db);
			messageDB.deleteByActiveUserAndChatId(activeUserId, chatId);
			db.close();
		}
	}

	// 更新消息状态 parameter: 1.Context 2.消息临时标记 3.消息状态
	public static synchronized void updateMessageStateByFlag(Context context,
			String msgFlag, int msgState) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			MessageDB messageDB = new MessageDB(db);
			messageDB.updateStateByFlag(msgFlag, String.valueOf(msgState));
			db.close();
		}
	}

	/*
	 * SysMessageDB
	 */

	// 查找系统消息 parameter: 1.Context 2.当前登录用户
	public static synchronized List<SysMessage> findSysMessageByActiveUser(
			Context context, String activeUserId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			SysMessageDB sysMessageDB = new SysMessageDB(db);
			List<SysMessage> lists = sysMessageDB
					.findByActiveUserId(activeUserId);
			db.close();
			return lists;
		}
	}

	// 插入系统消息 parameter: 1.Context 2.SysMessage
	public static synchronized void insertSysMessage(Context context,
			SysMessage msg) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			SysMessageDB sysMessageDB = new SysMessageDB(db);
			sysMessageDB.insert(msg);
			db.close();
		}
	}

	// 删除系统消息 parameter: 1.Context 2.主键ID
	public static synchronized void deleteSysMessage(Context context, int id) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			SysMessageDB sysMessageDB = new SysMessageDB(db);
			sysMessageDB.delete(id);
			db.close();
		}
	}

	// 更新系统消息状态 parameter: 1.Context 2.主键ID 3.状态
	public static synchronized void updateSysMessageState(Context context,
			int id, int state) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			SysMessageDB sysMessageDB = new SysMessageDB(db);
			sysMessageDB.updateSysMsgState(id, state);
			db.close();
		}
	}

	/*
	 * AlarmMaskDB
	 */

	// 查找报警屏蔽账号 parameter: 1.Context 2.当前登录用户
	public static synchronized List<AlarmMask> findAlarmMaskByActiveUser(
			Context context, String activeUserId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			AlarmMaskDB alarmMaskDB = new AlarmMaskDB(db);
			List<AlarmMask> lists = alarmMaskDB
					.findByActiveUserId(activeUserId);
			db.close();
			return lists;
		}
	}

	// 插入报警屏蔽账号 parameter: 1.Context 2.AlarmMask
	public static synchronized void insertAlarmMask(Context context,
			AlarmMask alarmMask) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			AlarmMaskDB alarmMaskDB = new AlarmMaskDB(db);
			alarmMaskDB.insert(alarmMask);
			db.close();
		}
	}

	// 删除报警屏蔽账号 parameter: 1.Context 2.当前登录用户 3.设备ID
	public static synchronized void deleteAlarmMask(Context context,
			String activeUserId, String deviceId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			AlarmMaskDB alarmMaskDB = new AlarmMaskDB(db);
			alarmMaskDB.deleteByActiveUserAndDeviceId(activeUserId, deviceId);
			db.close();
		}
	}

	/*
	 * AlarmRecordDB
	 */

	// 查找报警记录 parameter: 1.Context 2.当前登录用户
	public static synchronized List<AlarmRecord> findAlarmRecordByActiveUser(
			Context context, String activeUserId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			AlarmRecordDB alarmRecordDB = new AlarmRecordDB(db);
			List<AlarmRecord> lists = alarmRecordDB
					.findByActiveUserId(activeUserId);
			db.close();
			return lists;
		}
	}
	
	public static synchronized List<AlarmRecord> findAlarmRecordByActiveUser(
			Context context, String activeUserId,String startTime,String endTime) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			AlarmRecordDB alarmRecordDB = new AlarmRecordDB(db);
			List<AlarmRecord> lists = alarmRecordDB
					.findByActiveUserId(activeUserId,startTime,endTime);
			db.close();
			return lists;
		}
	}

	// 插入报警记录 parameter: 1.Context 2.AlarmRecord
	public static synchronized void insertAlarmRecord(Context context,
			AlarmRecord alarmRecord,String alarmInfo) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			AlarmRecordDB alarmRecordDB = new AlarmRecordDB(db);
			alarmRecordDB.insert(alarmRecord,alarmInfo);
			db.close();
		}
	}

	// 删除报警记录 parameter: 1.Context 2.主键id
	public static synchronized void deleteAlarmRecordById(Context context,
			int id) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			AlarmRecordDB alarmRecordDB = new AlarmRecordDB(db);
			alarmRecordDB.deleteById(id);
			db.close();
		}
	}

	// 清空报警记录 parameter: 1.Context 2.当前登录用户
	public static synchronized void clearAlarmRecord(Context context,
			String activeUserId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			AlarmRecordDB alarmRecordDB = new AlarmRecordDB(db);
			alarmRecordDB.deleteByActiveUser(activeUserId);
			db.close();
		}
	}

	/*
	 * NearlyTellDB
	 */

	// 查找最近通话记录 parameter: 1.Context 2.当前登录用户
	public static synchronized List<NearlyTell> findNearlyTellByActiveUser(
			Context context, String activeUserId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			NearlyTellDB nearlyTellDB = new NearlyTellDB(db);
			List<NearlyTell> lists = nearlyTellDB
					.findByActiveUserId(activeUserId);
			db.close();
			return lists;
		}
	}

	// 查找最近通话记录 parameter: 1.Context 2.当前登录用户 3.通话ID
	public static synchronized List<NearlyTell> findNearlyTellByActiveUserAndTellId(
			Context context, String activeUserId, String tellId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			NearlyTellDB nearlyTellDB = new NearlyTellDB(db);
			List<NearlyTell> lists = nearlyTellDB.findByActiveUserIdAndTellId(
					activeUserId, tellId);
			db.close();
			return lists;
		}
	}

	// 插入最近通话记录 parameter: 1.Context 2.NearlyTell
	public static synchronized void insertNearlyTell(Context context,
			NearlyTell nearlyTell) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			NearlyTellDB nearlyTellDB = new NearlyTellDB(db);
			nearlyTellDB.insert(nearlyTell);
			db.close();
		}
	}

	// 删除最近通话记录 parameter: 1.Context 2.主键id
	public static synchronized void deleteNearlyTellById(Context context, int id) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			NearlyTellDB nearlyTellDB = new NearlyTellDB(db);
			nearlyTellDB.deleteById(id);
			db.close();
		}
	}

	// 删除最近通话记录 parameter: 1.Context 2.通话ID
	public static synchronized void deleteNearlyTellByTellId(Context context,
			String tellId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			NearlyTellDB nearlyTellDB = new NearlyTellDB(db);
			nearlyTellDB.deleteByTellId(tellId);
			db.close();
		}
	}

	// 清空最近通话记录 parameter: 1.Context 2.当前登录用户
	public static synchronized void clearNearlyTell(Context context,
			String activeUserId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			NearlyTellDB nearlyTellDB = new NearlyTellDB(db);
			nearlyTellDB.deleteByActiveUserId(activeUserId);
			db.close();
		}
	}

	/*
	 * ContactDB
	 */

	// 查找联系人 parameter: 1.Context 2.当前登录用户
	public static synchronized List<Contact> findContactByActiveUser(
			Context context, String activeUserId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContactDB contactDB = new ContactDB(db);
			List<Contact> lists = contactDB.findByActiveUserId(activeUserId);
			db.close();
			return lists;
		}
	}
	
	// 查找联系人 parameter: 1.Context 2.cameraID
		public static synchronized Contact findContactByContactId(
				Context context, String contactId) {
			synchronized (DataManager.class) {
				DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
						DataBaseVersion);
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				ContactDB contactDB = new ContactDB(db);
				Contact camera = contactDB.findByContactId(contactId);
				db.close();
				return camera;
			}
		}

	// 查找联系人 parameter: 1.Context 2.当前登录用户 3.联系人Id
	public static synchronized Contact findContactByActiveUserAndContactId(
			Context context, String activeUserId, String contactId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContactDB contactDB = new ContactDB(db);
			List<Contact> lists = contactDB.findByActiveUserIdAndContactId(
					activeUserId, contactId);
			db.close();
			if (lists.size() > 0) {
				return lists.get(0);
			} else {
				return null;
			}
		}
	}

	// 插入联系人 parameter: 1.Context 2.Contact
	public static synchronized void insertContact(Context context,
			Contact contact) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContactDB contactDB = new ContactDB(db);
			contactDB.insert(contact);
			db.close();
		}
	}
	
	//删除全部联系人
	public static synchronized void deleteAll(Context context){
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContactDB contactDB = new ContactDB(db);
			contactDB.deleteAll();
			db.close();
		}
	}

	// 更新联系人 parameter: 1.Context 2.Contact
	public static synchronized void updateContact(Context context,
			Contact contact) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContactDB contactDB = new ContactDB(db);
			contactDB.update(contact);
			db.close();
		}
	}
	
	public static synchronized boolean ifExitByActiveUserIdAndContactId(Context context,
			String activeUserId,String ContactId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContactDB contactDB = new ContactDB(db);
			boolean ifExit = contactDB.ifExitByActiveUserIdAndContactId(activeUserId, ContactId);
			db.close();
			return ifExit;
		}
	}

	// 删除联系人 parameter: 1.Context 2.当前登录用户 3.联系人ID
	public static synchronized void deleteContactByActiveUserAndContactId(
			Context context, String activeUserId, String contactId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContactDB contactDB = new ContactDB(db);
			contactDB.deleteByActiveUserIdAndContactId(activeUserId, contactId);
			db.close();
		}
	}

	// 删除联系人 parameter: 1.Context 2.主键id
	public static synchronized void deleteContactById(Context context, int id) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContactDB contactDB = new ContactDB(db);
			contactDB.deleteById(id);
			db.close();
		}
	}
	
	// DefenceArea
	public static synchronized boolean ifExitDefence(Context context, String defenceID) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			DefenceDB defenceDB = new DefenceDB(db);
			boolean ifExit = defenceDB.findDefenceExit(defenceID);
			db.close();
			return ifExit;
		}
	}
	
	public static synchronized String findDefence(Context context, String defenceID) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			DefenceDB defenceDB = new DefenceDB(db);
			String areaName = defenceDB.findDefence(defenceID);
			db.close();
			return areaName;
		}
	}
	
	public static synchronized Map<String,String> findDefenceByDefenceID(Context context, String defenceID) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			DefenceDB defenceDB = new DefenceDB(db);
			Map<String,String> map = defenceDB.findDefenceByDefenceID(defenceID);
			db.close();
			return map;
		}
	}
	
	public static synchronized void updateDefence(Context context,String defenceName,String defenceID) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			DefenceDB defenceDB = new DefenceDB(db);
			defenceDB.updateDefence(defenceID, defenceName);
			db.close();
		}
	}
	
	public static synchronized void updateDefenceAlarmType(Context context,String alarmType,String defenceID) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			DefenceDB defenceDB = new DefenceDB(db);
			defenceDB.updateDefenceAlarmType(defenceID, alarmType);
			db.close();
		}
	}
	
	public static synchronized void updateDefenceFromServer(Context context,String defenceID,String alarmType,String defenceName) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			DefenceDB defenceDB = new DefenceDB(db);
			defenceDB.updateDefenceFromServer(defenceID,alarmType,defenceName);
			db.close();
		}
	}
	
	public static synchronized void insertDefence(Context context,String defenceID) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			DefenceDB defenceDB = new DefenceDB(db);
			defenceDB.insert(defenceID);
			db.close();
		}
	}
	
	public static synchronized Map<String,String> findDefenceMap(Context context, String defenceID) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			DefenceDB defenceDB = new DefenceDB(db);
			Map<String,String> map = defenceDB.findDefenceMap(defenceID);
			db.close();
			return map;
		}
	}
	
	public static synchronized void deleteDefence(Context context,String defenceID) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			DefenceDB defenceDB = new DefenceDB(db);
			defenceDB.deleteDefence(defenceID);
			db.close();
		}
	}
	
	public static synchronized boolean findExit(Context context,String defenceID) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			DefenceDB defenceDB = new DefenceDB(db);
			boolean result = defenceDB.findExit(defenceID);
			db.close();
			return result;
		}
	}
	
	public static synchronized void updateAlarmType(Context context,List<Defence> defence) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			DefenceDB defenceDB = new DefenceDB(db);
			defenceDB.updateAlarmType(defence);
			db.close();
		}
	}
	
	public static synchronized void updateCameraAlarmType(Context context,String cameraId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			DefenceDB defenceDB = new DefenceDB(db);
			defenceDB.updateCameraAlarmType(cameraId);
			db.close();
		}
	}
	
	public static synchronized List<String> findDefenceAlarmType(Context context,String defenceID) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			DefenceDB defenceDB = new DefenceDB(db);
			List<String> list = defenceDB.findDefenceAlarmType(defenceID);
			db.close();
			return list;
		}
	}
	
	public static synchronized void updateListDefenceAlarmType(Context context,List<String> defenceID,String alarmType) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			DefenceDB defenceDB = new DefenceDB(db);
			defenceDB.updateListDefenceAlarmType(defenceID,alarmType);
			db.close();
		}
	}
}
