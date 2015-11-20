package com.kradac.android.ksimert;

import com.kradac.android.ksimert.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
	public static int zona = 1;
	public static double monto = 10;
	public static String response = ""; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Vamos a declarar un nuevo thread
		Thread timer = new Thread() {
			// El nuevo Thread exige el metodo run
			public void run() {
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					// Si no puedo ejecutar el sleep muestro el error
					e.printStackTrace();
				} finally {
					Intent i = new Intent(MainActivity.this,
							Menu.class);
					startActivity(i);
				}
			}
		};
		// ejecuto el thread
		timer.start();
	}
}
