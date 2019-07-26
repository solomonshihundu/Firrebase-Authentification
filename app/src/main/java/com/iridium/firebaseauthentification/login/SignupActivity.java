package com.iridium.firebaseauthentification.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextFullName;
    private EditText editTextPhoneNumber;
    private EditText editTextEmail;
    private EditText getEditTextPassword;
    private TextView textViewSignin;
    private EditText editTextConfirmPass;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    // setting the string inputs as variables
    public static final String PERSON_NAME = "name";
    public static final String PERSON_PHONE = "phone";
    public static final String PERSON_EMAIL = "email";
    private static final String TAG = "SIGNUP_ACTIVITY";

    private String name  ;
    private String phone;
    private String email;
    private String password;
    private String confirmPassword;

// ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();


        // Initializing views
        progressDialog = new ProgressDialog(this);
        buttonRegister =  findViewById(R.id.buttonRegister);
        editTextEmail =  findViewById(R.id.editTextEmail);
        editTextPhoneNumber =  findViewById(R.id.editTextPhoneNumber);
        getEditTextPassword=  findViewById(R.id.editTextPassword);
        editTextFullName =  findViewById(R.id.editTextFullName);
        textViewSignin =  findViewById(R.id.textViewSignin);
        editTextConfirmPass = findViewById(R.id.edit_text_confirm_password);

        // attaching listeners to buttons
        textViewSignin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

    }
    private void registerUser() {

        name = editTextFullName.getText().toString();
        phone = editTextPhoneNumber.getText().toString();
        email = editTextEmail.getText().toString().trim();
        password = getEditTextPassword.getText().toString();
        confirmPassword = editTextConfirmPass.getText().toString();


        if (TextUtils.isEmpty(email)){
            // email is empty
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }if (TextUtils.isEmpty(name)){
            // name is empty
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }if (TextUtils.isEmpty(phone)){
            // phone is empty
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }if (TextUtils.isEmpty(password)){
            // password is empty
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        if (!validatePassword(password,confirmPassword)){
            // password mismatch
            Toast.makeText(this, "password mismatch", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        if (!validatePhone(phone)){
            // phone number too short or too long
            Toast.makeText(this, "wrong phone number format", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        }


        // after validations are true
        // show a progress dialog if email and password are not empty
        progressDialog.setMessage("Just a moment...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Error. Please try again", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            sendEmailVerification();
                        }

                    }
                });

    }

    // method to show message
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View view) {
        if(view == buttonRegister) {
            registerUser();
        }

        // opens login activity
        if(view == textViewSignin) {
            toNextActivity(LoginActivity.class);

        }
    }

    private void toNextActivity(Class myActivity) {
        Intent intent = new Intent(this, myActivity);
        startActivity(intent);
    }

    private boolean validatePassword(String pass1,String pass2)
    {
        return pass1.contains(pass2);
    }
    private boolean validatePhone(String phone)
    {
        return phone.toCharArray().length == 10;
    }

    private void sendEmailVerification()
    {
       final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(),"Sign up successful,check verification email",Toast.LENGTH_SHORT).show();

                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"verification failed",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}
