package com.greenself.daogen;

import com.greenself.daogen.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table TASK_HISTORY.
 */
public class TaskHistory {

    private Long id;
    private java.util.Date completedDate;
    private long taskSourceId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient TaskHistoryDao myDao;

    private TaskSource taskSource;
    private Long taskSource__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public TaskHistory() {
    }

    public TaskHistory(Long id) {
        this.id = id;
    }

    public TaskHistory(Long id, java.util.Date completedDate, long taskSourceId) {
        this.id = id;
        this.completedDate = completedDate;
        this.taskSourceId = taskSourceId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTaskHistoryDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public java.util.Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(java.util.Date completedDate) {
        this.completedDate = completedDate;
    }

    public long getTaskSourceId() {
        return taskSourceId;
    }

    public void setTaskSourceId(long taskSourceId) {
        this.taskSourceId = taskSourceId;
    }

    /** To-one relationship, resolved on first access. */
    public TaskSource getTaskSource() {
        long __key = this.taskSourceId;
        if (taskSource__resolvedKey == null || !taskSource__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TaskSourceDao targetDao = daoSession.getTaskSourceDao();
            TaskSource taskSourceNew = targetDao.load(__key);
            synchronized (this) {
                taskSource = taskSourceNew;
            	taskSource__resolvedKey = __key;
            }
        }
        return taskSource;
    }

    public void setTaskSource(TaskSource taskSource) {
        if (taskSource == null) {
            throw new DaoException("To-one property 'taskSourceId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.taskSource = taskSource;
            taskSourceId = taskSource.getId();
            taskSource__resolvedKey = taskSourceId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    public TaskHistory(java.util.Date completedDate, TaskSource taskSource) {
        this.completedDate = completedDate;
        setTaskSource(taskSource);
    }

	@Override
	public String toString() {
		return "TaskHistory [id=" + id + ", completedDate=" + completedDate
				+ ", taskSource=" + getTaskSource() + "]";
	}
    // KEEP METHODS END

}
