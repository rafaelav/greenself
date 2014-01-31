package com.greenself;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import android.content.Context;

import com.greenself.daogen.DaoSession;
import com.greenself.daogen.Task;
import com.greenself.daogen.TaskDao;
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
	public static Boolean verifyIfPreviousTasks(List<Task> tasks) {
		if (tasks.size() == 0)
			return false;
		return true;
	}

	/**
	 * Randomly selectes "count" tasks from the database to make active
	 * 
	 * @param context
	 * @param count
	 *            (the number of tasks to activate)
	 */
	public static void generateActiveTasks(Context context, int count) {
		DaoSession daoSession = DBManager.getInstance(context).getDaoSession();
		TaskSourceDao taskSourceDao = daoSession.getTaskSourceDao();

		// using queries for sorting tasks randomly and picking the first
		List<TaskSource> taskSourceList = taskSourceDao.queryBuilder()
				.where(Properties.Applicability.eq(true)).orderRaw("random()")
				.limit(count).list();
		log.info("List of random tasks: " + taskSourceList);

		Iterator<TaskSource> iterator = taskSourceList.iterator();

		while (iterator.hasNext()) {
			TaskSource ts = iterator.next();

			// get current date
			Date date = new Date();

			// create task based on taskSource info
			Task t = new Task(false, date, ts);

			// insert task in active table in db
			daoSession.getTaskDao().insert(t);

			log.info("Added task: " + t.getTaskSource().getName());
		}
	}

	/**
	 * @param tasksList
	 * @param context
	 * @return a task that is applicable and is not already in the tasksList
	 */
	public static Task getNewTask(List<Task> tasksList, Context context) {
		DaoSession daoSession = DBManager.getInstance(context).getDaoSession();
		TaskSourceDao taskSourceDao = daoSession.getTaskSourceDao();
		Boolean inList = false;

		// using queries for sorting tasks randomly and getting all
		LazyList<TaskSource> taskSourceList = taskSourceDao.queryBuilder()
				.where(Properties.Applicability.eq(true)).orderRaw("random()")
				.listLazy();

		Task newTask = null;
		Iterator<TaskSource> iterator = taskSourceList.iterator();

		while (iterator.hasNext()) {
			TaskSource ts = iterator.next();

			// skip if not applicable
			// if (ts.getApplicability() == false)
			// continue;

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
	public static void switchTasks(Task oldTask, Task newTask, Context context) {
		DaoSession daoSession = DBManager.getInstance(context).getDaoSession();
		TaskDao taskDao = daoSession.getTaskDao();

		// we can't actually switch so just removing the old and pushing the new
		taskDao.delete(oldTask);
		taskDao.insert(newTask);

		// debuging to see which are now active
		for (Task t : taskDao.loadAll()) {
			log.info("Now in active: " + t.getTaskSource().getName());
		}
	}
}
