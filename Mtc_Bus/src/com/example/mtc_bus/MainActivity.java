package com.example.mtc_bus;

import com.example.hospitalmap.R;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

	SeekBar progressBar;
    private int progressStatus = 0;
	  private Handler handler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		 progressBar = (SeekBar) findViewById(R.id.progressBar);
	        new Thread(new Runnable() {
	            public void run() {
	                while (progressStatus < 100) {
	                    progressStatus += 6;
	                    // Update the progress bar and display the
	                    //current value in the text view
	                    handler.post(new Runnable() {
	                        public void run() {
	                            progressBar.setProgress(progressStatus);
	                         
	                        }
	                    });
	                    try {
	                        // Sleep for 200 milliseconds.
	                        Thread.sleep(200);
	                    } catch (InterruptedException e) {
	                        e.printStackTrace();
	                    }
	                }
	            }
	        }).start();
	        final Handler handler = new Handler();
	        handler.postDelayed(new Runnable() {
	            public void run() {
	                // TODO: Your application init goes here.
	                Intent mInHome = new Intent(MainActivity.this, Bus_Page.class);
	                startActivity(mInHome);
	                MainActivity.this.finish();
	            }
	        }, 3000);
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
}