package ltd.solutions.software.myt.asfapp;

public class Class {
    public String name , availablePlaces , date , time, booked;


    public Class(String name , String capacity , String date , String time) {
        this.name = name;
        this.availablePlaces = capacity;
        this.date = date;
        this.time = time;
        this.booked = "0";
    }
}
