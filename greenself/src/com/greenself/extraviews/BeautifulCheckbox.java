package com.greenself.extraviews;

import com.greenself.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BeautifulCheckbox extends ImageView {
	private boolean status;

	public BeautifulCheckbox(Context context) {
		super(context);
		setChecked(false);
	}

	public BeautifulCheckbox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setChecked(false);
	}

	public BeautifulCheckbox(Context context, AttributeSet attrs) {
		super(context, attrs);
		setChecked(false);
	}

	public boolean isChecked() {
		return status;
	}

	public void setChecked(boolean newStatus) {
		status = newStatus;
		if (newStatus) {
			setImageResource(R.drawable.ic_happy);
		} else {
			setImageResource(R.drawable.ic_sad);
		}
	}
}
