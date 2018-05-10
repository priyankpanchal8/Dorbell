package com.example.a.varnidoorbell;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.UrlQuerySanitizer.ValueSanitizer;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class DBHandler extends SQLiteOpenHelper {
	
	//public SQLiteDatabase db = getWritableDatabase();
			//this.getWritableDatabase();
	
	final boolean doesDatabaseExist(ContextWrapper context, String dbName) {
	    File dbFile = context.getDatabasePath(dbName);
	    return dbFile.exists();
	}
	
	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "mydatabase.db";
	
	//connectivity table
	public static final String TBL_Connectivity = "connectivity";
//	public static final String IP = "_ip";
//	public static final String PORT = "_port";
	public static final String USERNAME = "_username";
	public static final String PASSWORD = "_password";
	
	public static final String CREATE_CONNECTIVITY_TBL = "Create table if not exists connectivity (_username text, _password text)";

	///Switch name table
//	public static final String TBL_SWITCH_NAME = "switch_name";
//	public static final String S1 = "_s1";
//	public static final String S2 = "_s2";
//	public static final String S3 = "_s3";
//	public static final String S4 = "_s4";
//	public static final String S5 = "_s5";
//
//	public static final String CREATE_SWITCH_NAME_TBL = "Create table if not exists switch_name (_s1 text, _s2 text, _s3 text, _s4 text, _s5 text)";
//
//	//Profile Table
//	public static final String TBL_PROFILE = "profile";
//	public static final String NAME = "_name";
//	public static final String BV1 = "_bv1";
//	public static final String BV2 = "_bv2";
//	public static final String BV3 = "_bv3";
//	public static final String BV4 = "_bv4";
//	public static final String FV1 = "_fv1";
//	public static final String F1SPEEDV = "_f1speedv";
//
//	public static final String CREATE_PROFILE_TBL = "Create table if not exists profile (_id integer primary key autoincrement," +
//			" _name text, _bv1 integer, _bv2 integer, _bv3 integer," +
//			"_bv4 integer, _fv1 integer, _f1speedv integer) ";
//
//
//
	//public SQLiteDatabase db = this.getWritableDatabase();
	
	
	public DBHandler(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub

	}
	
	
		
	
	//
	//for connectivity table getters
	//
//	public String getIP() {
//		SQLiteDatabase db = this.getReadableDatabase();
//		String ip="";
//		Cursor c = db.rawQuery("Select "+ IP +" from " + TBL_Connectivity, null);
//
//		// check the cursor size and initialize
//		if (c.getCount() > 0 && c != null) {
//			while (c.moveToNext()) {
//				ip = c.getString(c.getColumnIndex(IP));
//			}
//		}
//		return ip;
//
//	}
//
//	public int getPORT() {
//		SQLiteDatabase db = this.getReadableDatabase();
//		int port = 0;
//		Cursor c = db.rawQuery("Select "+ PORT +" from " + TBL_Connectivity, null);
//		// check the cursor size and initialize
//		if (c.getCount() > 0 && c != null) {
//			while (c.moveToNext()) {
//				port = c.getInt(c.getColumnIndex(PORT));
//			}
//
//		}
//		return port;
//
//	}
	
	public String getUSERNAME() {
		SQLiteDatabase db = this.getReadableDatabase();
		String username="";
		Cursor c = db.rawQuery("Select "+ USERNAME +" from " + TBL_Connectivity, null);

		// check the cursor size and initialize
		if (c.getCount() > 0 && c != null) {
			while (c.moveToNext()) {
				username = c.getString(c.getColumnIndex(USERNAME));
			}
		}
		return username;

	}
	
	public String getPASSWORD() {
		SQLiteDatabase db = this.getReadableDatabase();
		String password="";
		Cursor c = db.rawQuery("Select "+ PASSWORD +" from " + TBL_Connectivity, null);

		// check the cursor size and initialize
		if (c.getCount() > 0 && c != null) {
			while (c.moveToNext()) {
				
				password = c.getString(c.getColumnIndex(PASSWORD));
			}
		}
		return password;

	}

	
	//
	//for switch name getters
	//
//	public String getSwitch1() {
//		SQLiteDatabase db = this.getReadableDatabase();
//		String str="";
//		Cursor c = db.rawQuery("Select "+ S1 +" from " + TBL_SWITCH_NAME, null);
//
//		// check the cursor size and initialize
//		if (c.getCount() > 0 && c != null) {
//			while (c.moveToNext()) {
//				str = c.getString(c.getColumnIndex(S1));
//			}
//		}
//		return str;
//	}
//
//	public String getSwitch2() {
//		SQLiteDatabase db = this.getReadableDatabase();
//		String str="";
//		Cursor c = db.rawQuery("Select "+ S2 +" from " + TBL_SWITCH_NAME, null);
//
//		// check the cursor size and initialize
//		if (c.getCount() > 0 && c != null) {
//			while (c.moveToNext()) {
//				str = c.getString(c.getColumnIndex(S2));
//			}
//		}
//		return str;
//	}
//	public String getSwitch3() {
//		SQLiteDatabase db = this.getReadableDatabase();
//		String str="";
//		Cursor c = db.rawQuery("Select "+ S3 +" from " + TBL_SWITCH_NAME, null);
//
//		// check the cursor size and initialize
//		if (c.getCount() > 0 && c != null) {
//			while (c.moveToNext()) {
//				str = c.getString(c.getColumnIndex(S3));
//			}
//		}
//		return str;
//	}
//	public String getSwitch4() {
//		SQLiteDatabase db = this.getReadableDatabase();
//		String str="";
//		Cursor c = db.rawQuery("Select "+ S4 +" from " + TBL_SWITCH_NAME, null);
//
//		// check the cursor size and initialize
//		if (c.getCount() > 0 && c != null) {
//			while (c.moveToNext()) {
//				str = c.getString(c.getColumnIndex(S4));
//			}
//		}
//		return str;
//	}
//	public String getSwitch5() {
//		SQLiteDatabase db = this.getReadableDatabase();
//		String str="";
//		Cursor c = db.rawQuery("Select "+ S5 +" from " + TBL_SWITCH_NAME, null);
//
//		// check the cursor size and initialize
//		if (c.getCount() > 0 && c != null) {
//			while (c.moveToNext()) {
//				str = c.getString(c.getColumnIndex(S5));
//			}
//		}
//		return str;
//	}
//
//
//	//update into table switch name
//	public void updateSwitchName(String s1, String s2, String s3, String s4, String s5)
//	{
//		SQLiteDatabase db = this.getWritableDatabase();
//
//		ContentValues values = new ContentValues();
//		values.put(S1, s1);
//		values.put(S2, s2);
//		values.put(S3, s3);
//		values.put(S4, s4);
//		values.put(S5, s5);
//
//		String whereClause ="";
//		String[] whereArgs = null;
//
//		db.update(TBL_SWITCH_NAME, values, whereClause, whereArgs );
//		db.close();
//	}
	
	//update connection information table
	
	public void updateConnectivity(String username, String password)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(USERNAME, username);
		values.put(PASSWORD, password);
		
		String whereClause ="";
		String[] whereArgs = null;

		db.update(TBL_Connectivity, values, whereClause, whereArgs );
		db.close();

	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_CONNECTIVITY_TBL);
//		db.execSQL(CREATE_SWITCH_NAME_TBL);
//		db.execSQL(CREATE_PROFILE_TBL);
		
		
		//insert data into connectivity table
		//insertConnectivity();
		ContentValues values = new ContentValues();
//		values.put(IP, "192.168.4.1");
//		values.put(PORT, 10150);
		values.put(USERNAME, " ");
		values.put(PASSWORD, " ");
		
		db.insert(TBL_Connectivity, null, values);
		values.clear();
		//insert data into switch name table
		//insertSwitch_Name();
		
//		values.put(S1, "Bulb 1");
//		values.put(S2, "Bulb 2");
//		values.put(S3, "Bulb 3");
//		values.put(S4, "Bulb 4");
//		values.put(S5, "Fan 1");
//
//		db.insert(TBL_SWITCH_NAME, null, values);
//		values.clear();
//
//		insert data into profile table
//		insertProfile();
//		values.put(NAME, "All Off");
//		values.put(BV1, 0);
//		values.put(BV2, 0);
//		values.put(BV3, 0);
//		values.put(BV4, 0);
//		values.put(FV1, 0);
//		values.put(F1SPEEDV, 0);
//
//		db.insert(TBL_PROFILE, null, values);
//		values.clear();
//
//		ContentValues values1 = new ContentValues();
//
//		values1.put(NAME, "All On");
//		values1.put(BV1, 1);
//		values1.put(BV2, 1);
//		values1.put(BV3, 1);
//		values1.put(BV4, 1);
//		values1.put(FV1, 1);
//		values1.put(F1SPEEDV, 5);
//
//		db.insert(TBL_PROFILE, null, values1);
//		values1.clear();
//
//		ContentValues values2 = new ContentValues();
//
//		values2.put(NAME, "All Bulb Off");
//		values2.put(BV1, 0);
//		values2.put(BV2, 0);
//		values2.put(BV3, 0);
//		values2.put(BV4, 0);
//		values2.put(FV1, -1);
//		values2.put(F1SPEEDV, -1);
//
//
//		db.insert(TBL_PROFILE, null, values2);
//		values2.clear();
//
//		ContentValues values3 = new ContentValues();
//
//		values3.put(NAME, "All Bulb On");
//		values3.put(BV1, 1);
//		values3.put(BV2, 1);
//		values3.put(BV3, 1);
//		values3.put(BV4, 1);
//		values3.put(FV1, -1);
//		values3.put(F1SPEEDV, -1);
//
//		db.insert(TBL_PROFILE, null, values3);
//		values3.clear();
//
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	

}
