package com.iridium.firebaseauthentification.login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.iridium.firebaseauthentification.R;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
    }
}
