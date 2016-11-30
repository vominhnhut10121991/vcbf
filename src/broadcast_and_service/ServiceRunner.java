package broadcast_and_service;

import utility.Data;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ServiceRunner extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		final Context finalContext = context;
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null
				|| Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			if (info.isConnected()) {
				new Thread() {
					@Override
					public void run() {
						Data.context = finalContext;
						Data.initialiseData();
						Intent mServiceIntent = new Intent(finalContext,
								VCBFService.class);
						finalContext.startService(mServiceIntent);
					}
				}.start();
			}
		}
	}
}