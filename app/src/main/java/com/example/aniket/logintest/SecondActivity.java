package com.example.aniket.logintest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.jar.Attributes;

public class SecondActivity extends AppCompatActivity {

    EditText etName;
    EditText etEmail;
    EditText etPassword;
    EditText etCPassword;
    Button btnRegister;
    TextView textViewSignIn;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    public  void setupUIViews()
    {
        etName = (EditText) findViewById(R.id.Name);
        etEmail = (EditText) findViewById(R.id.Email);
        etPassword = (EditText) findViewById(R.id.Password);
        etCPassword = (EditText) findViewById(R.id.CPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        textViewSignIn = (TextView) findViewById(R.id.btnSignIn);

        progressDialog = new ProgressDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();

        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this,MainActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please wait...");
                if (Register()){

                    String useremail = etEmail.getText().toString().trim();
                    String userpassword = etPassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(useremail,userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                progressDialog.dismiss();
                                sendEmailVerification();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(SecondActivity.this,"Registration failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }


    public boolean Register()
    {
        Boolean result = false;

        String user_name = etName.getText().toString();
        String user_email = etEmail.getText().toString();
        String user_password = etPassword.getText().toString();
        String user_cpassword = etCPassword.getText().toString();

        progressDialog.show();

        if ((user_name.isEmpty()) || (user_email.isEmpty()) || (user_password.isEmpty()) || (user_cpassword.isEmpty())) {
            progressDialog.dismiss();
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
        }
        else {
            if (!user_password.equals(user_cpassword))
            {
                progressDialog.dismiss();
                Toast.makeText(this, "Password and Confirm Password must match!", Toast.LENGTH_SHORT).show();
            } else {
                result = true;
            }
        }
        return result;
    }

    public void sendEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        firebaseAuth.signOut();
                        finish();
                        Toast.makeText(SecondActivity.this, "Verification Email has been sent!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SecondActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(SecondActivity.this, "Verification Email has not been sent!", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                    }
                }
            });
        }
    }

}
