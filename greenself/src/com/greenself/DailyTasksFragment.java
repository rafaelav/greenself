package com.greenself;

import java.util.List;
import java.util.logging.Logger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.greenself.adapters.DailyTaskItemAdapter;
import com.greenself.daogen.Task;
import com.greenself.dbhandlers.DBManager;

public class DailyTasksFragment extends Fragment {

	private static final Logger log = Logger.getLogger(DailyTasksFragment.class
			.getName());

	private ListView taskListView;
	DailyTaskItemAdapter taskAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.daily_task_challenge, null);

		taskListView = (ListView) view.findViewById(R.id.TasksListView);
		registerForContextMenu(taskListView);

		List<Task> tasks;

		tasks = TaskHandler.loadActiveTasks(getActivity());

		log.info("Active tasks: " + tasks);

		this.taskAdapter = new DailyTaskItemAdapter(tasks, getActivity());

		taskListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Task task = taskAdapter.getItem(position);

				log.info("Cliked element " + position + ": "
						+ task.getTaskSource().getName());
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setMessage(task.getTaskSource().getInfo()).setTitle(
						task.getTaskSource().getName());
				builder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User clicked OK button
							}
						});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});

		taskListView.setAdapter(this.taskAdapter);

		return view;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.daily_task_long_click_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// AdapterContextMenuInfo info = (AdapterContextMenuInfo)
		// item.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.MenuChangeTask:
			replaceTask(item);
			return true;
		case R.id.MenuApplicability:
			changeApplicabilityToFalse(item);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void replaceTask(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Task oldTask = taskAdapter.getItem(info.position);
		log.info("Item to change at position " + info.position + ": "
				+ oldTask.getTaskSource().getName());

		Task newTask = TaskHandler.getNewTask(taskAdapter.getTasks(),
				getActivity());
		boolean switched = TaskHandler.switchTasks(oldTask, newTask,
				getActivity());

		// only if the switch is done should UI be updated
		if (switched) {
			// update ui
			taskAdapter.addTask(newTask);
			taskAdapter.removeTask(oldTask);
			taskAdapter.notifyDataSetChanged();

			Toast.makeText(getActivity(), "Change is done!", Toast.LENGTH_LONG)
					.show();
		}
	}

	private void changeApplicabilityToFalse(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Task unapplicableTask = taskAdapter.getItem(info.position);

		// change applicability & update task source db
		unapplicableTask.getTaskSource().setApplicability(false);
		DBManager.getInstance(getActivity()).getDaoSession().getTaskSourceDao()
				.update(unapplicableTask.getTaskSource());
		// because this will not appear anymore something else must take its
		// place
		Task newTask = TaskHandler.getNewTask(taskAdapter.getTasks(),
				getActivity());

		// update ui
		taskAdapter.addTask(newTask);
		taskAdapter.removeTask(unapplicableTask);
		taskAdapter.notifyDataSetChanged();

		// remove old task from active tasks and add new one
		DBManager.getInstance(getActivity()).getDaoSession().getTaskDao()
				.delete(unapplicableTask);
		DBManager.getInstance(getActivity()).getDaoSession().getTaskDao()
				.insert(newTask);
		Toast.makeText(getActivity(), "Applicability changed!",
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onPause() {
		super.onPause();
		DBManager.getInstance(getActivity()).getDaoSession().getTaskDao()
				.updateInTx(taskAdapter.getTasks());
	}

	// private void replaceTask(Task oldTask) {
	// Task newTask = TaskHandler.getNewTask(taskAdapter.getTasks(),
	// getActivity());
	// TaskHandler.switchTasks(oldTask, newTask, getActivity());
	//
	// // update ui
	// taskAdapter.addTask(newTask);
	// taskAdapter.removeTask(oldTask);
	// taskAdapter.notifyDataSetChanged();
	// }
}
