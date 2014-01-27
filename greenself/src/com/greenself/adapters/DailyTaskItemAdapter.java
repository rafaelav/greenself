package com.greenself.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.greenself.R;
import com.greenself.daogen.Task;

public class DailyTaskItemAdapter extends BaseAdapter {

	private ArrayList<Task> tasks;
	private Context context;

	public DailyTaskItemAdapter(List<Task> tasks, Context context) {
		ArrayList<Task> newTasks = new ArrayList<Task>(tasks);
		this.tasks = newTasks;
		this.context = context;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_daily_task_challenge,
					null);

			ViewHolder viewHolder = new ViewHolder();
			viewHolder.checkbox = (CheckBox) convertView
					.findViewById(R.id.TaskStatusCheckBox);
			viewHolder.text = (TextView) convertView
					.findViewById(R.id.TaskTextView);

			convertView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.text.setText(tasks.get(position).getTaskSource().getName());
		holder.checkbox.setChecked(tasks.get(position).getStatus());

		return convertView;
	}

	static class ViewHolder {
		public CheckBox checkbox;
		public TextView text;
	}

}
