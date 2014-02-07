package com.greenself;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import android.content.Context;
import android.content.SharedPreferences;

import com.greenself.daogen.Task;
import com.greenself.objects.Constants;

public class EndOfCycleHandler {
	private static final Logger log = Logger.getLogger(EndOfCycleHandler.class
			.getName());

	public static void endOfCycleUpdates(Context context) {
		// used in saving last updates for weekly/monthly updates if needed
		Date now = new Date();

		// save to history the tasks that have been completed
		TaskHandler.archiveCompletedTasks(context);

		// drop completed tasks
		TaskHandler.dropCompletedTasks(context);

		// drop daily tasks from active
		TaskHandler.dropNotCompletedTasksFromActive(context,
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
			TaskHandler.dropNotCompletedTasksFromActive(context,
					Constants.Type.WEEKLY);
			TaskHandler.generateNewTasks(context, Constants.NO_OF_WEEKLY_TASKS,
					Constants.Type.WEEKLY);
			// update time stamp for last weekly update
			prefs.edit()
					.putLong(Constants.LAST_WEEKLY_UPDATE, now.getTime() / 1000)
					.commit();
		}

		// drop monthly tasks if needed (either all completed or time to change
		// them) & regenerate them for next cycle
		if (monthlyTasksToReg == Constants.NO_OF_MONTHLY_TASKS) {
			TaskHandler.dropNotCompletedTasksFromActive(context,
					Constants.Type.MONTHLY);
			TaskHandler.generateNewTasks(context,
					Constants.NO_OF_MONTHLY_TASKS, Constants.Type.MONTHLY);
			// update time stamp for last monthly update
			prefs.edit()
					.putLong(Constants.LAST_MONTHLY_UPDATE,
							now.getTime() / 1000).commit();
		}

		// generating new daily tasks for next cycle
		TaskHandler.generateNewTasks(context, Constants.NO_OF_DAILY_TASKS,
				Constants.Type.DAILY);

		// TODO : update UI
	}

	private static int determineNoOfMonthlyTasksToReg(Context context,
			Long lastUpdate) {
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

	private static int determineNoOfWeeklyTasksToReg(Context context,
			Long lastUpdate) {
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
}
