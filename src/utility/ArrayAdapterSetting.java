package utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vcbf.R;

/**
 * Created by dangdoan on 8/13/14.
 */
public class ArrayAdapterSetting extends ArrayAdapter {
	private Context context;
	private String[] settings;
	private int[] images;

	public ArrayAdapterSetting(Context context, String[] settings, int[] images) {
		super(context, R.layout.list_settings, settings);
		this.context = context;
		this.settings = settings;
		this.images = images;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.list_settings, null, true);
		ImageView imageView = (ImageView) view
				.findViewById(R.id.settings_image);
		TextView textView = (TextView) view.findViewById(R.id.settings_text);
		imageView.setImageResource(images[position]);
		textView.setText(settings[position]);
		return view;
	}
}
