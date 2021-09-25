package in.cbslgroup.ezeeoffice.Model;

public class WorkFlowList {

    String workFlowName;
    String workFlowId;


    public WorkFlowList(String workFlowName, String workFlowId) {
        this.workFlowName = workFlowName;
        this.workFlowId = workFlowId;
    }

    public String getWorkFlowName() {

        return workFlowName;
    }

    public void setWorkFlowName(String workFlowName) {
        this.workFlowName = workFlowName;
    }

    public String getWorkFlowId() {
        return workFlowId;
    }

    public void setWorkFlowId(String workFlowId) {
        this.workFlowId = workFlowId;
    }
}
