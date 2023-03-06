package com.duan2.camnangamthuc.camnangamthuc.Model;

public class FoodInfomation {
    private String Name;
    private String Image;
    private String Infomation;
    private String InfomationView;
    private String ImageView;
    private String Happy;
    private String MenuId;

    public FoodInfomation() {
    }

    public FoodInfomation(String name, String image, String infomation, String infomationView, String imageView, String happy, String menuId) {
        Name = name;
        Image = image;
        Infomation = infomation;
        InfomationView = infomationView;
        ImageView = imageView;
        Happy = happy;
        MenuId = menuId;
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

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }
}
