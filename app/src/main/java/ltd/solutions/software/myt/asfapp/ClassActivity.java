package ltd.solutions.software.myt.asfapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ClassActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private RecyclerView classesRecycler;
    private List<ClassObject> classesList = new ArrayList<>();
    private ClassesAdapter classesAdapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference classesReference = database.getReference("Classes");
    private ClassObject targetClass;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener date;
    private String datePicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassActivity.this, AddClassActivity.class);
                startActivity(intent);
                finish();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        classesAdapter = new ClassesAdapter(classesList, new OnClassAdapterClickListener() {
            @Override
            public void onItemClicked(ClassObject classObject) {
                setupMenu(findViewById(R.id.class_name));
                targetClass = classObject;
            }
        });
        classesRecycler = (RecyclerView) findViewById(R.id.admin_recycler_view);
        classesRecycler.setLayoutManager(mLayoutManager);
        classesRecycler.setItemAnimator(new DefaultItemAnimator());
        classesRecycler.setAdapter(classesAdapter);
        registerForContextMenu(classesRecycler);
        loadFromFirebase();
    }

    public void loadFromFirebase() {
        final ClassComparator comparator = new ClassComparator();
        classesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                classesList.clear();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    ClassObject dbClass = child.getValue(ClassObject.class);
                    classesList.add(dbClass);
                    Collections.sort(classesList, comparator);
                    classesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setupMenu(View v) {
        PopupMenu menu = new PopupMenu(this, v);
        menu.setOnMenuItemClickListener(this);
        MenuInflater inflater = menu.getMenuInflater();
        inflater.inflate(R.menu.admin_class_menu, menu.getMenu());
        menu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.admin_class_menu_edit_name:
                editName(targetClass);
                break;

            case R.id.admin_class_menu_edit_date:
                editDate(targetClass);
                break;

            case R.id.admin_class_menu_edit_time:
                editTime(targetClass);
                break;

            case R.id.admin_class_menu_edit_space:
                editSpace(targetClass);
                break;

            case R.id.admin_class_menu_delete:
                deleteClass(targetClass);
                break;

            case R.id.admin_class_menu_view:
                viewClassInfo(targetClass.getID());
                break;

            default:
                break;
        }
        return false;
    }

    public void editName(final ClassObject targetedClass) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ClassActivity.this);
        builder.setTitle("Please Enter The New Class Name");

        final EditText input = new EditText(getApplicationContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                classesReference.child(String.valueOf(targetedClass.getID())).child("className").setValue(input.getText().toString());
                targetedClass.setClassName(input.getText().toString());
                classesAdapter.notifyDataSetChanged();
                Toast.makeText(ClassActivity.this, "Class Name Changed To " + input.getText().toString(), Toast.LENGTH_LONG).show();
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

    public void editDate(final ClassObject targetedClass) {
        calendar = Calendar.getInstance();
        new DatePickerDialog(ClassActivity.this, date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String formatDate = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.US);
                datePicked = sdf.format(calendar.getTime());
                targetedClass.setClassDate(datePicked);
                classesReference.child(String.valueOf(targetedClass.getID())).child("classDate").setValue(datePicked);
                Toast.makeText(ClassActivity.this, "Class Date Changed To " + datePicked, Toast.LENGTH_SHORT).show();
                classesAdapter.notifyDataSetChanged();
            }
        };
    }

    public void editTime(final ClassObject targetedClass) {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ClassActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                targetedClass.setTime(selectedHour + ":" + selectedMinute);
                classesReference.child(String.valueOf(targetedClass.getID())).child("time").setValue(selectedHour + ":" + selectedMinute);
                classesAdapter.notifyDataSetChanged();
                Toast.makeText(ClassActivity.this, "Time Of Class Changed To " + selectedHour + ":" + selectedMinute, Toast.LENGTH_SHORT).show();
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select A New Time For The Class");
        mTimePicker.show();
    }

    public void editSpace(final ClassObject targetedClass) {
        final NumberPicker numberPicker = new NumberPicker(ClassActivity.this);
        numberPicker.setMaxValue(50);
        numberPicker.setMinValue(0);
        AlertDialog.Builder builder = new AlertDialog.Builder(ClassActivity.this);
        builder.setView(numberPicker);
        builder.setTitle("Choose Free Spaces For This Class");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                classesReference.child(String.valueOf(targetedClass.getID())).child("availablePlaces").setValue(numberPicker.getValue());
                targetedClass.setAvailablePlaces(numberPicker.getValue());
                classesAdapter.notifyDataSetChanged();
                Toast.makeText(ClassActivity.this, "Class Available Places Set To " + numberPicker.getValue(), Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    public void deleteClass(final ClassObject targetedClass) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ClassActivity.this);
        builder.setTitle("Are You Sure You Wish To Delete This Class?");
        builder.setMessage("Deleted Classes Cannot Be Recovered");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                classesReference.child(String.valueOf(targetedClass.getID())).removeValue();
                classesAdapter.notifyDataSetChanged();
                Toast.makeText(ClassActivity.this, "Class " + targetedClass.getClassName() + " for " + targetedClass.getClassDate() + " at " + targetedClass.getTime() + " Has Been Successfully Deleted", Toast.LENGTH_LONG).show();
                classesList.remove(targetedClass);
                classesAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    public void viewClassInfo(int selectedClassID) {
        Intent classInfo= new Intent(ClassActivity.this, AdminClassInfo.class);
        classInfo.putExtra("selectedID", selectedClassID);
        startActivity(classInfo);
    }
}
