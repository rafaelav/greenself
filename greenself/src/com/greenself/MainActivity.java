package com.greenself;

import java.util.logging.Logger;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.greenself.constants.Constants;
import com.greenself.dbhandlers.DBManager;

public class MainActivity extends FragmentActivity {
	private static final Logger log = Logger.getLogger(MainActivity.class
			.getName());
	private DailyTasksFragment dailyTasksFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// only first time the app runs

		// log.info(DBManager.getInstance(this).getDaoSession().getTaskSourceDao()
		// .loadAll()
		// + "");

		// check if database is initialized
		SharedPreferences prefs = this.getSharedPreferences(Constants.APP,
				Context.MODE_PRIVATE);
		int databaseVersion = prefs.getInt(Constants.PREF_DB_VERSION, 0);
		if (databaseVersion < Constants.APP_DB_VERSION) {
			DBManager.getInstance(this).resetDB();
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

		this.dailyTasksFragment = (DailyTasksFragment) getSupportFragmentManager()
				.findFragmentById(R.id.daily_tasks_fragment);
		setContentView(R.layout.activity_main);

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

		EndOfCycleHandler.getInstance().checkEndOfCycle(this,
				dailyTasksFragment);
	}
}
