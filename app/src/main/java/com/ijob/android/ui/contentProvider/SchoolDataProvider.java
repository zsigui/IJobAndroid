package com.ijob.android.ui.contentProvider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.ijob.android.model.Job;
import com.ijob.android.model.Orgnization;
import com.ijob.android.model.Recruitment;

import java.util.ArrayList;

/**
 * Created by JackieZhuang on 2015/1/20.
 */
public class SchoolDataProvider extends ContentProvider {

	/* MATCH CODE */
	private static final int ITEM_JOB = 1;
	private static final int ITEM_JOB_ID = 2;
	private static final int ITEM_ORG = 3;
	private static final int ITEM_ORG_ID = 4;
	private static final int ITEM_RCM = 5;
	private static final int ITEM_RCM_ID = 6;

	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	static {
		sUriMatcher.addURI(SchoolDataDBOpenHelper.AUTHORITY, Job.ParamName.DB_NAME, ITEM_JOB);
		sUriMatcher.addURI(SchoolDataDBOpenHelper.AUTHORITY, Job.ParamName.DB_NAME + "/#", ITEM_JOB_ID);
		sUriMatcher.addURI(SchoolDataDBOpenHelper.AUTHORITY, Orgnization.ParamName.DB_NAME, ITEM_ORG);
		sUriMatcher.addURI(SchoolDataDBOpenHelper.AUTHORITY, Orgnization.ParamName.DB_NAME + "/#", ITEM_ORG_ID);
		sUriMatcher.addURI(SchoolDataDBOpenHelper.AUTHORITY, Recruitment.ParamName.DB_NAME, ITEM_RCM);
		sUriMatcher.addURI(SchoolDataDBOpenHelper.AUTHORITY, Recruitment.ParamName.DB_NAME + "/#", ITEM_RCM_ID);
	}

	private SQLiteOpenHelper mOpenHelper;
	// 用于监听通知
	private ContentResolver mResolver;

	@Override
	public boolean onCreate() {
		mOpenHelper = new SchoolDataDBOpenHelper(getContext());
		mResolver = getContext().getContentResolver();
		return true;
	}

	@Override
	public String getType(Uri uri) {
		int match = sUriMatcher.match(uri);
		switch (match) {
			case ITEM_JOB:
				// go through
			case ITEM_ORG:
				// go through
			case ITEM_RCM:
				return SchoolDataDBOpenHelper.CONTENT_TYPE;
			case ITEM_JOB_ID:
				// go through
			case ITEM_ORG_ID:
				// go through
			case ITEM_RCM_ID:
				return SchoolDataDBOpenHelper.CONTENT_ITEM_TYPE;
			default:
				throw new IllegalArgumentException("Unknown Uri : " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		String realSelection = null;
		String id = null;
		String realSortOrder = null;
		int match = sUriMatcher.match(uri);
		SQLiteQueryBuilder sqBuilder = new SQLiteQueryBuilder();
		switch (match) {
			case ITEM_JOB_ID:
				id = uri.getPathSegments().get(1);
				realSelection = constructSelection(Job.ParamName._ID, id, selection);
				// go through
			case ITEM_JOB:
				realSortOrder = (TextUtils.isEmpty(sortOrder) ? Job.ParamName.DEFAULT_SORT_ORDER : sortOrder);
				sqBuilder.setTables(Job.ParamName.DB_NAME);
				break;
			case ITEM_ORG_ID:
				id = uri.getPathSegments().get(1);
				realSelection = constructSelection(Orgnization.ParamName._ID, id, selection);
				// go through
			case ITEM_ORG:
				realSortOrder = (TextUtils.isEmpty(sortOrder) ? Orgnization.ParamName.DEFAULT_SORT_ORDER : sortOrder);
				sqBuilder.setTables(Orgnization.ParamName.DB_NAME);
				break;
			case ITEM_RCM_ID:
				id = uri.getPathSegments().get(1);
				realSelection = constructSelection(Recruitment.ParamName._ID, id, selection);
				// go through
			case ITEM_RCM:
				realSortOrder = (TextUtils.isEmpty(sortOrder) ? Recruitment.ParamName.DEFAULT_SORT_ORDER : sortOrder);
				sqBuilder.setTables(Recruitment.ParamName.DB_NAME);
				break;
			default:
				throw new IllegalArgumentException("Can't query from uri : " + uri);
		}
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor cursor = sqBuilder.query(db, projection, realSelection, selectionArgs, null, null, realSortOrder, null);
		cursor.setNotificationUri(mResolver, uri);
		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int match = sUriMatcher.match(uri);
		if (match != ITEM_JOB_ID || match != ITEM_ORG_ID || match != ITEM_RCM_ID) {
			throw new IllegalArgumentException("Can't insert into uri : " + uri);
		}
		if (values == null) {
			throw new IllegalArgumentException("ContentValues can't be null");
		}
		ArrayList<String> colNames = null;
		switch (match) {
			case ITEM_JOB_ID:
				colNames = Job.ParamName.getRequiredColumns();
				break;
			case ITEM_RCM_ID:
				colNames = Recruitment.ParamName.getRequiredColumns();
				break;
			case ITEM_ORG_ID:
				colNames = Orgnization.ParamName.getRequiredColumns();
				break;
			default:
				// can't happend
		}
		for (String colName : colNames) {
			if (!values.containsKey(colName)) {
				throw new IllegalArgumentException("Missing column : " + colName);
			}
		}
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = -1;
		switch (match) {
			case ITEM_JOB_ID:
				rowId = db.insert(Job.ParamName.DB_NAME, Job.ParamName._ID, values);
				break;
			case ITEM_RCM_ID:
				rowId = db.insert(Recruitment.ParamName.DB_NAME, Recruitment.ParamName._ID, values);
				break;
			case ITEM_ORG_ID:
				rowId = db.insert(Orgnization.ParamName.DB_NAME, Orgnization.ParamName._ID, values);
				break;
			default:
				// can't happend
		}
		if (rowId < 0) {
			throw new SQLiteException("Unable to insert into uri " + uri + " with values " + values);
		}
		Uri newUri = ContentUris.withAppendedId(uri, rowId);
		mResolver.notifyChange(newUri, null);
		return newUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		String dbName = null;
		String realSelection = null;
		String id = null;
		int match = sUriMatcher.match(uri);
		switch (match) {
			case ITEM_JOB_ID:
				id = uri.getPathSegments().get(1);
				realSelection = constructSelection(Job.ParamName._ID, id, selection);
				// go through
			case ITEM_JOB:
				dbName = Job.ParamName.DB_NAME;
				break;
			case ITEM_ORG_ID:
				id = uri.getPathSegments().get(1);
				realSelection = constructSelection(Orgnization.ParamName._ID, id, selection);
				// go through
			case ITEM_ORG:
				dbName = Orgnization.ParamName.DB_NAME;
				break;
			case ITEM_RCM_ID:
				id = uri.getPathSegments().get(1);
				realSelection = constructSelection(Recruitment.ParamName._ID, id, selection);
				// go through
			case ITEM_RCM:
				dbName = Recruitment.ParamName.DB_NAME;
				break;
			default:
				throw new IllegalArgumentException("Can't delete from uri : " + uri);
		}

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		if (id == null) {
			count = db.delete(dbName, realSelection, selectionArgs);
		} else {
			count = db.delete(dbName, realSelection, selectionArgs);
		}

		if (count > 0) {
			mResolver.notifyChange(uri, null);
		}
		return count;
	}

	private String constructSelection(String idColumnName, String id, String selection) {
		return idColumnName + " = " + id + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")");
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int count = 0;
		if (values == null) {
			throw new IllegalArgumentException("ContentValues can't be null");
		}
		String dbName = null;
		String realSelection = null;
		String id = null;
		int match = sUriMatcher.match(uri);
		switch (match) {
			case ITEM_JOB_ID:
				id = uri.getPathSegments().get(1);
				realSelection = constructSelection(Job.ParamName._ID, id, selection);
				// go through
			case ITEM_JOB:
				dbName = Job.ParamName.DB_NAME;
				break;
			case ITEM_ORG_ID:
				id = uri.getPathSegments().get(1);
				realSelection = constructSelection(Orgnization.ParamName._ID, id, selection);
				// go through
			case ITEM_ORG:
				dbName = Orgnization.ParamName.DB_NAME;
				break;
			case ITEM_RCM_ID:
				id = uri.getPathSegments().get(1);
				realSelection = constructSelection(Recruitment.ParamName._ID, id, selection);
				// go through
			case ITEM_RCM:
				dbName = Recruitment.ParamName.DB_NAME;
				break;
			default:
				throw new IllegalArgumentException("Can't update for uri : " + uri);
		}

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		if (id == null) {
			count = db.update(dbName, values, realSelection, selectionArgs);
		} else {
			count = db.update(dbName, values, realSelection, selectionArgs);
		}
		if (count > 0) {
			mResolver.notifyChange(uri, null);
		}
		return count;
	}

	/**
	 * Created by JackieZhuang on 2015/1/17.
	 */
	private static class SchoolDataDBOpenHelper extends SQLiteOpenHelper {

		private static final String DB_NAME = "ijob_schooldata.db";
		private static final int DB_VERSION = 1;

		public static final String AUTHORITY = "com.ijob.provider.schoolData";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/cp.schooldata";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/cp.schooldata";

		/*private static final String TABLE_ORG_CREATE_SQL = "CREATE TABLE IF NOT EXISTS organization (org_id INTEGER
		PRIMARY KEY" +
				" " +
				"AUTOINCREMENT NOT NULL, org_name VARCHAR(40) NOT NULL UNIQUE, org_industry VARCHAR(30) DEFAULT NULL,
				 " +
				"org_nature VARCHAR(20) DEFAULT NULL, org_scale VARCHAR(20) DEFAULT NULL, " +
				"org_registered_capital VARCHAR(20) DEFAULT NULL, org_real_capital VARCHAR(20) DEFAULT NULL, " +
				"org_type VARCHAR(20) DEFAULT NULL, org_website VARCHAR(50) DEFAULT NULL, org_description text, " +
				"org_address text, org_postal_code VARCHAR(8) DEFAULT NULL, org_contacts VARCHAR(20) DEFAULT NULL, " +
				"org_email VARCHAR(40) DEFAULT NULL, org_pic text, org_scan_num INTEGER DEFAULT 0);";
		private static final String TABLE_JOB_CREATE_SQL = "CREATE TABLE IF NOT EXISTS jobs (job_id INTEGER PRIMARY
		KEY " +
				"AUTOINCREMENT NOT NULL, job_org_id INTEGER NOT NULL, job_name VARCHAR(40) NOT NULL, " +
				"job_location VARCHAR(40) DEFAULT NULL, job_pub_time VARCHAR(10) DEFAULT NULL, " +
				"job_deadline VARCHAR(10) DEFAULT NULL, job_recuit_num  VARCHAR(10) DEFAULT NULL, " +
				"job_monthly_pay VARCHAR(20) DEFAULT NULL, job_category VARCHAR(30) DEFAULT NULL, " +
				"job_type VARCHAR(4) DEFAULT NULL, job_gender_require  VARCHAR(10) DEFAULT NULL, " +
				"job_language_require  VARCHAR(20) DEFAULT NULL, job_proficiency VARCHAR(8) DEFAULT NULL, " +
				"job_min_qualification VARCHAR(8) DEFAULT NULL, job_major_require VARCHAR(40) DEFAULT NULL, " +
				"job_description TEXT, job_contactType TEXT, job_scan_num INTEGER NOT NULL DEFAULT 0, " +
				"FOREIGN KEY (job_org_id) REFERENCES organization (org_id) ON DELETE CASCADE ON UPDATE CASCADE);";
		private static final String TABLE_REC_CREATE_SQL = "CREATE TABLE IF NOT EXISTS recruitment (rcm_id INTEGER
		PRIMARY
		 KEY " +
				"AUTOINCREMENT NOT NULL, rcm_org_id INTEGER NOT NULL, rcm_type VARCHAR(20) DEFAULT NULL, " +
				"rcm_info_provider VARCHAR(50) NOT NULL, rcm_name VARCHAR(50) NOT NULL,
				rcm_hold_location VARCHAR(50) NOT
				 " +
				"NULL, rcm_hold_time VARCHAR(30) DEFAULT NULL, rcm_target_educ VARCHAR(40) DEFAULT NULL, " +
				"rcm_target_major VARCHAR(40) DEFAULT NULL, rcm_description TEXT, rcm_contacts VARCHAR(20) DEFAULT
				NULL,
				 " +
				"rcm_contact_type VARCHAR(20) DEFAULT NULL, rcm_fax_code VARCHAR(30) DEFAULT NULL, " +
				"rcm_email VARCHAR(40) DEFAULT NULL, rcm_relate_link VARCHAR(50) DEFAULT NULL, " +
				"rcm_scan_num INTEGER DEFAULT 0, FOREIGN KEY (rcm_org_id) REFERENCES organization (org_id) ON DELETE
				 " +
				"CASCADE ON UPDATE CASCADE);";*/
		public static final String TABLE_ORG_DROP_SQL = "DROP TABLE IF EXISTS organization";
		public static final String TABLE_REC_DROP_SQL = "DROP TABLE IF EXISTS recruitment";
		public static final String TABLE_JOB_DROP_SQL = "DROP TABLE IF EXISTS jobs";

		public SchoolDataDBOpenHelper(Context context) {
			this(context, DB_NAME, null, DB_VERSION);
		}

		public SchoolDataDBOpenHelper(Context context, SQLiteDatabase.CursorFactory factory) {
			this(context, DB_NAME, factory, DB_VERSION);
		}

		public SchoolDataDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
		                              int version) {
			this(context, name, factory, version, null);
		}

		public SchoolDataDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version,
		                              DatabaseErrorHandler errorHandler) {
			super(context, name, factory, version, errorHandler);
		}

		@Override
		public void onOpen(SQLiteDatabase db) {
			super.onOpen(db);
			// 开启外键约束
			if (db.isReadOnly()) {
				db.execSQL("PRAGMA foreign_keys=ON;");
			}
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(buildOrgCreateSql().toString());
			db.execSQL(buildJobCreateSql().toString());
			db.execSQL(buildRcmCreateSql().toString());
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// 最简单的数据库升级方法，移除所有数据
			// 实际解决方法步骤：1.创建临时表 2.将原表数据转移到临时表 3.删除临时表
			db.execSQL(TABLE_ORG_DROP_SQL);
			db.execSQL(TABLE_JOB_DROP_SQL);
			db.execSQL(TABLE_REC_DROP_SQL);
			onCreate(db);
		}

		private StringBuilder buildRcmCreateSql() {
			StringBuilder rcmCreateSql = new StringBuilder();
			rcmCreateSql.append("CREATE TABLE IF NOT EXISTS ").append(Recruitment.ParamName.DB_NAME).append(" (")
					.append(Recruitment.ParamName._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ")
					.append(Recruitment.ParamName.ORG_ID).append(" INTEGER NOT NULL, ")
					.append(Recruitment.ParamName.TYPE).append(" VARCHAR(20) DEFAULT NULL, ")
					.append(Recruitment.ParamName.INFO_PROVIDER).append(" VARCHAR(50) NOT NULL, ")
					.append(Recruitment.ParamName.NAME).append(" VARCHAR(50) NOT NULL, ")
					.append(Recruitment.ParamName.HOLD_LOCATION).append(" VARCHAR(50) NOT NULL, ")
					.append(Recruitment.ParamName.HOLD_TIME).append(" VARCHAR(30) DEFAULT NULL, ")
					.append(Recruitment.ParamName.TARGET_EDUC).append(" VARCHAR(40) DEFAULT NULL, ")
					.append(Recruitment.ParamName.TARGET_MAJOR).append(" VARCHAR(40) DEFAULT NULL, ")
					.append(Recruitment.ParamName.DESCRIPTION).append(" TEXT, ")
					.append(Recruitment.ParamName.CONTACTS).append(" VARCHAR(20) DEFAULT NULL, ")
					.append(Recruitment.ParamName.CONTACT_TYPE).append(" VARCHAR(20) DEFAULT NULL, ")
					.append(Recruitment.ParamName.FAX_CODE).append(" VARCHAR(30) DEFAULT NULL, ")
					.append(Recruitment.ParamName.EMAIL).append(" VARCHAR(40) DEFAULT NULL, ")
					.append(Recruitment.ParamName.RELATE_LINK).append(" VARCHAR(50) DEFAULT NULL, ")
					.append(Recruitment.ParamName.SCAN_NUM).append(" INTEGER DEFAULT 0, FOREIGN KEY (")
					.append(Recruitment.ParamName.ORG_ID).append(") REFERENCES ")
					.append(Orgnization.ParamName.DB_NAME).append(" (")
					.append(Orgnization.ParamName._ID).append(" ON DELETE CASCADE ON UPDATE CASCADE);");
			return rcmCreateSql;
		}

		private StringBuilder buildJobCreateSql() {
			StringBuilder jobCreateSql = new StringBuilder();
			jobCreateSql.append("CREATE TABLE IF NOT EXISTS ").append(Job.ParamName.DB_NAME).append(" (")
					.append(Job.ParamName._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ")
					.append(Job.ParamName.ORG_ID).append(" INTEGER NOT NULL, ")
					.append(Job.ParamName.NAME).append(" VARCHAR(40) NOT NULL, ")
					.append(Job.ParamName.LOCATION).append(" VARCHAR(40) DEFAULT NULL, ")
					.append(Job.ParamName.PUB_TIME).append(" VARCHAR(10) DEFAULT NULL, ")
					.append(Job.ParamName.DEADLINE).append(" VARCHAR(10) DEFAULT NULL, ")
					.append(Job.ParamName.RECRUIT_NUM).append(" VARCHAR(10) DEFAULT NULL, ")
					.append(Job.ParamName.MONTHLY_PAY).append(" VARCHAR(20) DEFAULT NULL, ")
					.append(Job.ParamName.CATEGORY).append(" VARCHAR(30) DEFAULT NULL, ")
					.append(Job.ParamName.TYPE).append(" VARCHAR(4) DEFAULT NULL, ")
					.append(Job.ParamName.GENDER_REQUIRE).append(" VARCHAR(10) DEFAULT NULL, ")
					.append(Job.ParamName.LANGUAGE_REQUIRE).append(" VARCHAR(20) DEFAULT NULL, ")
					.append(Job.ParamName.PROFICIENCY).append(" VARCHAR(8) DEFAULT NULL, ")
					.append(Job.ParamName.MIN_QUALIFICATION).append(" VARCHAR(8) DEFAULT NULL, ")
					.append(Job.ParamName.MAJOR_REQUIRE).append(" VARCHAR(40) DEFAULT NULL, ")
					.append(Job.ParamName.DESCRIPTION).append(" TEXT, ")
					.append(Job.ParamName.CONTACT_TYPE).append(" TEXT, ")
					.append(Job.ParamName.SCAN_NUM).append(" INTEGER DEFAULT 0, FOREIGN KEY (")
					.append(Job.ParamName.ORG_ID).append(") REFERENCES ")
					.append(Orgnization.ParamName.DB_NAME).append(" (")
					.append(Orgnization.ParamName._ID).append(" ON DELETE CASCADE ON UPDATE CASCADE);");
			return jobCreateSql;
		}

		private StringBuilder buildOrgCreateSql() {
			StringBuilder orgCreateSql = new StringBuilder();
			orgCreateSql.append("CREATE TABLE IF NOT EXISTS ").append(Orgnization.ParamName.DB_NAME).append(" (")
					.append(Orgnization.ParamName._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ")
					.append(Orgnization.ParamName.NAME).append(" VARCHAR(40) NOT NULL UNIQUE, ")
					.append(Orgnization.ParamName.INDUSTRY).append(" VARCHAR(30) DEFAULT NULL, ")
					.append(Orgnization.ParamName.NATURE).append(" VARCHAR(20) DEFAULT NULL, ")
					.append(Orgnization.ParamName.SCALE).append(" VARCHAR(20) DEFAULT NULL, ")
					.append(Orgnization.ParamName.REGISTERED_CAPITAL).append(" VARCHAR(20) DEFAULT NULL, ")
					.append(Orgnization.ParamName.REAL_CAPITAL).append(" VARCHAR(20) DEFAULT NULL, ")
					.append(Orgnization.ParamName.TYPE).append(" VARCHAR(20) DEFAULT NULL, ")
					.append(Orgnization.ParamName.WEBSITE).append(" VARCHAR(50) DEFAULT NULL, ")
					.append(Orgnization.ParamName.DESCRIPTION).append(" TEXT, ")
					.append(Orgnization.ParamName.ADDRESS).append(" TEXT, ")
					.append(Orgnization.ParamName.POSTAL_CODE).append(" VARCHAR(8) DEFAULT NULL, ")
					.append(Orgnization.ParamName.CONTACTS).append(" VARCHAR(20) DEFAULT NULL, ")
					.append(Orgnization.ParamName.EMAIL).append(" VARCHAR(40) DEFAULT NULL, ")
					.append(Orgnization.ParamName.PIC).append(" TEXT, ")
					.append(Orgnization.ParamName.SCAN_NUM).append(" INTEGER DEFAULT 0);");
			return orgCreateSql;
		}
	}
}
