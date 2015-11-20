package com.kradac.android.ksimert;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.MethodNotSupportedException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.R.menu;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends FragmentActivity implements OnMapClickListener {
	private final ArrayList<LatLng> parqueaderos = new ArrayList<LatLng>();
	private ArrayList<Double> lat = new ArrayList<Double>();
	private ArrayList<Double> lon = new ArrayList<Double>();
	private ArrayList<Integer> libres = new ArrayList<Integer>();
	private ArrayList<String> descripcion = new ArrayList<String>();
	public static ArrayList<String> direccionPar = new ArrayList<String>();

	boolean bandera = true;
	int estado = 0, distancia = 0, estado2 = 0, mejorDistancia;
	boolean bandera2 = true;
	private String direccionMejor;
	private double latitudMejor = 0, longitudMejor = 0;
	private GoogleMap mapa;
	private TextView pr;
	private int validar = 0;
	private ConectHttp conectarHp = new ConectHttp();
	private String respuesta;

	private static final int menu_mapas = 1;
	private static final int menu_buscar = 2;
	private static final int menu_mapas_normal = 3;
	private static final int menu_mapas_hibrido = 4;
	private static final int menu_mapas_satelite = 5;
	private static final int menu_mapas_terreno = 6;
	private static final int menu_buscar_1 = 7;
	private static final int menu_reservar = 8;
	private static final int menu_ayuda = 9;

	private final LatLng KRADAC = new LatLng(-3.9949646826415, -79.200601627079);
	private List<NameValuePair> postValores = new ArrayList();
	private Marker marcadorPar;
	/*
	 * Handler handler2 = new Handler(); private void verificarConexInter(){
	 * Thread hilo2 = new Thread(){ public void run(){ try{
	 * while(bandera2==true){ handler2.post(proceso2); Thread.sleep(5000); } }
	 * catch(Exception e){ } } }; hilo2.start(); }
	 * 
	 * Runnable proceso2 = new Runnable(){ public void run() {
	 * if(isOnline()==true){ if(estado==0){ bandera=true; hilador(); estado++; }
	 * }else{ Toast mensaje=Toast.makeText(getApplicationContext(),
	 * "Verifique su conexion a internet", Toast.LENGTH_SHORT); mensaje.show();
	 * bandera=false; estado=0; } }};
	 */

	Handler handler = new Handler();

	private void hilador() {
		Thread hilo = new Thread() {
			public void run() {
				try {
					while (bandera == true) {
						handler.post(proceso);
						Thread.sleep(5000);
					}
				} catch (Exception e) {
				}
			}
		};
		hilo.start();
	}

	Runnable proceso = new Runnable() {
		public void run() {
			mapa.clear();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);

		pr = (TextView) findViewById(R.id.textView1);

		mapa = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(KRADAC, 15));
		mapa.setMyLocationEnabled(true);
		mapa.getUiSettings().setZoomControlsEnabled(false);
		mapa.getUiSettings().setCompassEnabled(true);

		
		 /*try { postValores.add(new BasicNameValuePair("usuario", "kradac"));
		 postValores.add(new BasicNameValuePair("contrasenia", "kradac"));
		 
		 try { respuesta = conectarHp .postData(
		 "http://200.0.29.117:8080/kparkingmobile/cargarDatoslibres.php",
		 postValores); respuesta =
		 "-3.9949646826415@-79.200601627079$Bernardo;Colon#";
		 MainActivity.response = respuesta;
		 
		 extraerDatos(respuesta);
		 
		 } catch (Exception e) { e.printStackTrace(); } } catch (Exception e)
		 { }*/
		 
		extraerDatos(respuesta);
		mapa.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng point) {
				// TODO Auto-generated method stub
				Projection proj = mapa.getProjection();
				Point coord = proj.toScreenLocation(point);
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("google.navigation:q=" + point.latitude + ","
								+ point.longitude));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
	}

	/*
	 * public boolean onKeyDown(int keyCode, KeyEvent event) { // TODO
	 * Auto-generated method stub
	 * 
	 * switch(keyCode){ case KeyEvent.KEYCODE_MENU: Toast.makeText(this,
	 * "Boton de menu presionado", Toast.LENGTH_SHORT).show(); return true; }
	 * return super.onKeyDown(keyCode, event); }
	 */
	public void extraerDatos(String cadena) {
		cadena = "-79.200601627079%-3.9949646826415%Bernardo ; Colon%12#-79.200409232823%-3.9955380622328%Olmedo; Colon%11#-79.200719127465%-3.9959418749041%Olmedo; Eguiguren%10#-79.200155343056%-3.9981505763116%Olmedo; Rocafuerte%13#-79.200948054695%-3.9984570798862%Bernardo; Miguel Riofrio%5#-79.200929380798%-3.9987464196635%Bernardo; Miguel Riofrio%13#-79.202158534242%-3.9939345858975%Bolivar; Quito%9#-79.202398915104%-3.993678424163%Bolivar; Quito%10#-79.202312563898%-3.9946419963387%Bolivar; Imbabura%11#-79.201916591835%-3.9956910543853%Bolivar; Colon%15#-79.201721096421%-3.999498806405%Bolivar; Miguel Riofrio%8#-79.201670328332%-3.9999353467014%Bolivar; Miguel Riofrio%9#-79.201796442714%-4.0019453210846%Bolivar; Mercadillo%10#-79.204080334229%-3.9948610741807%18 de Noviembre; Imbabura%8#-79.203786740823%-3.9962536719268%J.Antonio Eguiguren; 18 de Noviembre%13#-79.204384723855%-3.9984262643304%Rocafuerte; 18 de Noviembre%10#-79.199910799219%-3.9981854530564%Olmedo; Rocafuerte%15#-79.204551020814%-3.9974255592401%10 de Agosto; 18 de Noviembre%6#-79.202533999635%-4.0003955627582%Azuay; Sucre%13#-79.202555457307%-3.9991486977267%Riofrio; Sucre%11";
		cadena = cadena.substring(0, cadena.length()-1);
		String[] dataPoint = cadena.split("#");
		for (int i = 0; i < dataPoint.length; i++) {
			String data[] = dataPoint[i].split("%");

			LatLng point = new LatLng(Double.parseDouble(data[1]),
					Double.parseDouble(data[0]));

			graficarMapa(point, data[2], "plazas libres: " + data[3],
					Integer.parseInt(data[3]));
		}

	}

	public void graficarMapa(LatLng posicion, String Titulo,
			String Descripcion, int pla_libres) {
		if (pla_libres >= 10) {
			mapa.addMarker(new MarkerOptions()
					.position(posicion)
					.title(Titulo)
					.snippet(Descripcion)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_parqueadero))
					.anchor(0.2f, 0.2f));

		} else if (pla_libres >= 5 && pla_libres < 10) {
			mapa.addMarker(new MarkerOptions()
					.position(posicion)
					.title(Titulo)
					.snippet(Descripcion)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_par_naranja))
					.anchor(0.2f, 0.2f));
		} else if (pla_libres < 5) {
			mapa.addMarker(new MarkerOptions()
					.position(posicion)
					.title(Titulo)
					.snippet(Descripcion)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_parq_rojo))
					.anchor(0.2f, 0.2f));

		}

	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}

		return false;
	}

	public boolean onCreateOptionsMenu(Menu menu) {

		SubMenu submenu1 = menu.addSubMenu(Menu.NONE, menu_mapas, Menu.NONE,
				"Mapas").setIcon(R.drawable.mapaicono);
		submenu1.add(Menu.NONE, menu_mapas_normal, Menu.NONE, "Mapa Normal");
		submenu1.add(Menu.NONE, menu_mapas_hibrido, Menu.NONE, "Mapa Hibrido");
		submenu1.add(Menu.NONE, menu_mapas_satelite, Menu.NONE, "Mapa Satelite");
		submenu1.add(Menu.NONE, menu_mapas_terreno, Menu.NONE, "Mapa Terreno");

		SubMenu submenu2 = menu.addSubMenu(Menu.NONE, menu_buscar, Menu.NONE,
				"Buscar").setIcon(R.drawable.buscar);
		submenu2.add(Menu.NONE, menu_buscar_1, Menu.NONE, direccionMejor);

		menu.add(Menu.NONE, menu_ayuda, Menu.NONE, "Ayuda").setIcon(
				R.drawable.help);
		menu.add(Menu.NONE, menu_reservar, Menu.NONE, "Reservar").setIcon(
				R.drawable.help);

		return true;
	}

	// Añadiendo funcionalidad a las opciones de menú
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case menu_mapas_normal:
			mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			return true;
		case menu_mapas_hibrido:
			mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			return true;
		case menu_mapas_satelite:
			mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			return true;
		case menu_mapas_terreno:
			mapa.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			return true;
		case menu_buscar_1:
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("google.navigation:q=" + latitudMejor + ","
							+ longitudMejor));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			return true;
		case menu_ayuda:
			Intent ayuda = new Intent(Login.this, Help.class);
			startActivity(ayuda);
			return true;
		case menu_reservar:
			Intent reservar = new Intent(Login.this, Reservar.class);
			startActivity(reservar);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			bandera2 = false;
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public static int getDistance(double lat1, double lon1, double lat2,
			double lon2) {
		double radius = 6371000; // Radio de la tierra
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
				* Math.sin(dLon / 2);
		double c = 2 * Math.asin(Math.sqrt(a));
		return (int) (radius * c);
	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
	}

}