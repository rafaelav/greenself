package com.greenself.daogen;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.greenself.daogen.Habbit;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table HABBIT.
*/
public class HabbitDao extends AbstractDao<Habbit, Long> {

    public static final String TABLENAME = "HABBIT";

    /**
     * Properties of entity Habbit.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Actvity = new Property(1, String.class, "actvity", false, "ACTVITY");
        public final static Property Type = new Property(2, Integer.class, "type", false, "TYPE");
        public final static Property Applicability = new Property(3, Boolean.class, "applicability", false, "APPLICABILITY");
        public final static Property Status = new Property(4, Boolean.class, "status", false, "STATUS");
        public final static Property Info = new Property(5, String.class, "info", false, "INFO");
        public final static Property Date = new Property(6, java.util.Date.class, "date", false, "DATE");
        public final static Property Motivation = new Property(7, String.class, "motivation", false, "MOTIVATION");
    };


    public HabbitDao(DaoConfig config) {
        super(config);
    }
    
    public HabbitDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'HABBIT' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'ACTVITY' TEXT NOT NULL ," + // 1: actvity
                "'TYPE' INTEGER," + // 2: type
                "'APPLICABILITY' INTEGER," + // 3: applicability
                "'STATUS' INTEGER," + // 4: status
                "'INFO' TEXT," + // 5: info
                "'DATE' INTEGER," + // 6: date
                "'MOTIVATION' TEXT);"); // 7: motivation
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'HABBIT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Habbit entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getActvity());
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(3, type);
        }
 
        Boolean applicability = entity.getApplicability();
        if (applicability != null) {
            stmt.bindLong(4, applicability ? 1l: 0l);
        }
 
        Boolean status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(5, status ? 1l: 0l);
        }
 
        String info = entity.getInfo();
        if (info != null) {
            stmt.bindString(6, info);
        }
 
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(7, date.getTime());
        }
 
        String motivation = entity.getMotivation();
        if (motivation != null) {
            stmt.bindString(8, motivation);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Habbit readEntity(Cursor cursor, int offset) {
        Habbit entity = new Habbit( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // actvity
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // type
            cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0, // applicability
            cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0, // status
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // info
            cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)), // date
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // motivation
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Habbit entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setActvity(cursor.getString(offset + 1));
        entity.setType(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setApplicability(cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0);
        entity.setStatus(cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0);
        entity.setInfo(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDate(cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)));
        entity.setMotivation(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Habbit entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Habbit entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
