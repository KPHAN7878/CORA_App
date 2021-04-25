package com.example.coraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button send_Button;
    private EditText inputFP_Email;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        //set variables for widgets
        send_Button = findViewById(R.id.button_SendEmail);
        inputFP_Email = findViewById(R.id.editText_email);
        mAuth = FirebaseAuth.getInstance();

        send_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = inputFP_Email.getText().toString();

                //If not empty send email using text
                //Use Firebase to find email and use that to send email
                //Send message that if email exists then mail is sent
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            //send message if task is sent or not sent
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if (task.isSuccessful())
                                    Toast.makeText(ForgotPasswordActivity.this,
                                            "Password has been sent",
                                            Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(ForgotPasswordActivity.this,
                                            task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                            }
                        });
            }
         });
    }

    /*
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/

}