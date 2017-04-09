package dev.linhnv.fbee.model;

import java.io.Serializable;

/**
 * Created by DevLinhnv on 1/16/2017.
 */

public class Post implements Serializable {
    public  int post_id;
    public int cate_id;
    public String post_title;
    public String post_description;
    public String post_videoUrl;
    public String post_link;
    public String post_image;

    public Post(){

    }

    public Post(int postId, int cateId, String postTitle, String postDescription, String postVideoUrl, String postLink, String postImage){
        this.post_id = postId;
        this.cate_id = cateId;
        this.post_title = postTitle;
        this.post_description = postDescription;
        this.post_videoUrl = postVideoUrl;
        this.post_link = postLink;
        this.post_image = postImage;
    }

}
