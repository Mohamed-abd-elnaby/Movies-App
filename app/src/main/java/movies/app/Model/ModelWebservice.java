package movies.app.Model;

import java.io.Serializable;

/**
 * Created by mohamed on 16/11/16.
 */

public class ModelWebservice implements Serializable {
    String poster_path;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }


}
