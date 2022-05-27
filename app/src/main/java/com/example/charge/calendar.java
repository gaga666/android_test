package com.example.charge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class calendar extends AppCompatActivity {
    private SignDate signDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        signDate = findViewById(R.id.signDate);
        signDate.setOnSignedSuccess(new OnSignedSuccess() {
            @Override
            public void OnSignedSuccess() {
                Log.e("wqf","Success");
            }
        });
    }
}