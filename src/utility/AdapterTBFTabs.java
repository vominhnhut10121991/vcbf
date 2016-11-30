package utility;

import activity_and_fragment.FragmentTBFInformation;
import activity_and_fragment.FragmentFundLogin;
import activity_and_fragment.FragmentTBFNAV;
import activity_and_fragment.FragmentTBFPerformance;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AdapterTBFTabs extends FragmentPagerAdapter {

	public AdapterTBFTabs(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
		case 0:
			return new FragmentTBFInformation();
		case 1:
			return new FragmentTBFNAV();
		case 2:
			return new FragmentTBFPerformance();
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