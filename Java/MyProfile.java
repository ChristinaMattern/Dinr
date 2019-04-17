package com.example.dinr;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyProfile extends AppCompatActivity {
    private TextView fullName;
    private String userId;
    private TextView bioText;
    private TextView yearText;
    private TextView majorText;
    private Button editButton;
    private ImageView userPic;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile);
        fullName = (TextView)findViewById(R.id.fullName);
        bioText = (TextView)findViewById(R.id.bioText);
        yearText = (TextView)findViewById(R.id.yearText);
        majorText = (TextView)findViewById(R.id.majorText);
        editButton = (Button)findViewById(R.id.edit_button);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        userId=currentFirebaseUser.getUid();//retrieves user id of signed in user
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query query = rootRef.child("users").orderByChild("userId").equalTo(userId);//finds the user in the database

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String fName="";
                String lName="";
                String bio="";
                String year="";
                String major="";
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id = (String) ds.getKey();//retrieves user's id to know where to put info into profile
                    fName= (String) ds.child("fName").getValue();//retrieves first name
                    lName= (String) ds.child("lName").getValue();//retrieves last name
                    bio= (String) ds.child("bio").getValue();//retrieves bio
                    year= (String) ds.child("year").getValue();//retrieves year
                    major= (String) ds.child("major").getValue();//retrieves major
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


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Toast.makeText(MyProfile.this, "Settings", Toast.LENGTH_SHORT).show();
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

}