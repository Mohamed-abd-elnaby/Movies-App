package movies.app.Model;

import java.io.Serializable;

/**
 * Created by mohamed on 19/11/16.
 */

public class Modelreviwer implements Serializable{
    String author,content;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
