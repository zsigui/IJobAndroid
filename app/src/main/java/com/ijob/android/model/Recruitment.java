package com.ijob.android.model;

import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * Created by JackieZhuang on 2015/1/18.
 */
public class Recruitment {

	private int mId;
	private int mOrgId;
	private String mType;
	private String mInfoProvider;
	private String mName;
	private String mHoldLocation;
	private String mHoldTime;
	private String mTargetEduc;
	private String mTargetMajor;
	private String mDescriPtion;
	private String mContacts;
	private String mContactType;
	private String mFaxCode;
	private String mEmail;
	private String mRelateLink;
	private int mScanNum;

	public Recruitment() {
	}

	public Recruitment(int id, int orgId, String type, String infoProvider, String name, String holdLocation,
	                   String holdTime, String targetEduc, String targetMajor, String descriPtion, String contacts,
	                   String contactType, String faxCode, String email, String relateLink, int scanNum) {
		mId = id;
		mOrgId = orgId;
		mType = type;
		mInfoProvider = infoProvider;
		mName = name;
		mHoldLocation = holdLocation;
		mHoldTime = holdTime;
		mTargetEduc = targetEduc;
		mTargetMajor = targetMajor;
		mDescriPtion = descriPtion;
		mContacts = contacts;
		mContactType = contactType;
		mFaxCode = faxCode;
		mEmail = email;
		mRelateLink = relateLink;
		mScanNum = scanNum;
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		mId = id;
	}

	public int getOrgId() {
		return mOrgId;
	}

	public void setOrgId(int orgId) {
		mOrgId = orgId;
	}

	public String getType() {
		return mType;
	}

	public void setType(String type) {
		mType = type;
	}

	public String getInfoProvider() {
		return mInfoProvider;
	}

	public void setInfoProvider(String infoProvider) {
		mInfoProvider = infoProvider;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getHoldLocation() {
		return mHoldLocation;
	}

	public void setHoldLocation(String holdLocation) {
		mHoldLocation = holdLocation;
	}

	public String getHoldTime() {
		return mHoldTime;
	}

	public void setHoldTime(String holdTime) {
		mHoldTime = holdTime;
	}

	public String getTargetEduc() {
		return mTargetEduc;
	}

	public void setTargetEduc(String targetEduc) {
		mTargetEduc = targetEduc;
	}

	public String getTargetMajor() {
		return mTargetMajor;
	}

	public void setTargetMajor(String targetMajor) {
		mTargetMajor = targetMajor;
	}

	public String getDescriPtion() {
		return mDescriPtion;
	}

	public void setDescriPtion(String descriPtion) {
		mDescriPtion = descriPtion;
	}

	public String getContacts() {
		return mContacts;
	}

	public void setContacts(String contacts) {
		mContacts = contacts;
	}

	public String getContactType() {
		return mContactType;
	}

	public void setContactType(String contactType) {
		mContactType = contactType;
	}

	public String getFaxCode() {
		return mFaxCode;
	}

	public void setFaxCode(String faxCode) {
		mFaxCode = faxCode;
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String email) {
		mEmail = email;
	}

	public String getRelateLink() {
		return mRelateLink;
	}

	public void setRelateLink(String relateLink) {
		mRelateLink = relateLink;
	}

	public int getScanNum() {
		return mScanNum;
	}

	public void setScanNum(int scanNum) {
		mScanNum = scanNum;
	}

	/**
	 * 数据库参数字段
	 */
	public static abstract class ParamName implements BaseColumns{

		/* DATABASE NAME */
		public static final String DB_NAME = "recruitment";

		/* DATA FILED */
		public static final String ORG_ID = "_org_id";
		public static final String TYPE = "_type";
		public static final String INFO_PROVIDER = "_info_provider";
		public static final String NAME = "_name";
		public static final String HOLD_LOCATION = "_hold_location";
		public static final String HOLD_TIME = "_hold_time";
		public static final String TARGET_EDUC = "_target_educ";
		public static final String TARGET_MAJOR = "_target_major";
		public static final String DESCRIPTION = "_description";
		public static final String CONTACTS = "_contacts";
		public static final String CONTACT_TYPE = "_contact_type";
		public static final String FAX_CODE = "_fax_code";
		public static final String EMAIL = "_email";
		public static final String RELATE_LINK = "_relate_link";
		public static final String SCAN_NUM = "_scan_num";

		/* DEFAULT SORT ORDER */
		public static final String DEFAULT_SORT_ORDER = HOLD_TIME + " desc";

		public static ArrayList<String> getRequiredColumns() {
			ArrayList<String> columns = new ArrayList<>();
			columns.add(ParamName._ID);
			columns.add(ParamName.ORG_ID);
			columns.add(ParamName.TYPE);
			columns.add(ParamName.INFO_PROVIDER);
			columns.add(ParamName.NAME);
			columns.add(ParamName.HOLD_LOCATION);
			columns.add(ParamName.HOLD_TIME);
			columns.add(ParamName.TARGET_EDUC);
			columns.add(ParamName.TARGET_MAJOR);
			columns.add(ParamName.DESCRIPTION);
			columns.add(ParamName.CONTACTS);
			columns.add(ParamName.CONTACT_TYPE);
			columns.add(ParamName.FAX_CODE);
			columns.add(ParamName.EMAIL);
			columns.add(ParamName.RELATE_LINK);
			columns.add(ParamName.SCAN_NUM);
			return columns;
		}

	}
}
