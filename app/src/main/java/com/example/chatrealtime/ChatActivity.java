package com.example.chatrealtime;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    EditText editMessage;
    Button btnSend;
    RecyclerView recyclerView;

    ArrayList<Message> list;
    ChatAdapter adapter;

    String username;
    String roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        username = getIntent().getStringExtra("username");
        roomId = getIntent().getStringExtra("roomId");

        if (username == null) {
            username = "User";
        }

        editMessage = findViewById(R.id.editMessage);
        btnSend = findViewById(R.id.btnSend);
        recyclerView = findViewById(R.id.recyclerView);

        list = new ArrayList<>();

        // ⚠️ TẠO FIREBASE TRƯỚC
        databaseReference = FirebaseDatabase
                .getInstance("https://chatrealtimeroom-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("rooms")
                .child(roomId)
                .child("chat");

        // ⚠️ SAU ĐÓ MỚI TẠO ADAPTER
        adapter = new ChatAdapter(list, username, databaseReference);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        btnSend.setOnClickListener(v -> {

            String text = editMessage.getText().toString().trim();

            if (!text.isEmpty()) {

                Message message = new Message(username, text);

                databaseReference.push().setValue(message);

                editMessage.setText("");
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {

                Message msg = snapshot.getValue(Message.class);

                if (msg != null) {

                    msg.key = snapshot.getKey();

                    list.add(msg);

                    adapter.notifyItemInserted(list.size() - 1);

                    recyclerView.scrollToPosition(list.size() - 1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {

                Message msg = snapshot.getValue(Message.class);

                if (msg != null) {

                    msg.key = snapshot.getKey();

                    for (int i = 0; i < list.size(); i++) {

                        if (list.get(i).key != null &&
                                list.get(i).key.equals(msg.key)) {

                            list.set(i, msg);
                            adapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {

                String key = snapshot.getKey();

                for (int i = 0; i < list.size(); i++) {

                    if (list.get(i).key != null &&
                            list.get(i).key.equals(key)) {

                        list.remove(i);
                        adapter.notifyItemRemoved(i);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }
}