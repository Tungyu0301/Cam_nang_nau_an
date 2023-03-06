package com.duan2.camnangamthuc.camnangamthuc.Model;

public class Comment {
    public String id,namecomment,namefoodcomment,nameusecomment,emailusecomment,imageusecomment,commentId;
    public long timecomment;

    public Comment() {
    }

    public Comment(String id, String namecomment, String namefoodcomment, String nameusecomment, String emailusecomment, String imageusecomment, String commentId, long timecomment) {
        this.id = id;
        this.namecomment = namecomment;
        this.namefoodcomment = namefoodcomment;
        this.nameusecomment = nameusecomment;
        this.emailusecomment = emailusecomment;
        this.imageusecomment = imageusecomment;
        this.commentId = commentId;
        this.timecomment = timecomment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamecomment() {
        return namecomment;
    }

    public void setNamecomment(String namecomment) {
        this.namecomment = namecomment;
    }

    public String getNamefoodcomment() {
        return namefoodcomment;
    }

    public void setNamefoodcomment(String namefoodcomment) {
        this.namefoodcomment = namefoodcomment;
    }

    public String getNameusecomment() {
        return nameusecomment;
    }

    public void setNameusecomment(String nameusecomment) {
        this.nameusecomment = nameusecomment;
    }

    public String getEmailusecomment() {
        return emailusecomment;
    }

    public void setEmailusecomment(String emailusecomment) {
        this.emailusecomment = emailusecomment;
    }

    public String getImageusecomment() {
        return imageusecomment;
    }

    public void setImageusecomment(String imageusecomment) {
        this.imageusecomment = imageusecomment;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public long getTimecomment() {
        return timecomment;
    }

    public void setTimecomment(long timecomment) {
        this.timecomment = timecomment;
    }
}
