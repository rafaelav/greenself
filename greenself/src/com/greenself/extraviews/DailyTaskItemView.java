package com.greenself.extraviews;

import java.util.logging.Logger;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greenself.R;
import com.greenself.adapters.DailyTaskItemAdapter;
import com.greenself.daogen.Task;
import com.greenself.events.EventsBusFactory;
import com.greenself.events.SingleTaskChangeEvent;
import com.squareup.otto.Bus;

public class DailyTaskItemView extends RelativeLayout {

	private static final Logger log = Logger.getLogger(DailyTaskItemView.class
			.getName());

	private BeautifulCheckbox checkBox;
	private TextView text;
	private TextView points;
	private Task task;
	private DailyTaskItemAdapter adapter;

	public DailyTaskItemView(Context context, DailyTaskItemAdapter adapter) {
		super(context);
		this.adapter = adapter;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_daily_task_challenge, this, true);

		checkBox = (BeautifulCheckbox) this.findViewById(R.id.TaskStatusCheckBox);
		text = (TextView) this.findViewById(R.id.TaskTextView);
		points = (TextView) this.findViewById(R.id.TaskTextPointsView);
	}

	public void bind(Task t) {
		this.task = t;
		this.checkBox.setOnClickListener(null);
		this.checkBox.setChecked(this.task.getStatus());
		this.text.setText(this.task.getTaskSource().getName());
		this.points.setText(this.task.getTaskSource().getXpPoints().toString());

		// making sure that tasks who are done have strike and the others don't
		setTaskAppearence();

		this.checkBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (checkBox.isChecked())
					checkBox.setChecked(false);
				else
					checkBox.setChecked(true);
				task.setStatus(checkBox.isChecked());
				// send update to interested entities about change for a single task
				EventsBusFactory.getInstance().post(new SingleTaskChangeEvent(task));
				
				setTaskAppearence();
				adapter.onTaskStatusChanged(task);				
			}
		});
	}

	/**
	 * If a task has the status Done then its text has a strike line If not - it
	 * doesn't
	 */
	public void setTaskAppearence() {
		// strike text or not
		if (task.getStatus()) {
			text.setPaintFlags(text.getPaintFlags()
					| Paint.STRIKE_THRU_TEXT_FLAG);
			text.setTextColor(Color.GRAY);
		} else {
			text.setPaintFlags(text.getPaintFlags()
					& (~Paint.STRIKE_THRU_TEXT_FLAG));
			text.setTextColor(Color.BLACK);
		}
	}

}
