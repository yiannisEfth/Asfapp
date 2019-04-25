package ltd.solution.software.myt.asfapp;

public class ClassObject {

    private String className;
    private int availablePlaces;
    private int reservedPlaces;
    private String classDate;
    private int ID;
    private String time;

    //default constructor from database
    public ClassObject() {

    }

    public ClassObject(String className, int avaiablePlaces, int reservedPlaces, String classDate, int ID, String time) {
        this.className = className;
        this.availablePlaces = avaiablePlaces;
        this.reservedPlaces = reservedPlaces;
        this.classDate = classDate;
        this.ID = ID;
        this.time = time;

    }

    public String getClassName() {
        return className;
    }

    public int getAvailablePlaces() {
        return availablePlaces;
    }

    public int getReservedPlaces() {
        return reservedPlaces;
    }

    public String getClassDate() {
        return classDate;
    }

    public String getTime() {
        return time;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setAvailablePlaces(int avaiablePlaces) {
        this.availablePlaces = avaiablePlaces;
    }

    public void setReservedPlaces(int reservedPlaces) {
        this.reservedPlaces = reservedPlaces;
    }

    public void setClassDate(String classDate) {
        this.classDate = classDate;
    }
}
