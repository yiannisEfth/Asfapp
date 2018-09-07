package ltd.solutions.software.myt.asfapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class UserClassesFragment extends Fragment {

    private RecyclerView userClassesView;
    private List<ClassObject> userClassesList = new ArrayList<>();
    private ClassesAdapter classesAdapter;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    public void onStart() {
        super.onStart();
        userClassesView = (RecyclerView) getView().findViewById(R.id.class_recycler_view);
        classesAdapter = new ClassesAdapter(userClassesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        userClassesView.setLayoutManager(mLayoutManager);
        userClassesView.setItemAnimator(new DefaultItemAnimator());
        userClassesView.setAdapter(classesAdapter);
        sharedPref = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_classes_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
