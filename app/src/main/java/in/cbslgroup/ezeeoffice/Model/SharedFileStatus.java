package in.cbslgroup.ezeeoffice.Model;

public class SharedFileStatus {


    private String text;
    private int statusImage;

    public SharedFileStatus(String text, int statusImage) {
        this.text = text;
        this.statusImage = statusImage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getStatusImage() {
        return statusImage;
    }

    public void setStatusImage(int statusImage) {
        this.statusImage = statusImage;
    }


}
