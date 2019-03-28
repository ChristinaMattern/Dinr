package com.example.simpletestlogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Register extends AppCompatActivity {

    private Button registerButton;
    private EditText newUserFirstName, newUserLastName, studentID, studentEmail, newUserPassword, confirmPassword;
    private TextView missingInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        registerButton = findViewById(R.id.register_button);
        newUserFirstName = findViewById(R.id.first_name);
        newUserLastName = findViewById(R.id.last_name);
        studentEmail = findViewById(R.id.email);
        newUserPassword = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.re_password);
        studentID = findViewById(R.id.strose_id);
        missingInfo = findViewById(R.id.missingInfoTextView);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                String fName = newUserFirstName.getText().toString();
                String lName = newUserLastName.getText().toString();
                String email = studentEmail.getText().toString();
                String password = newUserPassword.getText().toString();
                String confirmPass = confirmPassword.getText().toString();
                String id = studentID.getText().toString();
                if(validate(fName, lName, id, email, password, confirmPass)){
                    startActivity(new Intent(Register.this, HomeScreen.class));
                }
                else{
                    if (password != confirmPass){
                        missingInfo.setText("Password must be the same to register");
                    }
                    else{
                        missingInfo.setText("Must fill out all fields to register");
                    }
                }
            }
        });
    }

    private Boolean validate(String fName, String lName, String id, String email, String password, String confirmPass){
        if(!fName.isEmpty() && !lName.isEmpty() && !id.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPass.isEmpty() && password.equals(confirmPass)){
            return true;
        }
       else{
           return false;
        }
    }

}