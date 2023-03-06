package com.duan2.camnangamthuc.camnangamthuc.Model;

import java.io.Serializable;

public class Favorite implements Serializable {
    public String id,keyid,namefood,imagefood,resourcesfood,recipefood,nameusefood,emailusefood,imageusefood;
    public long timefood;

    public Favorite() {
    }

    public Favorite(String id, String keyid, String namefood, String imagefood, String resourcesfood, String recipefood, String nameusefood, String emailusefood, String imageusefood, long timefood) {
        this.id = id;
        this.keyid = keyid;
        this.namefood = namefood;
        this.imagefood = imagefood;
        this.resourcesfood = resourcesfood;
        this.recipefood = recipefood;
        this.nameusefood = nameusefood;
        this.emailusefood = emailusefood;
        this.imageusefood = imageusefood;
        this.timefood = timefood;
    }

    public String getKeyid() {
        return keyid;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
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

    public void setTimefood(long timefood) {
        this.timefood = timefood;
    }
}
