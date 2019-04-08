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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private DataSnapshot databaseRef;
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
        firebaseAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                final String fName = newUserFirstName.getText().toString();
                final String lName = newUserLastName.getText().toString();
                final String email = studentEmail.getText().toString();
                final String password = newUserPassword.getText().toString();
                final String confirmPass = confirmPassword.getText().toString();
                final String id = studentID.getText().toString();
                final boolean[] checked = new boolean[1];//to approve that id was checked
                checked[0] = true;//id slot

                mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapShot) {
                        if (snapShot.hasChild(id)) {//checks to see if another user has an the same id
                                        checked[0] = false;
                        }
                        if (checked[0] == true) {
                            if (validate(fName, lName, id, email, password, confirmPass)) {

                                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            mDatabase = FirebaseDatabase.getInstance().getReference();
                                            writeNewUser(id, fName, lName, email, password);
                                            Toast.makeText(Register.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Register.this, HomeScreen.class));
                                        } else {
                                            try {
                                                throw task.getException();
                                            } catch (FirebaseAuthUserCollisionException existEmail) {
                                                Toast.makeText(Register.this, "Email already registered", Toast.LENGTH_SHORT).show();
                                            } catch (Exception e) {
                                                Toast.makeText(Register.this, "Registration Unsuccessful!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });
                            }
                        }
                        else {
                            Toast.makeText(Register.this, "ID is already registered", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }


    //adds user to database
    private void writeNewUser(String id, String fName, String lName, String email, String password) {
        User user = new User(id, fName, lName, email, password);

        mDatabase.child("users").child(id).setValue(id);
        mDatabase.child("users").child(id).child("fName").setValue(fName);
        mDatabase.child("users").child(id).child("lName").setValue(lName);
        mDatabase.child("users").child(id).child("email").setValue(email);
        mDatabase.child("users").child(id).child("password").setValue(password);

    }


    private Boolean validate(String fName, String lName, final String id, String email, String password, String confirmPass) {
        if (!fName.isEmpty() && !lName.isEmpty() && !id.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPass.isEmpty() && password.equals(confirmPass) && password.length() >= 6) {
            return true;
        } else if (password.length() < 6) {
            Toast.makeText(Register.this, "Passwords must be 6 numbers or longer!", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPass)) {
            Toast.makeText(Register.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Register.this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
            ;
        }
        return false;
    }

}

