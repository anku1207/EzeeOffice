package in.cbslgroup.ezeeoffice.Model;

public class Recyclebin {

    private String Filename;
    private String FileExtension;
    private String FileSize;
    private String StorageName;
    private String docid;
    private String filepath;
    private String date;

    public Recyclebin(String filename, String fileExtension, String fileSize, String storageName, String docid, String filepath, String date) {
        Filename = filename;
        FileExtension = fileExtension;
        FileSize = fileSize;
        StorageName = storageName;
        this.docid = docid;
        this.filepath = filepath;
        this.date = date;
    }

    public String getFilename() {
        return Filename;
    }

    public void setFilename(String filename) {
        Filename = filename;
    }

    public String getFileExtension() {
        return FileExtension;
    }

    public void setFileExtension(String fileExtension) {
        FileExtension = fileExtension;
    }

    public String getFileSize() {
        return FileSize;
    }

    public void setFileSize(String fileSize) {
        FileSize = fileSize;
    }

    public String getStorageName() {
        return StorageName;
    }

    public void setStorageName(String storageName) {
        StorageName = storageName;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
