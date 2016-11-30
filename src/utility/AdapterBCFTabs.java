package utility;

import activity_and_fragment.FragmentBCFInformation;
import activity_and_fragment.FragmentFundLogin;
import activity_and_fragment.FragmentBCFNAV;
import activity_and_fragment.FragmentBCFPerformance;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AdapterBCFTabs extends FragmentPagerAdapter {

	public AdapterBCFTabs(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
		case 0:
			return new FragmentBCFInformation();
		case 1:
			return new FragmentBCFNAV();
		case 2:
			return new FragmentBCFPerformance();
		case 3:
			return new FragmentFundLogin();
		}
		return null;
	}

	@Override
	public int getCount() {
		return 4;
	}
}