package com.example.simpletestlogin;
/**
 * @author Christina Mattern
 * @date 3/25/2019
 * This is the login screen for the DINR application. It requires the user's username and password. It will be linked to a Firebase
 * database
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginScreen extends AppCompatActivity {

    private EditText userNameEditText; //username EditText
    private EditText passwordEditTextNum; //password EditText
    private TextView incorrectMessageTextView; //incorrectMessage TextView
    private Button registerButton; //register Button (Borderless)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        userNameEditText = findViewById(R.id.userNameEditText);
        passwordEditTextNum = findViewById(R.id.passwordEditTextNum);
        Button loginButton = findViewById((R.id.loginButton));
        incorrectMessageTextView = findViewById(R.id.incorrectMessageTextView);
        registerButton = findViewById(R.id.registerButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            // creates an OnClick that brings to validate method
            @Override
            public void onClick(View v) {
               validate();
            }
            public void validate(){
                // this method will test the entered username and password against saved usernames and passwords in a database
                if (userNameEditText.getText().toString().equals("student123") && passwordEditTextNum.getText().toString().equals("456")){
                    startActivity(new Intent(LoginScreen.this, HomeScreen.class));
                }
                else{
                    // this message displays if the user enters an incorrect username and/or password
                    incorrectMessageTextView.setText("Incorrect Combination of Username and Password. Try Again!");
                }
            }
        });

      registerButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              // fill in with intent for registration page
          }
      });
    }
}
