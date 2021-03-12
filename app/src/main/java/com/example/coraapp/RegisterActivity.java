package com.example.coraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity
{

    private EditText email_Register, password_Register, confirmpassword_Register;
    private Button register_btn_Register, login_btn_Register;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email_Register = findViewById(R.id.email_Register);
        password_Register = findViewById(R.id.password_Register);
        confirmpassword_Register = findViewById(R.id.confirmpassword_Register);
        register_btn_Register = findViewById(R.id.register_btn_Register);
        login_btn_Register = findViewById(R.id.login_btn_Register);
        
    }
}