package ltd.solutions.software.myt.asfapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class GymInfoFragment extends Fragment implements OnMapReadyCallback {
    ImageButton iButton,fbButton,twitterButton;
    GoogleMap mGoogleMap;
    MapView mapView;
    View mView;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       mView= inflater.inflate(R.layout.gyminfo_fragment, null);
       return mView;
    }

    @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        iButton=(ImageButton)getView().findViewById(R.id.img_button2);
        fbButton=(ImageButton)getView().findViewById(R.id.img_button1);
        twitterButton=(ImageButton)getView().findViewById(R.id.img_button3);

        iButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent instaIntent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/asf.performance.cy/"));
                startActivity(instaIntent);
            }
        });

        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fbIntent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/ASF.PERFORMANCE/"));
                startActivity(fbIntent);
            }
        });

        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent twitterIntent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://twitter.com/asf_performance"));
                startActivity(twitterIntent);
            }
        });

        mapView=(MapView)mView.findViewById(R.id.map);
        if (mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position((new LatLng(34.6866451, 33.0254199))).title("Asf & Performance"));

        CameraPosition Asf= CameraPosition.builder().target(new LatLng(34.6866451, 33.0254199)).zoom(16).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Asf));

    }
}

