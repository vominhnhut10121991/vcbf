package broadcast_and_service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import utility.Data;

import activity_and_fragment.ActivityMain;
import activity_and_fragment.ActivityPopupMessage;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.vcbf.R;

public class VCBFService extends IntentService {

	public VCBFService() {
		super("VCBFService");
	}

	@Override
	protected void onHandleIntent(Intent work_intent) {
		Log.d("Current date", getTime());
		if (!Data.remindingMessage.isEmpty()) {
			String[] currentDateTime = getTime().split(" ");
			String[] currentDate = currentDateTime[0].split("-");
			String[] currentTime = currentDateTime[1].split(":");
			Calendar current = Calendar.getInstance();

			current.set(Integer.parseInt(currentDate[0]),
					Integer.parseInt(currentDate[1]) - 1,
					Integer.parseInt(currentDate[2]),
					Integer.parseInt(currentTime[0]),
					Integer.parseInt(currentTime[1]),
					Integer.parseInt(currentTime[2]));

			String[] scheduledDateTime = Data.remindingMessage.get(0).date
					.split(" ");
			String[] scheduledDate = scheduledDateTime[0].split("-");
			String[] scheduledTime = scheduledDateTime[1].split(":");
			Calendar scheduled = Calendar.getInstance();
			scheduled.set(Integer.parseInt(scheduledDate[0]),
					Integer.parseInt(scheduledDate[1]) - 1,
					Integer.parseInt(scheduledDate[2]),
					Integer.parseInt(scheduledTime[0]),
					Integer.parseInt(scheduledTime[1]),
					Integer.parseInt(scheduledTime[2]));

			long delay = scheduled.getTimeInMillis()
					- current.getTimeInMillis();
			Log.d("schedule", Data.remindingMessage.get(0).date);
			Timer timer = new Timer();
			TimerTask tt = new TimerTask() {
				@Override
				public void run() {
					Uri sound = RingtoneManager
							.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
					Intent result_intent = new Intent(getApplicationContext(),
							ActivityMain.class);
					TaskStackBuilder stack_builder = TaskStackBuilder
							.create(getApplicationContext());
					stack_builder.addParentStack(ActivityMain.class);
					stack_builder.addNextIntent(result_intent);
					PendingIntent result_pending_intent = stack_builder
							.getPendingIntent(0,
									PendingIntent.FLAG_UPDATE_CURRENT);
					Notification notificication = new Notification.Builder(
							getApplicationContext())
							.setContentTitle(Data.remindingMessage.get(0).title)
							.setContentText(
									Data.remindingMessage.get(0).content)
							.setSmallIcon(R.drawable.vcbf_icon)
							.setAutoCancel(true)
							.setWhen(System.currentTimeMillis())
							.setVibrate(
									new long[] { 1000, 1000, 1000, 1000, 1000 })
							.setLights(Color.RED, 3000, 3000).setSound(sound)
							.setContentIntent(result_pending_intent).build();

					NotificationManagerCompat notificationMananger = NotificationManagerCompat
							.from(getApplicationContext());

					notificationMananger.notify(0, notificication);

					Data.storedRemindingMessages.add(Data.remindingMessage
							.get(0));
					Data.writeStoredRemindingMessages();

					Intent intent_popup_message = new Intent(
							getApplicationContext(), ActivityPopupMessage.class);
					intent_popup_message
							.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent_popup_message);
				}
			};
			if (delay > 0) {
				timer.schedule(tt, delay);
			}
		}
	}

	public String getTime() {
		String responseString = "";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(new HttpGet(
					Data.webServiceLink + "getTime"));
			StatusLine sl = response.getStatusLine();
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();
				Log.d("Response ne", responseString);
			} else {
				response.getEntity().getContent().close();
			}
		} catch (ClientProtocolException e) {
			Log.d("Loi client protocol", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input out put", e.getMessage());
		}
		return responseString;
	}
}