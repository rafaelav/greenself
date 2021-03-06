package com.greenself;

import java.util.Date;
import java.util.logging.Logger;

import android.content.Context;
import android.content.SharedPreferences;

import com.greenself.constants.Constants;
import com.greenself.constants.Constants.Type;

public class EndOfCycleHandler {
	private static final Logger log = Logger.getLogger(EndOfCycleHandler.class
			.getName());

	private static EndOfCycleHandler instance = null;

	public EndOfCycleHandler() {
		// exists only to defeat instantiation
	}

	public static EndOfCycleHandler getInstance() {
		if (instance == null) {
			instance = new EndOfCycleHandler();
		}
		return instance;
	}

	public Boolean checkEndOfCycle(Context context) {
		Boolean identifiedEndOfACycle = false;

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
			identifiedEndOfACycle = true;
		}
		if (now.getTime() / 1000 - lastWeeklyUpdateTime >= Constants.BETWEEN_WEEKS) {
			endOfCycleUpdates(context, Constants.Type.WEEKLY);
			identifiedEndOfACycle = true;
		}
		if (now.getTime() / 1000 - lastMonthlyUpdateTime >= Constants.BETWEEN_MONTHS) {
			endOfCycleUpdates(context, Constants.Type.MONTHLY);
			identifiedEndOfACycle = true;
		}

		log.info("Identified an end of a cycle: " + identifiedEndOfACycle);
		return identifiedEndOfACycle;
	}

	public void endOfCycleUpdates(Context context, Type type) {
		// save to history the tasks that have been completed
		TaskHandler.archiveCompletedTasks(context, type);

		// drop task of certain type from active
		TaskHandler.dropTasks(context, type);

		// drop completed tasks
		// TaskHandler.dropCompletedTasks(context, type);

		// drop tasks from active
		// TaskHandler.dropNotCompletedTasksFromActive(context, type);

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
	}
}
