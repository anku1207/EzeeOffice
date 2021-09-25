package in.cbslgroup.ezeeoffice.Model;

public class Divison {

    private String divisionName;
    private String divisionId;
    private String companyId;

    public Divison(String divisionName, String divisionId, String companyId) {
        this.divisionName = divisionName;
        this.divisionId = divisionId;
        this.companyId = companyId;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

}
