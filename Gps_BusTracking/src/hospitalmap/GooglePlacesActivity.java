package hospitalmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import com.connection.Service_Call;
import com.example.gps_bustracking.MainActivity;
import com.example.hospitalmap.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("NewApi")
public class GooglePlacesActivity extends FragmentActivity implements
		LocationListener {

	public static final String GOOGLE_API_KEY = "AIzaSyCeUrl7gFCnSwD5BXd0dcyFSRGCWjtmYEM";
	GoogleMap googleMap;
	// PolylineOptions lineOptions = null;
	Intent intent;
	EditText placeText;
	double latitude = 0;
	double longitude = 0;
	public static final int PROXIMITY_RADIUS = 1500;
	String address = "";
	String latlng = "";
	public static Handler Handler;
	List<MarkerOptions> marker = new ArrayList<MarkerOptions>();

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {

			if (!isGooglePlayServicesAvailable()) {
				finish();
			}
			setContentView(R.layout.activity_google_places);

			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}

			intent = new Intent(getApplicationContext(), Service_Call.class);
			startService(intent);

			SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.googleMap);
			googleMap = fragment.getMap();
			googleMap.setMyLocationEnabled(true);
			LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			String bestProvider = locationManager.getBestProvider(criteria,
					true);
			Location location = locationManager
					.getLastKnownLocation(bestProvider);
			if (location != null) {
				onLocationChanged(location);
			}
			locationManager
					.requestLocationUpdates(bestProvider, 20000, 0, this);

			LatLng latLng = new LatLng(latitude, longitude);
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

			Handler = new Handler() {
				public void handleMessage(Message msg) {
					try {

						googleMap.clear();

						org.json.simple.JSONArray jarray = new org.json.simple.JSONArray();

						JSONParser parse1 = new JSONParser();
						org.json.simple.JSONObject job = new org.json.simple.JSONObject();
						job = (org.json.simple.JSONObject) parse1.parse(msg.obj
								.toString());

						String lati = (job.get("lat_data").toString());
						String longi = (job.get("long_data").toString());
						String repl = (job.get("UTIME").toString());

						LatLng latLng2 = new LatLng(Double.parseDouble(lati),
								Double.parseDouble(longi));

						MarkerOptions markerOptions = new MarkerOptions();
						markerOptions.position(latLng2);
						markerOptions.title("Reached At-:->" + repl);
						markerOptions.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.bb));

						googleMap.addMarker(markerOptions);

					} catch (Exception e) {
						try {
							File myFile = new File(
									"/sdcard/Diwakar/BusTRack.txt");
							myFile.createNewFile();
							FileOutputStream fOut = new FileOutputStream(myFile);
							PrintStream ps = new PrintStream(fOut);
							e.printStackTrace(ps);
							ps.close();
							fOut.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			};

			CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
			CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
			googleMap.moveCamera(center);
			googleMap.animateCamera(zoom);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG)
					.show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long

	}

	private boolean isGooglePlayServicesAvailable() {
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (ConnectionResult.SUCCESS == status) {
			return true;
		} else {
			GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
			return false;
		}
	}

	public void onBackPressed(){
		stopService(intent);
		startActivity(new Intent(getApplicationContext(),MainActivity.class));
		finish();
	}
	
	@Override
	public void onLocationChanged(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

	public static String getLatLongFromGivenAddress(String youraddress) {
		double lat = 0;
		double lng = 0;

		String location = youraddress;
		String uri = "http://maps.googleapis.com/maps/api/geocode/json?address="
				+ location + "&sensor=true";
		HttpGet httpGet = new HttpGet(uri);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());

			lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lng");

			lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lat");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		String latlng = String.valueOf(lat) + "@" + String.valueOf(lng);
		return latlng;
	}

}