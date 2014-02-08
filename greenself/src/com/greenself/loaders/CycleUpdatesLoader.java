package com.greenself.loaders;

import java.util.logging.Logger;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.greenself.EndOfCycleHandler;

public class CycleUpdatesLoader extends AsyncTaskLoader<Boolean> {
	private static final Logger log = Logger.getLogger(CycleUpdatesLoader.class
			.getName());

	public CycleUpdatesLoader(Context context) {
		super(context);
	}

	@Override
	public Boolean loadInBackground() {
		log.info("Loading in background");
		return EndOfCycleHandler.getInstance().checkEndOfCycle(getContext());
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		log.info("In onStartLoading...");
		forceLoad();
	}

}
