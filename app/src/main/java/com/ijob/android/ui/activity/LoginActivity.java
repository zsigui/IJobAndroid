package com.ijob.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dk.animation.SwitchAnimationUtil;
import com.ijob.android.R;
import com.ijob.android.constants.HttpFeedbackConstants;
import com.ijob.android.model.User;
import com.ijob.android.ui.async.Abs_UAAsyncTask;
import com.ijob.android.ui.async.LoginAsyncTask;
import com.ijob.android.ui.listener.FilterTextWatcher;
import com.ijob.android.util.LUtil;
import com.ijob.android.util.T;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JackieZhuang on 2015/1/10.
 */
public class LoginActivity extends ActionBarActivity {

	private Button btnLogin;
	private Button btnReg;
	private Button btnBack;
	private EditText etUsername;
	private EditText etPassword;
	private TextView tvTitle;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initUI();
		setListener();
	}

	private void initUI() {
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnReg = (Button) findViewById(R.id.btnReg);
		btnBack = (Button) findViewById(R.id.btnBack);
		etUsername = (EditText) findViewById(R.id.etUsername);
		etPassword = (EditText) findViewById(R.id.etPwd);
		tvTitle = (TextView) findViewById(R.id.tvTitle);

		btnBack.setVisibility(View.INVISIBLE);
		btnLogin.setEnabled(false);
		tvTitle.setText("登陆界面");
	}

	private Abs_UAAsyncTask newTaskInstance() {
		Abs_UAAsyncTask logTask = new LoginAsyncTask(LoginActivity.this);
		logTask.setFinishCallback(new Abs_UAAsyncTask.FinishCallback() {
			@Override
			public void dealResult(String jsonResultStr) {
				LUtil.d("tss", "dealResult=" + jsonResultStr);
				if (jsonResultStr == null) {
					T.showL(LoginActivity.this, "未知异常，登录失败");
					return;
				}
				try {
					JSONObject jsonResult = new JSONObject(jsonResultStr);
					if (jsonResult.getInt(HttpFeedbackConstants.TAG_CODE) == HttpFeedbackConstants.CODE_OK) {
						T.showL(LoginActivity.this, "登录成功");
						LUtil.d("LoginResult", jsonResult.getJSONObject(HttpFeedbackConstants.TAG_DATA).toString());
					} else {
						T.showL(LoginActivity.this, "登录失败，code = " + jsonResult.getInt(HttpFeedbackConstants
								.TAG_ERROR_CODE)
								+ ", msg = " + jsonResult.getString(HttpFeedbackConstants.TAG_MSG));
					}
					Intent intent = new Intent();
					intent.setClass(LoginActivity.this, MainActivity.class);
					startActivity(intent);
					LoginActivity.this.finish();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		logTask.setWhichViewToShowPW(this.findViewById(R.id.lyMainView));
		return logTask;
	}

	private void setListener() {
		new SwitchAnimationUtil().startAnimation(getWindow().getDecorView(), SwitchAnimationUtil.AnimationType.ALPHA);
		FilterTextWatcher nameTextWatcher = new FilterTextWatcher(etUsername, "[\\d\\w_@.]{1,20}", 20);
		FilterTextWatcher.FilterCallback filterCallback = new FilterTextWatcher.FilterCallback() {
			@Override
			public void doInAfter() {
				if (TextUtils.isEmpty(etUsername.getText().toString()) || TextUtils.isEmpty(etPassword.getText()
						.toString())) {
					btnLogin.setEnabled(false);
				} else {
					btnLogin.setEnabled(true);
				}
			}
		};
		nameTextWatcher.setCallback(filterCallback);
		FilterTextWatcher pwdTextWatcher = new FilterTextWatcher(etPassword, "[\\d\\w_&*~.+=]{1,20}", 20);
		pwdTextWatcher.setCallback(filterCallback);
		etUsername.addTextChangedListener(nameTextWatcher);
		etPassword.addTextChangedListener(pwdTextWatcher);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				User user = new User();
				newTaskInstance().execute(user);
			}
		});
		btnReg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				overridePendingTransition(R.anim.alpha_zoom_in, R.anim.alpha_shrink_out);
				Intent intent = new Intent(LoginActivity.this, RegActivity.class);
				startActivity(intent);
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
