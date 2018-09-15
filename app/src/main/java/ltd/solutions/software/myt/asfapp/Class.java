package ltd.solutions.software.myt.asfapp;

public class Class {
    public String className, availablePlaces, classDate, time, reservedPlaces;
    public int id;

    public Class(String name, String capacity, String date, String hours , String minutes) {
        this.className = name;
        this.availablePlaces = capacity;
        this.classDate = date;
        this.time = hours + ":" +minutes;
        this.reservedPlaces = "0";
        this.id = 123;
    }
}
