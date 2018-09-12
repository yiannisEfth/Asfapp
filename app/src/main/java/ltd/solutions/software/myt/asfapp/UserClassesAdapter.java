package ltd.solutions.software.myt.asfapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UserClassesAdapter extends RecyclerView.Adapter<UserClassesAdapter.MyViewHolder> {
    private List<ClassObject> classList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView className, classDate, classSpots, classTime;

        public MyViewHolder(View view) {
            super(view);
            className = (TextView) view.findViewById(R.id.class_name);
            classDate = view.findViewById(R.id.class_date);
            classSpots = view.findViewById(R.id.class_spots);
            classTime = view.findViewById(R.id.class_time);
        }
    }

    public UserClassesAdapter(List<ClassObject> classList) {
        this.classList = classList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.classes_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserClassesAdapter.MyViewHolder holder, final int position) {
        ClassObject classObject = classList.get(position);
        holder.className.setText(classObject.getClassName());
        holder.classDate.setText(classObject.getClassDate());
        holder.classSpots.setText(Integer.toString(classObject.getAvailablePlaces()));
        holder.classTime.setText(classObject.getTime());

    }


    @Override
    public int getItemCount() {
        return classList.size();
    }
}
