package com.greenself;

import java.util.List;
import java.util.logging.Logger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.greenself.adapters.DailyTaskItemAdapter;
import com.greenself.daogen.Task;

public class DailyTasksFragment extends Fragment {

	private static final Logger log = Logger.getLogger(DailyTasksFragment.class
			.getName());

	private ListView taskListView;
	private List<Task> tasks;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.daily_task_challenge, null);

		taskListView = (ListView) view.findViewById(R.id.TasksListView);
		tasks = TaskHandler.loadActiveTasks(getActivity());

		log.info("Active tasks: " + tasks);

		DailyTaskItemAdapter taskAdapter = new DailyTaskItemAdapter(tasks,
				getActivity());

		taskListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Task task = tasks.get(position);

				log.info("Cliked element "+position+": "+task.getTaskSource().getName());
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
		
		taskListView.setAdapter(taskAdapter);

		return view;
	}
}
