package ltd.solutions.software.myt.asfapp;

import android.app.AlarmManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference classesReference = database.getReference("Classes");
    private Date previousDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Use of Firebase with no Internet Connection
        database.getInstance().setPersistenceEnabled(true);
        classesReference.keepSynced(true);

        //Use of Firebase with no Internet Connection

        //Notifications
         AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
         Calendar calendar = Calendar.getInstance();
         calendar.add(Calendar.SECOND,5);

         Intent intent = new Intent("");
         PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, intent,PendingIntent.FLAG_UPDATE_CURRENT);

         alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),broadcast);
        //Notifications

        loadFragment(new MainFragment());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setSelectedItemId(R.id.navigation_home);
        sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED &&
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
            //we are not connected to a network
            Toast.makeText(this, "Please Connect To The Internet To Use The App", Toast.LENGTH_LONG).show();
            this.finish();
        }
        checkFirstTime();
        deleteOldClasses();



    }

    private boolean loadFragment(android.support.v4.app.Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new MainFragment();
                break;
            case R.id.navigation_classes:
                fragment = new ClassesFragment();
                break;
            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                break;
            case R.id.navigation_info:
                fragment = new GymInfoFragment();
                break;
        }
        return loadFragment(fragment);
    }

    //Adding a comment to check the commits in github
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.admin:
                //   Toast.makeText(this, "Hi", Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Password");
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().toString().equals("wtfchampion3")) {
                            Intent intent = new Intent(MainActivity.this, AdminMenu.class);
                            startActivity(intent);
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
        return true;
    }

    public void checkFirstTime() {
        String firstStart = sharedPref.getString("firstStart", null);
        if (firstStart == null) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Please Enter Your ID");

            final EditText input = new EditText(MainActivity.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (Pattern.matches("\\b([1-8][0-9]{5}|9[0-8][0-9]{4}|99[0-8][0-9]{3}|999[0-8][0-9]{2}|9999[0-8][0-9]|99999[0-9])\\b", input.getText().toString())) {
                        editor.putString("id", input.getText().toString());
                        editor.commit();
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid input. ID is a 6-digit number", Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.setNegativeButton("Skip", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    Toast.makeText(MainActivity.this, "You can save your ID in the Profile tab", Toast.LENGTH_LONG).show();
                }
            });

            builder.show();
        }
        editor.putString("firstStart", "no").commit();
    }

    //Method to delete classes from the databse which are less than a week old
    public void deleteOldClasses() {
        Calendar cal = Calendar.getInstance();
        final SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
        cal.add(Calendar.DAY_OF_YEAR, -7);
        final String date = s.format(new Date(cal.getTimeInMillis()));
        try {
            previousDate = s.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        classesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    ClassObject dbClass = child.getValue(ClassObject.class);
                    try {
                        Date dbDate = s.parse(dbClass.getClassDate());
                        if (dbDate.before(previousDate)) {
                            classesReference.child(String.valueOf(dbClass.getID())).removeValue();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED &&
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
            //we are not connected to a network
            Toast.makeText(this, "Please Connect To The Internet To Use The App", Toast.LENGTH_LONG).show();
            this.finish();
        }
    }




}


