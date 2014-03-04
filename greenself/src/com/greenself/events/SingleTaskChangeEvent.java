package com.greenself.events;

import com.greenself.daogen.Task;

public class SingleTaskChangeEvent {

	Task task;
	public SingleTaskChangeEvent(Task task) {
		this.task = task;
	}
}
