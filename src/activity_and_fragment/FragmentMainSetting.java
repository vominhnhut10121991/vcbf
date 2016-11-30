package activity_and_fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import utility.ArrayAdapterSetting;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vcbf.R;

public class FragmentMainSetting extends Fragment implements
		AdapterView.OnItemClickListener {
	Locale locale;
	private int options[] = { 0, 0 };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_main_setting,
				container, false);
		String[] settings = getResources().getStringArray(R.array.settings);
		int[] images = { R.drawable.ic_settings_language,
				R.drawable.ic_settings_notification };
		ArrayAdapterSetting adapter = new ArrayAdapterSetting(getActivity(),
				settings, images);
		ListView listView = (ListView) rootView.findViewById(R.id.settings);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		initializeOptions(getActivity());
		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.choose_language)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface dialogInterface, int i) {
								ListView list = ((AlertDialog) dialogInterface)
										.getListView();
								int option = list.getCheckedItemPosition();

								String lang = "vi";
								switch (option) {
								case 0:
									lang = "en";
									break;
								case 1:
									lang = "vi";
									break;
								}

								options[0] = option;
								writeFile(getActivity());
								setLocale(lang);
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface dialogInterface, int i) {

							}
						})
				.setSingleChoiceItems(R.array.languages, options[0],
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface dialogInterface, int i) {

							}
						});
		AlertDialog languageDialog = builder.create();
		builder.setTitle(R.string.turn_notification)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface dialogInterface, int i) {
								ListView lv = ((AlertDialog) dialogInterface)
										.getListView();
								int option = lv.getCheckedItemPosition();
								options[1] = option;
								writeFile(getActivity());
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface dialogInterface, int i) {

							}
						})
				.setSingleChoiceItems(R.array.onAndOff, options[1],
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface dialogInterface, int i) {

							}
						});
		AlertDialog notificationDialog = builder.create();

		switch (position) {
		case 0:
			languageDialog.show();
			break;
		case 1:
			notificationDialog.show();
			break;
		}
	}

	public void setLocale(String lang) {
		locale = new Locale(lang);
		Resources res = getResources();
		DisplayMetrics metrics = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = locale;
		res.updateConfiguration(conf, metrics);
		this.getActivity().finish();
		Intent refresh = new Intent(getActivity(), ActivityMain.class);
		startActivity(refresh);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onStop() {
		super.onStop();

	}

	public void writeFile(Context context) {
		String filename = "settings.dat";
		FileOutputStream outputStream;

		File file = new File(context.getFilesDir(), filename);
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {
				Log.d("Loi IO", e1.getMessage());
			}

		try {
			outputStream = context.openFileOutput(filename,
					Context.MODE_PRIVATE);
			String separator = System.getProperty("line.separator");
			for (int i = 0; i < options.length; i++) {
				outputStream.write(Integer.toString(options[i]).getBytes());
				outputStream.write(separator.getBytes());
			}
			outputStream.close();
		} catch (IOException e) {
			Log.d("Loi IO", e.getMessage());
		}
	}

	public void initializeOptions(Context context) {
		try {
			String filename = "settings.dat";
			File settingsFile = context.getFileStreamPath(filename);
			if (settingsFile.exists()) {
				FileInputStream fis = context.openFileInput(filename);
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i != -1;) {
					i = fis.read();
					if (i != -1)
						sb.append(Character.toString((char) i));
				}
				String settingsStr = sb.toString();
				settingsStr = settingsStr.trim();
				settingsStr = settingsStr.replaceAll("[^0-9]+", " ");
				int index = 0;
				for (String value : settingsStr.split(" ")) {
					options[index] = Integer.parseInt(value);
					index++;
				}

				fis.close();
			} else {
				options[0] = 0;
				options[1] = 0;
			}
		} catch (IOException e) {
			Log.d("Loi IO", e.getMessage());
		}
	}
}