package ltd.solutions.software.myt.asfapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class ClassesFragment extends Fragment {

    private RecyclerView classListView;
    private List<ClassObject> classesList = new ArrayList<>();
    private List<ClassObject> dummyList = new ArrayList<>();
    private ClassesAdapter classesAdapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference classesReference = database.getReference("Classes");
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener date;
    private String datePicked;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.classes_fragment, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        classListView = (RecyclerView) getView().findViewById(R.id.classView);
        classesAdapter = new ClassesAdapter(classesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        classListView.setLayoutManager(mLayoutManager);
        classListView.setItemAnimator(new DefaultItemAnimator());
        classListView.setAdapter(classesAdapter);
        setUpFirebase();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button dateBtn = (Button) getView().findViewById(R.id.datespinner);
        calendar = Calendar.getInstance();
        //making the datepicked variable the same as the date picked from the calendar
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String formatDate = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(formatDate , Locale.US);
                datePicked =  sdf.format(calendar.getTime());
            }
        };
        //Show the calendar to pick a date
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(getContext(), date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });
                    for (ClassObject co : dummyList) {
                        if (datePicked.equals(co.getClassDate())) {
                            classesList.add(co);
                        }
                    }
                }


    public void setUpFirebase() {
        classesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dummyList.clear();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    ClassObject dbClass = child.getValue(ClassObject.class);
                    dummyList.add(dbClass);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
