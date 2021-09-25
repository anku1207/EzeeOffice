package in.cbslgroup.ezeeoffice.Model;

public class Search {

    private String fileName;
    private String fileSize;
    private String noOfPages;
    private String storageName;
    private String metadata;
    private String path;
    private String extension;
    private String docid;
    private String date;

    public Search(String fileName, String fileSize, String noOfPages, String storageName, String metadata, String path, String extension, String date, String docid) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.noOfPages = noOfPages;
        this.storageName = storageName;
        this.metadata = metadata;
        this.path = path;
        this.extension = extension;
        this.date = date;
        this.docid = docid;

    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getNoOfPages() {
        return noOfPages;
    }

    public void setNoOfPages(String noOfPages) {
        this.noOfPages = noOfPages;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }


}
