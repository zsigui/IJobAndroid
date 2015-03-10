package com.ijob.hx.ui.dialog;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ijob.android.R;
import com.ijob.hx.ui.activity.BaseActivity;

/**
 * Created by JackieZhuang on 2015/3/9.
 */
public class ExitGroupDialog extends BaseActivity {
	private TextView text;
	private Button exitBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hx_dialog_logout_actionsheet);

		text = (TextView) findViewById(R.id.tv_text);
		exitBtn = (Button) findViewById(R.id.btn_exit);

		text.setText(R.string.exit_group_hint);
		String toast = getIntent().getStringExtra("deleteToast");
		if(toast != null)
			text.setText(toast);
		exitBtn.setText(R.string.exit_group);
	}

	public void logout(View view){
		setResult(RESULT_OK);
		finish();

	}

	public void cancel(View view) {
		finish();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}
}
