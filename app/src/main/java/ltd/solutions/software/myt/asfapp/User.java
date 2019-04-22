package ltd.solutions.software.myt.asfapp;

import java.io.Serializable;

public class User implements Serializable {
    public String name, surname, email, phone;
    public int id;
    public boolean isActive;

    public User() {

    }

    public User(String name, String surname, String email, String phone, int id, boolean status) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.id = id;
        this.isActive = status;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public int getId() {
        return id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setId(int id) {
        this.id = id;
    }
}
