package com.example.aniket.logintest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;

public class forgot_passwordActivity extends AppCompatActivity {

    EditText forgot_password;
    Button btnResetPassword;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_activity);

        forgot_password = (EditText) findViewById(R.id.email_forgot_password);
        btnResetPassword = (Button) findViewById(R.id.ResetPassword);

        firebaseAuth = FirebaseAuth.getInstance();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordResetEmail();
            }
        });
    }

    public void sendPasswordResetEmail()
    {
        String useremail = forgot_password.getText().toString().trim();
        final MainActivity c1 = new MainActivity();

        if (useremail.isEmpty())
        {
            Toast.makeText(forgot_passwordActivity.this,"Please enter your Email!",Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(forgot_passwordActivity.this,"Reset password email has been sent!",Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(forgot_passwordActivity.this,MainActivity.class));
                    } else {
                        Toast.makeText(forgot_passwordActivity.this,"Something went wrong while sending reset link!",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
