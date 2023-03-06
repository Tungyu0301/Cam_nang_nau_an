package com.duan2.camnangamthuc.camnangamthuc.Model;

import java.io.Serializable;

public class Users implements Serializable {
    private String id,code,name,image,phone,email,password,role,ngaysinh,diachi;

    public Users() {
    }

    public Users(String id, String code, String name, String image, String phone, String email, String password, String role, String ngaysinh, String diachi) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.image = image;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.role = role;
        this.ngaysinh = ngaysinh;
        this.diachi = diachi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(String ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }
}
