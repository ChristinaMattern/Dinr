package com.example.dinr;

/**
 * @author Christina Mattern
 * @date 3/25/2019
 * This is the login screen for the DINR application. It requires the user's username and password. It will be linked to a Firebase
 * database
 */

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

public class LoginScreen extends AppCompatActivity {

    private EditText userNameEditText; //username EditText
    private EditText passwordEditTextNum; //password EditText
    private Button registerButton; //register Button (Borderless)
    private FirebaseAuth firebaseAuth;
    private Button forgotPassBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        userNameEditText = findViewById(R.id.userNameEditText);
        passwordEditTextNum = findViewById(R.id.passwordEditTextNum);
        Button loginButton = findViewById((R.id.loginButton));
        registerButton = findViewById(R.id.registerButton);
        forgotPassBtn = findViewById(R.id.forgotPassButton);


        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginScreen.this, HomeScreen.class));
            finish();
        } else {
            loginButton.setOnClickListener(new View.OnClickListener() {
                // creates an OnClick that brings to validate method
                @Override
                public void onClick(View v) {
                    validate();
                }

                public void validate() {
                    String email = userNameEditText.getText().toString();
                    String password = passwordEditTextNum.getText().toString();
                    if (!email.isEmpty() && !password.isEmpty()) {
                        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginScreen.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginScreen.this, HomeScreen.class));
                                } else {
                                    Toast.makeText(LoginScreen.this, "Login Unsuccessful!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(LoginScreen.this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginScreen.this, Register.class));
                }
            });
            forgotPassBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginScreen.this, ForgotPass.class));
                }
            });
        }
    }
    @Override
    public void onBackPressed(){}
}