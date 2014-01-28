package extraviews;

import java.util.logging.Logger;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greenself.R;
import com.greenself.daogen.Task;

public class DailyTaskItemView extends RelativeLayout {

	private static final Logger log = Logger.getLogger(DailyTaskItemView.class
			.getName());

	private CheckBox checkBox;
	private TextView text;
	private Task task;

	public DailyTaskItemView(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_daily_task_challenge, this, true);

		checkBox = (CheckBox) this.findViewById(R.id.TaskStatusCheckBox);
		text = (TextView) this.findViewById(R.id.TaskTextView);
	}

	public void bind(Task t) {
		this.task = t;
		this.checkBox.setChecked(this.task.getStatus());
		this.text.setText(this.task.getTaskSource().getName());

		this.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				log.info("Before click: " + task.getStatus());
				task.setStatus(isChecked);
				log.info("After click: " + task.getStatus());
			}
		});
	}

}
