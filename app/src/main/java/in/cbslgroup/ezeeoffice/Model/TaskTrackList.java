package in.cbslgroup.ezeeoffice.Model;

public class TaskTrackList {

    private String ticketId;
    private String taskStatus;
    private String TicketDate;
    private String Action;
    private String Docid;

    public TaskTrackList(String ticketId, String taskStatus, String ticketDate, String action, String Docid) {
        this.ticketId = ticketId;
        this.taskStatus = taskStatus;
        this.TicketDate = ticketDate;
        this.Action = action;
        this.Docid = Docid;

    }

    public String getDocid() {
        return Docid;
    }

    public void setDocid(String docid) {
        Docid = docid;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTicketDate() {
        return TicketDate;
    }

    public void setTicketDate(String ticketDate) {
        TicketDate = ticketDate;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }


}



