package com.greenself.objects;

import com.greenself.objects.Constants.Recurrence;

public class Task {
	/* how often can the task be completed*/
	private Recurrence recurrence;
	/* special requirements task do not apply to everyone*/
	private boolean applicability;
	/* done/not done*/
	private boolean status;
	/*extra info about task*/
	private String text;
}
