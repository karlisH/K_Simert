package com.kradac.android.ksimert;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Registro extends Activity {
	private Button registrarR, cancelarR;
	private EditText usuarioR, contraseniaR, emailR;
	private TextView mensaje;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registro);
		registrarR = (Button) findViewById(R.id.btRegistrarR);
		cancelarR = (Button) findViewById(R.id.btCancelarR);

		usuarioR = (EditText) findViewById(R.id.etUsuarioR);
		contraseniaR = (EditText) findViewById(R.id.etContraseniaR);
		emailR = (EditText) findViewById(R.id.etEmailR);
		mensaje = (TextView) findViewById(R.id.tvMensaje);

		registrarR.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				ArrayList<NameValuePair> postValores = new ArrayList<NameValuePair>();
				postValores.add(new BasicNameValuePair("usuario", usuarioR
						.getText().toString()));
				postValores.add(new BasicNameValuePair("contrasenia",
						contraseniaR.getText().toString()));
				postValores.add(new BasicNameValuePair("email", emailR
						.getText().toString()));
				String respuesta = null;
				try {
					respuesta = ConexionHttpCliente
							.executeHttpPost(
									"http://200.0.29.117:8080/kparkingmobile/registrar.php",
									postValores);
					String res = respuesta.toString();
					res = res.replace("\\s+", "");
					if (res.equals("1\n")) {
						Intent regis = new Intent(Registro.this, Login.class);
						startActivity(regis);
					} else {
						mensaje.setText("Usuario o email ya registrado");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		cancelarR.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent login = new Intent(Registro.this, Login.class);
				startActivity(login);
			}
		});
	}

}
