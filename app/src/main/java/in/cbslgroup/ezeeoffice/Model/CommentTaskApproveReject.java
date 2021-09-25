package in.cbslgroup.ezeeoffice.Model;

public class CommentTaskApproveReject {

    private String name;
    private String taskname;
    private String date;
    private String comment;
    private String pic;

    public CommentTaskApproveReject(String name, String taskname, String comment, String date, String pic) {
        this.name = name;
        this.taskname = taskname;
        this.date = date;
        this.comment = comment;
        this.pic = pic;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
