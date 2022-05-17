package com.example.tensorflowlitedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class PatientInfoActivity extends AppCompatActivity {
     Intent intent;
     MaterialButton save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);
        save=findViewById(R.id.patient_info_act_btn_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(PatientInfoActivity.this,LoginScreen.class);
                startActivity(intent);
            }
        });

    }
}