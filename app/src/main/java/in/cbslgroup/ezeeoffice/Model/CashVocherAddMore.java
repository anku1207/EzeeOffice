package in.cbslgroup.ezeeoffice.Model;

public class CashVocherAddMore {

    private String Rupee;
    private String Paisa;

    public CashVocherAddMore(String rupee, String paisa) {
        Rupee = rupee;
        Paisa = paisa;
    }

    public String getRupee() {
        return Rupee;
    }

    public void setRupee(String rupee) {
        Rupee = rupee;
    }

    public String getPaisa() {
        return Paisa;
    }

    public void setPaisa(String paisa) {
        Paisa = paisa;
    }

}
