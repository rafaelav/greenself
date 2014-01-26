package com.greenself.objects;

import com.greenself.objects.Constants.Type;

public class Task {
	/* how often can the task be completed*/
	private Type recurrence;
	/* special requirements task do not apply to everyone*/
	private boolean applicability;
	/* done/not done*/
	private boolean status;
	/*extra info about task*/
	private String text;
}
