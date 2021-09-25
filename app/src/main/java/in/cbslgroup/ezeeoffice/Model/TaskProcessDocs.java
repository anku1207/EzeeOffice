package in.cbslgroup.ezeeoffice.Model;

public class TaskProcessDocs {



    String docname;
    String docPath;
    String extension;

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    String docid;

    public TaskProcessDocs(String docname, String docPath, String extension) {
        this.docname = docname;
        this.docPath = docPath;
        this.extension = extension;
    }

    public TaskProcessDocs(String docname, String docPath, String extension, String docid) {
        this.docname = docname;
        this.docPath = docPath;
        this.extension = extension;
        this.docid = docid;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getDocPath() {
        return docPath;
    }

    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    public String getDocname() {
        return docname;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

}
