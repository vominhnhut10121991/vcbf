package activity_and_fragment;

import utility.Data;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.vcbf.R;

public class ActivityPopupMessage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popup_message);

		if (Data.settings[0] == 0) {
			((TextView) findViewById(R.id.tv_title))
					.setText(Data.remindingMessage.get(0).title);
			((TextView) findViewById(R.id.tv_content))
					.setText(Data.remindingMessage.get(0).content);
		} else {
			((TextView) findViewById(R.id.tv_title))
					.setText(Data.remindingMessage.get(0).title_vn);
			((TextView) findViewById(R.id.tv_content))
					.setText(Data.remindingMessage.get(0).content_vn);
		}
	}
}