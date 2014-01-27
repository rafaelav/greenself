package com.greenself;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import android.content.Context;

import com.greenself.daogen.DaoSession;
import com.greenself.daogen.Task;
import com.greenself.daogen.TaskSource;
import com.greenself.daogen.TaskSourceDao;
import com.greenself.dbhandlers.DBManager;

public class TaskHandler {
	private static final Logger log=Logger.getLogger(TaskHandler.class.getName());

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
	 * @param context
	 * @param count (the number of tasks to activate)
	 */
	public static void generateActiveTasks(Context context, int count) {
		DaoSession daoSession = DBManager.getInstance(context).getDaoSession();
		TaskSourceDao taskSourceDao = daoSession.getTaskSourceDao();

		// using queries for sorting tasks randomly and picking the first
		List<TaskSource> taskSourceList = taskSourceDao.queryBuilder()
				.orderRaw("random()").limit(count).list();
		log.info("List of random tasks: "+taskSourceList);

		Iterator<TaskSource> iterator = taskSourceList.iterator();

		while (iterator.hasNext()) {
			TaskSource ts = iterator.next();

			// get current date
			Date date = new Date();

			// create task based on taskSource info
			Task t = new Task(false, date, ts);

			// insert task in active table in db
			daoSession.getTaskDao().insert(t);
			
			log.info("Added task: "+t.getTaskSource().getName());
		}

	}
}
