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

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE =
            "com.example.android.twoactivities.extra.MESSAGE";

    private EditText mUsername;
    private Button buttonRegister;
    private EditText editTextPassword;
    private Button buttonLogin;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //get Firebase instance
        firebaseAuth = FirebaseAuth.getInstance();
        // save reference to register field
        buttonRegister = findViewById(R.id.register_button);
        // save reference to username field
        mUsername = findViewById(R.id.username_field);
        // save reference to password field
        editTextPassword = findViewById(R.id.password_field);
        // save reference to login field
        buttonLogin = findViewById(R.id.login_button);


        // Registration
        buttonRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //trim username and password strings
                String username = mUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                //display error message if username and password fields are empty
                if (username.isEmpty() && password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                }
                //display error message if username field is empty
                else if (username.isEmpty()) {
                    mUsername.setError("Please enter username");
                    mUsername.requestFocus();
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
                else if (!(username.isEmpty() && password.isEmpty())) {
                    firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // if registration fails show an error message
                            if (!task.isSuccessful())
                            {
                                Toast.makeText(MainActivity.this, "Sign-up unsuccessful, Please try again!", Toast.LENGTH_SHORT).show();
                            }
                            // if registration is successful go to the same Activity
                            else {
                                startActivity(new Intent(MainActivity.this, MainActivity.class));
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
                String username = mUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                //display error message if username and password fields are empty
                if (username.isEmpty() && password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                }
                //display error message if username field is empty
                else if (username.isEmpty()) {
                    mUsername.setError("Please enter username id");
                    mUsername.requestFocus();
                }
                //display error message if password field is empty
                else if (password.isEmpty()) {
                    editTextPassword.setError("Please enter password");
                    editTextPassword.requestFocus();
                }
                //if username and password fields are not empty then login
                else if (!(username.isEmpty() && password.isEmpty())) {

                    firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // if login fails show an error message
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(MainActivity.this, "LOGIN FAILED!!", Toast.LENGTH_LONG).show();
                            }
                            // if login is successful go to the Home Activity
                            else
                            {
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                }
            }
        });




    }

    /*public void login(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        String username = mUsername.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, username);
        startActivity(intent);
    }*/
}