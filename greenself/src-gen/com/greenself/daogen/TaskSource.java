package com.greenself.daogen;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

import android.R.integer;

// KEEP INCLUDES - put your custom includes here
import com.greenself.constants.Constants.Type;
// KEEP INCLUDES END
/**
 * Entity mapped to table TASK_SOURCE.
 */
public class TaskSource {

    private Long id;
    private String typeDB;
    private Boolean applicability;
    private String info;
    /** Not-null value. */
    private String name;
    private Integer xpPoints;

    // KEEP FIELDS - put your custom fields here
    private Type type;
    // KEEP FIELDS END

    public TaskSource() {
    }

    public TaskSource(Long id) {
        this.id = id;
    }

    public TaskSource(Long id, String typeDB, Boolean applicability, String info, String name, Integer xpPoints) {
        this.id = id;
        this.typeDB = typeDB;
        this.applicability = applicability;
        this.info = info;
        this.name = name;
        this.xpPoints = xpPoints;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeDB() {
        return typeDB;
    }

    public void setTypeDB(String typeDB) {
        this.typeDB = typeDB;
    }

    public Boolean getApplicability() {
        return applicability;
    }

    public void setApplicability(Boolean applicability) {
        this.applicability = applicability;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    /** Not-null value. */
    public String getName() {
        return name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName(String name) {
        this.name = name;
    }

    public Integer getXpPoints() {
        return xpPoints;
    }

    public void setXpPoints(Integer xpPoints) {
        this.xpPoints = xpPoints;
    }

    // KEEP METHODS - put your custom methods here
    public TaskSource(Boolean applicability, Type type, int xpPoints, String name, String info) {
        this.applicability = applicability;
        this.info = info;
        this.name = name;
        this.xpPoints = xpPoints;
        setType(type);
    }
    
    public void setType(Type recurrence) {
    	this.typeDB = recurrence.name();
    	this.type = recurrence;
    }
    public Type getType() {
    	if(this.type==null) {
	    	try {
	    		this.type = Type.valueOf(this.typeDB);
	    	} catch (Exception ex) {
	    		ex.printStackTrace();
	    		this.type = Type.DAILY;
	    	}
    	} 
    	
    	return this.type;
    }

	@Override
	public String toString() {
		return "TaskSource [id=" + id + ", name=" + name + ", type=" + getTypeDB()
				+ ", applic=" + applicability + "]";
	}    
    // KEEP METHODS END

}
