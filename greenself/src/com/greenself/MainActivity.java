package com.greenself;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.greenself.daogen.DaoMaster;
import com.greenself.daogen.DaoMaster.DevOpenHelper;
import com.greenself.daogen.DaoSession;
import com.greenself.daogen.Task;
import com.greenself.objects.Constants.Type;

public class MainActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/*Task task = new Task();
		task.setName("Task 1");
		task.setType(Type.DAILY);

		DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
		SQLiteDatabase db = helper.getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		DaoSession daoSession = daoMaster.newSession();
		daoSession.getTaskDao().insert(task);*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
