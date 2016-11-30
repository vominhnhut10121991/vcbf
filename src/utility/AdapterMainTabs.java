package utility;

import activity_and_fragment.FragmentMainAboutUs;
import activity_and_fragment.FragmentMainFund;
import activity_and_fragment.FragmentMainMessage;
import activity_and_fragment.FragmentMainNews;
import activity_and_fragment.FragmentMainSetting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AdapterMainTabs extends FragmentPagerAdapter {

	public AdapterMainTabs(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
		case 0:
			return new FragmentMainAboutUs();
		case 1:
			return new FragmentMainNews();
		case 2:
			return new FragmentMainFund();
		case 3:
			return new FragmentMainMessage();
		case 4:
			return new FragmentMainSetting();
		}
		return null;
	}

	@Override
	public int getCount() {
		return 5;
	}
}