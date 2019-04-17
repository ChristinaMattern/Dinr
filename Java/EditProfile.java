package com.example.dinr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class EditProfile extends AppCompatActivity {
    private static Bitmap Image = null;
    private static Bitmap rotateImage = null;
    private static ImageButton userPic;
    private static final int GALLERY = 1;
    private Button saveButton;
    private TextView fullName;
    private EditText bioTextOld;
    private EditText bioTextNew;
    private RadioGroup yearTextOld;
    private RadioGroup yearTextNew;
    private Bitmap userImage;
    private EditText majorTextOld;
    private EditText majorTextNew;
    private String userId;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        saveButton = findViewById(R.id.save_button);
        bioTextOld = findViewById(R.id.bioText);
        yearTextOld = findViewById(R.id.year);
        majorTextOld = findViewById(R.id.major);
        bioTextNew = findViewById(R.id.bioText);
        yearTextNew= findViewById(R.id.year);
        majorTextNew = findViewById(R.id.major);
        userPic= findViewById(R.id.userPic);
        fullName=findViewById(R.id.fullName);
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
                //retrieves current profile data
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    fName= (String) ds.child("fName").getValue();//retrieves first name
                    lName= (String) ds.child("lName").getValue();//retrieves last name
                    bio= (String) ds.child("bio").getValue();//retrieves bio
                    year= (String) ds.child("year").getValue();//retrieves year
                    major= (String) ds.child("major").getValue();//retrieves major
                }

                fullName.setText(fName+" "+lName);//combines first and last name to create full name
                bioTextOld.setText(bio);//sets old bio
                majorTextOld.setText(major);//sets old major
                if(year.equals("Freshman")) {//checks year to know which to check
                    ((RadioButton) yearTextOld.getChildAt(1)).setChecked(true);
                }
                if(year.equals("Sophmore")) {
                    ((RadioButton) yearTextOld.getChildAt(2)).setChecked(true);
                }
                if(year.equals("Junior")) {
                    ((RadioButton) yearTextOld.getChildAt(3)).setChecked(true);
                }
                if(year.equals("Senior")) {
                    ((RadioButton) yearTextOld.getChildAt(4)).setChecked(true);
                }
                if(year.equals("Graduate")) {
                    ((RadioButton) yearTextOld.getChildAt(1)).setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RadioGroup rg = (RadioGroup)findViewById(R.id.year);
                final String yearText= ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();
                final String bio=bioTextNew.getText().toString();
                final String major= majorTextNew.getText().toString();
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                userId=currentFirebaseUser.getUid();//retrieves current user
                final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                final StorageReference storageRef = storage.getReference();
                StorageReference imagesRef = storageRef.child("images");
                final Query query = rootRef.child("users").orderByChild("userId").equalTo(userId);//finds user in database
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            String id = (String) ds.getKey();//retrieves user's id to know where to put info into profile
                            StorageReference imagesRef= storageRef.child(id).child("image");
                            userPic.setDrawingCacheEnabled(true);
                            userPic.buildDrawingCache();
                            Bitmap bitmap = ((BitmapDrawable) userPic.getDrawable()).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data = baos.toByteArray();
                            UploadTask uploadTask = imagesRef.putBytes(data);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                }
                            });
                            editUser(id, bio, yearText, major);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(EditProfile.this, MyProfile.class));
                    }
                }, 1000);//delays opening for 1 second to allow firebase to update


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
                    userImage=Image;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_profile_menu, menu);
        return true;

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        firebaseAuth = firebaseAuth.getInstance();
        switch (item.getItemId()){
            case R.id.Settings:
                Toast.makeText(EditProfile.this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.Logout:
                Toast.makeText(EditProfile.this, "Logging Out...", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                startActivity(new Intent(EditProfile.this, LoginScreen.class));
                return true;
            case R.id.Help:
                startActivity(new Intent(EditProfile.this, Faq.class));
                return true;
            case R.id.Home:
                startActivity(new Intent(EditProfile.this, HomeScreen.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}