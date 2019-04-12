package com.example.dinr;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditProfile extends AppCompatActivity {
    private Button saveButton;
    private EditText bioText;
    private RadioGroup yearText;
    private EditText majorText;
    private String userId;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        saveButton = findViewById(R.id.save_button);
        bioText = findViewById(R.id.bioText);
        yearText = findViewById(R.id.year);
        majorText = findViewById(R.id.major);



        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RadioGroup rg = (RadioGroup)findViewById(R.id.year);
                final String yearText = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();
                final String bio=bioText.getText().toString();
                final String major= majorText.getText().toString();
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                userId=currentFirebaseUser.getUid();//retrieves current user
                final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

                final Query query = rootRef.child("users").orderByChild("userId").equalTo(userId);//finds user in database
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                String id = (String) ds.getKey();//retrieves user's id to know where to put info into profile
                                editUser(id, bio, yearText, major);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                startActivity(new Intent(EditProfile.this, MyProfile.class));
            }
        });
    }

    //adds user's profile to database
    private void editUser(String id, String bio, String year, String major) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(id).child("bio");
        mDatabase.setValue(bio);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(id).child("year");
        mDatabase.setValue(year);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(id).child("major");
        mDatabase.setValue(major);
        return;
    }
}
