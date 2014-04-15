package com.greenself;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greenself.events.EventsBusFactory;
import com.greenself.events.MultipleTasksChangeEvent;
import com.greenself.events.SingleTaskChangeEvent;
import com.greenself.handlers.ScoreHandler;
import com.squareup.otto.Subscribe;

public class StatisticsFragment extends Fragment{
	
	private TextView statsDailyTasksScore;
	private TextView statsWeeklyTasksScore;
	private TextView statsMonthlyTasksScore;
	private TextView statsOverallScore;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.statistics, null);
		
		// calculate statistics
		ScoreHandler.getInstance(getActivity()).updateAllStatistics();
		
		// update scores published on fragment
		statsDailyTasksScore = (TextView) view.findViewById(R.id.statsDailyTasksScore);
		statsWeeklyTasksScore = (TextView) view.findViewById(R.id.statsWeeklyTasksScore);
		statsMonthlyTasksScore = (TextView) view.findViewById(R.id.statsMonthlyTasksScore);
		statsOverallScore = (TextView) view.findViewById(R.id.statsOverallScore);
		
		// register for updates from bus
		EventsBusFactory.getInstance().register(this);
		
		return view;
	}
	
	@Subscribe public void updateStatisticsForMultipleTasksChange(MultipleTasksChangeEvent event) {
		ScoreHandler.getInstance(getActivity()).updateAllStatistics();
		
		statsDailyTasksScore.setText(""+ScoreHandler.getInstance(getActivity()).getNumberOfCompletedDailyTasks());
		statsWeeklyTasksScore.setText(""+ScoreHandler.getInstance(getActivity()).getNumberOfCompletedWeeklyTasks());
		statsMonthlyTasksScore.setText(""+ScoreHandler.getInstance(getActivity()).getNumberOfCompletedMonthlyTasks());
		statsOverallScore.setText(ScoreHandler.getInstance(getActivity()).getOverallScore()+"");
	}
	
	@Subscribe public void updateStatisticsForSingleTaskChange(SingleTaskChangeEvent event) {
		ScoreHandler.getInstance(getActivity()).updateStatisticsForSingleTaskChange(event.getChangedTask());
		
		statsDailyTasksScore.setText(""+ScoreHandler.getInstance(getActivity()).getNumberOfCompletedDailyTasks());
		statsWeeklyTasksScore.setText(""+ScoreHandler.getInstance(getActivity()).getNumberOfCompletedWeeklyTasks());
		statsMonthlyTasksScore.setText(""+ScoreHandler.getInstance(getActivity()).getNumberOfCompletedMonthlyTasks());
		statsOverallScore.setText(ScoreHandler.getInstance(getActivity()).getOverallScore()+"");		
	}
}
