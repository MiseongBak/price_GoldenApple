package com.aaa.aaa;

import java.util.ArrayList;
import java.util.Date;

public class PostInfo {
    String category;
    String title;
    String uid;
    Date created;
    private ArrayList<String> content;
    String postKey;

    public PostInfo(String category, String title, String uid, Date created, ArrayList<String> content, String postKey) {
        this.category = category;
        this.title = title;
        this.uid = uid;
        this.created = created;
        this.content = content;
        this.postKey = postKey;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public ArrayList<String> getContent() {
        return content;
    }

    public void setContent(ArrayList<String> content) {
        this.content = content;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }
}
