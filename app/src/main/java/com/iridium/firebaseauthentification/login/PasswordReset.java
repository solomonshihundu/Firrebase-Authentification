package com.iridium.firebaseauthentification.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.iridium.firebaseauthentification.R;


public class PasswordReset extends AppCompatActivity
{
    private EditText emailEdit;
    private Button resetPassBtn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4169E1")));
        getSupportActionBar().setTitle("Password Reset");

        emailEdit = findViewById(R.id.reset_email_edit);
        resetPassBtn = findViewById(R.id.reset_pass_btn);

        resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                firebaseAuth = FirebaseAuth.getInstance();
                String userEmail = emailEdit.getText().toString().trim();
                if(userEmail.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"enter email ",Toast.LENGTH_LONG).show();
                }
                else{
                    firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(), "reset email sent", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(PasswordReset.this,LoginActivity.class));
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "error,invalid email address", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
