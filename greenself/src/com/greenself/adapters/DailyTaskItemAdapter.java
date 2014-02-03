package com.greenself.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.greenself.DailyTasksFragment;
import com.greenself.R;
import com.greenself.daogen.Task;
import com.greenself.dbhandlers.DBManager;
import com.greenself.objects.Constants;

import extraviews.DailyTaskItemView;

public class DailyTaskItemAdapter extends BaseAdapter {

	private List<Task> tasks;
	private Context context;

	private static final Logger log = Logger
			.getLogger(DailyTaskItemAdapter.class.getName());

	public DailyTaskItemAdapter(List<Task> tasks, Context context) {
		ArrayList<Task> newTasks = new ArrayList<Task>(tasks);
		this.tasks = newTasks;
		this.context = context;
	}

	/**
	 * @return an unmodifiable view of the internal task list
	 */
	public List<Task> getTasks() {
		return Collections.unmodifiableList(this.tasks);
	}

	@Override
	public int getCount() {
		return tasks.size();
	}

	@Override
	public Task getItem(int position) {
		return tasks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return tasks.get(position).getId();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new DailyTaskItemView(context);
		}

		// tie the convert view to the task at hand (either new view or recycled
		// view)
		((DailyTaskItemView) convertView).bind(tasks.get(position));

		return convertView;
	}

	public boolean addTask(Task task) {
		return tasks.add(task);
	}

	public boolean removeTask(Task task) {
		return tasks.remove(task);
	}
}
