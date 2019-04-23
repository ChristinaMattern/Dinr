package com.example.dinr;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPass extends AppCompatActivity {

    private Button forgotBtn;
    private TextView emailAddressTV;
    private EditText studentID;
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pass);

        forgotBtn = findViewById(R.id.sendEmailBtn);
        emailAddressTV = findViewById(R.id.emailAddress);
        studentID = findViewById(R.id.studentID);
        backBtn = findViewById(R.id.backButton);

        String id = studentID.getText().toString();

        forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = emailAddressTV.getText().toString();
                auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPass.this, "Email Sent!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(ForgotPass.this, "Email Not Sent!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPass.this, LoginScreen.class));
            }
        });
    }
}
