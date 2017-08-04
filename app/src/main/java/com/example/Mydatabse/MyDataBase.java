package com.example.Mydatabse;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.biometrics.rssolution.Helper.ReportItem;
import com.biometrics.rssolution.models.CityForecastBO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDataBase extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 3;

	// Database Name
	//private static final String DATABASE_NAME = "MyUserManage";
	private static final String DATABASE_NAME = "MyUserManage";

	// Contacts table name
	private static final String TABLE_USER = "MyStudenttable";

	// Contacts Table Columns names

	// private static final String KEY_EMAIL_ID = "email-id";
	private static final String IMEINO = "imeino";
	private static final String VTPNO = "vtpno";
	private static final String DESCRIPTION = "description";
	private static final String BATCHID = "batchid";
	private static final String STUDENT_NAME = "studentName";
	private static final String STUDENT_ID = "studentId";
	private static final String FP1 = "fp1";
	private static final String FP2 = "fp2";
	private static final String PHOTO = "photo";
	private static final String DATE = "date";
	private static final String TIME = "time";

	public MyDataBase(Context context) {
		super(context, "/sdcard/"+DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_USER + "("
				+ IMEINO + " TEXT," + VTPNO + " TEXT," + DESCRIPTION + " TEXT,"
				+ BATCHID + " TEXT," + STUDENT_NAME + " TEXT," + STUDENT_ID
				+ " TEXT," + DATE + " TEXT," + TIME + " TEXT," + FP1 + " BLOB,"
				+ FP2 + " BLOB," + PHOTO + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
		Log.i("ondba", "database oncreate called");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

		// Create tables again
		onCreate(db);
	}

	// Adding new contact
	public void addStudent(Student MyUser) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(STUDENT_ID, MyUser.getStudentId());
		values.put(STUDENT_NAME, MyUser.getStudentName()); // Contact Name
		// values.put(LEFTIF_IMPRESSION, MyUser.getLeftImpression()); // Contact
		// Phone Number
		// values.put(RIGHTIF_IMPRESSION, MyUser.getRightImpression());

		// Inserting Row
		db.insert(TABLE_USER, null, values);
		db.close(); // Closing database connection
		Log.i("database", MyUser.getStudentName()
				+ " successfully added to data base");
	}

	// insert imei
	public void insertImeiDetails(String imeino, String vtpno, String desc) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(IMEINO, imeino);
		values.put(VTPNO, vtpno); // Contact Name
		values.put(DESCRIPTION, desc); // Contact Phone Number
		values.put(BATCHID, "");
		values.put(STUDENT_NAME, "");
		values.put(STUDENT_ID, "");
		values.put(FP1, "");
		values.put(FP2, "");
		values.put(PHOTO, "");

		// Inserting Row
		db.insert(TABLE_USER, null, values);
		db.close(); // Closing database connection
		Log.i("database", " successfully added to data base");
	}

	// insert batchid

	public void insertBatchId(String batchid, String vtpno) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(IMEINO, "");
		values.put(VTPNO, vtpno); // Contact Name
		values.put(DESCRIPTION, ""); // Contact Phone Number
		values.put(BATCHID, batchid);
		values.put(STUDENT_NAME, "");
		values.put(STUDENT_ID, "");
		values.put(FP1, "");
		values.put(FP2, "");
		values.put(PHOTO, "");

		// Inserting Row
		db.insert(TABLE_USER, null, values);
		db.close(); // Closing database connection
		Log.i("database", " successfully added to data base");
	}

	// insert student details

	public void insertStudentDetails(String batchid, String vtpno, String name,
			String id) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(IMEINO, "");
		values.put(VTPNO, vtpno); // Contact Name
		values.put(DESCRIPTION, ""); // Contact Phone Number
		values.put(BATCHID, batchid);
		values.put(STUDENT_NAME, name);
		values.put(STUDENT_ID, id);
		values.put(FP1, "");
		values.put(FP2, "");
		values.put(PHOTO, "");

		// Inserting Row
		db.insert(TABLE_USER, null, values);
		db.close(); // Closing database connection
		Log.i("database", " successfully added to data base");
	}

	public void insertStudentFullDetails(String batchid, String vtpno,
			String name, String id, String fp1, String fp2, String photo) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(IMEINO, "");
		values.put(VTPNO, vtpno); // Contact Name
		values.put(DESCRIPTION, ""); // Contact Phone Number
		values.put(BATCHID, batchid);
		values.put(STUDENT_NAME, name);
		values.put(STUDENT_ID, id);
		values.put(FP1, fp1);
		values.put(FP2, fp2);
		values.put(PHOTO, photo);

		// Inserting Row
		db.insert(TABLE_USER, null, values);
		db.close(); // Closing database connection
		Log.i("database", " successfully added to data base");
	}

	// insert fingerPrint

	public int insertFingerPrints(String batchid, String vtpno, byte[] fp1,
			byte[] fp2, String id) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(FP1, fp1);
		values.put(FP2, fp2);
		// values.put(LEFTIF_IMPRESSION, MyUser.getLeftImpression());
		// values.put(RIGHTIF_IMPRESSION, MyUser.getRightImpression());
		// String sql = "UPDATE "+TABLE_USER +" SET " + FP1+
		// " = '"+fp1+","+FP2+" = "+fp2+" WHERE "+STUDENT_ID+ " = "+id;
		// String s =
		// "update Streets (Counter) Set (Value) Where Street_id=5, (Value) Where Street_id=5";
		//
		//

		// updating row
		return db.update(TABLE_USER, values, STUDENT_ID + " = ?",
				new String[] { id });

	}

	public void insertDateTime(String rollNo, String Date, String Time,
			String batchid, String vtp, String name) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(DATE, Date);
		values.put(TIME, Time);
		values.put(STUDENT_ID, rollNo);
		values.put(BATCHID, batchid);
		values.put(VTPNO, vtp);
		values.put(STUDENT_NAME, name);
		// values.put(LEFTIF_IMPRESSION, MyUser.getLeftImpression());
		// values.put(RIGHTIF_IMPRESSION, MyUser.getRightImpression());
		// String sql = "UPDATE "+TABLE_USER +" SET " + FP1+
		// " = '"+fp1+","+FP2+" = "+fp2+" WHERE "+STUDENT_ID+ " = "+id;
		// String s =
		// "update Streets (Counter) Set (Value) Where Street_id=5, (Value) Where Street_id=5";
		//
		//

		// updating row
		db.insert(TABLE_USER, null, values);
		db.close();

	}

	public int insertPhoto(String id, String photo) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(PHOTO, photo);
		// values.put(FP2, fp2);
		// values.put(LEFTIF_IMPRESSION, MyUser.getLeftImpression());
		// values.put(RIGHTIF_IMPRESSION, MyUser.getRightImpression());
		// String sql = "UPDATE "+TABLE_USER +" SET " + FP1+
		// " = '"+fp1+","+FP2+" = "+fp2+" WHERE "+STUDENT_ID+ " = "+id;
		// String s =
		// "update Streets (Counter) Set (Value) Where Street_id=5, (Value) Where Street_id=5";
		//
		//

		// updating row
		return db.update(TABLE_USER, values, STUDENT_ID + " = ?",
				new String[] { id });

	}

	// Getting single contact
	public String getnameByBatchId(String batchid) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_USER, new String[] { STUDENT_NAME },
				BATCHID + "=?", new String[] { batchid }, null, null, null,
				null);
		if (cursor != null)
			cursor.moveToFirst();

		String MyUser = cursor.getString(0);
		// return contact
		return MyUser;
	}

	public String getStudentIdByBatchId(String batchid) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_USER, new String[] { STUDENT_NAME },
				BATCHID + "=?", new String[] { batchid }, null, null, null,
				null);
		if (cursor != null)
			cursor.moveToFirst();

		String MyUser = cursor.getString(0);
		// return contact
		return MyUser;
	}

	public String getStudentNameByrollno(String id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_USER, new String[] { STUDENT_NAME },
				STUDENT_ID + "=?", new String[] { id }, null, null, null, null);
		if (cursor.getCount() != 0) {
			if (cursor != null)
				cursor.moveToFirst();

			String MyUser = cursor.getString(0);
			// return contact
			return MyUser;
		} else
			return null;
	}

	public ArrayList<String> getAllDateByRollNo(String id) {
		// int lastindex = getStudentsCount();
		SQLiteDatabase db = this.getReadableDatabase();
		String v = null;
		LinkedHashMap<String, String> list = new LinkedHashMap<String, String>();
		// ArrayList<String> list = new ArrayList<String>();
		Cursor cursor = db.query(TABLE_USER, new String[] { STUDENT_NAME },
				STUDENT_ID + "=?", new String[] { id }, null, null, null, null);
		if (cursor != null)
			if (cursor.moveToFirst()) {
				do {
					v = cursor.getString(0);
					// Log.i("dba","Batchid : "+v);
					// Adding contact to list
					if (v != null || !v.equals(""))
						list.put(v, v);

				} while (cursor.moveToNext());
			}

		// cursor.moveToLast();
		//
		// String MyUser = cursor.getString(0);
		// Log.i()
		// return contact
		return new ArrayList<String>(list.values());
	}

	public ArrayList<String> getAllDateByBatchId(String id) {
		// int lastindex = getStudentsCount();
		SQLiteDatabase db = this.getReadableDatabase();
		String v = null;
		LinkedHashMap<String, String> list = new LinkedHashMap<String, String>();
		// ArrayList<String> list = new ArrayList<String>();
		Cursor cursor = db.query(TABLE_USER, new String[] { DATE }, BATCHID
				+ "=?", new String[] { id }, null, null, null, null);
		if (cursor != null)
			if (cursor.moveToFirst()) {
				do {
					v = cursor.getString(0);
					// Log.i("dba","Batchid : "+v);
					// Adding contact to list

					list.put(v, v);

				} while (cursor.moveToNext());
			}

		// cursor.moveToLast();
		//
		// String MyUser = cursor.getString(0);
		// Log.i()
		// return contact
		return new ArrayList<String>(list.values());
	}

	/*
	 * public ArrayList<String> getAllDateByBatchId(String id){ // int lastindex
	 * = getStudentsCount(); SQLiteDatabase db = this.getReadableDatabase();
	 * String v=null; //LinkedHashMap<String, String> list = new
	 * LinkedHashMap<String,String>(); ArrayList<String> list = new
	 * ArrayList<String>(); Cursor cursor = db.query(TABLE_USER, new String[] {
	 * DATE}, BATCHID + "=?", new String[] { id }, null, null, null, null); if
	 * (cursor != null) if (cursor.moveToFirst()) { do { v =
	 * cursor.getString(0); Log.i("dba","Batchid : "+v); // Adding contact to
	 * list list.add( v);
	 * 
	 * } while (cursor.moveToNext()); }
	 * 
	 * 
	 * //cursor.moveToLast(); // // String MyUser = cursor.getString(0); //
	 * Log.i() // return contact return list; }
	 */

	public String getTimeByrollno(String id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_USER, new String[] { STUDENT_NAME },
				STUDENT_ID + "=?", new String[] { id }, null, null, null, null);
		if (cursor.getCount() != 0) {
			if (cursor != null)
				cursor.moveToFirst();

			String MyUser = cursor.getString(0);
			// return contact
			return MyUser;
		} else
			return null;
	}

	public byte[] getStudentFP1Byrollno(String id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_USER, new String[] { FP1 }, STUDENT_ID
				+ "=?", new String[] { id }, null, null, null, null);
		if (cursor.getCount() != 0) {
			if (cursor != null)
				cursor.moveToFirst();

			byte[] MyUser = cursor.getBlob(0);
			// return contact
			return MyUser;
		} else
			return null;
	}

	public String getStudentPhotoByrollno(String id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_USER, new String[] { PHOTO }, STUDENT_ID
				+ "=?", new String[] { id }, null, null, null, null);
		if (cursor.getCount() != 0) {
			if (cursor != null)
				cursor.moveToFirst();

			String MyUser = cursor.getString(0);
			// return contact
			return MyUser;
		} else
			return null;
	}
	public String getStudentBatchIdByrollno(String id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_USER, new String[] { BATCHID }, STUDENT_ID
				+ "=?", new String[] { id }, null, null, null, null);
		if (cursor.getCount() != 0) {
			if (cursor != null)
				cursor.moveToFirst();

			String MyUser = cursor.getString(0);
			// return contact
			return MyUser;
		} else
			return null;
	}

	public byte[] getStudentFP2Byrollno(String id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_USER, new String[] { FP2 }, STUDENT_ID
				+ "=?", new String[] { id }, null, null, null, null);
		if (cursor.getCount() != 0) {
			if (cursor != null)
				cursor.moveToFirst();

			byte[] MyUser = cursor.getBlob(0);
			// return contact
			return MyUser;
		} else
			return null;
	}

	public String getLatestVtp() {
		// int lastindex = getStudentsCount();
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_USER, new String[] { VTPNO }, null,
				null, null, null, null, null);
		if (cursor != null)
			cursor.moveToLast();

		String MyUser = cursor.getString(0);
		// return contact
		return MyUser;
	}

	public String getLatestBatchId() {
		// int lastindex = getStudentsCount();
		SQLiteDatabase db = this.getReadableDatabase();
		String v = null;
		Cursor cursor = db.query(TABLE_USER, new String[] { BATCHID }, null,
				null, null, null, null, null);
		if (cursor != null)
			if (cursor.moveToFirst()) {
				do {
					v = cursor.getString(0);
					Log.i("dba", " ho Batchid : " + v);
					// Adding contact to list

				} while (cursor.moveToNext());
			}

		// cursor.moveToLast();
		//
		// String MyUser = cursor.getString(0);
		// Log.i()
		// return contact
		return v;
	}

	public ArrayList<String> getAllBatchId() {
		// int lastindex = getStudentsCount();
		SQLiteDatabase db = this.getReadableDatabase();
		String v = null;
		LinkedHashMap<String, String> list = new LinkedHashMap<String, String>();
		Cursor cursor = db.query(TABLE_USER, new String[] { BATCHID }, null,
				null, null, null, null, null);
		if (cursor != null)
			if (cursor.moveToFirst()) {
				do {
					v = cursor.getString(0);
					Log.i("dba", "all Batchid : " + v);
					// Adding contact to list
					if (v != null || !v.equals(""))
						list.put(v, v);

				} while (cursor.moveToNext());
			}

		if (list.size() > 0)
			return new ArrayList<String>(list.values());
		else
			return null;
	}

	public ArrayList<CityForecastBO> getNameAndRollbyBatch(String batchid) {
		// int lastindex = getStudentsCount();
		SQLiteDatabase db = this.getReadableDatabase();
		LinkedHashMap<String, CityForecastBO> list = new LinkedHashMap<String, CityForecastBO>();

		String id = null;
		String name = null;
		Cursor cursor = db.query(TABLE_USER, new String[] { STUDENT_ID,
				STUDENT_NAME }, BATCHID + "=?", new String[] { batchid }, null,
				null, null, null);
		if (cursor != null)
			if (cursor.moveToFirst()) {
				do {
					id = cursor.getString(0);
					name = cursor.getString(1);
					if (id != null || !id.equals(""))
						list.put(id, new CityForecastBO(name, id));
					// Log.i("dba","Batchid : "+name);
					// Adding contact to list

				} while (cursor.moveToNext());
			}

		// cursor.moveToLast();
		//
		// String MyUser = cursor.getString(0);
		// Log.i()
		// return contact
		return new ArrayList<CityForecastBO>(list.values());
	}

	public ArrayList<ReportItem> getNameAndRollbyDate(String date,
			String batchid) {
		// int lastindex = getStudentsCount();
		SQLiteDatabase db = this.getReadableDatabase();
		LinkedHashMap<String, ReportItem> list = new LinkedHashMap<String, ReportItem>();

		String id = null;
		String name = null;
		String time = null;
		Cursor cursor = db.query(TABLE_USER, new String[] { STUDENT_ID,
				STUDENT_NAME, TIME }, "date = ? AND batchid = ?", new String[] {
				date, batchid }, null, null, null, null);
		if (cursor != null)
			if (cursor.moveToFirst()) {
				do {
					id = cursor.getString(0);
					name = cursor.getString(1);
					time = cursor.getString(2);
					if (id != null || !id.equals(""))
						list.put(time, new ReportItem(name, id, time));
					// Log.i("dba","Batchid : "+name);
					// Adding contact to list

				} while (cursor.moveToNext());
			}

		// cursor.moveToLast();
		//
		// String MyUser = cursor.getString(0);
		// Log.i()
		// return contact
		return new ArrayList<ReportItem>(list.values());
	}

	/*
	 * public Student getUserByLimpression(String leftimpression) {
	 * SQLiteDatabase db = this.getReadableDatabase();
	 * 
	 * Cursor cursor = db.query(TABLE_USER, new String[] { STUDENT_ID,
	 * STUDENT_NAME, LEFTIF_IMPRESSION,RIGHTIF_IMPRESSION}, LEFTIF_IMPRESSION +
	 * "=?", new String[] { leftimpression }, null, null, null, null); if
	 * (cursor != null) cursor.moveToFirst();
	 * 
	 * Student student = new Student((cursor.getString(0)), cursor.getString(1),
	 * cursor.getString(2),cursor.getString(3)); // return contact return
	 * student; }
	 */
	/*
	 * public Student getUserByRimpression(String rightimpression) {
	 * SQLiteDatabase db = this.getReadableDatabase();
	 * 
	 * Cursor cursor = db.query(TABLE_USER, new String[] { STUDENT_ID,
	 * STUDENT_NAME, LEFTIF_IMPRESSION,RIGHTIF_IMPRESSION}, RIGHTIF_IMPRESSION +
	 * "=?", new String[] { rightimpression }, null, null, null, null); if
	 * (cursor != null) cursor.moveToFirst();
	 * 
	 * Student student = new Student((cursor.getString(0)), cursor.getString(1),
	 * cursor.getString(2),cursor.getString(3)); // return contact return
	 * student; }
	 */

	// Getting All Contacts

	public ArrayList<Student> getAllStudents() {
		ArrayList<Student> studentList = new ArrayList<Student>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_USER;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Student contact = new Student();
				contact.setLeftImpression(cursor.getString(0));
				contact.setStudentName(cursor.getString(1));
				contact.setRightImpression(cursor.getString(2));
				contact.setStudentId(cursor.getString(3));

				// Adding contact to list
				studentList.add(contact);
			} while (cursor.moveToNext());
		}

		// return contact list
		return studentList;
	}

	// Getting contacts Count
	public int getStudentsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_USER;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

	// Updating single contact
	/*
	 * public int updateUser(Student MyUser) { SQLiteDatabase db =
	 * this.getWritableDatabase();
	 * 
	 * ContentValues values = new ContentValues(); values.put(STUDENT_NAME,
	 * MyUser.getStudentName()); values.put(STUDENT_ID, MyUser.getStudentId());
	 * values.put(LEFTIF_IMPRESSION, MyUser.getLeftImpression());
	 * values.put(RIGHTIF_IMPRESSION, MyUser.getRightImpression()); // updating
	 * row return db.update(TABLE_USER, values, STUDENT_ID + " = ?", new
	 * String[] { String.valueOf(MyUser.getStudentId()) }); }
	 */

	// Deleting single contact
	public void deleteUser(String studentId) {

		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USER, STUDENT_ID + " = ?",
				new String[] { String.valueOf(studentId) });
		db.close();
	}

	// ---deletes a particular title---
	public boolean deleteStudentById(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(TABLE_USER, STUDENT_ID + "=" + id, null) > 0;
	}

}