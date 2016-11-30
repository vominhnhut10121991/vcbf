package activity_and_fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vcbf.R;

public class FragmentMainAboutUs extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_main_about_us,
				container, false);
		TextView textView = (TextView) rootView
				.findViewById(R.id.tv_fragment_about_us);
		textView.setText(Html.fromHtml(getResources().getString(
				R.string.about_us_text)));
		return rootView;
	}
}