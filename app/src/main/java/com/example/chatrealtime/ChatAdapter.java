package com.example.chatrealtime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

    ArrayList<Message> list;
    String username;
    DatabaseReference databaseReference;

    public ChatAdapter(ArrayList<Message> list, String username, DatabaseReference databaseReference){
        this.list = list;
        this.username = username;
        this.databaseReference = databaseReference;
    }

    @Override
    public int getItemViewType(int position){

        Message m = list.get(position);

        if(m.name != null && m.name.equals(username)){
            return 1; // tin nhắn của mình
        }
        return 0; // tin nhắn người khác
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){

        View view;

        if(viewType == 1){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_right,parent,false);
        }else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_left,parent,false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){

        Message m = list.get(position);

        if(m.name != null && m.name.equals(username)){
            holder.txtMessage.setText(m.text);
        }else{
            holder.txtMessage.setText(m.name + ": " + m.text);
        }

        holder.txtMessage.setTextColor(0xFF000000);

        if(m.time != 0){
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            holder.txtTime.setText(sdf.format(new Date(m.time)));
        }else{
            holder.txtTime.setText("");
        }

        holder.itemView.setOnLongClickListener(v -> {

            PopupMenu menu = new PopupMenu(v.getContext(), v);

            menu.getMenu().add("Thu hồi");
            menu.getMenu().add("Xóa");

            menu.setOnMenuItemClickListener(item -> {

                String action = item.getTitle().toString();

                if(databaseReference == null) return true;

                if(action.equals("Thu hồi") && m.key != null){

                    databaseReference
                            .child(m.key)
                            .child("text")
                            .setValue("Tin nhắn đã thu hồi");
                }

                if(action.equals("Xóa") && m.key != null){

                    databaseReference
                            .child(m.key)
                            .removeValue();
                }

                return true;
            });

            menu.show();

            return true;
        });
    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtMessage, txtTime;

        public ViewHolder(View itemView){
            super(itemView);

            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtTime = itemView.findViewById(R.id.txtTime);
        }
    }
}