package ltd.solutions.software.myt.asfapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class RegisterUserActivity extends AppCompatActivity {
    private TextView nameText, surnameText, emailText, phoneText;
    private ImageView nameEdit, surnameEdit , emailEdit, phoneEdit;
    private Button registerBtn;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference users = database.getReference("Users");
    private ArrayList<Integer> idList = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        //Variables for the inputs
        nameText = findViewById(R.id.register_name_input);
        surnameText = findViewById(R.id.register_surname_input);
        emailText = findViewById(R.id.register_email_input);
        phoneText = findViewById(R.id.register_phone_input);
        registerBtn = findViewById(R.id.register_btn);
        //Variables for the edit button
        nameEdit = findViewById(R.id.profile_name_edit);
        surnameEdit = findViewById(R.id.register_surname_edit);
        emailEdit = findViewById(R.id.register_email_edit);
        phoneEdit = findViewById(R.id.register_phone_edit);
        //initialising the listeners
        nameSetup();
        surnameSetup();
        emailSetup();
        phoneSetup();
        buttonListener();

    }

    public void nameSetup() {
        nameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
                builder.setTitle("Please Enter Your Name");

                final EditText input = new EditText(RegisterUserActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Pattern.matches("[A-z]{0,20}", input.getText().toString())) {
                            nameText.setText(input.getText().toString());
                        } else {
                            Toast.makeText(RegisterUserActivity.this, "Invalid input. Name can only contain letters and no spaces.", Toast.LENGTH_LONG).show();
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

    public void surnameSetup() {
        surnameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
                builder.setTitle("Please Enter Your Name");

                final EditText input = new EditText(RegisterUserActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Pattern.matches("[A-z]{0,20}", input.getText().toString())) {
                            surnameText.setText(input.getText().toString());
                        } else {
                            Toast.makeText(RegisterUserActivity.this, "Invalid input. Surname can only contain letters and no spaces.", Toast.LENGTH_LONG).show();
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

    public void emailSetup() {
        emailEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
                builder.setTitle("Please Enter Your Email");

                final EditText input = new EditText(RegisterUserActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //No regex for email

                            emailText.setText(input.getText().toString());

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

    public void phoneSetup() {
        phoneEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
                builder.setTitle("Please Enter Your Phone Number");

                final EditText input = new EditText(RegisterUserActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Regex for heights between 80 and 270 cm.
                        if (Pattern.matches("([0-9]{8})", input.getText().toString())) {
                            phoneText.setText(input.getText().toString());

                        } else {
                            Toast.makeText(RegisterUserActivity.this, "Invalid Phone Number", Toast.LENGTH_LONG).show();
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

    public void addingToDatabase(){
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot user : dataSnapshot.getChildren()){
                    idList.add(Integer.parseInt(user.getKey()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void buttonListener(){
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToDatabase();
                Random rdm = new Random();
                int usersID = rdm.nextInt(999999);
                while (recursionForID(usersID) == true) {
                    usersID = rdm.nextInt(999999);
                }
                DatabaseReference newUser = database.getReference().child("Users").child(String.valueOf(usersID)).push();
                User user = new User(nameText.getText().toString(), surnameText.getText().toString(), emailText.getText().toString(), phoneText.getText().toString(), usersID);
                newUser.setValue(user);

                if (recursionForID(usersID)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
                    builder.setTitle("Success");
                    builder.setMessage("Successful registration. Your id is: " +usersID);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(RegisterUserActivity.this, AdminMenu.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.show();
                }
            }
        });
    }

    public boolean recursionForID(int usersID){
        addingToDatabase();
        boolean found = false;
        for(int i : idList){
            if(i == usersID){
                found = true;
            }
        }
        return found;
    }

}
