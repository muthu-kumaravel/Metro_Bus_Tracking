package com.connection;

import hospitalmap.GooglePlacesActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;



public class Service_Call extends Service
{
	boolean b = true;
	String routenum= "";
	
	@Override
	public void onCreate() {
		try {
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
			
			SharedPreferences shar = getApplicationContext().getSharedPreferences("User", Context.MODE_PRIVATE);
			routenum = shar.getString("Detail", null);
			
			
		} catch (Exception e) {
			Log.d("Myservice", "" + e);
		}
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try
		{
			new Thread(){
				@Override
				public void run()
				{
					while(b)
					{
						try
						{		
						    String url=Connection.BUSSTATUS+"route_id="+routenum;
		    				url=url.replace(" ", "%20");
		    				HttpClient httpClient = new DefaultHttpClient();
		    				HttpGet getRequest = new HttpGet(url);
		    				HttpResponse res = httpClient.execute(getRequest);
		    				InputStream is = res.getEntity().getContent();
		    				byte[] b = null;
		    				ByteArrayOutputStream bos = new ByteArrayOutputStream();
		    				int ch;
		    				while ((ch = is.read()) != -1) {
		    					bos.write(ch);
		    				}
		    				b = bos.toByteArray();
		    				String str = new String(b).replace("\n", "").trim();

		    				Message msgToActivity = new Message();
							msgToActivity.what = 0;
							msgToActivity.obj = str;

							if (!str.equalsIgnoreCase("")) {
								Log.e("jsonreturns", str);
								GooglePlacesActivity.Handler.sendMessage(msgToActivity);

							}
		    				
		    				sleep(15000);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}.start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
		return START_STICKY;
	}
	
	@Override
    public void onDestroy()
    {
		stopSelf();
		b =false;
		 Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}