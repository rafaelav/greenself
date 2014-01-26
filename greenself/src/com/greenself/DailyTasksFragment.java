package com.greenself;

import java.util.ArrayList;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.greenself.adapters.DailyTaskItemAdapter;
import com.greenself.daogen.DaoMaster;
import com.greenself.daogen.DaoMaster.DevOpenHelper;
import com.greenself.daogen.DaoSession;
import com.greenself.daogen.Task;

public class DailyTasksFragment extends Fragment {

	private ListView taskListView;
	private ArrayList<Task> tasks;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.daily_task_challenge, null);

		taskListView = (ListView) view.findViewById(R.id.TasksListView);

		tasks = getTasksFromDB();
		
		Log.d("DailyTasks - task list: ", tasks.toString());

		DailyTaskItemAdapter taskAdapter = new DailyTaskItemAdapter(tasks,
				getActivity());

		taskListView.setAdapter(taskAdapter);

		return view;
	}

	private ArrayList<Task> getTasksFromDB() {
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(),
				"notes-db", null);
		SQLiteDatabase db = helper.getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		DaoSession daoSession = daoMaster.newSession();
		return (ArrayList<Task>) daoSession.getTaskDao().loadAll();
	}
}
