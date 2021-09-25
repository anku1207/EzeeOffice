package in.cbslgroup.ezeeoffice.Model;

public class MetaDataViewList {

    private String fileName;
    private String fileSize;
    private String noOfPages;
    private String uploadedBy;
    private String uploadDate;
    private String filePath;
    private String filetype;
    private String metadata;
    private String docId;
    private String docName;
    private String versionShowHide;
    private String checkincheckout;

    public MetaDataViewList(String fileName, String fileSize, String noOfPages, String uploadedBy, String uploadDate, String filePath, String filetype, String metadata, String docId, String docName, String versionShowHide, String checkincheckout) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.noOfPages = noOfPages;
        this.uploadedBy = uploadedBy;
        this.uploadDate = uploadDate;
        this.filePath = filePath;
        this.filetype = filetype;
        this.metadata = metadata;
        this.docId = docId;
        this.docName = docName;
        this.versionShowHide = versionShowHide;
        this.checkincheckout = checkincheckout;
    }



    public String getVersionShowHide() {
        return versionShowHide;
    }

    public void setVersionShowHide(String versionShowHide) {
        this.versionShowHide = versionShowHide;
    }

    public String getCheckincheckout() {
        return checkincheckout;
    }

    public void setCheckincheckout(String checkincheckout) {
        this.checkincheckout = checkincheckout;
    }





    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
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

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }


}
