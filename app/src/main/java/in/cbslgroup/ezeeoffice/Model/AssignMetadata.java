package in.cbslgroup.ezeeoffice.Model;

public class AssignMetadata {

    private String metaname;
    private String metaid;
    private int drawableId;

    public AssignMetadata(String metaname, String metaid, int drawableId) {
        this.metaname = metaname;
        this.metaid = metaid;
        this.drawableId = drawableId;
    }


    public String getMetaname() {
        return metaname;
    }

    public void setMetaname(String metaname) {
        this.metaname = metaname;
    }

    public String getMetaid() {
        return metaid;
    }

    public void setMetaid(String metaid) {
        this.metaid = metaid;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }


}
