package com.aaa.aaa;

import java.util.Date;

public class commentInfo {
    String comment_id;
    String comment_uid;
    String comment_content;
    String post_id;
    Date comment_time;

    public commentInfo(String comment_id, String comment_uid, String comment_content, String post_id, Date comment_time) {
        this.comment_id = comment_id;
        this.comment_uid = comment_uid;
        this.comment_content = comment_content;
        this.post_id = post_id;
        this.comment_time = comment_time;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_uid() {
        return comment_uid;
    }

    public void setComment_uid(String comment_uid) {
        this.comment_uid = comment_uid;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public Date getComment_time() {
        return comment_time;
    }

    public void setComment_time(Date comment_time) {
        this.comment_time = comment_time;
    }
}
