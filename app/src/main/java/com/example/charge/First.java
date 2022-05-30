package com.example.charge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.charge.changemail.change_mail;

public class First extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        ImageView first_main,first_cal,first_resetpass,first_exit;
        first_main = findViewById(R.id.first_main);
        first_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(First.this,BottomBar.class);
                startActivity(i);
                overridePendingTransition(0,0);
            }
        });
        first_cal =  findViewById(R.id.first_cal);
        first_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(First.this, change_mail.class);
                startActivity(i);
                overridePendingTransition(0,0);
            }
        });
        first_resetpass =  findViewById(R.id.first_resetpass);
        first_resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(First.this,resetpassword.class);
                startActivity(i);
                overridePendingTransition(0,0);
            }
        });
        first_exit = findViewById(R.id.first_exit);
        first_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(First.this,uses_head.class);
                startActivity(i);
            }
        });
    }
}