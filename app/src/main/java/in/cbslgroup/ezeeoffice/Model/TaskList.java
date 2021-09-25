package in.cbslgroup.ezeeoffice.Model;

public class TaskList {

    private String taskName;
    private String taskId;

    public TaskList(String taskName, String taskId) {
        this.taskName = taskName;
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }


}
