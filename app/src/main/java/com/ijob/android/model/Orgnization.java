package com.ijob.android.model;

import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * Created by JackieZhuang on 2015/1/18.
 */
public class Orgnization {

	private int mId;
	private String mName;
	private String mIndustry;
	private String mNature;
	private String mScale;
	private String mRegisteredCapital;
	private String mRealCapital;
	private String mType;
	private String mWebsite;
	private String mDescription;
	private String mAddress;
	private String mPostalCode;
	private String mContacts;
	private String mEmail;
	private String mPic;
	private String mScanNum;

	public Orgnization() {
	}

	public Orgnization(int id, String name, String industry, String nature, String scale, String registeredCapital,
	                   String realCapital, String type, String website, String description, String address,
	                   String postalCode, String contacts, String email, String pic, String scanNum) {
		mId = id;
		mName = name;
		mIndustry = industry;
		mNature = nature;
		mScale = scale;
		mRegisteredCapital = registeredCapital;
		mRealCapital = realCapital;
		mType = type;
		mWebsite = website;
		mDescription = description;
		mAddress = address;
		mPostalCode = postalCode;
		mContacts = contacts;
		mEmail = email;
		mPic = pic;
		mScanNum = scanNum;
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		mId = id;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getIndustry() {
		return mIndustry;
	}

	public void setIndustry(String industry) {
		mIndustry = industry;
	}

	public String getNature() {
		return mNature;
	}

	public void setNature(String nature) {
		mNature = nature;
	}

	public String getScale() {
		return mScale;
	}

	public void setScale(String scale) {
		mScale = scale;
	}

	public String getRegisteredCapital() {
		return mRegisteredCapital;
	}

	public void setRegisteredCapital(String registeredCapital) {
		mRegisteredCapital = registeredCapital;
	}

	public String getRealCapital() {
		return mRealCapital;
	}

	public void setRealCapital(String realCapital) {
		mRealCapital = realCapital;
	}

	public String getType() {
		return mType;
	}

	public void setType(String type) {
		mType = type;
	}

	public String getWebsite() {
		return mWebsite;
	}

	public void setWebsite(String website) {
		mWebsite = website;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		mDescription = description;
	}

	public String getAddress() {
		return mAddress;
	}

	public void setAddress(String address) {
		mAddress = address;
	}

	public String getPostalCode() {
		return mPostalCode;
	}

	public void setPostalCode(String postalCode) {
		mPostalCode = postalCode;
	}

	public String getContacts() {
		return mContacts;
	}

	public void setContacts(String contacts) {
		mContacts = contacts;
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String email) {
		mEmail = email;
	}

	public String getPic() {
		return mPic;
	}

	public void setPic(String pic) {
		mPic = pic;
	}

	public String getScanNum() {
		return mScanNum;
	}

	public void setScanNum(String scanNum) {
		mScanNum = scanNum;
	}

	/**
	 * 数据库参数字段
	 */
	public static abstract class ParamName implements BaseColumns{

		/* DATABASE NAME */
		public static final String DB_NAME = "organization";

		/* DATA FILED */
		public static final String NAME = "_name";
		public static final String INDUSTRY = "_industry";
		public static final String NATURE = "_nature";
		public static final String SCALE = "_scale";
		public static final String REGISTERED_CAPITAL = "_registered_capital";
		public static final String REAL_CAPITAL = "_real_capital";
		public static final String TYPE = "_type";
		public static final String WEBSITE = "_website";
		public static final String DESCRIPTION = "_description";
		public static final String ADDRESS = "_address";
		public static final String POSTAL_CODE = "_postal_code";
		public static final String CONTACTS = "_contacts";
		public static final String EMAIL = "_email";
		public static final String PIC = "_pic";
		public static final String SCAN_NUM = "_scan_num";

		/* DEFAULT SORT ORDER */
		public static final String DEFAULT_SORT_ORDER = _ID + " asc";

		public static ArrayList<String> getRequiredColumns() {
			ArrayList<String> columns = new ArrayList<>();
			columns.add(ParamName._ID);
			columns.add(ParamName.NAME);
			columns.add(ParamName.INDUSTRY);
			columns.add(ParamName.NATURE);
			columns.add(ParamName.SCALE);
			columns.add(ParamName.REGISTERED_CAPITAL);
			columns.add(ParamName.REAL_CAPITAL);
			columns.add(ParamName.TYPE);
			columns.add(ParamName.WEBSITE);
			columns.add(ParamName.DESCRIPTION);
			columns.add(ParamName.ADDRESS);
			columns.add(ParamName.POSTAL_CODE);
			columns.add(ParamName.CONTACTS);
			columns.add(ParamName.EMAIL);
			columns.add(ParamName.PIC);
			columns.add(ParamName.SCAN_NUM);
			return columns;
		}

	}
}
