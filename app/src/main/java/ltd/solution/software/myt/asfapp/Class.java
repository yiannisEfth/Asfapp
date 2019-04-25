package ltd.solution.software.myt.asfapp;

public class Class {
    public String className, classDate, time;
    public int id , reservedPlaces , availablePlaces;

    public Class(String name, int capacity, String date, String hours , String minutes) {
        this.className = name;
        this.availablePlaces = capacity;
        this.classDate = date;
        this.time = hours + ":" +minutes;
        this.reservedPlaces = 0;
        this.id = 123;
    }
}
