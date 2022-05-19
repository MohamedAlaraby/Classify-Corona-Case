package com.example.tensorflowlitedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;

public class IdentityActivity extends AppCompatActivity {
MaterialButton btn_patient,btn_admin;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity);
        btn_patient=findViewById(R.id.Ident_Activity_btn_old_Patient);
        btn_admin=findViewById(R.id.Ident_Activity_btn_admin);

        btn_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(IdentityActivity.this,LoginScreen.class);
                startActivity(intent);
            }
        });

        btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(IdentityActivity.this,TasksOfPartnerActivity.class);
                startActivity(intent);
            }
        });

    }
}