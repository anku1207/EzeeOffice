package in.cbslgroup.ezeeoffice.Model;

import java.util.ArrayList;

public class MetadataDynamicForm {

    String text;
    ArrayList<String> metadatalist;
    ArrayList<String> conditionlist;


    public MetadataDynamicForm(String text, ArrayList<String> metadatalist, ArrayList<String> conditionlist) {
        this.text = text;
        this.metadatalist = metadatalist;
        this.conditionlist = conditionlist;
    }

    public MetadataDynamicForm(ArrayList<String> metadatalist, ArrayList<String> conditionlist) {
        this.text = text;
        this.metadatalist = metadatalist;
        this.conditionlist = conditionlist;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<String> getMetadatalist() {
        return metadatalist;
    }

    public void setMetadatalist(ArrayList<String> metadatalist) {
        this.metadatalist = metadatalist;
    }

    public ArrayList<String> getConditionlist() {
        return conditionlist;
    }

    public void setConditionlist(ArrayList<String> conditionlist) {
        this.conditionlist = conditionlist;
    }

}
