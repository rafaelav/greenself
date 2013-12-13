package com.greenself;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenselfDAOGenerator {
	
	public static void main(String[] args) throws Exception {
        Schema schema = new Schema(3, "com.greenself.daogen");
        schema.enableKeepSectionsByDefault();
        
        addTask(schema);
        addHabbit(schema);

        new DaoGenerator().generateAll(schema, "../greenself/src-gen");
    }

	private static void addTask(Schema schema) {
        Entity task = schema.addEntity("Task");
        task.addIdProperty();
        task.addStringProperty("actvity").notNull();
        task.addStringProperty("recurrenceDB");
        task.addBooleanProperty("applicability");
        task.addBooleanProperty("status");
        task.addStringProperty("info");
        task.addDateProperty("date");
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
