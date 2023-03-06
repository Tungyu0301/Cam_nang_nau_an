package com.duan2.camnangamthuc.camnangamthuc.Model;

import java.io.Serializable;

public class ListShoping  implements Serializable {
    private String id;
    private String Title;
    private String Content;

    public ListShoping() {
    }

    public ListShoping(String id, String title, String content) {
        this.id = id;
        Title = title;
        Content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
