package activity_and_fragment;

import java.util.Locale;

import utility.Data;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import broadcast_and_service.VCBFService;

import com.example.vcbf.R;

public class ActivityMain extends Activity implements OnTouchListener {
	private ActionBar actionBar;
	private boolean doubleBackToExitPressedOnce = false;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle saved_instance_state) {

		super.onCreate(saved_instance_state);
		Data.context = getApplicationContext();
		setContentView(R.layout.activity_main);


		new Thread() {
			@Override
			public void run() {
				Data.initialiseData();
			}
		}.start();

		Intent mServiceIntent = new Intent(getApplicationContext(),
				VCBFService.class);
		getApplicationContext().startService(mServiceIntent);

		Data.getSettings();
		Locale locale = null;
		ImageView imageView = (ImageView)this.findViewById(R.id.imageview_logo);
		
		if (Data.settings[0] == 0) {
			locale = new Locale("en");
			imageView.setImageResource(R.drawable.vcbf_logo_en);
		} else {
			locale = new Locale("vi");
			imageView.setImageResource(R.drawable.vcbf_logo_vn);
		}
		Resources res = getResources();
		DisplayMetrics metrics = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		if (locale != null) {
			conf.locale = locale;
		}
		res.updateConfiguration(conf, metrics);
		if (Data.count == 0) {
			new LoadViewTask().execute();
		}
		Data.count++;

		actionBar = getActionBar();
		actionBar.hide();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowCustomEnabled(false);

		RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
		layout.setOnTouchListener(this);
	}

	// To use the AsyncTask, it must be subclassed
	private class LoadViewTask extends AsyncTask<Void, Integer, Void> {
		// Before running code in the separate thread
		@Override
		protected void onPreExecute() {
			// Create a new progress dialog
			progressDialog = new ProgressDialog(ActivityMain.this);
			// Set the progress dialog to display a horizontal progress bar
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			// Set the dialog title to 'Loading...'
			progressDialog.setTitle(getResources().getString(R.string.loading));
			// Set the dialog message to 'Loading application View, please
			// wait...'
			progressDialog.setMessage(getResources().getString(R.string.loadingDB));
			// This dialog can't be canceled by pressing the back key
			progressDialog.setCancelable(false);
			// This dialog isn't indeterminate
			progressDialog.setIndeterminate(false);
			// The maximum number of items is 100
			progressDialog.setMax(100);
			// Set the current progress to zero
			progressDialog.setProgress(0);
			// Display the progress dialog
			progressDialog.show();
		}

		// The code to be executed in a background thread.
		@Override
		protected Void doInBackground(Void... params) {
			/*
			 * This is just a code that delays the thread execution 4 times,
			 * during 850 milliseconds and updates the current progress. This is
			 * where the code that is going to be executed on a background
			 * thread must be placed.
			 */
			try {

				// Get the current thread's token
				synchronized (this) {

					// Initialize an integer (that will act as a counter) to
					// zero
					int counter = 0;
					// While the counter is smaller than four
					while (counter <= 10) {
						// Wait 850 milliseconds
						this.wait(300);
						// Increment the counter
						counter++;
						// Set the current progress.
						// This value is going to be passed to the
						// onProgressUpdate() method.
						publishProgress(counter * 10);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		// Update the progress
		@Override
		protected void onProgressUpdate(Integer... values) {
			// set the current progress of the progress dialog
			progressDialog.setProgress(values[0]);
		}

		// after executing the code in the thread
		@Override
		protected void onPostExecute(Void result) {
			// close the progress dialog
			progressDialog.dismiss();
			// initialize the View

		}
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

	@Override
	public void onBackPressed() {
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
			return;
		}

		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, "Please click back again to exit",
				Toast.LENGTH_SHORT).show();

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
			}
		}, 2000);
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		Intent k = new Intent(this, ActivityMainTab.class);
		this.startActivity(k);
		this.finish();
		return false;
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		super.onPause();
	}
}