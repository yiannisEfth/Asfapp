package ltd.solutions.software.myt.asfapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class ProfileFragment extends Fragment {

    ImageButton iButton,fbButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        iButton=(ImageButton)getView().findViewById(R.id.img_button1);
        fbButton=(ImageButton)getView().findViewById(R.id.img_button2);

        return inflater.inflate(R.layout.profile_fragment, null);
    }

}
