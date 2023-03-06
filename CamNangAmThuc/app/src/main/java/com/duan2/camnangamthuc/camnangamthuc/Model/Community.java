package com.duan2.camnangamthuc.camnangamthuc.Model;
import java.util.Date;

public class Community {
    public String id,namefood,imagefood,resourcesfood,recipefood,nameusefood,emailusefood,imageusefood,statusfood;
    public long timefood;
    public int likecount;

    public Community() {
    }


    public Community(String id, String namefood, String imagefood, String resourcesfood, String recipefood, String nameusefood, String emailusefood, String imageusefood, String statusfood, long timefood, int likecount) {
        this.id = id;
        this.namefood = namefood;
        this.imagefood = imagefood;
        this.resourcesfood = resourcesfood;
        this.recipefood = recipefood;
        this.nameusefood = nameusefood;
        this.emailusefood = emailusefood;
        this.imageusefood = imageusefood;
        this.statusfood = statusfood;
        this.timefood = timefood;
        this.likecount = likecount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamefood() {
        return namefood;
    }

    public void setNamefood(String namefood) {
        this.namefood = namefood;
    }

    public String getImagefood() {
        return imagefood;
    }

    public void setImagefood(String imagefood) {
        this.imagefood = imagefood;
    }

    public String getResourcesfood() {
        return resourcesfood;
    }

    public void setResourcesfood(String resourcesfood) {
        this.resourcesfood = resourcesfood;
    }

    public String getRecipefood() {
        return recipefood;
    }

    public void setRecipefood(String recipefood) {
        this.recipefood = recipefood;
    }

    public String getNameusefood() {
        return nameusefood;
    }

    public void setNameusefood(String nameusefood) {
        this.nameusefood = nameusefood;
    }

    public String getEmailusefood() {
        return emailusefood;
    }

    public void setEmailusefood(String emailusefood) {
        this.emailusefood = emailusefood;
    }

    public String getImageusefood() {
        return imageusefood;
    }

    public void setImageusefood(String imageusefood) {
        this.imageusefood = imageusefood;
    }

    public long getTimefood() {
        return timefood;
    }

    public String getStatusfood() {
        return statusfood;
    }

    public void setStatusfood(String statusfood) {
        this.statusfood = statusfood;
    }

    public void setTimefood(long timefood) {
        this.timefood = timefood;
    }

    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }
}
