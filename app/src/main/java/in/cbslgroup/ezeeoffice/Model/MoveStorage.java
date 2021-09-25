package in.cbslgroup.ezeeoffice.Model;

public class MoveStorage {

    String foldername;
    String slid;
    int drawableicon;

    public MoveStorage(String foldername, String slid, int drawableicon) {
        this.foldername = foldername;
        this.slid = slid;
        this.drawableicon = drawableicon;
    }

    public String getFoldername() {
        return foldername;
    }

    public void setFoldername(String foldername) {
        this.foldername = foldername;
    }

    public String getSlid() {
        return slid;
    }

    public void setSlid(String slid) {
        this.slid = slid;
    }

    public int getDrawableicon() {
        return drawableicon;
    }

    public void setDrawableicon(int drawableicon) {
        this.drawableicon = drawableicon;
    }


}
