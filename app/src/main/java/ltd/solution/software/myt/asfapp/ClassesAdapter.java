package ltd.solution.software.myt.asfapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.MyViewHolder> {

    private List<ClassObject> classList;
    public OnClassAdapterClickListener onCAL;

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

    public ClassesAdapter(List<ClassObject> classList, OnClassAdapterClickListener onCAL) {
        this.classList = classList;
        this.onCAL = onCAL;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.classes_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ClassObject classObject = classList.get(position);
        holder.className.setText(classObject.getClassName());
        holder.classDate.setText(classObject.getClassDate());
        holder.classSpots.setText(Integer.toString(classObject.getAvailablePlaces()));
        holder.classTime.setText(classObject.getTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCAL.onItemClicked(classList.get(position));
            }
        });
    }


    @Override
    public int getItemCount() {
        return classList.size();
    }
}
