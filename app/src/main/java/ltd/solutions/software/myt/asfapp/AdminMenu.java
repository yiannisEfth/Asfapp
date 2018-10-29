package ltd.solutions.software.myt.asfapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class AdminMenu extends AppCompatActivity {
    Button register, viewClasses, viewClients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_menu_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        register = (Button) findViewById(R.id.register_btn);
        viewClasses = (Button) findViewById(R.id.classes_btn);
        viewClients = (Button) findViewById(R.id.clients_btn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerView = new Intent(AdminMenu.this, RegisterUserActivity.class);
                startActivity(registerView);
            }
        });

        viewClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent classes = new Intent(AdminMenu.this, ClassActivity.class);
                startActivity(classes);
            }
        });

        viewClients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clients = new Intent(AdminMenu.this, ClientView_Activity.class);
                startActivity(clients);
            }
        });
    }

}
