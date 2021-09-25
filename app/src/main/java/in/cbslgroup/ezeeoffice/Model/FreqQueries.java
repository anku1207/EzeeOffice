package in.cbslgroup.ezeeoffice.Model;

public class FreqQueries {

    String query;
    String queryName;
    String slid;
    String metadata;
    String condition;


    public FreqQueries(String query, String queryName, String slid, String metadata, String condition) {
        this.query = query;
        this.queryName = queryName;
        this.slid = slid;
        this.metadata = metadata;
        this.condition = condition;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public String getSlid() {
        return slid;
    }

    public void setSlid(String slid) {
        this.slid = slid;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

}
