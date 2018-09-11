package ltd.solutions.software.myt.asfapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
        Log.i("ACTIVITY", "CREATED");
        classesAdapter = new UserClassesAdapter(userClassesList);
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
        classesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allClassesList.clear();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    ClassObject dbClass = child.getValue(ClassObject.class);
                    allClassesList.add(dbClass);
                }
                Log.i("FETCHED CLASSES", String.valueOf(allClassesList.size()));
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
                Log.i("USER FETCHED CLASSES", String.valueOf(classesFetchedList.size()));

                setUpList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void setUpList() {
        for (ClassObject classO : allClassesList) {
            if (classesFetchedList.contains(Integer.toString(classO.getID()))) {
                userClassesList.add(classO);
            }
        }
        classesAdapter.notifyDataSetChanged();
    }
}

