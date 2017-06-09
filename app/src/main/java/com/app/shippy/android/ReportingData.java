package com.app.shippy.android;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This class will make it very easy to use gson.toJSON as it contains the types and all
 * for the moment I won't be using it as it requires quite some re-writing and re-factoring
 * Sore other day!
 */
class ReportingData {

    ReportingData() {

    }

    @SerializedName("activity_start")
    @Expose
    private long activityStart;
    @SerializedName("fragName")
    @Expose
    private String fragName;
    @SerializedName("activity_name")
    @Expose
    private String activityName;
    @SerializedName("sessID")
    @Expose
    private long sessID;
    @SerializedName("fragActions")
    @Expose
    private FragActions fragActions;

    public long getActivityStart() {
        return activityStart;
    }

    public void setActivityStart(long activityStart) {
        this.activityStart = activityStart;
    }

    public String getFragName() {
        return fragName;
    }

    public void setFragName(String fragName) {
        this.fragName = fragName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public long getSessID() {
        return sessID;
    }

    public void setSessID(long sessID) {
        this.sessID = sessID;
    }

    public FragActions getFragActions() {
        return fragActions;
    }

    public void setFragActions(FragActions fragActions) {
        this.fragActions = fragActions;
    }

}