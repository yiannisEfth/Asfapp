package ltd.solutions.software.myt.asfapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

public class ClassObject {

    private String className;
    private int availablePlaces;
    private int reservedPlaces;
    private Date classDate;

    public ClassObject(String className, int avaiablePlaces, int reservedPlaces, Date classDate) {
        this.className = className;
        this.availablePlaces = avaiablePlaces;
        this.reservedPlaces = reservedPlaces;
        this.classDate = classDate;
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

    public Date getClassDate() {
        return classDate;
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

    public void setClassDate(Date classDate) {
        this.classDate = classDate;
    }
}
