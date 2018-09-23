package ltd.solutions.software.myt.asfapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import java.util.Comparator;
import java.util.List;

public class ClientView_Activity extends AppCompatActivity {

    private RecyclerView usersRecycler;
    private List<User> userList = new ArrayList<>();
    private UserAdapter userAdapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersReference = database.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        userAdapter = new UserAdapter(userList, new OnUserAdapterClickListener() {
            @Override
            public void onItemClicked(User user) {
                changeUserActiveInactive(user);
            }
        });
        usersRecycler = (RecyclerView) findViewById(R.id.clientList);
        usersRecycler.setLayoutManager(mLayoutManager);
        usersRecycler.setItemAnimator(new DefaultItemAnimator());
        usersRecycler.setAdapter(userAdapter);
        loadFromFB();
    }

    public void changeUserActiveInactive(final User user) {
        if (user.isActive) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ClientView_Activity.this);
            builder.setTitle("Mark This Client As Inactive?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    userList.clear();
                    usersReference.child(String.valueOf(user.getId())).child("isActive").setValue(false);
                    Toast.makeText(ClientView_Activity.this, "Client " + user.getName() + " " + user.getSurname() + " Is Now Inactive.", Toast.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ClientView_Activity.this);
            builder.setTitle("Mark This Client As Active?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    userList.clear();
                    usersReference.child(String.valueOf(user.getId())).child("isActive").setValue(true);
                    Toast.makeText(ClientView_Activity.this, "Client " + user.getName() + " " + user.getSurname() + " Is Now Active.", Toast.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.show();
        }
    }

    public void loadFromFB() {
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    User dbClass = child.getValue(User.class);
                    userList.add(dbClass);
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
                            return Boolean.compare(u2.isActive, u1.isActive);
                        }
                    });
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
