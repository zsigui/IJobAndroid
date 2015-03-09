package com.ijob.android.model;

import android.net.Uri;
import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * Created by JackieZhuang on 2015/1/18.
 */
public class Job {

	private int mId;
	private int mOrgId;
	private String mName;
	private String mLocation;
	private String mPubTime;
	private String mDeadline;
	private String mRecruitNum;
	private String mMonthlyPay;
	private String mCategory;
	private String mType;
	private String mGenderRequire;
	private String mLanguageRequire;
	private String mProficiency;
	private String mMinQualification;
	private String mMajorRequire;
	private String mDescription;
	private String mContactType;
	private int mScanNum = 0;

	public Job() {
	}

	public Job(int id, int orgId, String name, String location, String pubTime, String deadline, String recruitNum,
	           String monthlyPay, String category, String type, String genderRequire, String languageRequire,
	           String proficiency, String minQualification, String majorRequire, String description,
	           String contactType, int scanNum) {
		mId = id;
		mOrgId = orgId;
		mName = name;
		mLocation = location;
		mPubTime = pubTime;
		mDeadline = deadline;
		mRecruitNum = recruitNum;
		mMonthlyPay = monthlyPay;
		mCategory = category;
		mType = type;
		mGenderRequire = genderRequire;
		mLanguageRequire = languageRequire;
		mProficiency = proficiency;
		mMinQualification = minQualification;
		mMajorRequire = majorRequire;
		mDescription = description;
		mContactType = contactType;
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

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getLocation() {
		return mLocation;
	}

	public void setLocation(String location) {
		mLocation = location;
	}

	public String getPubTime() {
		return mPubTime;
	}

	public void setPubTime(String pubTime) {
		mPubTime = pubTime;
	}

	public String getDeadline() {
		return mDeadline;
	}

	public void setDeadline(String deadline) {
		mDeadline = deadline;
	}

	public String getRecruitNum() {
		return mRecruitNum;
	}

	public void setRecruitNum(String recruitNum) {
		mRecruitNum = recruitNum;
	}

	public String getMonthlyPay() {
		return mMonthlyPay;
	}

	public void setMonthlyPay(String monthlyPay) {
		mMonthlyPay = monthlyPay;
	}

	public String getCategory() {
		return mCategory;
	}

	public void setCategory(String category) {
		mCategory = category;
	}

	public String getType() {
		return mType;
	}

	public void setType(String type) {
		mType = type;
	}

	public String getGenderRequire() {
		return mGenderRequire;
	}

	public void setGenderRequire(String genderRequire) {
		mGenderRequire = genderRequire;
	}

	public String getLanguageRequire() {
		return mLanguageRequire;
	}

	public void setLanguageRequire(String languageRequire) {
		mLanguageRequire = languageRequire;
	}

	public String getProficiency() {
		return mProficiency;
	}

	public void setProficiency(String proficiency) {
		mProficiency = proficiency;
	}

	public String getMinQualification() {
		return mMinQualification;
	}

	public void setMinQualification(String minQualification) {
		mMinQualification = minQualification;
	}

	public String getMajorRequire() {
		return mMajorRequire;
	}

	public void setMajorRequire(String majorRequire) {
		mMajorRequire = majorRequire;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		mDescription = description;
	}

	public String getContactType() {
		return mContactType;
	}

	public void setContactType(String contactType) {
		mContactType = contactType;
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
		public static final String DB_NAME = "jobs";

		/* DATA FILED */
		public static final String ORG_ID = "_org_id";
		public static final String NAME = "_name";
		public static final String LOCATION = "_location";
		public static final String PUB_TIME = "_pub_time";
		public static final String DEADLINE = "_deadline";
		public static final String RECRUIT_NUM = "_recruit_num";
		public static final String MONTHLY_PAY = "_monthly_pay";
		public static final String CATEGORY = "_category";
		public static final String TYPE = "_type";
		public static final String GENDER_REQUIRE = "_gender_require";
		public static final String LANGUAGE_REQUIRE = "_language_require";
		public static final String PROFICIENCY = "_proficiency";
		public static final String MIN_QUALIFICATION = "_min_qualification";
		public static final String MAJOR_REQUIRE = "_major_require";
		public static final String DESCRIPTION = "_description";
		public static final String CONTACT_TYPE = "_contactType";
		public static final String SCAN_NUM = "_scan_num";

		/* DEFAULT SORT ORDER */
		public static final String DEFAULT_SORT_ORDER = PUB_TIME + " desc";

		public static ArrayList<String> getRequiredColumns() {
			ArrayList<String> columns = new ArrayList<>();
			columns.add(ParamName._ID);
			columns.add(ParamName.ORG_ID);
			columns.add(ParamName.NAME);
			columns.add(ParamName.LOCATION);
			columns.add(ParamName.PUB_TIME);
			columns.add(ParamName.DEADLINE);
			columns.add(ParamName.RECRUIT_NUM);
			columns.add(ParamName.MONTHLY_PAY);
			columns.add(ParamName.CATEGORY);
			columns.add(ParamName.TYPE);
			columns.add(ParamName.GENDER_REQUIRE);
			columns.add(ParamName.LANGUAGE_REQUIRE);
			columns.add(ParamName.PROFICIENCY);
			columns.add(ParamName.MIN_QUALIFICATION);
			columns.add(ParamName.MAJOR_REQUIRE);
			columns.add(ParamName.DESCRIPTION);
			columns.add(ParamName.CONTACT_TYPE);
			columns.add(ParamName.SCAN_NUM);
			return columns;
		}

	}
}
