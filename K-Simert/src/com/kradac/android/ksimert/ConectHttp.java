package com.kradac.android.ksimert;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;

public class ConectHttp {
	public String postData(String conexion, List nameValuePairs) {
		// Create a new HttpClient and Post Header
		String cadena = "";
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(conexion);

		try {
			// Add your data
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			InputStream is = response.getEntity().getContent();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(20);

			int current = 0;

			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			/* Convert the Bytes read to a String. */
			cadena = new String(baf.toByteArray());
			httpclient.getConnectionManager().shutdown();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			cadena = "error";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			cadena = "error";
		}

		return cadena;

	}

}
