package com.greenself;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greenself.events.EventsBusFactory;
import com.greenself.events.MultipleTasksChangeEvent;
import com.greenself.events.SingleTaskChangeEvent;
import com.greenself.handlers.ScoreHandler;
import com.squareup.otto.Subscribe;

public class StatisticsFragment extends Fragment {

	private TextView statsDailyTasksScore;
	private TextView statsWeeklyTasksScore;
	private TextView statsMonthlyTasksScore;
	private TextView statsOverallScore;
	private RelativeLayout fullScoreLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.statistics, null);

		// calculate statistics
		ScoreHandler.getInstance(getActivity()).updateAllStatistics();

		// update scores published on fragment
		statsDailyTasksScore = (TextView) view
				.findViewById(R.id.statsDailyTasksScore);
		statsWeeklyTasksScore = (TextView) view
				.findViewById(R.id.statsWeeklyTasksScore);
		statsMonthlyTasksScore = (TextView) view
				.findViewById(R.id.statsMonthlyTasksScore);
		statsOverallScore = (TextView) view
				.findViewById(R.id.statsOverallScore);
		fullScoreLayout = (RelativeLayout) view
				.findViewById(R.id.fullScoreLayout);
		fullScoreLayout.setClickable(true);

		fullScoreLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setMessage("Clicked!");
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

		// register for updates from bus
		EventsBusFactory.getInstance().register(this);

		return view;
	}

	// @Override
	// public void onResume() {
	// // TODO Auto-generated method stub
	// super.onResume();
	// PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.SCALE_X,
	// 1.2f);
	// PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.SCALE_Y,
	// 1.2f);
	// ObjectAnimator myObj =
	// ObjectAnimator.ofPropertyValuesHolder(fullScoreLayout, pvhX, pvhY);
	// myObj.setRepeatCount(8);
	// myObj.setRepeatMode(ValueAnimator.REVERSE);
	// myObj.setDuration(700);
	// myObj.start();
	// }
	void startFullScoreAnimation() {
		PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.SCALE_X,
				1.2f);
		PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.SCALE_Y,
				1.2f);
		ObjectAnimator myObj = ObjectAnimator.ofPropertyValuesHolder(
				fullScoreLayout, pvhX, pvhY);
		myObj.setRepeatCount(7);
		myObj.setRepeatMode(ValueAnimator.REVERSE);
		myObj.setDuration(700);
		myObj.start();
		myObj.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				fullScoreLayout.clearAnimation();
			}
		});
	}

	@Subscribe
	public void updateStatisticsForMultipleTasksChange(
			MultipleTasksChangeEvent event) {
		ScoreHandler.getInstance(getActivity()).updateAllStatistics();

		statsDailyTasksScore.setText(""
				+ ScoreHandler.getInstance(getActivity())
						.getNumberOfCompletedDailyTasks());
		statsWeeklyTasksScore.setText(""
				+ ScoreHandler.getInstance(getActivity())
						.getNumberOfCompletedWeeklyTasks());
		statsMonthlyTasksScore.setText(""
				+ ScoreHandler.getInstance(getActivity())
						.getNumberOfCompletedMonthlyTasks());
		statsOverallScore.setText(ScoreHandler.getInstance(getActivity())
				.getOverallScore() + "");
	}

	@Subscribe
	public void updateStatisticsForSingleTaskChange(SingleTaskChangeEvent event) {
		ScoreHandler.getInstance(getActivity())
				.updateStatisticsForSingleTaskChange(event.getChangedTask());

		statsDailyTasksScore.setText(""
				+ ScoreHandler.getInstance(getActivity())
						.getNumberOfCompletedDailyTasks());
		statsWeeklyTasksScore.setText(""
				+ ScoreHandler.getInstance(getActivity())
						.getNumberOfCompletedWeeklyTasks());
		statsMonthlyTasksScore.setText(""
				+ ScoreHandler.getInstance(getActivity())
						.getNumberOfCompletedMonthlyTasks());
		statsOverallScore.setText(ScoreHandler.getInstance(getActivity())
				.getOverallScore() + "");
	}
}
