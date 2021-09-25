package in.cbslgroup.ezeeoffice.Model;

public class Description {

    private String desc, rupee, paisa;

    public Description(String desc, String rupee, String paisa) {
        this.desc = desc;
        this.rupee = rupee;
        this.paisa = paisa;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRupee() {
        return rupee;
    }

    public void setRupee(String rupee) {
        this.rupee = rupee;
    }

    public String getPaisa() {
        return paisa;
    }

    public void setPaisa(String paisa) {
        this.paisa = paisa;
    }
}
