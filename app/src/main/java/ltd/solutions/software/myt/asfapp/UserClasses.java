package ltd.solutions.software.myt.asfapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserClasses extends AppCompatActivity {

    private RecyclerView userClassesView;
    private List<ClassObject> allClassesList = new ArrayList<>();
    private List<ClassObject> userClassesList = new ArrayList<>();
    private List<String> classesFetchedList = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference classesReference = database.getReference("Classes");
    private DatabaseReference usersReference = database.getReference("Users");
    private UserClassesAdapter classesAdapter;
    private SharedPreferences sharedPref;
    private String currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_classes_activity);
        classesAdapter = new UserClassesAdapter(userClassesList, new OnClassAdapterClickListener() {
            @Override
            public void onItemClicked(final ClassObject classObject) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserClasses.this);
                builder.setTitle("Do You Wish To De-list From This Class?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        classesReference.child(String.valueOf(classObject.getID())).child("availablePlaces").setValue(classObject.getAvailablePlaces() + 1);
                        classesReference.child(String.valueOf(classObject.getID())).child("reservedPlaces").setValue(classObject.getReservedPlaces() - 1);
                        usersReference.child(currentUser).child("Attending").child(String.valueOf(classObject.getID())).removeValue();
                        removeCLass(classObject.getID());
                        classesAdapter.notifyDataSetChanged();
                        Toast.makeText(UserClasses.this, "Class Successfully Deleted", Toast.LENGTH_LONG).show();
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
        userClassesView = (RecyclerView) findViewById(R.id.class_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        userClassesView.setLayoutManager(mLayoutManager);
        userClassesView.setItemAnimator(new DefaultItemAnimator());
        userClassesView.setAdapter(classesAdapter);
        sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        currentUser = sharedPref.getString("id", null);
        fetchFromFirebase();
    }

    public void fetchFromFirebase() {
        userClassesList.clear();
        allClassesList.clear();
        classesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userClassesList.clear();
                allClassesList.clear();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    ClassObject dbClass = child.getValue(ClassObject.class);
                    allClassesList.add(dbClass);
                }
                setUpList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        usersReference.child(currentUser).child("Attending").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    classesFetchedList.add(child.getKey());
                }

                setUpList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void setUpList() {
        userClassesList.clear();
        ClassComparator comparator = new ClassComparator();
        classesAdapter.notifyDataSetChanged();
        for (ClassObject classO : allClassesList) {
            if (classesFetchedList.contains(Integer.toString(classO.getID()))) {
                userClassesList.add(classO);
            }
        }
        Collections.sort(userClassesList, comparator);
        classesAdapter.notifyDataSetChanged();
    }

    public void removeCLass(int id) {
        for (int i = 0; i < allClassesList.size(); i++) {
            if (allClassesList.get(i).getID() == id) {
                allClassesList.remove(i);
            }
        }

        for (int j = 0; j < userClassesList.size(); j++) {
            if (userClassesList.get(j).getID() == id) {
                userClassesList.remove(j);
            }
        }

        for (int z = 0; z < classesFetchedList.size(); z++) {
            if (classesFetchedList.get(z).equals(String.valueOf(id))) {
                classesFetchedList.remove(z);
            }
        }
        classesAdapter.notifyDataSetChanged();
    }
}

