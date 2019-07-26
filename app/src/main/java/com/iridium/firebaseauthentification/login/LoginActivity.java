package com.iridium.firebaseauthentification.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iridium.firebaseauthentification.R;

public class LoginActivity extends AppCompatActivity
{

    private Button buttonSignin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;
    private TextView forgotPassword;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private final String TAG = "LoginActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        buttonSignin =  findViewById(R.id.buttonSignin);
        editTextEmail =  findViewById(R.id.editTextEmail);
        editTextPassword =  findViewById(R.id.editTextPassword);
        textViewSignup =  findViewById(R.id.textViewSignup);
        forgotPassword = findViewById(R.id.forgotPassword);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        textViewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toNextActivity(SignupActivity.class);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               toNextActivity(PasswordReset.class);
            }
        });


        progressDialog = new ProgressDialog(this);

    }

    private  void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show();
            // stopping further function execution
            return;
        }

        if (TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            // stopping further function execution
            return;
        }
        // after validations are true
        // show a progressdialog

        progressDialog.setMessage("Logging In...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG,"LOGIN IS SUCCESSFUL".toUpperCase());
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            //start the profile activity
                            finish();
                            checkEmailVerification();

                        }else{
                            // display message when login is not successful
                            Toast.makeText(LoginActivity.this, "Login failed! Check your credentials and try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkEmailVerification()
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Boolean emailFlag = firebaseUser.isEmailVerified();

        if(emailFlag)
        {
            finish();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
        else
        {
            Toast.makeText(getApplicationContext(),"verify email",Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();
        }

    }


    private void toNextActivity(Class myActivity) {
        Intent intent = new Intent(this, myActivity);
        startActivity(intent);
    }
}
