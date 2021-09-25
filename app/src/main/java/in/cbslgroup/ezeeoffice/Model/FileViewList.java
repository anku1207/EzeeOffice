package in.cbslgroup.ezeeoffice.Model;

public class FileViewList {

    private boolean isSelected = false;
    private String fileName;
    private String fileSize;
    private String noOfPages;
    private String uploadedBy;
    private String uploadDate;
    private String filePath;
    private String filetype;
    private String docId;
    private String versionShowHide;
    private String checkincheckout;
    private String docName;

    public FileViewList(String fileName, String fileSize, String noOfPages, String uploadedBy, String uploadDate, String filePath, String filetype, String docId, String versionShowHide, String checkincheckout, Boolean isSelected, String docName) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.noOfPages = noOfPages;
        this.uploadedBy = uploadedBy;
        this.uploadDate = uploadDate;
        this.filePath = filePath;
        this.filetype = filetype;
        this.docId = docId;
        this.versionShowHide = versionShowHide;
        this.checkincheckout = checkincheckout;
        this.isSelected = isSelected;
        this.docName = docName;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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


    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }


    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
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


}

