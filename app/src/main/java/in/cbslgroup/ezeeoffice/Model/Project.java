package in.cbslgroup.ezeeoffice.Model;

public class Project {

    private String Id;
    private String divisionId;
    private String projectName;
    private String location;
    private String companyId;

    public Project(String id, String divisionId, String projectName, String location, String companyId) {
        Id = id;
        this.divisionId = divisionId;
        this.projectName = projectName;
        this.location = location;
        this.companyId = companyId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
