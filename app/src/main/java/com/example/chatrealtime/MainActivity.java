package com.example.chatrealtime;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText editName;
    Button btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editName);
        btnJoin = findViewById(R.id.btnJoin);

        btnJoin.setOnClickListener(v -> {

            String name = editName.getText().toString().trim();

            if(!name.isEmpty()){

                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("username", name);

                startActivity(intent);
            }
        });
    }
}