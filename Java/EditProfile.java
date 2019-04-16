package com.example.dinr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.IOException;


public class EditProfile extends AppCompatActivity {
    private static Bitmap Image = null;
    private static Bitmap rotateImage = null;
    private static ImageButton userPic;
    private static final int GALLERY = 1;
    private Button saveButton;
    private EditText bioText;
    private RadioGroup yearText;
    private EditText majorText;
    private String userId;
    private DatabaseReference mDatabase;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        saveButton = findViewById(R.id.save_button);
        bioText = findViewById(R.id.bioText);
        yearText = findViewById(R.id.year);
        majorText = findViewById(R.id.major);
        userPic= findViewById(R.id.userPic);



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
        userPic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userPic.setImageBitmap(null);
                if (Image != null)
                    Image.recycle();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY && resultCode != 0) {
            Uri mImageUri = data.getData();
            try {
                Image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                if (getOrientation(getApplicationContext(), mImageUri) != 0) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(getOrientation(getApplicationContext(), mImageUri));
                    if (rotateImage != null)
                        rotateImage.recycle();
                    rotateImage = Bitmap.createBitmap(Image, 0, 0, Image.getWidth(), Image.getHeight(), matrix,true);
                    userPic.setImageBitmap(rotateImage);
                } else
                    userPic.setImageBitmap(Image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static int getOrientation(Context context, Uri photoUri) {
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION },null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }
        cursor.moveToFirst();
        return cursor.getInt(0);
    }
}
