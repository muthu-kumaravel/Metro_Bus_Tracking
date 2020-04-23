package com.example.gps_bustracking;

import hospitalmap.GooglePlacesActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hospitalmap.R;

public class MainActivity extends ActionBarActivity {

	AutoCompleteTextView pref;
	EditText editText;
	List<String> list = new ArrayList<String>();
	Set<String> set = new HashSet<String>();
	List<String> list1;
	Set<String> set1;
	BufferedReader bufferedReader;
	HashMap<String, Set<String>> map;
	Button search;
	AutoCompleteTextView actv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		try {
			pref = (AutoCompleteTextView) findViewById(R.id.src_name);
			editText = (EditText) findViewById(R.id.dest_name);
			search = (Button) findViewById(R.id.search_button);

			bufferedReader = new BufferedReader(new InputStreamReader(
					getAssets().open("routes.txt")));
			String s = "";
			while ((s = bufferedReader.readLine()) != null) {
				String temp[] = s.split("->");
				for (int i = 1; i < temp.length; i++) {
					set.add(temp[i]);
				}

			}
			list.addAll(set);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.select_dialog_item, list);

			actv = (AutoCompleteTextView) findViewById(R.id.src_name);
			actv.setThreshold(1);// will start working from first character
			actv.setAdapter(adapter);

			editText.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						Log.e("oioioioioioioioioi", pref.getText().toString());
						map = new HashMap<String, Set<String>>();
						if (pref.getText().toString().equals("")) {
							pref.setError("Source Should not be Empty");
						} else {
							Log.e("oioioioioioioioioi", pref.getText().toString());
							list1 = new ArrayList<String>();
							bufferedReader = new BufferedReader(
									new InputStreamReader(getAssets().open(
											"routes.txt")));
							String s = "";
							while ((s = bufferedReader.readLine()) != null) {

								if (s.contains(pref.getText().toString())) {
									
									set1 = new HashSet<String>();
									String temp[] = s.split("->");
									String busid = temp[0];
									Log.e("inside", "-------" + busid);
									for (int i = 1; i < temp.length; i++) {
										if (temp[i].equalsIgnoreCase(pref
												.getText().toString())) {

										} else
											Log.e("name", temp[i]);
										set1.add(temp[i]);
									}
									map.put(busid, set1);
									list1.addAll(set1);
								}
							}
							Log.e("tempvariable", list1.toString());
							String sr[] = new String[list1.size()];
							list1.toArray(sr);
							showDialogBox("Select Destination", sr);
						}
					} catch (Exception e) {

					}

					
				}
			});

			search.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (editText.getText().toString().equals("")) {
						editText.setError("Destination should not be Empty");
					} else {
						int i = 0;
						List temp = new ArrayList();
						Log.e("map", map.toString());
						for (Object obj : map.keySet()) {
							Set s = map.get(obj.toString());
							if (s.contains(editText.getText().toString())) {
								temp.add(obj.toString());
								Log.e("tempbusvariable", temp.toString());
								i++;
							}

						}
						String sr[] = new String[temp.size()];
						temp.toArray(sr);
						Log.e("variable", temp.toString());
						showBus("Available Bus ", sr);
						
					}
				}
			});
		} catch (Exception e) {

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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	private void showDialogBox(final String type, final String[] items) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				MainActivity.this);
		alertDialogBuilder.setTitle(type);
		alertDialogBuilder.setSingleChoiceItems(items, 0,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						String selectedOption = items[item];
						editText.setText(selectedOption);
						dialog.dismiss();
					}

				});
		alertDialogBuilder.show();
	}

	private void showBus(final String type, final String[] items) {
		AlertDialog.Builder alertDiologueBuilder = new AlertDialog.Builder(
				MainActivity.this);
		AlertDialog alertDialogue = null;
		final ArrayList<String> mselecteditems = new ArrayList<String>();
		alertDiologueBuilder
				.setTitle(type)
				.setSingleChoiceItems(items, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								mselecteditems.add(items[item]);

							}

						})
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							if (mselecteditems.get(0) != null) {
								SharedPreferences shar = getSharedPreferences("User",
										Context.MODE_PRIVATE);
								SharedPreferences.Editor edit = shar.edit();
								edit.putString("Detail", mselecteditems.get(0));
								edit.commit();

								Intent it = new Intent(getApplicationContext(),
										GooglePlacesActivity.class);
								startActivity(it);
							}
						} catch (Exception e) {
							Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT).show();
						}

					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
		alertDialogue = alertDiologueBuilder.create();
		alertDialogue.show();
	}
}
