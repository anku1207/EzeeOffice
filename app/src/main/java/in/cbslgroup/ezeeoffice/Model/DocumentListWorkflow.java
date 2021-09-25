package in.cbslgroup.ezeeoffice.Model;

public class DocumentListWorkflow {

    String docname;
    String docid;
    String docpath;
    String extension;

    public DocumentListWorkflow(String docname, String docid, String docpath, String extension) {
        this.docname = docname;
        this.docid = docid;
        this.docpath = docpath;
        this.extension = extension;
    }

    public String getDocname() {
        return docname;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getDocpath() {
        return docpath;
    }

    public void setDocpath(String docpath) {
        this.docpath = docpath;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }


}
