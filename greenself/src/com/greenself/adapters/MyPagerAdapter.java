package com.greenself.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.greenself.DailyTasksFragment;
import com.greenself.StatisticsFragment;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

	public final static int FRAGMENT_POSITION_DAILY_TASKS = 0;
	public final static int FRAGMENT_POSITION_STATS = 1;

	public MyPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;
		switch (position) {
		case FRAGMENT_POSITION_DAILY_TASKS:
			fragment = new DailyTasksFragment();
			break;
		case FRAGMENT_POSITION_STATS:
			fragment = new StatisticsFragment();
			break;
		}

		return fragment;
	}

	@Override
	public int getCount() {
		// just 2 pages for now (Daily Tasks and Statistics)
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
		case 0:
			return "To Do List";
		case 1:
			return "Stats";
		}
		
		return null;
	}
}
