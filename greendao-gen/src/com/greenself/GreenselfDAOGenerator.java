package com.greenself;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class GreenselfDAOGenerator {

	public static void main(String[] args) throws Exception {
		Schema schema = new Schema(16, "com.greenself.daogen");
		schema.enableKeepSectionsByDefault();

		Entity task = addTask(schema);
		Entity taskSource = addTaskSource(schema);
		Entity taskHistory = addTaskHistory(schema);

		addRelations(task, taskSource, taskHistory);

		// addHabbit(schema);

		new DaoGenerator().generateAll(schema, "../greenself/src-gen");
	}

	private static void addRelations(Entity task, Entity taskSource,
			Entity taskHistory) {
		Property taskSourceIdProperty = task.addLongProperty("taskSourceId")
				.notNull().getProperty();
		task.addToOne(taskSource, taskSourceIdProperty);

		taskSourceIdProperty = taskHistory.addLongProperty("taskSourceId")
				.notNull().getProperty();
		taskHistory.addToOne(taskSource, taskSourceIdProperty);
	}

	private static Entity addTask(Schema schema) {
		Entity task = schema.addEntity("Task");
		task.addIdProperty();
		task.addBooleanProperty("status").notNull();
		task.addDateProperty("date");

		return task;
	}

	private static Entity addTaskSource(Schema schema) {
		Entity taskSource = schema.addEntity("TaskSource");
		taskSource.addIdProperty();
		taskSource.addStringProperty("typeDB");
		taskSource.addBooleanProperty("applicability");
		taskSource.addStringProperty("info");
		taskSource.addStringProperty("name").notNull();

		return taskSource;
	}

	private static Entity addTaskHistory(Schema schema) {
		Entity taskHistory = schema.addEntity("TaskHistory");
		taskHistory.addIdProperty();
		taskHistory.addDateProperty("completedDate");

		return taskHistory;
	}

	private static void addHabbit(Schema schema) {
		Entity habbit = schema.addEntity("Habbit");
		habbit.addIdProperty();
		habbit.addStringProperty("actvity").notNull();
		habbit.addIntProperty("type");
		habbit.addBooleanProperty("applicability");
		habbit.addBooleanProperty("status");
		habbit.addStringProperty("info");
		habbit.addDateProperty("date");
		// extra from Task
		habbit.addStringProperty("motivation");
	}
}
