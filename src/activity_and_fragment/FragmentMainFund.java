package activity_and_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.vcbf.R;

public class FragmentMainFund extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main_fund, container,
				false);
		rootView.findViewById(R.id.button_balance_fund).setOnClickListener(
				(new View.OnClickListener() {
					public void onClick(View view) {
						startActivity(new Intent(getActivity(),
								ActivityFundBalanced.class));
					}
				}));
		rootView.findViewById(R.id.button_blue_chip_fund).setOnClickListener(
				(new View.OnClickListener() {
					public void onClick(View view) {
						startActivity(new Intent(getActivity(),
								ActivityFundBlueChip.class));
					}
				}));
		return rootView;
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
}