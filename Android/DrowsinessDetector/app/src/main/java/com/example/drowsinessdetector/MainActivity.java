package com.example.drowsinessdetector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE =
            "com.example.android.twoactivities.extra.MESSAGE";

    private EditText editTextEmail;
    private Button buttonRegister;
    private EditText editTextPassword;
    private Button buttonLogin;
    public int mStreak = 0;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbReference = mFirebaseDatabase.getInstance().getReference("streak");

        //get Firebase instance
        firebaseAuth = FirebaseAuth.getInstance();
        // save reference to register field
        buttonRegister = findViewById(R.id.register_button);
        // save reference to username field
        editTextEmail = findViewById(R.id.email_field);
        // save reference to password field
        editTextPassword = findViewById(R.id.password_field);
        // save reference to login field
        buttonLogin = findViewById(R.id.login_button);


        // Registration
        buttonRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //trim username and password strings
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                //Splitting email id to get a username
                String[] username_arr = email.split("@");
                final String uname = username_arr[0];

                //display error message if username and password fields are empty
                if (email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                }
                //display error message if username field is empty
                else if (email.isEmpty()) {
                    editTextEmail.setError("Please enter username");
                    editTextEmail.requestFocus();
                }
                //display error message if password field is empty
                else if (password.isEmpty()) {
                    editTextPassword.setError("Please enter password");
                    editTextPassword.requestFocus();
                }
                //display error message if password is less than 6 characters
                else if(password.length() < 6)
                {
                    editTextPassword.setError("Enter password greater than 6 characters!");
                    editTextPassword.requestFocus();
                }
                //if username and password fields are not empty then register the user
                else if (!(email.isEmpty() && password.isEmpty())) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // if registration fails show an error message
                            if (!task.isSuccessful())
                            {
                                Toast.makeText(MainActivity.this, "Registration failed!" + "\n" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                            // if registration is successful go to the same Activity
                            else {
                                startActivity(new Intent(MainActivity.this, MainActivity.class));
                                // On successful registration, create an entry for a particular user and set the streak value to 0 in the database
                                String id = mDbReference.push().getKey();
                                Streak stk = new Streak(uname, 0);
                                mDbReference.child(id).setValue(stk);

                            }
                        }
                    });
                }
                //display error message if something else goes wrong
                else {
                    Toast.makeText(MainActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });



        // Login
        buttonLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                //trim username and password strings
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                //display error message if username and password fields are empty
                if (email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                }
                //display error message if username field is empty
                else if (email.isEmpty()) {
                    editTextEmail.setError("Please enter username id");
                    editTextEmail.requestFocus();
                }
                //display error message if password field is empty
                else if (password.isEmpty()) {
                    editTextPassword.setError("Please enter password");
                    editTextPassword.requestFocus();
                }
                //if username and password fields are not empty then login
                else if (!(email.isEmpty() && password.isEmpty())) {

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // if login fails show an error message
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(MainActivity.this, "LOGIN FAILED!" + "\n" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                            // if login is successful go to the Home Activity
                            else
                            {
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                String email = editTextEmail.getText().toString();
                                intent.putExtra(EXTRA_MESSAGE, email);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                }
            }
        });
    }
}