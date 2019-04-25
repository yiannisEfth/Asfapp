package ltd.solution.software.myt.asfapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UserClassAttendanceAdapter extends RecyclerView.Adapter<UserClassAttendanceAdapter.MyViewHolder> {
    private List<User> userList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView userName, userSurname, userId;

        public MyViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.name_text);
            userSurname = (TextView) view.findViewById(R.id.surname_text);
            userId = (TextView) view.findViewById(R.id.id_text);
        }
    }

    public UserClassAttendanceAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public UserClassAttendanceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_class_info_row, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(UserClassAttendanceAdapter.MyViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userSurname.setText(user.getSurname());
        holder.userName.setText(user.getName());
        holder.userId.setText(String.valueOf(user.getId()));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
