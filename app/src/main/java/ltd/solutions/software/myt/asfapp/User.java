package ltd.solutions.software.myt.asfapp;

public class User {
    public String name , surname , email , phone;
    public int id;
    public boolean isActive;

    public User(String name , String surname , String email , String phone , int id, boolean status) {
        this.name = name;
        this.surname = surname ;
        this.email = email;
        this.phone = phone;
        this.id = id;
        this.isActive = status;
    }

}
