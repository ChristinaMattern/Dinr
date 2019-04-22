package com.example.dinr;
/*@author Nola Smtih
@date 4/17/2019 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyProfile extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private TextView fullName;
    private String userId;
    private TextView bioText;
    private TextView yearText;
    private TextView majorText;
    private Button editButton;
    private ImageView userPic;
    private FirebaseAuth firebaseAuth;
    private TextView locationText;
    private Spinner dropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile);
        fullName = (TextView)findViewById(R.id.fullName);
        bioText = (TextView)findViewById(R.id.bioText);
        yearText = (TextView)findViewById(R.id.yearText);
        majorText = (TextView)findViewById(R.id.majorText);
        editButton = (Button)findViewById(R.id.edit_button);
        locationText = (TextView) findViewById(R.id.locationText);
        dropdown = findViewById(R.id.location);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        userId=currentFirebaseUser.getUid();//retrieves user id of signed in user
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query query = rootRef.child("users").orderByChild("userId").equalTo(userId);//finds the user in the database

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //retireves user's profile from database
                String fName=" ";
                String lName=" ";
                String bio=" ";
                String year=" ";
                String major=" ";
                String location=" ";
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id = (String) ds.getKey();//retrieves user's id to know where to put info into profile
                    userId=id;
                    fName= (String) ds.child("fName").getValue();//retrieves first name
                    lName= (String) ds.child("lName").getValue();//retrieves last name
                    bio= (String) ds.child("bio").getValue();//retrieves bio
                    year= (String) ds.child("year").getValue();//retrieves year
                    major= (String) ds.child("major").getValue();//retrieves major
                    location=(String) ds.child("location").getValue();//retrieves location

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    StorageReference photoReference= storageReference.child(id).child("image");
                    userPic = (ImageView)findViewById(R.id.userPic);
                    final long ONE_MEGABYTE = 1024 * 1024;
                    photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            userPic.setImageBitmap(bmp);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                fullName.setText(fName+" "+lName);//combines first and last name to create full name
                bioText.setText(bio);//sets bio
                yearText.setText(year);//sets year
                majorText.setText(major);//sets major
                ArrayAdapter myAdap = (ArrayAdapter) dropdown.getAdapter();
                int spinnerPosition = myAdap.getPosition(location);
                dropdown.setSelection(spinnerPosition);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);
        //create a list of items for the spinner.
        String[] items = new String[]{"Offline","Dining Hall", "Camelot Room", "Brubacher Cafe", "Starbucks", "Lally Cafe"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        final String location = dropdown.getSelectedItem().toString();
                        editUser(userId,location);//changes the location
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                    //add some code here
                }
        );

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//takes you to the user's edit profile page
                startActivity(new Intent(MyProfile.this, EditProfile.class));
            }
        });

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_profile_menu, menu);
        return true;

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        firebaseAuth = firebaseAuth.getInstance();
        switch (item.getItemId()){
            case R.id.Settings:
                startActivity(new Intent(MyProfile.this, Settings.class));
                return true;
            case R.id.Logout:
                Toast.makeText(MyProfile.this, "Logging Out...", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                startActivity(new Intent(MyProfile.this, LoginScreen.class));
                return true;
            case R.id.Help:
                startActivity(new Intent(MyProfile.this, Faq.class));
                return true;
            case R.id.Home:
                startActivity(new Intent(MyProfile.this, HomeScreen.class));
                return true;
            case R.id.EditProfile:
                startActivity(new Intent(MyProfile.this, EditProfile.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //adds user's profile to database
    private void editUser(String id,String location) {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(id).child("location");
        mDatabase.setValue(location);
        return;
    }

}