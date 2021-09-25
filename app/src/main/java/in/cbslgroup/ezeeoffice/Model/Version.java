package in.cbslgroup.ezeeoffice.Model;

public class Version {

    public Version(String docName, String docId, String docpath, String docVersionName, String docSlid) {
        this.docName = docName;
        this.docId = docId;
        this.docpath = docpath;
        this.docVersionName = docVersionName;
        this.docSlid = docSlid;
    }

    private String docName;
    private String docId;
    private String docpath;
    private String docVersionName;
    private String docSlid;


    public String getDocSlid() {
        return docSlid;
    }

    public void setDocSlid(String docSlid) {
        this.docSlid = docSlid;
    }


    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDocpath() {
        return docpath;
    }

    public void setDocpath(String docpath) {
        this.docpath = docpath;
    }

    public String getDocVersionName() {
        return docVersionName;
    }

    public void setDocVersionName(String docVersionName) {
        this.docVersionName = docVersionName;
    }

}
