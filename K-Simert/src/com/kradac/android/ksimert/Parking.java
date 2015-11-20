package com.kradac.android.ksimert;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.kradac.android.ksimert.R;
import com.kradac.android.ksimert.R.id;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class Parking extends Activity {
	private Context mycontext;
	private RadioButton rbZone1;
	private TextView txtViewDate;
	private Button btnStart;
	private Button btnFinish;
	private TextView txtViewMonto;
	private TextView txtViewGasto;
	private TextView txtViewTimeRestante;
	private ImageView imgState;
	private Chronometer chronometer;

	private boolean start = false;
	private int contSeg = 0;

	private double gasto = 0;
	private double monto = 10.00;
	private int timeHora = 0;

	Handler handler = new Handler();

	private Thread t;

	private void startThread() {
		t = new Thread() {
			public void run() {
				while (start) {
					try {
						contSeg++;
						if (contSeg == 3600) {
							contSeg = 0;
							handler.post(proceso);
						} 
						sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
	}

	Runnable proceso = new Runnable() {
		@SuppressLint("SimpleDateFormat")
		public void run() {
			if (rbZone1.isChecked()) {
				gasto += 0.25;
				monto -= 0.25;
				timeHora = (int) (monto / 0.25);
			} else {
				gasto += 0.50;
				monto -= 0.50;
				timeHora = (int) (monto / 0.50);
			}
			txtViewGasto.setText("$" + String.format("%.2f", gasto));
			txtViewMonto.setText("$" + String.format("%.2f", monto));
			txtViewTimeRestante.setText(timeHora + ":00:00");

			/*
			 * Date d = new Date(chronometer.getBase()); SimpleDateFormat sdf =
			 * new SimpleDateFormat("HH:mm:ss");
			 * txtViewZona.setText(sdf.format(d));
			 */

			if (monto <= 1) {
				imgState.setImageResource(R.drawable.state_yellow);
			}

			if (monto <= -2) {
				imgState.setImageResource(R.drawable.state_red);
				MainActivity.monto = monto;
				start = false;
				btnStart.setEnabled(true);
				btnFinish.setEnabled(false);
				chronometer.stop();

				AlertDialog.Builder alertaSimple = new AlertDialog.Builder(
						mycontext);
				alertaSimple.setTitle("Mensaje");
				alertaSimple
						.setMessage("El Monto de su Parquimetro se agotado y se ha cargado a su cuenta $2. Debido ha que sobrepaso el limite. Realice una recarga para continuar.");
				alertaSimple.setPositiveButton("Aceptar", null);

				alertaSimple.create();
				alertaSimple.show();
			} else if (monto < -1) {
				imgState.setImageResource(R.drawable.state_red);
			}
		}
	};

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parking);
		mycontext = this;
		monto = MainActivity.monto;

		if (monto <= 0) {
			messageLowerMount();
		} else {
			txtViewDate = (TextView) findViewById(id.txtViewDate);
			btnStart = (Button) findViewById(id.btnStart);
			btnFinish = (Button) findViewById(id.btnFinish);
			rbZone1 = (RadioButton) findViewById(id.rbZone1);
			txtViewMonto = (TextView) findViewById(id.txtViewMonto);
			txtViewGasto = (TextView) findViewById(id.txtViewGasto);
			txtViewTimeRestante = (TextView) findViewById(id.txtViewTimeRestante);
			imgState = (ImageView) findViewById(id.imgState);
			chronometer = (Chronometer) findViewById(id.chStart);

			txtViewMonto.setText("$" + monto);

			if (rbZone1.isChecked()) {
				timeHora = (int) (monto / 0.25);
			} else {
				timeHora = (int) (monto / 0.50);
			}
			txtViewTimeRestante.setText(timeHora + ":00:00");

			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			txtViewDate.setText(sdf.format(now) + " ");

			btnStart.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (monto <= 0) {
						messageLowerMount();
					} else {
						start = true;
						gasto = 0;
						contSeg = 0;
						startThread();

						btnStart.setEnabled(false);
						btnFinish.setEnabled(true);

						chronometer.setBase(SystemClock.elapsedRealtime());
						chronometer.start();
					}
				}
			});

			btnFinish.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showConfirmExit("¿Esta seguro que desea Finalizar el Control de Parquimetro?.");
				}
			});
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (start) {
				showConfirmExit("El Parquimetro se encuentra en ejecución. ¿Esta seguro que desea Finalizar el Control de Parquimetro?.");
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void showConfirmExit(String message) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mycontext);

		// Setting Dialog Title
		alertDialog.setTitle("Mensaje");

		// Setting Dialog Message
		alertDialog
				.setMessage(message);

		// On pressing Settings button
		alertDialog.setPositiveButton("Si",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						MainActivity.monto = monto;
						start = false;
						btnStart.setEnabled(true);
						btnFinish.setEnabled(false);
						chronometer.stop();
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

	public void messageLowerMount() {
		AlertDialog.Builder alertaSimple = new AlertDialog.Builder(mycontext);
		alertaSimple.setTitle("Mensaje");
		alertaSimple
				.setMessage("Usted no cuenta con un monto superior a $0. Realice una recarga para continuar.");
		alertaSimple.setPositiveButton("Aceptar",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				});

		alertaSimple.create();
		alertaSimple.show();
	}
}
