package ltd.solutions.software.myt.asfapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

public class ClassComparator implements Comparator<ClassObject> {
    DateFormat f = new SimpleDateFormat("dd/MM/yyyy-hh:mm");

    @Override
    public int compare(ClassObject class1, ClassObject class2) {
        String date1 = class1.getClassDate() + "-" + class1.getTime();
        String date2 = class2.getClassDate() + "-" + class2.getTime();

        try {
            return f.parse(date1).compareTo(f.parse(date2));

        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
