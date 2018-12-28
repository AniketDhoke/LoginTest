package com.example.aniket.logintest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button login;
    private TextView Info;
    private TextView textViewSignUp;
    private TextView txtForgotPass;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    private int count=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.UName);
        password = (EditText) findViewById(R.id.Pass);
        login = (Button) findViewById(R.id.login);
        Info = (TextView) findViewById(R.id.infos);
        textViewSignUp = (TextView) findViewById(R.id.btnSignUp);
        txtForgotPass = (TextView) findViewById(R.id.txtForgotPassword);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null)
        {
            finish();
            startActivity(new Intent(MainActivity.this, ThirdActivity.class));
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login(username.getText().toString(),password.getText().toString());
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });

        txtForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,forgot_passwordActivity.class));
            }
        });
    }

    public void Login(String userName, String passWord)
    {
        progressDialog.setMessage("Please wait!");
        progressDialog.show();

        if (!(username.getText().toString().isEmpty()) && !(password.getText().toString().isEmpty()))
        {
            if (isNetworkAvailable(this))
            {
                firebaseAuth.signInWithEmailAndPassword(userName,passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                checkUserVerification();
                            } else {
                                progressDialog.dismiss();
                                count--;
                                Info.setText("Attempts remaining : " + count + "");
                                Toast.makeText(MainActivity.this,"Wrong Email or Password!",Toast.LENGTH_SHORT).show();
                                if (count == 0) {
                                    login.setEnabled(false);
                                }
                            }
                        }
                    });

            } else {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this,"Check your Internet Connection",Toast.LENGTH_SHORT).show();
            }


        }

        else
        {
            progressDialog.dismiss();
            Toast.makeText(this, "Please enter Email and Password", Toast.LENGTH_SHORT).show();

        }

    }

    public void checkUserVerification()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user.isEmailVerified()){
                progressDialog.dismiss();
                finish();
                Intent intent = new Intent(MainActivity.this,ThirdActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this,"Please verify your email before login!",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                firebaseAuth.signOut();
            }


    }

    public boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
