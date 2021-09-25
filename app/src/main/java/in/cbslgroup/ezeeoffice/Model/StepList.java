package in.cbslgroup.ezeeoffice.Model;

public class StepList {

    private String stepName, stepId, workflowId;

    public StepList(String stepName, String stepId, String workflowId) {
        this.stepName = stepName;
        this.stepId = stepId;
        this.workflowId = workflowId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }


}
