package in.cbslgroup.ezeeoffice.Model;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


public class TimelineView {

    private String date = null;
    private String title = null;
    private String description = null;
    private String taskcount = null;
    private int bellowLineColor = 0;
    private int bellowLineSize = 6;
    private String taskApprovedBy;
    private String avatar;
    private String taskHeading;
    private String taskStatus;

    public TimelineView(String date, String title, String description, String taskcount, int bellowLineColor, int bellowLineSize, String avatar, String taskHeading, String taskApprovedBy, String taskstatus) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.taskcount = taskcount;
        this.bellowLineColor = bellowLineColor;
        this.bellowLineSize = bellowLineSize;
        this.avatar = avatar;
        this.taskHeading = taskHeading;
        this.taskApprovedBy = taskApprovedBy;
        this.taskStatus = taskstatus;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskApprovedBy() {
        return taskApprovedBy;
    }

    public void setTaskApprovedBy(String taskApprovedBy) {
        this.taskApprovedBy = taskApprovedBy;
    }

    public String getTaskHeading() {
        return taskHeading;
    }

    public void setTaskHeading(String taskHeading) {
        this.taskHeading = taskHeading;
    }

    public String getTaskcount() {
        return taskcount;
    }

    public void setTaskcount(String task_count) {
        this.taskcount = task_count;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getBellowLineSize() {
        return this.bellowLineSize;
    }

    public void setBellowLineSize(int bellowLineSize) {
        this.bellowLineSize = bellowLineSize;
    }


    public int getBellowLineColor() {
        return this.bellowLineColor;
    }

    public void setBellowLineColor(int bellowLineColor) {
        this.bellowLineColor = bellowLineColor;
    }


    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}

