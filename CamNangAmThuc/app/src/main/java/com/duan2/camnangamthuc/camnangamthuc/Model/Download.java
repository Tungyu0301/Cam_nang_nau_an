package com.duan2.camnangamthuc.camnangamthuc.Model;

import java.io.Serializable;

public class Download  implements Serializable {
    private String id;
    private String Name;
    private String Image;
    private String ImageView;
    private String Infomation;
    private String InfomationView;
    private String Happy;

    public Download() {
    }

    public Download(String id, String name, String image,String imageView, String infomation,String infomationView, String happy) {
        this.id = id;
        Name = name;
        Image = image;
        ImageView = imageView;
        Infomation = infomation;
        InfomationView = infomationView;
        Happy = happy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getInfomation() {
        return Infomation;
    }

    public void setInfomation(String infomation) {
        Infomation = infomation;
    }

    public String getInfomationView() {
        return InfomationView;
    }

    public void setInfomationView(String infomationView) {
        InfomationView = infomationView;
    }

    public String getImageView() {
        return ImageView;
    }

    public void setImageView(String imageView) {
        ImageView = imageView;
    }

    public String getHappy() {
        return Happy;
    }

    public void setHappy(String happy) {
        Happy = happy;
    }

}
