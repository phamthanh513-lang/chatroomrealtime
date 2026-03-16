package com.example.chatrealtime;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText editName, editRoom;
    Button btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editName);
        editRoom = findViewById(R.id.editRoom);
        btnJoin = findViewById(R.id.btnJoin);

        btnJoin.setOnClickListener(v -> {

            String name = editName.getText().toString().trim();
            String room = editRoom.getText().toString().trim();

            if(!name.isEmpty() && !room.isEmpty()){

                Intent intent =
                        new Intent(LoginActivity.this, ChatActivity.class);

                intent.putExtra("username", name);
                intent.putExtra("roomId", room);

                startActivity(intent);

            }
        });
    }
}