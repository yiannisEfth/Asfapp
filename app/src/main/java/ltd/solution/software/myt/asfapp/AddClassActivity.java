package ltd.solution.software.myt.asfapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.Random;
import java.util.regex.Pattern;

public class AddClassActivity extends AppCompatActivity {

    private CheckBox chkMonday, chkTuesday, chkWendseday, chkThursday, chkFriday, chkSaturday, chkSunday, chk1Month, chk3Months, chkSingleClass;
    private TextView nameText, capacityText, hoursText, minutesText;
    private ImageView nameEdit, capacityEdit;
    private Button btnAdd;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference classesReference = database.getReference("Classes");
    private String time;
    private List<ClassObject> currentClasses = new ArrayList<>();
    private boolean classAlreadyExists;
    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;
    private String datePicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        fetchClassIds();
        chkMonday = findViewById(R.id.chkMonday);
        chkTuesday = findViewById(R.id.chkTuesday);
        chkWendseday = findViewById(R.id.chkWendesday);
        chkThursday = findViewById(R.id.chkThursday);
        chkFriday = findViewById(R.id.chkFriday);
        chkSaturday = findViewById(R.id.chkSaturday);
        chkSunday = findViewById(R.id.chkSunday);
        chk1Month = findViewById(R.id.chkOneMonth);
        chk3Months = findViewById(R.id.chkThreeMonths);
        chkSingleClass = findViewById(R.id.chkSingleClass);
        nameText = findViewById(R.id.class_name_input);
        capacityText = findViewById(R.id.class_capacity_input);
        hoursText = findViewById(R.id.hours);
        minutesText = findViewById(R.id.minutes);
        nameEdit = findViewById(R.id.class_name_edit);
        capacityEdit = findViewById(R.id.class_capacity_edit);
        btnAdd = findViewById(R.id.add_btn);
        nameSetup();
        capacitySetup();
        btnListener();


    }

    public void checkBoxListeners(View view) {
        Boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {

            case R.id.chkOneMonth:
                chk3Months.setChecked(false);
                chkSingleClass.setChecked(false);
                chkSingleClass.setText(R.string.singleClass);
                chkMonday.setEnabled(true);
                chkTuesday.setEnabled(true);
                chkWendseday.setEnabled(true);
                chkThursday.setEnabled(true);
                chkFriday.setEnabled(true);
                chkSaturday.setEnabled(true);
                chkSunday.setEnabled(true);
                break;
            case R.id.chkThreeMonths:
                chk1Month.setChecked(false);
                chkSingleClass.setChecked(false);
                chkSingleClass.setText(R.string.singleClass);
                chkMonday.setEnabled(true);
                chkTuesday.setEnabled(true);
                chkWendseday.setEnabled(true);
                chkThursday.setEnabled(true);
                chkFriday.setEnabled(true);
                chkSaturday.setEnabled(true);
                chkSunday.setEnabled(true);
                break;
            case R.id.chkSingleClass:
                chk1Month.setChecked(false);
                chk3Months.setChecked(false);
                chkMonday.setEnabled(false);
                chkTuesday.setEnabled(false);
                chkWendseday.setEnabled(false);
                chkThursday.setEnabled(false);
                chkFriday.setEnabled(false);
                chkSaturday.setEnabled(false);
                chkSunday.setEnabled(false);

                date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String formatDate = "dd/MM/yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.US);
                        datePicked = sdf.format(calendar.getTime());
                        chkSingleClass.setText(datePicked);
                    }
                };

                new DatePickerDialog(AddClassActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();


                break;
            default:
                break;
        }

    }

    public void nameSetup() {
        nameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddClassActivity.this);
                builder.setTitle("Please Enter Your Name");

                final EditText input = new EditText(AddClassActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            nameText.setText(input.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    public void capacitySetup() {
        capacityEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddClassActivity.this);
                builder.setTitle("Please Enter the class' capacity");

                final EditText input = new EditText(AddClassActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Pattern.matches("[0-9]{0,4}", input.getText().toString())) {
                            capacityText.setText(input.getText().toString());
                        } else {
                            Toast.makeText(AddClassActivity.this, "Invalid input. Capacity must only contain numbers", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }


    private void btnListener() {
        btnAdd.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                //Checking for 1 month
                if (nameText.getText().toString().isEmpty() ||
                        capacityText.getText().toString().isEmpty() || hoursText.getText().toString().isEmpty()
                        || minutesText.getText().toString().isEmpty()) {
                    Toast.makeText(AddClassActivity.this, "Class Creation Failed. Please Fill Out All Necessary Fields", Toast.LENGTH_LONG).show();
                } else {
                    if (chkMonday.isChecked() && chk1Month.isChecked()) {
                        dayCheckedOneMonth(2);
                    }
                    if (chkTuesday.isChecked() && chk1Month.isChecked()) {
                        dayCheckedOneMonth(3);
                    }
                    if (chkWendseday.isChecked() && chk1Month.isChecked()) {
                        dayCheckedOneMonth(4);
                    }
                    if (chkThursday.isChecked() && chk1Month.isChecked()) {
                        dayCheckedOneMonth(5);
                    }
                    if (chkFriday.isChecked() && chk1Month.isChecked()) {
                        dayCheckedOneMonth(6);
                    }
                    if (chkSaturday.isChecked() && chk1Month.isChecked()) {
                        dayCheckedOneMonth(7);
                    }
                    if (chkSunday.isChecked() && chk1Month.isChecked()) {
                        dayCheckedOneMonth(1);
                    }
                    //Checking for 3 months
                    if (chkSunday.isChecked() && chk3Months.isChecked()) {
                        dayCheckedThreeMonth(1);
                    }
                    if (chkMonday.isChecked() && chk3Months.isChecked()) {
                        dayCheckedThreeMonth(2);
                    }
                    if (chkTuesday.isChecked() && chk3Months.isChecked()) {
                        dayCheckedThreeMonth(3);
                    }
                    if (chkWendseday.isChecked() && chk3Months.isChecked()) {
                        dayCheckedThreeMonth(4);
                    }
                    if (chkThursday.isChecked() && chk3Months.isChecked()) {
                        dayCheckedThreeMonth(5);
                    }
                    if (chkFriday.isChecked() && chk3Months.isChecked()) {
                        dayCheckedThreeMonth(6);
                    }
                    if (chkSaturday.isChecked() && chk3Months.isChecked()) {
                        dayCheckedThreeMonth(7);
                    }

                    //check for single events
                    if (chkSingleClass.isChecked()) {
                        dayCheckedSingleClass();
                    }

                    final AlertDialog.Builder builder = new AlertDialog.Builder(AddClassActivity.this);
                    builder.setTitle("Success");
                    builder.setMessage("Successful addition of " + nameText.getText().toString());
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(AddClassActivity.this, AdminMenu.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.show();

                }
            }
        });
    }

    private void dayCheckedOneMonth(int day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Calendar calendar1 = Calendar.getInstance();
        while (calendar1.get(Calendar.DAY_OF_WEEK) != day) {
            calendar1.add(Calendar.DATE, 1);
        }
        Random rdm = new Random();
        for (int i = 0; i < 4; i++) {
            int classID = rdm.nextInt(999999);
            if (checkClassExistance(classID)) {
                classID = rdm.nextInt(999999);
            }
            String date = dateFormat.format(calendar1.getTime());
            DatabaseReference newClass = FirebaseDatabase.getInstance().getReference();
            Class classes = new Class(nameText.getText().toString(), Integer.parseInt(capacityText.getText().toString()), date, hoursText.getText().toString(), minutesText.getText().toString());
            newClass.child("Classes").child(String.valueOf(classID)).setValue(classes);
            newClass.child("Classes").child(String.valueOf(classID)).child("id").setValue(classID);
            calendar1.add(Calendar.DATE, 7);
        }


    }

    private void dayCheckedThreeMonth(int day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Calendar calendar2 = Calendar.getInstance();
        while (calendar2.get(Calendar.DAY_OF_WEEK) != day) {
            calendar2.add(Calendar.DATE, 1);
        }
        Random rdm = new Random();
        for (int i = 0; i < 12; i++) {
            int classID = rdm.nextInt(999999);
            if (checkClassExistance(classID)) {
                classID = rdm.nextInt(999999);
            }
            String date = dateFormat.format(calendar2.getTime());
            DatabaseReference newClass = FirebaseDatabase.getInstance().getReference();
            Class classes = new Class(nameText.getText().toString(), Integer.parseInt(capacityText.getText().toString()), date, hoursText.getText().toString(), minutesText.getText().toString());
            newClass.child("Classes").child(String.valueOf(classID)).setValue(classes);
            newClass.child("Classes").child(String.valueOf(classID)).child("id").setValue(classID);
            calendar2.add(Calendar.DATE, 7);
        }


    }

    private void dayCheckedSingleClass() {
        Random rdm = new Random();
        int classID = rdm.nextInt(999999);
        if (checkClassExistance(classID)) {
            classID = rdm.nextInt(999999);
        }
        String date = chkSingleClass.getText().toString();
        DatabaseReference newClass = FirebaseDatabase.getInstance().getReference();
        Class classes = new Class(nameText.getText().toString(), Integer.parseInt(capacityText.getText().toString()), date, hoursText.getText().toString(), minutesText.getText().toString());
        newClass.child("Classes").child(String.valueOf(classID)).setValue(classes);
        newClass.child("Classes").child(String.valueOf(classID)).child("id").setValue(classID);
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

    public boolean checkClassExistance(int id) {
        classAlreadyExists = false;
        for (ClassObject c : currentClasses) {
            if (c.getID() == id) {
                classAlreadyExists = true;
            }
        }
        return classAlreadyExists;
    }
}
