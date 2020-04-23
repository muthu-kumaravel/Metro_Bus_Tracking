package com.example.mtc_bus;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

public class Gps_Service extends Service 
{
	boolean bool=true;
	boolean smsbool=true;
	boolean mailbool=true;	
	
	String latitude,longitude,routenum;
	GpsTracker gpt = null;
//	LocationManager locationManager;
//	String provider;
	boolean isDualSIM=false;
	;
	public Gps_Service()
	{
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		Bundle extras=intent.getExtras();
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	
	@Override
    public void onCreate()
	{
		if (android.os.Build.VERSION.SDK_INT > 9) {
       	 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

       }
		
		SharedPreferences shar = getApplicationContext().getSharedPreferences("User", Context.MODE_PRIVATE);
		routenum = shar.getString("Detail", null);
		
		
		bool=true;
    }
 
	 
	/*
    @Override
    public void onStart(Intent intent, int startId)
    {
    	gpt = new GpsTracker(getApplicationContext());
    	 Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
    	Thread thread = new Thread()
    	{
    	    @Override
    	    public void run()
    	    {
    	        try
    	        {
    	            while(bool)
    	            {
    	            	try
    	            	{
	    	                sleep(1000);
	    	                 gpt.getLocation();
	    			    	 latitude=String.valueOf(gpt.getLatitude());
							 longitude=String.valueOf(gpt.getLongitude());	 
							 Log.e("service lat", latitude);
							 Log.e("service long", longitude);
	    			     
	    	                Message msgToActivity = new Message();
    						msgToActivity.what = 0; 
  							msgToActivity.obj  = latitude+"-"+longitude;
   							MainActivity.Handler.sendMessage(msgToActivity);
	    	                
	    	            }
    	            	catch (Exception e)
    	            	{

    	    	        }
    	            }
    	        }
    	        catch (Exception e)
    	        {
    	            e.printStackTrace();
    	        }
    	    }
    	};
    	thread.start();
	}*/
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
				gpt = new GpsTracker(getApplicationContext());
			Toast.makeText(getApplicationContext(), "Username="+routenum, Toast.LENGTH_LONG).show();
			new Thread() {
				@SuppressLint("NewApi")
				@Override
				public void run() {
					while (bool) {
						try {
						
								  gpt.getLocation();
	    			    	 latitude=String.valueOf(gpt.getLatitude());
							 longitude=String.valueOf(gpt.getLongitude());	
						     
							String url = Connection.UPLOAD+"route_id="+routenum+"&lat="+latitude+"&long="+longitude;
							url = url.replace(" ", "%20");
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
							msgToActivity.obj = latitude+"-------------"+longitude;

							if (!str.equalsIgnoreCase("")) {
								Log.e("jsonreturns", str);
							

							}

						
							else {
								
							}
							sleep(15000);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return START_STICKY;
	}
 
    @Override
    public void onDestroy()
    {
    	bool=false;
    	smsbool=true;
    	mailbool=true;
    	gpt.stopUsingGPS();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

	  
   

	


	
}
