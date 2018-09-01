package ltd.solutions.software.myt.asfapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ClassesFragment extends Fragment {

    private RecyclerView classList;
    private RecyclerView.Adapter listAdapter;
    private RecyclerView.LayoutManager listloManager;
    private String[] classes = new String[2];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.classes_fragment, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        classes[0] = "test1";
        classes[1] = "test2";
        classList = (RecyclerView) getView().findViewById(R.id.classView);
        classList.hasFixedSize();
        listloManager = new LinearLayoutManager(this.getContext());
        classList.setLayoutManager(listloManager);
        listAdapter = new ClassesAdapter(classes);
        classList.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner dateSpinner = (Spinner) getView().findViewById(R.id.datespinner);
        //to create a smaller pop up window for spinner
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(dateSpinner);
            // Set popupWindow height to 700px
            popupWindow.setHeight(700);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {

        }
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        List<String> dateList = new ArrayList<>();
        dateList.add(date);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //for loop that gets all the dates within 2 weeks
        for (int i = 1; i < 14; i++) {
            //instance to get dates from calendar
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, +i);
            String nextDates = dateFormat.format(calendar.getTime());
            dateList.add(nextDates);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, dateList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(adapter);
    }
}
