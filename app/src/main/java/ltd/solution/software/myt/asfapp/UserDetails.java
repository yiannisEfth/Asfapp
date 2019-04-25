package ltd.solution.software.myt.asfapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDetails extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersReference = database.getReference("Users");
    User user;
    String name,userId,userStatus;
    TextView fullName,textID,email,phone,status;
    Button activeBtn,deleteBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundleObject = getIntent().getExtras();
        user =(User) bundleObject.getSerializable("user");
        name = user.getName()+" "+ user.getSurname();
        userId= Integer.toString(user.getId());
        if(user.isActive){
            userStatus="Active";
        }else{
            userStatus="Inactive";
        }

        fullName=(TextView)findViewById(R.id.fullName);
        textID=(TextView)findViewById(R.id.textID);
        email=(TextView)findViewById(R.id.email);
        phone=(TextView)findViewById(R.id.phone);
        status=(TextView)findViewById(R.id.status);

        fullName.setText(name);
        textID.setText(userId);
        if(user.getEmail()==null){
            email.setText("Email not available");
        }
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        status.setText(userStatus);
        activeBtn=(Button)findViewById(R.id.activeBtn);
        deleteBtn=(Button)findViewById(R.id.deleteBtn);

        activeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUserActiveInactive(user);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser(user);
            }
        });
    }


    public void changeUserActiveInactive(final User user) {
        if (user.isActive) {
            AlertDialog.Builder builder = new AlertDialog.Builder(UserDetails.this);
            builder.setTitle("Mark This Client As Inactive?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    usersReference.child(String.valueOf(user.getId())).child("isActive").setValue(false);
                    Toast.makeText(UserDetails.this, "Client " + user.getName() + " " + user.getSurname() + " Is Now Inactive.", Toast.LENGTH_LONG).show();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(UserDetails.this);
            builder.setTitle("Mark This Client As Active?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    usersReference.child(String.valueOf(user.getId())).child("isActive").setValue(true);
                    Toast.makeText(UserDetails.this, "Client " + user.getName() + " " + user.getSurname() + " Is Now Active.", Toast.LENGTH_LONG).show();
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

    public void deleteUser(final User user){
        AlertDialog.Builder builder = new AlertDialog.Builder(UserDetails.this);
        builder.setTitle("Are you sure you want to delete this Client?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                usersReference.child(String.valueOf(user.getId())).removeValue();
                Toast.makeText(UserDetails.this, "Client " + user.getName() + " " + user.getSurname() + " deleted.", Toast.LENGTH_LONG).show();
                finish();
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
