package com.kradac.android.ksimert;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class Help extends Activity {
	private TextView help;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);

		help = (TextView) findViewById(R.id.textView1);
		help.setText(MainActivity.response);
		//help.setMovementMethod(new ScrollingMovementMethod());
	}

}
