package com.example.mtc_bus;

import java.util.ArrayList;
import java.util.List;

import com.example.hospitalmap.GooglePlacesActivity;
import com.example.hospitalmap.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Bus_Page extends Activity {

	ListView listview;
	CustomAdapter1 adapter;
	private List<ViewItem> rowItems = new ArrayList<ViewItem>();
	String[] bus;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);

		listview = (ListView) findViewById(R.id.listView1);
		bus = new String[] { "6D", "500A", "1D", "11G", "25G", "21G", "56N",
				"57F", "12B", "51B" };

		for (String s : bus) {
			ViewItem row = new ViewItem();

			row.settask(s);
			rowItems.add(row);
		}

		adapter = (CustomAdapter1) new CustomAdapter1(getApplicationContext(),
				R.layout.list_view, rowItems);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ViewItem value = (ViewItem) listview
						.getItemAtPosition(position);
				String name = value.gettask();
				SharedPreferences shar = getSharedPreferences("User",
						Context.MODE_PRIVATE);
				SharedPreferences.Editor edit = shar.edit();
				edit.putString("Detail", name);
				edit.commit();

				Intent it = new Intent(getApplicationContext(),
						GooglePlacesActivity.class);
				it.putExtra("Bus", name);
				startActivity(it);
			}
		});

	}

}
