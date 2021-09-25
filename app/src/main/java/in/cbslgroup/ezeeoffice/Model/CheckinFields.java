package in.cbslgroup.ezeeoffice.Model;

public class CheckinFields {

    private String fieldname, size, mandatorystatus, value, datatype;

    public CheckinFields(String fieldname, String size, String mandatorystatus, String value, String datatype) {
        this.fieldname = fieldname;
        this.size = size;
        this.mandatorystatus = mandatorystatus;
        this.value = value;
        this.datatype = datatype;
    }

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMandatorystatus() {
        return mandatorystatus;
    }

    public void setMandatorystatus(String mandatorystatus) {
        this.mandatorystatus = mandatorystatus;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

}
