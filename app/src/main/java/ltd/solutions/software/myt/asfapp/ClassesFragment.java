package ltd.solutions.software.myt.asfapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class ClassesFragment extends Fragment {

    private RecyclerView classListView;
    private List<ClassObject> classesList = new ArrayList<>();
    private List<ClassObject> dummyList = new ArrayList<>();
    private List<ClassObject> allClasses = new ArrayList<>();
    private ClassesAdapter classesAdapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference classesReference = database.getReference("Classes");
    private DatabaseReference usersReference = database.getReference("Users");
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener date;
    private String datePicked;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private boolean isAttended = false;
    private boolean isUserActive;
    private boolean finalAlreadyAttending;
    private ClassObject classObject;
    private List<Integer> attending = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.classes_fragment, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        sharedPref = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        classListView = (RecyclerView) getView().findViewById(R.id.classView);
        classesAdapter = new ClassesAdapter(classesList, new OnClassAdapterClickListener() {
            @Override
            public void onItemClicked(ClassObject classObject) {
                setupClassBooking(classObject);
            }
        });
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
        final ClassComparator comparator = new ClassComparator();
        calendar = Calendar.getInstance();
        //making the datepicked variable the same as the date picked from the calendar
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String formatDate = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.US);
                datePicked = sdf.format(calendar.getTime());
                classesList.clear();
                for (ClassObject co : dummyList) {
                    if (datePicked.equals(co.getClassDate())) {
                        classesList.add(co);
                    }
                }

                Collections.sort(classesList, comparator);
                classesAdapter.notifyDataSetChanged();
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

    public void setupClassBooking(ClassObject fetchedClass) {
        final ClassObject desiredClass = fetchedClass;
        classObject = fetchedClass;
        boolean alreadyAttending = false;
        isAttended = false;
        final List<Integer> currentUsers = checkIfUserExists();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Please Enter Your ID To Book A Class");

        final EditText input = new EditText(getContext());
        if (sharedPref.getString("id", null) != null) {
            input.setText(sharedPref.getString("id", null));
            alreadyAttending = checkAttendance(fetchedClass.getID());
        }
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        finalAlreadyAttending = alreadyAttending;
        builder.setPositiveButton("Book", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkIfUserIsActive(input.getText().toString());
                editor.putString("id", input.getText().toString());
                editor.commit();
                if (!Pattern.matches("\\b([1-8][0-9]{5}|9[0-8][0-9]{4}|99[0-8][0-9]{3}|999[0-8][0-9]{2}|9999[0-8][0-9]|99999[0-9])\\b", input.getText().toString())) {
                    Toast.makeText(getContext(), "Invalid input. ID is a 6-digit number", Toast.LENGTH_LONG).show();
                } else if (desiredClass.getAvailablePlaces() == 0) {
                    Toast.makeText(getContext(), "This Class Is Fully Booked", Toast.LENGTH_LONG).show();
                } else if (finalAlreadyAttending) {
                    Toast.makeText(getContext(), "You Are Already Booked For this Class", Toast.LENGTH_LONG).show();

                } else if (checkAttendance(desiredClass.getID())) {
                    Toast.makeText(getContext(), "You Are Already Booked For this Class", Toast.LENGTH_LONG).show();
                } else if (!currentUsers.contains(Integer.parseInt(input.getText().toString()))) {
                    Toast.makeText(getContext(), "User Does Not Exist", Toast.LENGTH_LONG).show();
                } else if (!isUserActive) {
                    Toast.makeText(getContext(), "You Are Not Active. Please Contact The Gym", Toast.LENGTH_LONG).show();
                } else {
                    usersReference.child(input.getText().toString()).child("Attending").child(String.valueOf(desiredClass.getID())).setValue(desiredClass.getClassName());
                    classesReference.child(String.valueOf(desiredClass.getID())).child("availablePlaces").setValue(desiredClass.getAvailablePlaces() - 1);
                    classesReference.child(String.valueOf(desiredClass.getID())).child("reservedPlaces").setValue(desiredClass.getReservedPlaces() + 1);
                    desiredClass.setAvailablePlaces(desiredClass.getAvailablePlaces() - 1);
                    classesAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Booking Successful!", Toast.LENGTH_LONG).show();


                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());

                    builder2.setTitle("Book for 1 month?");
                    builder2.setMessage("Would you like to book the specific class on the same day for a month if it is available?");

                    builder2.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                            String currentDate = classObject.getClassDate();
                            try {
                                Date date = format.parse(currentDate);
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);
                                calendar.add(Calendar.DATE, 7);
                                currentDate = format.format(calendar.getTime());
                                classObject.setClassDate(currentDate);
                                int j = 0;
                                for (ClassObject classObject3 : dummyList) {
                                    finalAlreadyAttending = checkAttendance(classObject3.getID());
                                    if (classObject3.getClassDate().equals(classObject.getClassDate()) &&
                                            classObject3.getClassName().equals(classObject.getClassName())) {
                                        if (classObject3.getAvailablePlaces() == 0) {
                                            Toast.makeText(getContext(), "This Class Is Fully Booked", Toast.LENGTH_LONG).show();
                                        } else if (finalAlreadyAttending) {
                                            Toast.makeText(getContext(), "You Are Already Booked For this Class", Toast.LENGTH_LONG).show();

                                        } else if (checkAttendance(classObject3.getID())) {
                                            Toast.makeText(getContext(), "You Are Already Booked For this Class", Toast.LENGTH_LONG).show();
                                        } else {
                                            usersReference.child(input.getText().toString()).child("Attending").child(String.valueOf(classObject3.getID())).setValue(classObject.getClassName());
                                            classesReference.child(String.valueOf(classObject3.getID())).child("availablePlaces").setValue(classObject3.getAvailablePlaces() - 1);
                                            classesReference.child(String.valueOf(classObject3.getID())).child("reservedPlaces").setValue(classObject3.getReservedPlaces() + 1);
                                            classObject3.setAvailablePlaces(classObject3.getAvailablePlaces() - 1);
                                            classesAdapter.notifyDataSetChanged();
                                            Toast.makeText(getContext(), "Booking Successful at " + classObject3.getClassDate(), Toast.LENGTH_LONG).show();
                                            calendar.add(Calendar.DATE, 7);
                                            currentDate = format.format(calendar.getTime());
                                            classObject.setClassDate(currentDate);
                                        }
                                    }
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            dialog.dismiss();
                        }
                    });

                    builder2.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder2.create();
                    alert.show();

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

    public boolean checkAttendance(final int id) {
        Log.i("ID ENTERED", String.valueOf(id));
        attending = new ArrayList<>();
        usersReference.child(sharedPref.getString("id", null)).child("Attending").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    attending.add(Integer.parseInt(child.getKey()));
                    Log.i("ATTENDING", String.valueOf(child.getKey()) + "SIZE: " + String.valueOf(attending.size()));
                    if (id == Integer.parseInt(child.getKey())) {
                        isAttended = true;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return isAttended;
    }

    public List<Integer> checkIfUserExists() {
        final List<Integer> userList = new ArrayList<>();
        usersReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    userList.add(Integer.parseInt(child.getKey()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return userList;
    }

    public boolean checkIfUserIsActive(final String id) {
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isUserActive = dataSnapshot.child(id).child("isActive").getValue(Boolean.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return isUserActive;
    }
}
