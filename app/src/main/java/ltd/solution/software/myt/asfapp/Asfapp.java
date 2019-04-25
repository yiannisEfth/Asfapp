package ltd.solution.software.myt.asfapp;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class Asfapp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();


        //Firebase use offline
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
