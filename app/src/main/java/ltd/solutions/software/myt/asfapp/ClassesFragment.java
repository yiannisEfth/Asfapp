package ltd.solutions.software.myt.asfapp;

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
        Spinner dateSpinner = (Spinner) getView().findViewById(R.id.datespinner);
        //to create a smaller pop up window for spinner
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(dateSpinner);
            // Set popupWindow height to 700px
            popupWindow.setHeight(700);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {

        }
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        List<String> dateList = new ArrayList<>();
        dateList.add("Select A Date");
        dateList.add(date);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //for loop that gets all the dates within 2 weeks
        for (int i = 1; i < 14; i++) {
            //instance to get dates from calendar
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, +i);
            String nextDates = dateFormat.format(calendar.getTime());
            dateList.add(nextDates);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, dateList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(adapter);

        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!adapterView.getItemAtPosition(i).toString().equals("Select A Date")) {
                    classesList.clear();
                    String date = adapterView.getItemAtPosition(i).toString();
                    for (ClassObject co : dummyList) {
                        if (date.equals(co.getClassDate())) {
                            classesList.add(co);
                        }
                    }
                    classesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
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
