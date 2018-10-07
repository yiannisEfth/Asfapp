package ltd.solutions.software.myt.asfapp;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdminClassInfo extends AppCompatActivity {

    private RecyclerView usersRecycler;
    private int calledClass;
    private UserClassAttendanceAdapter userAdapter;
    private List<User> userList = new ArrayList<>();
    private User targetUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersReference = database.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_class_info);
        Intent intent = getIntent();
        calledClass = intent.getIntExtra("selectedID", 0);
        Log.i("acquiredID", String.valueOf(calledClass));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        userAdapter = new UserClassAttendanceAdapter(userList);
        usersRecycler = findViewById(R.id.class_users_recycler);
        usersRecycler.setLayoutManager(mLayoutManager);
        usersRecycler.setItemAnimator(new DefaultItemAnimator());
        usersRecycler.setAdapter(userAdapter);
        loadFromFB();
    }

    public void loadFromFB() {
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    targetUser = child.getValue(User.class);
                    Log.i("Fetching for", targetUser.getName());
                    fetchUserClasses(targetUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void fetchUserClasses(final User user) {
        Log.i("USER IS", user.getName());
        usersReference.child(String.valueOf(user.getId())).child("Attending").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.i("CalledClassIs", String.valueOf(calledClass));
                    Log.i("User class is", String.valueOf(child.getKey()));
                    if (child.getKey().equals(String.valueOf(calledClass))) {
                        addToList(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addToList(User user) {
        Log.i("ADDING", user.getName());
        userList.add(user);
        Collections.sort(userList, new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                int res = u1.getSurname().compareToIgnoreCase(u2.getSurname());
                if (res != 0) {
                    return res;
                }
                res = u1.getName().compareToIgnoreCase(u2.getName());
                if (res != 0) {
                    return res;
                }
                return Integer.compare(u1.getId(), u2.getId());
            }
        });
        userAdapter.notifyDataSetChanged();
    }
}
