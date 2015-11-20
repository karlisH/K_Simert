package com.kradac.android.ksimert;

import java.util.ArrayList;
import java.util.List;

import com.kradac.android.ksimert.R;
import com.kradac.android.ksimert.R.id;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Recharge extends Activity {
	private Context mycontext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recharge);
		mycontext = this;

		//final Spinner cbxZone = (Spinner) findViewById(id.cbxZone);
		final EditText txtDocument = (EditText) findViewById(id.txtDocument);
		final EditText txtPerson = (EditText) findViewById(id.txtPerson);
		final EditText txtMount = (EditText) findViewById(id.txtMount);
		Button btnAccept = (Button) findViewById(id.btnAccept);
		Button btnCancel = (Button) findViewById(id.btnCancel);

		/*List<String> list = new ArrayList<String>();
		list.add("Zona 1");
		list.add("Zona 2");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);

		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		cbxZone.setAdapter(dataAdapter);*/

		btnAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alertaSimple = new AlertDialog.Builder(
						mycontext);
				alertaSimple.setTitle("Mensaje");
				alertaSimple.setMessage("Recarga Realizada Correctamente.");
				alertaSimple.setPositiveButton("Aceptar",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								finish();
								//startActivity(intent);
								//intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
								//MainActivity.zona = cbxZone.getSelectedItemPosition()+1;
								MainActivity.monto = Double.parseDouble(txtMount.getText().toString());
							}
						});
				
				alertaSimple.create();
				alertaSimple.show();
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				txtDocument.setText("");
				txtPerson.setText("");
				txtMount.setText("");
			}
		});
	}
}
