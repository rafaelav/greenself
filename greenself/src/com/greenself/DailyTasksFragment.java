package com.greenself;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.greenself.constants.Constants;
import com.greenself.daogen.Task;
import com.greenself.daogen.TaskDao;
import com.greenself.dbhandlers.DBManager;
import com.greenself.observers.TasksChangeListener;

public class DailyTasksFragment extends Fragment implements TasksChangeListener {

	private static final Logger log = Logger.getLogger(DailyTasksFragment.class
			.getName());

	private ListView taskListView;
	private DailyTaskItemAdapter taskAdapter;
	private SharedPreferences prefs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.daily_task_challenge, null);

		taskListView = (ListView) view.findViewById(R.id.TasksListView);
		registerForContextMenu(taskListView);

		List<Task> tasks;

		tasks = TaskHandler.loadActiveTasks(getActivity());

		log.info("Active tasks: " + tasks);

		prefs = getActivity().getSharedPreferences(Constants.APP,
				Context.MODE_PRIVATE);

		// determines which tasks to show (if true - show all; else show only
		// the uncompleted
		boolean shownCompleted = prefs.getBoolean(
				Constants.SETTINGS_DONE_TASKS_VISIBILE, true);
		log.info("ShownCompleted option is: " + shownCompleted);

		this.taskAdapter = new DailyTaskItemAdapter(tasks, getActivity(),
				shownCompleted);

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
		
		setHasOptionsMenu(true);

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

		Task newTask = TaskHandler.getNewTask(getActivity(), oldTask
				.getTaskSource().getType());
		boolean switched = TaskHandler.switchTasks(oldTask, newTask,
				getActivity());

		// only if the switch is done should UI be updated
		if (switched & newTask != null) {
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
		Task newTask = TaskHandler.getNewTask(getActivity(), unapplicableTask
				.getTaskSource().getType());

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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.daily_challenge_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_task:
			addNewTask();
			return true;
		case R.id.generate_tasks:
			generateNewTasks();
			return true;
		case R.id.completed_visibility:
			// changeDoneTasksVisibility();
			log.info("ShownCompleted before change: "
					+ taskAdapter.isShowCompleted());
			changeShowCompleted();
			log.info("ShownCompleted after change: "
					+ taskAdapter.isShowCompleted());
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void addNewTask() {
		// only adding tasks that can be done during that day
		Task newTask = TaskHandler.getNewTask(getActivity(),
				Constants.Type.DAILY);

		if (newTask != null) {
			// update active db
			DBManager.getInstance(getActivity()).getDaoSession().getTaskDao()
					.insert(newTask);
			// update ui
			taskAdapter.addTask(newTask);
			taskAdapter.notifyDataSetChanged();

			Toast.makeText(getActivity(), "New task added!", Toast.LENGTH_LONG)
					.show();
		}
	}

	/**
	 * Generates randomly n tasks (n set in settings). (The tasks marked as done
	 * will not be removed from the screen) - All task are removed for now. The
	 * other tasks are and they are also removed from active before regeneration
	 * so that at regeneration some of them can reaper.
	 */
	private void generateNewTasks() {

		TaskDao currentTaskDao = DBManager.getInstance(getActivity())
				.getDaoSession().getTaskDao();
		List<Task> toRemove = new ArrayList<Task>();
		for (Task t : taskAdapter.getTasks()) {
			toRemove.add(t);
		}
		log.info("Removing all tasks to generate new: " + toRemove.toString());

		// remove from active and from ui now
		for (Task t : toRemove) {
			taskAdapter.removeTask(t);
			currentTaskDao.delete(t);
		}

		List<Task> newTasks = TaskHandler.generateActiveTasks(getActivity(),
				Constants.NO_OF_DAILY_TASKS, Constants.NO_OF_WEEKLY_TASKS,
				Constants.NO_OF_MONTHLY_TASKS);

		// add in ui (already added in active db when they have been returned
		// from db)
		for (Task t : newTasks) {
			taskAdapter.addTask(t);
		}

		taskAdapter.notifyDataSetChanged();
		Toast.makeText(getActivity(), "Generated new tasks!", Toast.LENGTH_LONG)
				.show();
	}

	private void changeShowCompleted() {
		if (taskAdapter.isShowCompleted()) {
			taskAdapter.setShowCompleted(false);
			prefs.edit()
					.putBoolean(Constants.SETTINGS_DONE_TASKS_VISIBILE, false)
					.commit();
		} else {
			taskAdapter.setShowCompleted(true);
			prefs.edit()
					.putBoolean(Constants.SETTINGS_DONE_TASKS_VISIBILE, true)
					.commit();
		}
	}

	@Override
	public void onTasksChanged() {
		log.info("Triggered task updates");
		this.taskAdapter.replaceTasks(TaskHandler
				.loadActiveTasks(getActivity()));
	}
}
