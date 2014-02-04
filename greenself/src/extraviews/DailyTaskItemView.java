package extraviews;

import java.util.logging.Logger;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greenself.R;
import com.greenself.adapters.DailyTaskItemAdapter;
import com.greenself.daogen.Task;

public class DailyTaskItemView extends RelativeLayout {

	private static final Logger log = Logger.getLogger(DailyTaskItemView.class
			.getName());

	private CheckBox checkBox;
	private TextView text;
	private Task task;
	private DailyTaskItemAdapter adapter;

	public DailyTaskItemView(Context context, DailyTaskItemAdapter adapter) {
		super(context);
		this.adapter = adapter;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_daily_task_challenge, this, true);

		checkBox = (CheckBox) this.findViewById(R.id.TaskStatusCheckBox);
		text = (TextView) this.findViewById(R.id.TaskTextView);
	}

	public void bind(Task t) {
		this.task = t;
		//reset listener
		this.checkBox.setOnCheckedChangeListener(null);
		this.checkBox.setChecked(this.task.getStatus());
		this.text.setText(this.task.getTaskSource().getName());

		// making sure that tasks who are done have strike and the others don't
		setTaskAppearence();

		this.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				//if (isChecked != task.getStatus()) {
					log.warning("Checked!");
					log.info("Task status Before click: " + task.getStatus()
							+ " task: " + task.getTaskSource().getName());
					
					task.setStatus(isChecked);
					
					log.info("isChecked: " + isChecked);
					log.info("Task status After click: " + task.getStatus()
							+ " task: " + task.getTaskSource().getName());
					
					setTaskAppearence();
					adapter.onTaskStatusChanged(task);
				//}
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
