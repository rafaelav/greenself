package com.greenself;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import android.content.Context;
import android.content.SharedPreferences;

import com.greenself.constants.Constants;
import com.greenself.daogen.Task;
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
		Long lastDailyUpdate = prefs.getLong(Constants.LAST_DAILY_UPDATE, 0);
		Date now = new Date();

		if (now.getTime() / 1000 - lastDailyUpdate >= Constants.BETWEEN_DAYS) {
			endOfCycleUpdates(context);
		}
	}

	public void endOfCycleUpdates(Context context) {
		// save to history the tasks that have been completed (daily)
		TaskHandler.archiveCompletedTasks(context, Constants.Type.DAILY);
		log.info("Now in History: "
				+ DBManager.getInstance(context).getDaoSession()
						.getTaskHistoryDao().loadAll().toString());

		// drop completed tasks (daily)
		TaskHandler.dropCompletedTasks(context, Constants.Type.DAILY);

		// drop daily tasks from active
		TaskHandler.dropNotCompletedTasksFromActive(context,
				Constants.Type.DAILY);
		
		// generating new daily tasks for next cycle
		TaskHandler.generateNewTasks(context, Constants.NO_OF_DAILY_TASKS,
				Constants.Type.DAILY);

		// get last updates of monthly/weekly tasks
		SharedPreferences prefs = context.getSharedPreferences(Constants.APP,
				Context.MODE_PRIVATE);
		Long lastWeeklyUpdateTime = prefs.getLong(Constants.LAST_WEEKLY_UPDATE,
				0);
		Long lastMonthlyUpdateTime = prefs.getLong(
				Constants.LAST_MONTHLY_UPDATE, 0);

		int weeklyTasksToReg = determineNoOfWeeklyTasksToReg(context,
				lastWeeklyUpdateTime);

		int monthlyTasksToReg = determineNoOfMonthlyTasksToReg(context,
				lastMonthlyUpdateTime);

		// drop weekly tasks if needed (either all completed or time to change
		// them) & regenerate them for next cycle
		if (weeklyTasksToReg == Constants.NO_OF_WEEKLY_TASKS) {
			TaskHandler.archiveCompletedTasks(context, Constants.Type.WEEKLY);
			log.info("Now in History: "
					+ DBManager.getInstance(context).getDaoSession()
							.getTaskHistoryDao().loadAll().toString());
			// drop completed tasks
			TaskHandler.dropCompletedTasks(context, Constants.Type.WEEKLY);
			// drop not completed
			TaskHandler.dropNotCompletedTasksFromActive(context,
					Constants.Type.WEEKLY);
			TaskHandler.generateNewTasks(context, Constants.NO_OF_WEEKLY_TASKS,
					Constants.Type.WEEKLY);
		}

		// drop monthly tasks if needed (either all completed or time to change
		// them) & regenerate them for next cycle
		if (monthlyTasksToReg == Constants.NO_OF_MONTHLY_TASKS) {
			TaskHandler.archiveCompletedTasks(context, Constants.Type.MONTHLY);
			log.info("Now in History: "
					+ DBManager.getInstance(context).getDaoSession()
							.getTaskHistoryDao().loadAll().toString());
			// drop completed tasks
			TaskHandler.dropCompletedTasks(context, Constants.Type.MONTHLY);
			// drop not completed
			TaskHandler.dropNotCompletedTasksFromActive(context,
					Constants.Type.MONTHLY);
			TaskHandler.generateNewTasks(context,
					Constants.NO_OF_MONTHLY_TASKS, Constants.Type.MONTHLY);
		}

		// notify changes to listeners
		notifyListeners();
	}

	private int determineNoOfMonthlyTasksToReg(Context context, Long lastUpdate) {
		Date now = new Date();
		Long betweenUpdates = now.getTime() / 1000 - lastUpdate;
		if (betweenUpdates >= Constants.BETWEEN_MONTHS) {
			return Constants.NO_OF_MONTHLY_TASKS;
		} else {
			List<Task> tasks = TaskHandler.getNotDoneTypeTasks(context,
					Constants.Type.WEEKLY);
			return Constants.NO_OF_MONTHLY_TASKS - tasks.size();
		}
	}

	private int determineNoOfWeeklyTasksToReg(Context context, Long lastUpdate) {
		Date now = new Date();
		Long betweenUpdates = now.getTime() / 1000 - lastUpdate;
		if (betweenUpdates >= Constants.BETWEEN_WEEKS) {
			return Constants.NO_OF_WEEKLY_TASKS;
		} else {
			List<Task> tasks = TaskHandler.getNotDoneTypeTasks(context,
					Constants.Type.WEEKLY);
			return Constants.NO_OF_WEEKLY_TASKS - tasks.size();
		}
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
