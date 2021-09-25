package in.cbslgroup.ezeeoffice.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class MultipleSelect implements Parcelable {

    public static final Creator<MultipleSelect> CREATOR = new Creator<MultipleSelect>() {
        @Override
        public MultipleSelect createFromParcel(Parcel in) {
            return new MultipleSelect(in);
        }

        @Override
        public MultipleSelect[] newArray(int size) {
            return new MultipleSelect[size];
        }
    };
    private String filePath;
    private String filesize;
    private String uri;

    public MultipleSelect(String filePath, String filesize, String uri) {
        this.filePath = filePath;
        this.filesize = filesize;
        this.uri = uri;
    }

    protected MultipleSelect(Parcel in) {
        filePath = in.readString();
        filesize = in.readString();
        uri = in.readString();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filePath);
        dest.writeString(filesize);
        dest.writeString(uri);
    }
}
