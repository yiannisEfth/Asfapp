package ltd.solutions.software.myt.asfapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private List<User> userList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, userSurname, isActive;

        public MyViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.user_name);
            userSurname = view.findViewById(R.id.user_surname);
            isActive = view.findViewById(R.id.isActive);
        }
    }



    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserAdapter.MyViewHolder holder, int position) {

        User user = userList.get(position);
        holder.userName.setText(user.getName());
        holder.userSurname.setText(user.getSurname());
        holder.isActive.setText(Boolean.toString(user.isActive));

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
