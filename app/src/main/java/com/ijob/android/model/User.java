package com.ijob.android.model;

import java.io.Serializable;

/**
 * Created by JackieZhuang on 2015/1/10.
 */
public class User implements Serializable {

	private static final long serialVersionUID = 209859831187660687L;

	private int mId;
	private String mName;
	private String mNickName;
	private String mPassword;
	private String mSalt;
	private String mEmail;
	private String mMobilePhone;
	private String mSecQuestion;
	private String mSecAnswer;
	private String mStudNo;
	private String mStudPwd;
	private String mGender;
	private String mDescription;

	public User() {
	}

	public User(int id, String name, String nickName, String password, String salt, String email,
	            String mobilePhone, String secQuestion, String secAnswer, String studNo, String studPwd,
	            String gender, String description) {
		mId = id;
		mName = name;
		mNickName = nickName;
		mPassword = password;
		mSalt = salt;
		mEmail = email;
		mMobilePhone = mobilePhone;
		mSecQuestion = secQuestion;
		mSecAnswer = secAnswer;
		mStudNo = studNo;
		mStudPwd = studPwd;
		mGender = gender;
		mDescription = description;
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

	public String getNickName() {
		return mNickName;
	}

	public void setNickName(String nickName) {
		mNickName = nickName;
	}

	public String getPassword() {
		return mPassword;
	}

	public void setPassword(String password) {
		mPassword = password;
	}

	public String getSalt() {
		return mSalt;
	}

	public void setSalt(String salt) {
		mSalt = salt;
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String email) {
		mEmail = email;
	}

	public String getMobilePhone() {
		return mMobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		mMobilePhone = mobilePhone;
	}

	public String getSecQuestion() {
		return mSecQuestion;
	}

	public void setSecQuestion(String secQuestion) {
		mSecQuestion = secQuestion;
	}

	public String getSecAnswer() {
		return mSecAnswer;
	}

	public void setSecAnswer(String secAnswer) {
		mSecAnswer = secAnswer;
	}

	public String getStudNo() {
		return mStudNo;
	}

	public void setStudNo(String studNo) {
		mStudNo = studNo;
	}

	public String getStudPwd() {
		return mStudPwd;
	}

	public void setStudPwd(String studPwd) {
		mStudPwd = studPwd;
	}

	public String getGender() {
		return mGender;
	}

	public void setGender(String gender) {
		mGender = gender;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		mDescription = description;
	}
}
