package com.greenself;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.greenself.constants.Constants;
import com.greenself.constants.Constants.Type;
import com.greenself.daogen.DaoSession;
import com.greenself.daogen.Task;
import com.greenself.daogen.TaskDao;
import com.greenself.daogen.TaskHistory;
import com.greenself.daogen.TaskHistoryDao;
import com.greenself.daogen.TaskSource;
import com.greenself.daogen.TaskSourceDao;
import com.greenself.daogen.TaskSourceDao.Properties;
import com.greenself.dbhandlers.DBManager;

import de.greenrobot.dao.query.LazyList;

public class TaskHandler {
	private static final Logger log = Logger.getLogger(TaskHandler.class
			.getName());

	/**
	 * Loads the active tasks from the database
	 * 
	 * @param context
	 * @return list of active tasks from database
	 */
	public static List<Task> loadActiveTasks(Context context) {
		DaoSession daoSession = DBManager.getInstance(context).getDaoSession();
		return daoSession.getTaskDao().loadAll();
	}

	/**
	 * Verifies if there are active tasks in database
	 * 
	 * @param tasks
	 *            (list of active tasks in database)
	 * @return false if there are no active tasks; true if there are active
	 *         tasks
	 */
	public static Boolean verifyIfPreviousTasks(Context context) {
		List<Task> tasks = loadActiveTasks(context);
		if (tasks.size() == 0)
			return false;
		return true;
	}

	/**
	 * Randomly selects "count" tasks from the database to make active
	 * 
	 * @param context
	 * @param count
	 *            (the number of tasks to activate)
	 */
	public static List<Task> generateActiveTasks(Context context,
			int countDaily, int countWeekly, int countMonthly) {
		List<Task> newActiveTasks = new ArrayList<Task>();
		List<Task> newDailyTasks = generateNewTasks(context, countDaily,
				Constants.Type.DAILY);
		List<Task> newWeeklyTasks = generateNewTasks(context, countWeekly,
				Constants.Type.WEEKLY);
		List<Task> newMonthlyTasks = generateNewTasks(context, countMonthly,
				Constants.Type.MONTHLY);

		log.info("Daily tasks generated: " + newDailyTasks.size());
		log.info("Weekly tasks generated: " + newWeeklyTasks.size());
		log.info("Monthly tasks generated: " + newMonthlyTasks.size());

		newActiveTasks.addAll(newDailyTasks);
		newActiveTasks.addAll(newWeeklyTasks);
		newActiveTasks.addAll(newMonthlyTasks);

		log.info("Generated new tasks " + newActiveTasks.toString());
		return newActiveTasks;
	}

	/**
	 * @param tasksList
	 * @param context
	 * @return a task that is applicable and is not already in the tasksList
	 */
	public static Task getNewTask(Context context, Type type) {
		DaoSession daoSession = DBManager.getInstance(context).getDaoSession();
		List<Task> tasksList = daoSession.getTaskDao().loadAll();
		TaskSourceDao taskSourceDao = daoSession.getTaskSourceDao();
		Boolean inList = false;

		// using queries for sorting tasks randomly and getting all
		LazyList<TaskSource> taskSourceList = taskSourceDao
				.queryBuilder()
				.where(Properties.Applicability.eq(true),
						Properties.TypeDB.eq(type.name())).orderRaw("random()")
				.listLazy();

		Task newTask = null;
		Iterator<TaskSource> iterator = taskSourceList.iterator();

		while (iterator.hasNext()) {
			TaskSource ts = iterator.next();
			
			// making sure it is not one of the tasks which were already in the
			// list including the one we're trying to exchange
			inList = false;
			for (Task t : tasksList) {
				if (ts.getId() == t.getTaskSource().getId()) {
					inList = true;
					break;
				}
			}

			if (inList) {
				continue;
			}
			// get current date
			Date date = new Date();

			// create task based on taskSource info
			newTask = new Task(false, date, ts);
			break;
		}

		taskSourceList.close();

		if (newTask != null)
			log.info("Found new task: " + newTask.getTaskSource().getName());
		else
			log.info("Can't find new task");

		return newTask;
	}

	/**
	 * Drops a task in favor of a new one
	 * 
	 * @param oldTask
	 *            - task to be removed
	 * @param newTask
	 *            - task to be added instead
	 * @param context
	 */
	public static boolean switchTasks(Task oldTask, Task newTask,
			Context context) {
		// switch can happen only when a task was not already marked as done
		if (oldTask.getStatus() == true) {
			Toast.makeText(context, "Can't switch done tasks",
					Toast.LENGTH_LONG).show();
			return false;
		}
		DaoSession daoSession = DBManager.getInstance(context).getDaoSession();
		TaskDao taskDao = daoSession.getTaskDao();

		// we can't actually switch so just removing the old and pushing the new
		taskDao.delete(oldTask);
		taskDao.insert(newTask);

		// debugging to see which are now active
		for (Task t : taskDao.loadAll()) {
			log.info("Now in active: " + t.getTaskSource().getName());
		}

		return true;
	}

	/**
	 * Saves completed tasks in the task history. Should be called at the end of
	 * a cycle.
	 * 
	 * @param context
	 */
	public static void archiveCompletedTasks(Context context, Type type) {
		DaoSession daoSession = DBManager.getInstance(context).getDaoSession();
		TaskHistoryDao taskHistoryDao = daoSession.getTaskHistoryDao();

		// load active tasks
		List<Task> activeTasks = daoSession.getTaskDao().loadAll();

		for (Task t : activeTasks) {
			if (t.getTaskSource().getType() == type && t.getStatus()) {

				// get current date
				Date completedDate = new Date();
				log.info("Current date added to archived task: "
						+ completedDate.toString());

				taskHistoryDao.insert(new TaskHistory(completedDate, t
						.getTaskSource()));
			}
		}
	}

	public static void dropCompletedTasks(Context context, Type type) {
		DaoSession daoSession = DBManager.getInstance(context).getDaoSession();
		TaskDao taskDao = daoSession.getTaskDao();

		// load active tasks
		List<Task> activeTasks = taskDao.loadAll();

		for (Task t : activeTasks) {
			if (t.getTaskSource().getType() == type && t.getStatus()) {
				taskDao.delete(t);
			}
		}
	}

	public static void dropNotCompletedTasksFromActive(Context context,
			Type type) {
		DaoSession daoSession = DBManager.getInstance(context).getDaoSession();
		TaskDao taskDao = daoSession.getTaskDao();

		// load active tasks
		List<Task> activeTasks = taskDao.loadAll();

		for (Task t : activeTasks) {
			// targeting not completed tasks
			if (t.getTaskSource().getType() == type && t.getStatus() == false) {
				log.info("Drop not completed; Type = " + type + "; Task: "
						+ t.getTaskSource().getName());
				taskDao.delete(t);
			}
		}
	}

//	public static List<Task> getNotDoneTypeTasks(Context context, Type type) {
//		DaoSession daoSession = DBManager.getInstance(context).getDaoSession();
//		// load active tasks
//		List<Task> activeTasks = daoSession.getTaskDao().loadAll();
//
//		List<Task> notDoneTasks = new ArrayList<Task>();
//
//		for (Task t : activeTasks) {
//			// targeting not completed tasks
//			if (t.getTaskSource().getType() == type && t.getStatus() == false) {
//				notDoneTasks.add(t);
//			}
//		}
//
//		log.info("Get list not completed; Type = " + type + "; Tasks: "
//				+ notDoneTasks.toString());
//
//		return notDoneTasks;
//	}

	public static List<Task> generateNewTasks(Context context, int noOfTasks,
			Type type) {
		DaoSession daoSession = DBManager.getInstance(context).getDaoSession();
		TaskSourceDao taskSourceDao = daoSession.getTaskSourceDao();

		// using queries for sorting tasks randomly and picking the first
		List<TaskSource> taskSourceList = taskSourceDao
				.queryBuilder()
				.where(Properties.Applicability.eq(true),
						Properties.TypeDB.eq(type.name())).orderRaw("random()")
				.limit(noOfTasks).list();
		log.info("List of random tasks: " + taskSourceList);

		Iterator<TaskSource> iterator = taskSourceList.iterator();

		List<Task> newTasks = new ArrayList<Task>();
		while (iterator.hasNext()) {
			TaskSource ts = iterator.next();

			// get current date
			Date date = new Date();

			// create task based on taskSource info
			Task t = new Task(false, date, ts);

			// insert task in active table in db
			daoSession.getTaskDao().insert(t);
			newTasks.add(t);

			log.info("Added task: " + t.getTaskSource().getName());
		}

		// update time stamp for last daily update
		// get last updates of monthly/weekly tasks
		Date now = new Date();
		SharedPreferences prefs = context.getSharedPreferences(Constants.APP,
				Context.MODE_PRIVATE);
		switch (type) {
		case DAILY:
			prefs.edit()
					.putLong(Constants.LAST_DAILY_UPDATE, now.getTime() / 1000)
					.commit();
			break;
		case WEEKLY:
			prefs.edit()
					.putLong(Constants.LAST_WEEKLY_UPDATE, now.getTime() / 1000)
					.commit();
			break;
		case MONTHLY:
			prefs.edit()
					.putLong(Constants.LAST_MONTHLY_UPDATE,
							now.getTime() / 1000).commit();
			break;
		}

		return newTasks;
	}
}
