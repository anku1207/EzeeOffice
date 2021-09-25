package in.cbslgroup.ezeeoffice.Model;

import android.graphics.drawable.Drawable;

public class DashBoard {

    Drawable image;
    String heading;
    String subheading;

    public DashBoard(Drawable image, String heading, String subheading) {
        this.image = image;
        this.heading = heading;
        this.subheading = subheading;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getSubheading() {
        return subheading;
    }

    public void setSubheading(String subheading) {
        this.subheading = subheading;
    }

}
