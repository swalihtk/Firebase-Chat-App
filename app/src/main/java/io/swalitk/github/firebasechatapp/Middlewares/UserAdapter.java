package io.swalitk.github.firebasechatapp.Middlewares;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import io.swalitk.github.firebasechatapp.ChatRoom;
import io.swalitk.github.firebasechatapp.R;

public class UserAdapter extends ArrayAdapter {

    ArrayList<String> all_users;
    Context context;
    public UserAdapter(@NonNull Context context, int resource, ArrayList<String> all_users) {
        super(context, resource, all_users);
        this.all_users=all_users;
        this.context=context;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return all_users.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.custom_users_layout, parent, false);
        TextView tw_username=convertView.findViewById(R.id.layout_textView_username);
        tw_username.setText(all_users.get(position));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetails.chatWith=all_users.get(position);
                context.startActivity(new Intent(context, ChatRoom.class));
            }
        });
        return convertView;
    }
}
