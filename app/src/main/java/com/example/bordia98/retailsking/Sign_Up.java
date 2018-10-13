package com.example.bordia98.retailsking;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;

public class Sign_Up extends AppCompatActivity {

    EditText email,password,confirmpassword;
    Button signup;
    private FirebaseAuth mAuth;
    ProgressBar pgbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);
        Toolbar bar = findViewById(R.id.toolbar);
        bar.setTitle("Sign Up Page");
        bar.setTitleTextColor(Color.WHITE);
        bar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        setSupportActionBar(bar);
        mAuth = FirebaseAuth.getInstance();
        pgbar=(ProgressBar)findViewById(R.id.progressBar);
        pgbar.setVisibility(View.GONE);
        email=(EditText)findViewById(R.id.emailid);
        password=(EditText)findViewById(R.id.password);

        //Code for admin check and email verification is remaining

        Button signup = (Button)findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signtheuser();
            }
        });
    }

    private void signtheuser() {
        String username = email.getText().toString().trim();
        String passwd = password.getText().toString().trim();
        String cnfpswd = confirmpassword.getText().toString().trim();
        if(username.isEmpty()){
            email.setError("email id is required");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            email.setError("Enter  a valid email id");
            email.requestFocus();
            return;
        }

        if(passwd.isEmpty()){
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        if(passwd.length()<6){
            password.setError("Minimum length of password is 6 characters");
            password.requestFocus();
            return;
        }

        if(cnfpswd.equals(passwd)){

            pgbar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(username,passwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    pgbar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "User created successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);
                    }
                    else{
                        if (task.getException() instanceof FirebaseAuthEmailException){
                            Toast.makeText(getApplicationContext(),"User already registered",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"make sure your email is correct and is not registered already",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

            });
        }
        else{
            confirmpassword.setError("password doesn't matched");
            confirmpassword.requestFocus();
            return;
        }

    }

}
