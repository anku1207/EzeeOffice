package in.cbslgroup.ezeeoffice.Model;

public class TodoList {


    String todoId;
    String empId;
    String taskName;
    String taskDesc;
    String taskDate;
    String taskTime;
    String taskNotiFreq;
    String taskNotiTime;
    String isArchived;

    public String getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(String isArchived) {
        this.isArchived = isArchived;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getTodoId() {
        return todoId;
    }

    public void setTodoId(String todoId) {
        this.todoId = todoId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public String getTaskNotiFreq() {
        return taskNotiFreq;
    }

    public void setTaskNotiFreq(String taskNotiFreq) {
        this.taskNotiFreq = taskNotiFreq;
    }

    public String getTaskNotiTime() {
        return taskNotiTime;
    }

    public void setTaskNotiTime(String taskNotiTime) {
        this.taskNotiTime = taskNotiTime;
    }


}
