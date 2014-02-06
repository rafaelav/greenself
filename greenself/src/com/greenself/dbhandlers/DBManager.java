package com.greenself.dbhandlers;

import java.util.Arrays;
import java.util.logging.Logger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.greenself.daogen.DaoMaster;
import com.greenself.daogen.DaoMaster.DevOpenHelper;
import com.greenself.daogen.DaoSession;
import com.greenself.daogen.Task;
import com.greenself.daogen.TaskSource;
import com.greenself.objects.Constants;

public class DBManager {
	private static final Logger log = Logger.getLogger(DBManager.class
			.getName());

	private static DBManager instance = null;
	private DaoSession daoSession = null;

	private DBManager() {
		// Exists only to defeat instantiation.
	}

	public static DBManager getInstance(Context context) {
		if (instance == null) {
			instance = new DBManager();

			DevOpenHelper helper = new DaoMaster.DevOpenHelper(context,
					Constants.DB_NAME, null);
			SQLiteDatabase db = helper.getWritableDatabase();
			DaoMaster daoMaster = new DaoMaster(db);
			instance.daoSession = daoMaster.newSession();
		}
		return instance;
	}

	public void resetDB() {
		if (this.daoSession == null) {
			log.info("Trying to create the standard database before opening database session");
			return;
		}
		// making sure it is free
		instance.daoSession.getTaskSourceDao().deleteAll();
		instance.daoSession.getTaskDao().deleteAll();
		instance.daoSession.getTaskHistoryDao().deleteAll();

		// adding list of tasks to DB
		TaskSource[] dbTasks = Constants.TASKS_IN_DB;
		this.daoSession.getTaskSourceDao().insertInTx(Arrays.asList(dbTasks));
	}

	public void resetActiveTasks() {
		if (this.daoSession == null) {
			log.info("Trying to create the standard database before opening database session");
			return;
		}
		// making sure it is free
		instance.daoSession.getTaskDao().deleteAll();
	}

	public void resetHistory() {
		if (this.daoSession == null) {
			log.info("Trying to create the standard database before opening database session");
			return;
		}
		// making sure it is free
		instance.daoSession.getTaskHistoryDao().deleteAll();
	}

	public DaoSession getDaoSession() {
		return this.daoSession;
	}
}
