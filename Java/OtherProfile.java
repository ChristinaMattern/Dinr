package com.example.dinr;
/*@author Nola Smtih
@date 4/17/2019 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class OtherProfile extends AppCompatActivity {
    private TextView fullName;
    private TextView bioText;
    private TextView yearText;
    private TextView majorText;
    private ImageView userPic;
    private TextView locationText;
    private Button followButton;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private String currentUserId;
    private String otherUserId;
    private TextView locationTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_profile);
        fullName = (TextView)findViewById(R.id.fullName);
        bioText = (TextView)findViewById(R.id.bioText);
        yearText = (TextView)findViewById(R.id.yearText);
        majorText = (TextView)findViewById(R.id.majorText);
        locationText=(TextView)findViewById(R.id.location);
        followButton = (Button)findViewById(R.id.follow);
        locationTime=(TextView)findViewById(R.id.locationTime);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        currentUserId=currentFirebaseUser.getUid();//retrieves user id of signed in user

        SharedPreferences sharedPref=getSharedPreferences("OtherId", Context.MODE_PRIVATE);
        otherUserId=sharedPref.getString("id","");//retrieves the user the current user clicked on
        final DatabaseReference rootRef2 = FirebaseDatabase.getInstance().getReference();
        Query otherQuery = rootRef2.child("users").orderByChild("userId").equalTo(otherUserId);//finds the other user in the database

        ValueEventListener valueEventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //all are blank just in case other user has blanks in their profile
                String fName=" ";
                String lName=" ";
                String bio=" ";
                String year=" ";
                String major=" ";
                String location=" ";
                String id=" ";
                String locationTimeW=" ";
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    id = (String) ds.getKey();//retrieves user's id to know where to put info into profile
                    otherUserId=id;
                    fName= (String) ds.child("fName").getValue();//retrieves first name
                    lName= (String) ds.child("lName").getValue();//retrieves last name
                    bio= (String) ds.child("bio").getValue();//retrieves bio
                    year= (String) ds.child("year").getValue();//retrieves year
                    major= (String) ds.child("major").getValue();//retrieves major
                    location=(String) ds.child("location").getValue();//retrieves location
                    locationTimeW=(String)ds.child("locationTime").getValue();//retrieves location time
                    //retrieves photo
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    StorageReference photoReference= storageReference.child(id).child("image");
                    userPic = (ImageView)findViewById(R.id.userPic);
                    final long ONE_MEGABYTE = 4000 * 4000;//size of photo
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
                if(location.equals("Offline")){
                locationText.setText(location);//sets location
                locationTime.setVisibility(View.GONE);
            }
                else {
                locationText.setText("At: " + location);//sets location
                locationTime.setVisibility(View.VISIBLE);
                locationTime.setText("Till: " + locationTimeW);//sets location time
            }
                //finds the current user in the database to check if the other user is in their friend's list
                final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                Query currentQuery = rootRef.child("users").orderByChild("userId").equalTo(currentUserId);
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            String id = (String) ds.getKey();//gets current user's id
                            currentUserId=id;
                        }
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("flist");
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            final boolean[] checked = new boolean[1];
                            //decides what to set the follow button to
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapShot) {
                                if (snapShot.hasChild(otherUserId)) {
                                    checked[0] = true;
                                    followButton.setText("Unfollow");
                                }
                                else{
                                    followButton.setText("Follow");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                currentQuery.addListenerForSingleValueEvent(valueEventListener);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        otherQuery.addListenerForSingleValueEvent(valueEventListener2);

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((followButton.getText().toString()).equals("Follow")) {//if user chooses to follow other user they will be added to their follow list in database
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("flist").child(otherUserId);
                    mDatabase.setValue(otherUserId);//adds other user to their follow list
                    followButton.setText("Unfollow");
                } else {//if user chooses to unfollow other user will be removed from current user's follow list
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("flist").child(otherUserId);
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapShot) {
                                mDatabase.removeValue();//removes other user from their follow list
                                followButton.setText("Follow");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.other_profile_menu, menu);
        return true;

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        firebaseAuth = firebaseAuth.getInstance();
        switch (item.getItemId()) {
            case R.id.Settings:
                startActivity(new Intent(OtherProfile.this, Settings.class));
                return true;
            case R.id.Logout:
                Toast.makeText(OtherProfile.this, "Logging Out...", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                startActivity(new Intent(OtherProfile.this, LoginScreen.class));
                return true;
            case R.id.Help:
                startActivity(new Intent(OtherProfile.this, Faq.class));
                return true;
            case R.id.Home:
                startActivity(new Intent(OtherProfile.this, HomeScreen.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}