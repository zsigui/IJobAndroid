package com.ijob.hx.db.domain;

import com.easemob.chat.EMContact;

/**
 * Created by JackieZhuang on 2015/3/4.
 */
public class HXUser extends EMContact {
	private int unreadMsgCount;
	private String header;
	private String avatar;

	public HXUser() {
	}

	public HXUser(String username) {
		this.username = username;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public int getUnreadMsgCount() {
		return unreadMsgCount;
	}

	public void setUnreadMsgCount(int unreadMsgCount) {
		this.unreadMsgCount = unreadMsgCount;
	}


	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public int hashCode() {
		return 17 * getUsername().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof HXUser)) {
			return false;
		}
		return getUsername().equals(((HXUser) o).getUsername());
	}

	@Override
	public String toString() {
		return nick == null ? username : nick;
	}
}
