package in.cbslgroup.ezeeoffice.Model;

public class AuditTrailWorkflow {
    String userName;
    String actionPerformed;
    String actionDateTime;
    String ip;
    String userid;
    String Sno;

    public AuditTrailWorkflow(String Sno, String userName, String actionPerformed, String actionDateTime, String ip, String userid) {
        this.Sno = Sno;
        this.userName = userName;
        this.actionPerformed = actionPerformed;
        this.actionDateTime = actionDateTime;
        this.ip = ip;
        this.userid = userid;
    }

    public String getSno() {
        return Sno;
    }

    public void setSno(String sno) {
        Sno = sno;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getActionPerformed() {
        return actionPerformed;
    }

    public void setActionPerformed(String actionPerformed) {
        this.actionPerformed = actionPerformed;
    }

    public String getActionDateTime() {
        return actionDateTime;
    }

    public void setActionDateType(String actionDateTime) {
        this.actionDateTime = actionDateTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


}
