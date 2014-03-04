package com.greenself.handlers;

import java.util.ArrayList;
import java.util.logging.Logger;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.greenself.DailyTasksFragment;
import com.greenself.constants.Constants;
import com.greenself.daogen.Task;
import com.greenself.daogen.TaskDao;
import com.greenself.daogen.TaskDao.Properties;
import com.greenself.daogen.TaskHistoryDao;
import com.greenself.daogen.TaskSourceDao;

public class ScoreHandler {
	private static final String QUERY_COMPUTE_SCORE = "SELECT sum("
			+ TaskSourceDao.TABLENAME + "."
			+ TaskSourceDao.Properties.XpPoints.columnName + ") FROM "
			+ TaskHistoryDao.TABLENAME + " JOIN " + TaskSourceDao.TABLENAME
			+ " ON " + TaskHistoryDao.TABLENAME + "."
			+ TaskHistoryDao.Properties.TaskSourceId.columnName + "="
			+ TaskSourceDao.TABLENAME + "."
			+ TaskSourceDao.Properties.Id.columnName;
	private static final Logger log = Logger.getLogger(ScoreHandler.class
			.getName());
	private static ScoreHandler instance = null;
	private static final String QUERY_COUNT_TASKS = "SELECT count("
			+ TaskHistoryDao.TABLENAME + "."
			+ TaskHistoryDao.Properties.Id.columnName + ") FROM "
			+ TaskSourceDao.TABLENAME + " JOIN " + TaskHistoryDao.TABLENAME
			+ " ON " + TaskHistoryDao.TABLENAME + "."
			+ TaskHistoryDao.Properties.TaskSourceId.columnName + "="
			+ TaskSourceDao.TABLENAME + "."
			+ TaskSourceDao.Properties.Id.columnName + " WHERE "
			+ TaskSourceDao.Properties.TypeDB.columnName + " = ?";
	private Context context;
	private long overallScore = 0;
	private int numberOfDailyTasks = 0;
	private int numberOfWeeklyTasks = 0;
	private int numberOfMonthlyTasks = 0;

	protected ScoreHandler() {
		// Exists only to defeat instantiation
	}

	public static ScoreHandler getInstance(Context context) {
		if (instance == null)
			instance = new ScoreHandler();

		instance.context = context.getApplicationContext();

		return instance;
	}

	/*
	 * Goes through the history of tasks and calculates the score, the number of
	 * daily, weekly and monthly tasks.
	 */
	public void calculateAllStatistics() {
		SQLiteDatabase database = DBManager.getInstance(context)
				.getDaoSession().getDatabase();
		TaskDao taskDao = DBManager.getInstance(context).getDaoSession()
				.getTaskDao();

		// total score of complted tasks (from active and from history)
		long score = 0;
		// number for completed tasks for each type of tasks
		int dailyTasks = 0;
		int weeklyTasks = 0;
		int monthlyTasks = 0;

		// get done tasks from active
		ArrayList<Task> completedTasksInActive = (ArrayList<Task>) taskDao
				.queryBuilder().where(Properties.Status.eq(true)).list();

		for (Task t : completedTasksInActive) {
			score = score + (long) t.getTaskSource().getXpPoints();

			switch (t.getTaskSource().getType()) {
			case DAILY:
				dailyTasks = dailyTasks + 1;
			case WEEKLY:
				weeklyTasks = weeklyTasks + 1;
			case MONTHLY:
				monthlyTasks = monthlyTasks + 1;
			}
		}

		// get sum for all previous done tasks
		log.info("COMPUTE_SCORE_QUERY:" + QUERY_COMPUTE_SCORE);
		Cursor cursor = database.rawQuery(QUERY_COMPUTE_SCORE, null);
		cursor.moveToFirst();
		score = score + cursor.getLong(0);
		log.info("Computed score:" + cursor.getLong(0));
		cursor.close();

		// get count for all previous daily tasks
		cursor = database.rawQuery(QUERY_COUNT_TASKS,
				new String[] { Constants.Type.DAILY.name() });
		cursor.moveToFirst();
		dailyTasks = dailyTasks + cursor.getInt(0);
		log.info("[History Info]"+QUERY_COUNT_TASKS);
		log.info("[History Info] Count of daily tasks:" + cursor.getInt(0));
		cursor.close();

		// get count for all previous weekly tasks
		cursor = database.rawQuery(QUERY_COUNT_TASKS,
				new String[] { Constants.Type.WEEKLY.name() });
		cursor.moveToFirst();
		weeklyTasks = weeklyTasks + cursor.getInt(0);
		log.info("[History Info] Count of weekly tasks:" + cursor.getInt(0));
		cursor.close();
		
		// get count for all previous monthly tasks
		cursor = database.rawQuery(QUERY_COUNT_TASKS,
				new String[] { Constants.Type.MONTHLY.name() });
		cursor.moveToFirst();
		monthlyTasks = monthlyTasks + cursor.getInt(0);
		log.info("[History Info] Count of monthly tasks:" + cursor.getInt(0));
		cursor.close();
		
		log.info("[History Info] Task history:"+DBManager.getInstance(context).getDaoSession().getTaskHistoryDao().loadAll());
		// update instance scores
		instance.overallScore = score;
		instance.numberOfDailyTasks = dailyTasks;
		instance.numberOfMonthlyTasks = monthlyTasks;
		instance.numberOfWeeklyTasks = weeklyTasks;
	}
}
