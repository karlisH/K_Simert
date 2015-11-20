package com.kradac.android.ksimert;

import com.kradac.android.ksimert.R;
import com.kradac.android.ksimert.R.id;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class Menu extends Activity {
	private Context mycontext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		mycontext = this;

		ImageButton btnParking, btnSeats, btnRecharge, btnExit;

		btnParking = (ImageButton) findViewById(id.btnParking);
		btnSeats = (ImageButton) findViewById(id.btnSeats);
		btnRecharge = (ImageButton) findViewById(id.btnRecharge);
		btnExit = (ImageButton) findViewById(id.btnExit);

		btnParking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Menu.this, Parking.class));
			}
		});

		btnSeats.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Menu.this, Login.class));
			}
		});

		btnRecharge.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Menu.this, Recharge.class));
			}
		});
		
		btnExit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showConfirmExit();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			showConfirmExit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void showConfirmExit() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mycontext);

		// Setting Dialog Title
		alertDialog.setTitle("Mensaje");

		// Setting Dialog Message
		alertDialog
				.setMessage("¿Esta seguro que desea Salir de la Aplicacion?.");

		// On pressing Settings button
		alertDialog.setPositiveButton("Si",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}
}
