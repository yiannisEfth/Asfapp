package ltd.solutions.software.myt.asfapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.MyViewHolder> {

    private List<ClassObject> classList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView className, classDate, classSpots;

        public MyViewHolder(View view) {
            super(view);
            className = (TextView) view.findViewById(R.id.class_name);
            classDate = view.findViewById(R.id.class_date);
            classSpots = view.findViewById(R.id.class_spots);
        }
    }

    public ClassesAdapter(List<ClassObject> classList) {
        this.classList = classList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.classes_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ClassObject classObject = classList.get(position);
        holder.className.setText(classObject.getClassName());
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(classObject.getClassDate());
        holder.classDate.setText(formattedDate);
        holder.classSpots.setText(Integer.toString(classObject.getAvailablePlaces()));
    }


    @Override
    public int getItemCount() {
        return classList.size();
    }
}
