package com.example.coraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

public class RegisterActivity extends AppCompatActivity
{

    private EditText email_Register, password_Register, confirmpassword_Register;
    private Button register_btn_Register, login_btn_Register;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        email_Register = findViewById(R.id.email_Register);
        password_Register = findViewById(R.id.password_Register);
        confirmpassword_Register = findViewById(R.id.confirmpassword_Register);
        register_btn_Register = findViewById(R.id.register_btn_Register);
        login_btn_Register = findViewById(R.id.login_btn_Register);

        loadingBar = new ProgressDialog(this);

        register_btn_Register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CreateNewAccount();
            }
        });
    }

    private void CreateNewAccount()
    {
        String email = email_Register.getText().toString();
        String password = password_Register.getText().toString();
        String confirmpass = confirmpassword_Register.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(confirmpass))
        {
            Toast.makeText(this, "Please Enter Confirm Password", Toast.LENGTH_SHORT).show();
        }

        else if(!password.equals(confirmpass))
        {
            Toast.makeText(this, "Password Must Match Confirm Password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please Wait");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(RegisterActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(RegisterActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }
}