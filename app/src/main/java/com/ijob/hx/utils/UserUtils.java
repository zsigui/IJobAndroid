package com.ijob.hx.utils;

import android.content.Context;
import android.widget.ImageView;

import com.ijob.android.R;
import com.ijob.android.ui.application.AppInfo;
import com.ijob.hx.db.domain.HXUser;
import com.squareup.picasso.Picasso;

/**
 * Created by JackieZhuang on 2015/3/10.
 */
public class UserUtils {
	/**
	 * 根据username获取相应user，由于demo没有真实的用户数据，这里给的模拟的数据；
	 * @param username
	 * @return
	 */
	public static HXUser getUserInfo(String username){
		HXUser user = AppInfo.sAppInstance.getContactList().get(username);
		if(user == null){
			user = new HXUser(username);
		}

		if(user != null){
			//demo没有这些数据，临时填充
			user.setNick(username);
//            user.setAvatar("http://downloads.easemob.com/downloads/57.png");
		}
		return user;
	}

	/**
	 * 设置用户头像
	 * @param username
	 */
	public static void setUserAvatar(Context context, String username, ImageView imageView){
		HXUser user = getUserInfo(username);
		if(user != null){
			Picasso.with(context).load(user.getAvatar()).placeholder(R.drawable.default_avatar).into(imageView);
		}else{
			Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
		}
	}
}
