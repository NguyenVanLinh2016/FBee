package dev.linhnv.fbee.model;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by DevLinhnv on 1/7/2017.
 */

public class Forum {
    private String topicID;
    private String title;
    private String description;
    private Bitmap image;

    public Forum(){}
    public Forum(String title, String description, Bitmap image, String topicID) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.topicID = topicID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTopicID() {
        return topicID;
    }

    public void setTopicID(String topicID) {
        this.topicID = topicID;
    }
}
