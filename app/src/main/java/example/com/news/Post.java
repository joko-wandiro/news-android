package example.com.news;

import java.io.Serializable;

/**
 * Created by Joko Wandiro on 20/01/2020.
 */

public class Post implements Serializable {
    String id, title, date, image;

    public Post(String id, String title, String date, String image) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }
}

