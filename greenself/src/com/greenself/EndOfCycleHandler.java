package com.greenself;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import android.content.Context;
import android.content.SharedPreferences;

import com.greenself.constants.Constants;
import com.greenself.constants.Constants.Type;
import com.greenself.dbhandlers.DBManager;
import com.greenself.observers.ObserverPatternWatched;
import com.greenself.observers.TasksChangeListener;

public class EndOfCycleHandler extends ObserverPatternWatched {
	private static final Logger log = Logger.getLogger(EndOfCycleHandler.class
			.getName());
	private List<TasksChangeListener> listenerList = new ArrayList<TasksChangeListener>();
	// private static DailyTasksFragment dailyTasksFragment;
	private static EndOfCycleHandler instance = null;

	public EndOfCycleHandler() {
		// exists only to defeat instantiation
	}

	// TODO - check instance
	public static EndOfCycleHandler getInstance() {
		if (instance == null) {
			instance = new EndOfCycleHandler();
		}
		return instance;
	}

	public void checkEndOfCycle(Context context, DailyTasksFragment fragment) {
		// dailyTasksFragment = fragment;
		SharedPreferences prefs = context.getSharedPreferences(Constants.APP,
				Context.MODE_PRIVATE);
		Long lastDailyUpdateTime = prefs
				.getLong(Constants.LAST_DAILY_UPDATE, 0);
		Long lastWeeklyUpdateTime = prefs.getLong(Constants.LAST_WEEKLY_UPDATE,
				0);
		Long lastMonthlyUpdateTime = prefs.getLong(
				Constants.LAST_MONTHLY_UPDATE, 0);

		Date now = new Date();

		if (now.getTime() / 1000 - lastDailyUpdateTime >= Constants.BETWEEN_DAYS) {
			endOfCycleUpdates(context, Constants.Type.DAILY);
		}
		if (now.getTime() / 1000 - lastWeeklyUpdateTime >= Constants.BETWEEN_WEEKS) {
			endOfCycleUpdates(context, Constants.Type.WEEKLY);
		}
		if (now.getTime() / 1000 - lastMonthlyUpdateTime >= Constants.BETWEEN_MONTHS) {
			endOfCycleUpdates(context, Constants.Type.MONTHLY);
		}
	}

	public void endOfCycleUpdates(Context context, Type type) {
		// save to history the tasks that have been completed 
		TaskHandler.archiveCompletedTasks(context, type);
		log.info("Now in History: "
				+ DBManager.getInstance(context).getDaoSession()
						.getTaskHistoryDao().loadAll().toString());

		// drop completed tasks
		TaskHandler.dropCompletedTasks(context, type);

		// drop tasks from active
		TaskHandler.dropNotCompletedTasksFromActive(context, type);

		// generating new daily tasks for next cycle
		switch (type) {
		case DAILY:
			TaskHandler.generateNewTasks(context, Constants.NO_OF_DAILY_TASKS,
					type);
			break;
		case WEEKLY:
			TaskHandler.generateNewTasks(context, Constants.NO_OF_WEEKLY_TASKS,
					type);
			break;
		case MONTHLY:
			TaskHandler.generateNewTasks(context,
					Constants.NO_OF_MONTHLY_TASKS, type);
			break;
		}

		// notify changes to listeners
		notifyListeners();
	}

	// TODO - check
	private void notifyListeners() {
		for (TasksChangeListener listener : this.listenerList) {
			listener.onTasksChanged();
		}

	}

	// TODO - check
	@Override
	public void addChangeListener(TasksChangeListener newListener) {
		listenerList.add(newListener);
	}
}
