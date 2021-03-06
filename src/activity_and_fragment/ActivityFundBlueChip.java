package activity_and_fragment;

import utility.AdapterBCFTabs;
import utility.AdapterTBFTabs;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.vcbf.R;

public class ActivityFundBlueChip extends FragmentActivity implements
		ActionBar.TabListener {

	// Global variables for tabs generation
	private ViewPager viewPager;
	private AdapterBCFTabs pagerAdapter;
	private ActionBar actionBar;
	private String[] tabTitles = new String[4];

	public void initializeActionBar() {
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowCustomEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		viewPager = (ViewPager) findViewById(R.id.fund_pager);
		pagerAdapter = new AdapterBCFTabs(getSupportFragmentManager());
		viewPager.setAdapter(pagerAdapter);

		tabTitles[0] = getResources().getString(R.string.fundInformation);
		tabTitles[1] = getResources().getString(R.string.fundNAV);
		tabTitles[2] = getResources().getString(R.string.fundPerformance);
		tabTitles[3] = getResources().getString(R.string.fundLogin);

		for (int i = 0; i < tabTitles.length; i++) {

			Tab newTab = actionBar.newTab().setTabListener(this)
					.setCustomView(R.layout.layout_tab);
			((TextView) newTab.getCustomView().findViewById(R.id.tv_tab_title))
					.setText(tabTitles[i]);
			actionBar.addTab(newTab);
		}

		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.activity_fund_tab);

		actionBar = getActionBar();
		initializeActionBar();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(arg0.getPosition());
	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(arg0.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

	}
}