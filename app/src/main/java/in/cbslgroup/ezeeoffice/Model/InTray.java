package in.cbslgroup.ezeeoffice.Model;

public class InTray {
    String taskname;
    String priority;
    String status;
    String assignDate;
    String deadLine;
    String startedBy;
    String docid;
    String taskid;
    String warning;

    public InTray(String taskname, String priority, String status, String assignDate, String deadLine, String startedBy, String docid, String taskid, String warning) {
        this.taskname = taskname;
        this.priority = priority;
        this.status = status;
        this.assignDate = assignDate;
        this.deadLine = deadLine;
        this.startedBy = startedBy;
        this.docid = docid;
        this.taskid = taskid;
        this.warning = warning;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(String assignDate) {
        this.assignDate = assignDate;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public String getStartedBy() {
        return startedBy;
    }

    public void setStartedBy(String startedBy) {
        this.startedBy = startedBy;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }


}
