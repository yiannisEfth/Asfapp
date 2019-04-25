package ltd.solution.software.myt.asfapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private List<User> userList;
    public OnUserAdapterClickListener onUAL;
    TextView userName, userSurname, isActive;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView userName, userSurname,userId, isActive;

        public MyViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.user_name);
            userId = (TextView) view.findViewById(R.id.user_id);
            userSurname = (TextView) view.findViewById(R.id.user_surname);
            isActive = (TextView) view.findViewById(R.id.isActive);
        }
    }

    public UserAdapter(List<User> userList, OnUserAdapterClickListener onUAL) {
        this.userList = userList;
        this.onUAL = onUAL;
    }

    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserAdapter.MyViewHolder holder, final int position) {

        User user = userList.get(position);
        holder.userSurname.setText(user.getSurname());
        holder.userName.setText(user.getName());
        holder.userId.setText(String.valueOf(user.getId()));
        if (user.isActive) {
            holder.isActive.setText("Yes");
        } else {
            holder.isActive.setText("No");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUAL.onItemClicked(userList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }



}
