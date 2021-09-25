package in.cbslgroup.ezeeoffice.Chip;

import java.io.Serializable;

public class User implements Serializable {

    private String slid;
    private String username;
    private int drawableId;
    private String userid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;

    private boolean isEnabled = true;

    public User() {
    }

    public boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }


    public User(String username, String userid) {
        this.username = username;
        this.userid = userid;
    }


    public User(String username, int drawableId, String userid) {
        this.username = username;
        this.drawableId = drawableId;
        this.userid = userid;
    }

    /*public User(){
        super();
    }*/

    public User(String username, String slid, int drawableId) {
        this.username = username;
        this.drawableId = drawableId;
        this.slid = slid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSlid() {
        return slid;
    }

    public void setSlid(String slid) {
        this.slid = slid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }


}
