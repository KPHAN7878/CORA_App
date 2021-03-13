package com.example.coraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//test from kevinphan724@gmail.com

public class LoginActivity extends AppCompatActivity
{

    private EditText emailEditText, passwordEditText;
    private Button loginBtn, registerBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerBtn = findViewById(R.id.registerBtn);
        loginBtn = findViewById(R.id.loginBtn);

        registerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendUserToRegisterActivity();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AllowUserLogin();
            }
        });
    }





    //if user is already logged in then don't have to enter credentials
    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
        {
            sendUserToMainActivity();
        }
    }


    //method for when user clicks login button
    private void AllowUserLogin()
    {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        //if email field is empty
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
        }
        //if password field is empty
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        }
        //if nothing is wrong then sign in
        else
        {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    //if login is successful
                    if(task.isSuccessful())
                    {
                        sendUserToMainActivity();
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    }
                    //if login fails
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(LoginActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //method for sending user to main page when login is successful
    private void sendUserToMainActivity()
    {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        //prevent user from pressing back
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    //method for when user clicks reigster button
    private void sendUserToRegisterActivity()
    {
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
        //finish();
    }


}