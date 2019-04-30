package com.example.dinr;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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

        forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseAuth auth = FirebaseAuth.getInstance();
                final String emailAddress = emailAddressTV.getText().toString();
                final String student=studentID.getText().toString();
                final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                final Query query = rootRef.child("users").orderByChild("email").equalTo(emailAddress);//finds the email in the database
                if(!emailAddress.isEmpty()&&!student.isEmpty()){
                    final ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String email = "";
                            String id = "";
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                id = (String) ds.getKey();//retrieves the id
                                email = (String) ds.child("email").getValue();//retrieves the email associated with the id
                            }
                            if (email.equals(emailAddress) && id.equals(student)) {//compares the data base information with the entry
                                auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ForgotPass.this, "Email Sent!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ForgotPass.this, "Email Not Sent!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(ForgotPass.this, "Wrong Combination", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    };
                    query.addListenerForSingleValueEvent(valueEventListener);

                } else{
                    Toast.makeText(ForgotPass.this, "Fill out all fields", Toast.LENGTH_SHORT).show();
                }
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