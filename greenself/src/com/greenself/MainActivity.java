package com.greenself;

import java.util.logging.Logger;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.greenself.adapters.MyPagerAdapter;
import com.greenself.constants.Constants;
import com.greenself.events.EventsBusFactory;
import com.greenself.events.MultipleTasksChangeEvent;
import com.greenself.handlers.DBManager;
import com.greenself.handlers.ScoreHandler;
import com.greenself.loaders.CycleUpdatesLoader;

public class MainActivity extends FragmentActivity implements
		LoaderCallbacks<Boolean> {
	private static final Logger log = Logger.getLogger(MainActivity.class
			.getName());

	private ViewPager viewPager = null;

//	private Fragment findFragmentByIndex(int index) {
//		String fragmentName = "android:switcher:" + viewPager.getId() + ":"
//				+ index;
//		return getSupportFragmentManager().findFragmentByTag(fragmentName);
//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// check if database is initialized
		SharedPreferences prefs = this.getSharedPreferences(Constants.APP,
				Context.MODE_PRIVATE);
		int databaseVersion = prefs.getInt(Constants.PREF_DB_VERSION, 0);
		if (databaseVersion < Constants.APP_DB_VERSION) {
			DBManager.getInstance(getBaseContext()).resetDB();
			prefs.edit()
					.putInt(Constants.PREF_DB_VERSION, Constants.APP_DB_VERSION)
					.commit();
		}

		// check existing active tasks and generate new if there aren't any
		if (TaskHandler.verifyIfPreviousTasks(this) == false) {
			log.info("No existing tasks. Need to generate.");

			TaskHandler
					.generateActiveTasks(this, Constants.NO_OF_DAILY_TASKS,
							Constants.NO_OF_WEEKLY_TASKS,
							Constants.NO_OF_MONTHLY_TASKS);
		} else {
			log.info("Existing tasks");
		}

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		viewPager = (ViewPager) findViewById(R.id.pager);
		FragmentManager fm = getSupportFragmentManager();
		viewPager.setAdapter(new MyPagerAdapter(fm));
		
		ScoreHandler.getInstance(this).calculateAllStatistics();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_resetdb:
			DBManager.getInstance(this).resetDB();
			Toast.makeText(this,
					"Reset database. Please restart your application.",
					Toast.LENGTH_LONG).show();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// log.info("IN RESUME");
		getSupportLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<Boolean> onCreateLoader(int id, Bundle args) {
		return new CycleUpdatesLoader(getBaseContext());
	}

	@Override
	public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
		if (data) {
			// this.dailyTasksFragment = (DailyTasksFragment)
			// getSupportFragmentManager()
			// .findFragmentById(R.id.daily_tasks_fragment);
//			DailyTasksFragment dailyTasksFragment = (DailyTasksFragment) findFragmentByIndex(MyPagerAdapter.FRAGMENT_POSITION_DAILY_TASKS);
//			if(dailyTasksFragment != null)
//				dailyTasksFragment.onTasksChanged();
			EventsBusFactory.getInstance().post(new MultipleTasksChangeEvent());
			log.info("Should have been an update on tasks");
		}
	}

	@Override
	public void onLoaderReset(Loader<Boolean> loader) {

	}

}
