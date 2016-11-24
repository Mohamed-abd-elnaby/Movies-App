package movies.app.Model;

import java.io.Serializable;

/**
 * Created by mohamed on 23/11/16.
 */

public class ModelImage implements Serializable {
    String url,image;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
