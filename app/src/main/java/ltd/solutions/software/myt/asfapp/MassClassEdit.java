package ltd.solutions.software.myt.asfapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MassClassEdit extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText className;
    private Spinner daySpinner, capacitySpinner, hourSpinner, minuteSpinner;
    private int selectedDay;
    private int selectedCapacity;
    private int selectHours;
    private int selectedMinute;
    private Button massChangeBtn;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference classesReference = database.getReference("Classes");
    private List<ClassObject> currentClasses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mass_class_edit);
        className = (EditText) findViewById(R.id.mass_edit_name);
        daySpinner = (Spinner) findViewById(R.id.mass_edit_spinner);
        capacitySpinner = (Spinner) findViewById(R.id.mass_edit_spinner_capacity);
        minuteSpinner = (Spinner) findViewById(R.id.mass_edit_minutes);
        hourSpinner = (Spinner) findViewById(R.id.mass_edit_hours);
        massChangeBtn = (Button) findViewById(R.id.mass_edit_btn);
        fetchClassIds();
        setupSpinners();
        setupBtn();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedDay = i+2;
        selectedCapacity = Integer.parseInt(capacitySpinner.getSelectedItem().toString());
        selectHours = Integer.parseInt(hourSpinner.getSelectedItem().toString());
        selectedMinute = Integer.parseInt(minuteSpinner.getSelectedItem().toString());
        Log.i("SELECTED DAY", String.valueOf(selectedDay));
        Log.i("SELECTED CAPACITY", String.valueOf(selectedCapacity));
        Log.i("SELECTED MINUTES", String.valueOf(selectedMinute));
        Log.i("SELECTED HOURS", String.valueOf(selectHours));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void setupSpinners() {
        daySpinner.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<>();
        categories.add("Monday");
        categories.add("Tuesday");
        categories.add("Wednesday");
        categories.add("Thursday");
        categories.add("Friday");
        categories.add("Saturday");
        categories.add("Sunday");
        List<Integer> capacityList = new ArrayList<>();
        List<Integer> hours = new ArrayList<>();
        for (int i = 0; i <= 24; i++) {
            capacityList.add(i);
            hours.add(i);
        }
        List<Integer> minutes = new ArrayList<>();
        for (int i = 0; i <= 11; i++) {
            minutes.add(i * 5);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<Integer> capacityAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, capacityList);
        ArrayAdapter<Integer> minutesAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, minutes);
        ArrayAdapter<Integer> hoursAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, hours);
        minutesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hoursAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        capacityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dataAdapter);
        capacitySpinner.setAdapter(capacityAdapter);
        minuteSpinner.setAdapter(minutesAdapter);
        hourSpinner.setAdapter(hoursAdapter);
    }

    public void fetchClassIds() {
        classesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentClasses.clear();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    ClassObject dbClass = child.getValue(ClassObject.class);
                    currentClasses.add(dbClass);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean checkNulls() {
        return daySpinner.getSelectedItem().toString().isEmpty() || capacitySpinner.getSelectedItem().toString().isEmpty()
                || minuteSpinner.getSelectedItem().toString().isEmpty() || hourSpinner.getSelectedItem().toString().isEmpty() || className.getText().toString().isEmpty();
    }

    private boolean checkIfClassExists() {
        boolean output = false;
        for (ClassObject aClass : currentClasses) {
            if (aClass.getClassName().equals(className.getText().toString())) {
                output = true;
            }
        }
        return output;
    }

    private void setupBtn() {
        massChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                massChange();
            }
        });
    }

    private void massChange() {
        if (checkNulls()) {
            Toast.makeText(this, "Error:Null fields!", Toast.LENGTH_SHORT).show();
        } else if (!checkIfClassExists()) {
            Toast.makeText(this, "Error:Class does not exist!", Toast.LENGTH_SHORT).show();
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            Calendar calendar2 = Calendar.getInstance();
            while (calendar2.get(Calendar.DAY_OF_WEEK) != selectedDay) {
                calendar2.add(Calendar.DATE, 1);
            }
            List<String> dates = new ArrayList<>();
            for (int i = 0; i < 14; i++) {
                String dateAdd = dateFormat.format(calendar2.getTime());
                dates.add(dateAdd);
                calendar2.add(Calendar.DATE, 7);
            }
            for (ClassObject aClass : currentClasses) {
                if (dates.contains(aClass.getClassDate()) && aClass.getClassName().equals(className.getText().toString())) {
                    classesReference.child(String.valueOf(aClass.getID())).child("availablePlaces").setValue(selectedCapacity);
                    classesReference.child(String.valueOf(aClass.getID())).child("time").setValue(selectHours + ":" + selectedMinute);
                }
            }
            Toast.makeText(this, className.getText().toString() + " classes on " + daySpinner.getSelectedItem().toString() + " successfully mass changed", Toast.LENGTH_SHORT).show();
        }
    }
}
