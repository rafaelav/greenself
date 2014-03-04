package com.greenself.events;

import com.squareup.otto.Bus;

public class EventsBusFactory {
	private static Bus instance = null;

	protected EventsBusFactory() {
		// Exists only to defeat instantiation
	}

	public static Bus getInstance() {
		if (instance == null)
			instance = new Bus();
		return instance;
	}
}
