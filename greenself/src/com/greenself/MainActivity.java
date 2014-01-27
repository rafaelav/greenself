package com.greenself;

import java.util.logging.Logger;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.greenself.dbhandlers.DBManager;
import com.greenself.objects.Constants;

public class MainActivity extends FragmentActivity {
	private static final Logger log=Logger.getLogger(MainActivity.class.getName());
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// only first time the app runs
		//DBManager.getInstance(this).resetDB();
		
		log.info(DBManager.getInstance(this).getDaoSession().getTaskSourceDao().loadAll()+"");

		// check existing active tasks and generate new if there aren't any
		if (TaskHandler
				.verifyIfPreviousTasks(TaskHandler.loadActiveTasks(this)) == false) {
			log.info("No existing tasks. Need to generate.");
			
			TaskHandler.generateActiveTasks(this, Constants.NO_OF_TASKS);
		} else {
			log.info("Existing tasks");
		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
