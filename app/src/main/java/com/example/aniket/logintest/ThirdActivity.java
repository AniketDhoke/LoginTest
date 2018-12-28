package com.example.aniket.logintest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ThirdActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    EditText UsrName;
    EditText emailAdd;
    EditText phoneNo;
    EditText ages;
    Button btnSave;
    String name,phone,age,email;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout : {
                Logout();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        firebaseAuth = FirebaseAuth.getInstance();

        UsrName = (EditText) findViewById(R.id.editTextName);
        emailAdd = (EditText) findViewById(R.id.editTextEmail);
        phoneNo = (EditText) findViewById(R.id.editTextPhone);
        ages = (EditText) findViewById(R.id.editTextAge);
        btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToServer();
            }
        });
    }
    public void Logout()
    {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(ThirdActivity.this,MainActivity.class));
    }

    private void sendDataToServer()
    {
        name = UsrName.getText().toString();
        email = emailAdd.getText().toString().trim();
        phone = phoneNo.getText().toString();
        age = ages.getText().toString().trim();


            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid());
            UserProfile userProfile = new UserProfile(name,email,phone,age);
            databaseReference.setValue(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(ThirdActivity.this,"Data saved successfully!",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ThirdActivity.this,"Something went wrong while saving data!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}
