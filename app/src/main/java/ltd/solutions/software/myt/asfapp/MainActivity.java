package ltd.solutions.software.myt.asfapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new MainFragment());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setSelectedItemId(R.id.navigation_home);
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
                        if (input.getText().toString().equals("admin")) {
                            Intent intent = new Intent(getApplicationContext(), AdminMenu.class);
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
}

