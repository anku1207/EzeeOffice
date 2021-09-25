package in.cbslgroup.ezeeoffice.Model;

public class SpinnerDropDown {

    private String storagename;
    private String slid;

    public SpinnerDropDown(String storagename, String slid) {
        this.storagename = storagename;
        this.slid = slid;
    }

    public String getStoragename() {
        return storagename;
    }

    public void setStoragename(String storagename) {
        this.storagename = storagename;
    }

    public String getSlid() {
        return slid;
    }

    public void setSlid(String slid) {
        this.slid = slid;
    }


}
