package in.cbslgroup.ezeeoffice.Model;

public class AuditTrail {

    String username;
    String actionPerformed;
    String actionStartDate;
    String actionEndDate;
    String ip;
    String remarks;
    String whichList;
    String sno;

    public AuditTrail(String username, String actionPerformed, String actionStartDate, String actionEndDate, String ip, String remarks, String whichList, String sno) {
        this.username = username;
        this.actionPerformed = actionPerformed;
        this.actionStartDate = actionStartDate;
        this.actionEndDate = actionEndDate;
        this.ip = ip;
        this.remarks = remarks;
        this.whichList = whichList;
        this.sno = sno;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getWhichList() {
        return whichList;
    }

    public void setWhichList(String whichList) {
        this.whichList = whichList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getActionPerformed() {
        return actionPerformed;
    }

    public void setActionPerformed(String actionPerformed) {
        this.actionPerformed = actionPerformed;
    }

    public String getActionStartDate() {
        return actionStartDate;
    }

    public void setActionStartDate(String actionStartDate) {
        this.actionStartDate = actionStartDate;
    }

    public String getActionEndDate() {
        return actionEndDate;
    }

    public void setActionEndDate(String actionEndDate) {
        this.actionEndDate = actionEndDate;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


}
