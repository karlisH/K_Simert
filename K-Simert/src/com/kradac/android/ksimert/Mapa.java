package com.kradac.android.ksimert;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class Mapa extends FragmentActivity implements OnMapClickListener {
	private final ArrayList<LatLng> parqueaderos = new ArrayList<LatLng>();
	private ArrayList<Double> lat = new ArrayList<Double>();
	private ArrayList<Double> lon = new ArrayList<Double>();
	private ArrayList<String> descripcion = new ArrayList<String>();

	private final LatLng KRADAC = new LatLng(-3.9949646826415, -79.200601627079);

	private GoogleMap mapa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);
		ArrayList<NameValuePair> postValores = new ArrayList<NameValuePair>();
		postValores.add(new BasicNameValuePair("usuario", "kradac"));
		postValores.add(new BasicNameValuePair("contrasenia", "kradac"));
		String respuesta = null;
		try {
			respuesta = ConexionHttpCliente.executeHttpPost(
					"http://200.0.29.117:8080/kparkingmobile/cargarDatos.php",
					postValores);
			String res = respuesta.toString();
			res = "-79.200601627079@-3.9949646826415$Bernardo ; Colon#11%-79.200409232823@-3.9955380622328$Olmedo; Colon#12%-79.200719127465@-3.9959418749041$Olmedo; Eguiguren#8%-79.200155343056@-3.9981505763116$Olmedo; Rocafuerte#11%-79.200948054695@-3.9984570798862$Bernardo; Miguel Riofrio#13%-79.200929380798@-3.9987464196635$Bernardo; Miguel Riofrio#12%-79.202158534242@-3.9939345858975$Bolivar; Quito#12%-79.202398915104@-3.993678424163$Bolivar; Quito#14%-79.202312563898@-3.9946419963387$Bolivar; Imbabura#11%-79.201916591835@-3.9956910543853$Bolivar; Colon#11%-79.201721096421@-3.999498806405$Bolivar; Miguel Riofrio#6%-79.201670328332@-3.9999353467014$Bolivar; Miguel Riofrio#9%-79.201796442714@-4.0019453210846$Bolivar; Mercadillo#12%-79.204080334229@-3.9948610741807$18 de Noviembre; Imbabura#7%-79.203786740823@-3.9962536719268$J.Antonio Eguiguren; 18 de Noviembre#8%-79.204384723855@-3.9984262643304$Rocafuerte; 18 de Noviembre#9%-79.199910799219@-3.9981854530564$Olmedo; Rocafuerte#11%-79.204551020814@-3.9974255592401$10 de Agosto; 18 de Noviembre#10%-79.202533999635@-4.0003955627582$Azuay; Sucre#12%-79.202555457307@-3.9991486977267$Riofrio; Sucre#12%";
			res = res.replace("\\s+", "");
			extraerDatos(res);
		} catch (Exception e) {
			e.printStackTrace();
		}

		mapa = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(KRADAC, 15));
		mapa.setMyLocationEnabled(true);
		mapa.getUiSettings().setZoomControlsEnabled(false);
		mapa.getUiSettings().setCompassEnabled(true);

		for (int a = 0; a < lat.size(); a++) {
			LatLng asa = new LatLng(lat.get(a), lon.get(a));
			parqueaderos.add(asa);
			graficarMapa(asa, "Parqueadero", descripcion.get(a));
		}
		mapa.setOnMapClickListener(this);
	}

	public void extraerDatos(String cadena) {
		cadena += "#";
		String latitud = "";
		String longitud = "";
		String nombre = "";
		int c = 0;
		for (int i = 0; i < cadena.length(); i++) {
			if (cadena.charAt(i) == '@') {
				latitud = cadena.substring(c + 1, i);
				lat.add(Double.valueOf(latitud));
				c = i;
			}
			if (cadena.charAt(i) == '$') {
				longitud = cadena.substring(c + 1, i);
				lon.add(Double.valueOf(longitud));

				c = i;
			}
			if (cadena.charAt(i) == '#') {
				nombre = cadena.substring(c + 1, i);
				descripcion.add(nombre);
				c = i;
			}
		}

	}

	public void graficarMapa(LatLng posicion, String Titulo, String Descripcion) {
		mapa.addMarker(new MarkerOptions()
				.position(posicion)
				.title(Titulo)
				.snippet(Descripcion)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.ic_parqueadero))
				.anchor(0.2f, 0.2f));
	}

	@Override
	public void onMapClick(LatLng puntoPulsado) {
		mapa.addMarker(new MarkerOptions().position(puntoPulsado).icon(
				BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
	}

}
