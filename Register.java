package com.example.dinr;

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

public class Register extends AppCompatActivity {

    private Button registerButton;
    private EditText newUserFirstName, newUserLastName, studentID, studentEmail, newUserPassword, confirmPassword;
    private TextView missingInfo;
    private FirebaseAuth firebaseAuth;

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
        firebaseAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                String fName = newUserFirstName.getText().toString();
                String lName = newUserLastName.getText().toString();
                String email = studentEmail.getText().toString();
                String password = newUserPassword.getText().toString();
                String confirmPass = confirmPassword.getText().toString();
                String id = studentID.getText().toString();
                if (validate(fName, lName, id, email, password, confirmPass)) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Register.this,"Registration Successful!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Register.this, HomeScreen.class));
                            }
                            else{
                                Toast.makeText(Register.this,"Registration Unsuccessful!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }

    private Boolean validate(String fName, String lName, String id, String email, String password, String confirmPass) {
        if (!fName.isEmpty() && !lName.isEmpty() && !id.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPass.isEmpty()&& password.equals(confirmPass)) {
            return true;
        }
        else if (!password.equals(confirmPass)){
            missingInfo.setText("Password does not match");
        }
        else{
            missingInfo.setText("All fields must be filled");
        }
        return false;
    }
}