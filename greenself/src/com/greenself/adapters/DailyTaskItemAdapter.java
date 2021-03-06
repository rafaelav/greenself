package com.greenself.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.greenself.daogen.Task;
import com.greenself.extraviews.DailyTaskItemView;

public class DailyTaskItemAdapter extends BaseAdapter {

	private boolean showCompleted;
	private List<Task> tasks;
	private List<Task> shownTasks;
	private Context context;

	private static final Logger log = Logger
			.getLogger(DailyTaskItemAdapter.class.getName());

	public DailyTaskItemAdapter(List<Task> tasks, Context context,
			boolean showCompleted) {
		ArrayList<Task> newTasks = new ArrayList<Task>(tasks);
		this.tasks = newTasks;
		this.context = context;
		this.setShowCompleted(showCompleted);

		// establish which task list to use
		updateShownTasks();
	}

	/**
	 * @return an unmodifiable view of the internal task list
	 */
	public List<Task> getTasks() {
		return Collections.unmodifiableList(this.tasks);
	}

	@Override
	public int getCount() {
		return shownTasks.size();
	}

	@Override
	public Task getItem(int position) {
		return shownTasks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return shownTasks.get(position).getId();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new DailyTaskItemView(context, this);
		}

		// tie the convert view to the task at hand (either new view or recycled
		// view)
		((DailyTaskItemView) convertView).bind(getItem(position));

		return convertView;
	}

	public boolean addTask(Task task) {
		boolean all = tasks.add(task);
		boolean notDone = false;
		if (!showCompleted && task.getStatus() == false) {
			notDone = shownTasks.add(task);
			if (notDone == false) {// if it failed put it back
				log.warning("Failed to add task to shown");
				tasks.remove(task);
			}
		}

		if (all && notDone) {
			notifyDataSetChanged();
			return true;
		} else
			return false;
	}

	public boolean removeTask(Task task) {
		boolean all = tasks.remove(task);
		boolean notDone = false;
		if (showCompleted == false) {
			// can only remove it if it is the the shown list otherwise it will
			// end up not being removed at all
			if (task.getStatus() == false) {
				notDone = shownTasks.remove(task);
				if (notDone == false) { // if it failed put it back
					log.warning("Failed to remove task from shown");
					tasks.add(task);
				}
			}
		}

		if (all && notDone) {
			notifyDataSetChanged();
			return true;
		} else
			return false;
	}

	public boolean isShowCompleted() {
		return showCompleted;
	}

	public void setShowCompleted(boolean showCompleted) {
		this.showCompleted = showCompleted;
		updateShownTasks();
	}

	public void onTaskStatusChanged(Task task) {
		// showing only not completed tasks
		if (!showCompleted) {
			// the task is now done so shouldn't be seen
			if (task.getStatus()) {
				this.shownTasks.remove(task);
				notifyDataSetChanged();
			} else { // should not reach this point
				this.shownTasks.add(task);
				notifyDataSetChanged();
				log.warning("Should not reach this point");
			}
		}
	}

	private void updateShownTasks() {
		if (showCompleted) {
			this.shownTasks = this.tasks;
		} else {
			this.shownTasks = new ArrayList<Task>();
			for (Task t : this.tasks) {
				if (t.getStatus() == false) {
					this.shownTasks.add(t);
				}
			}
		}

		notifyDataSetChanged();
	}

	public void replaceTasks(List<Task> activeTasks) {
		ArrayList<Task> newTasks = new ArrayList<Task>(activeTasks);
		this.tasks = newTasks;

		// establish which task list to use
		updateShownTasks();
	}
}
