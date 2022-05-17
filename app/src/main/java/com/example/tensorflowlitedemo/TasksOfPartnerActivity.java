package com.example.tensorflowlitedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class TasksOfPartnerActivity extends AppCompatActivity {
MaterialButton btn_check,btn_add,btn_mod;
Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_of_partner);
        btn_check=findViewById(R.id.Ident_Activity_btn_Check);
        btn_add=findViewById(R.id.Ident_Activity_btn_Add);
        btn_mod=findViewById(R.id.Tasks_Activity_btn_Modify);
        btn_mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(TasksOfPartnerActivity.this,PatientInfoActivity.class);
                startActivity(intent);
            }
        });
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(TasksOfPartnerActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(TasksOfPartnerActivity.this,PatientInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}