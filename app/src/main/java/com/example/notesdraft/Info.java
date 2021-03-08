package com.example.notesdraft;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        //setTitle("Information");



        setContentView(R.layout.activity_info);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        finish();
        super.onBackPressed();
    }
}