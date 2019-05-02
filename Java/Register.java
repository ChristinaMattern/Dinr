package com.example.dinr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Register extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private DataSnapshot databaseRef;
    private Button registerButton;
    private EditText newUserFirstName, newUserLastName, studentID, studentEmail, newUserPassword, confirmPassword;
    private TextView missingInfo;
    private FirebaseAuth firebaseAuth;
    private ImageView userPic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        userPic=findViewById(R.id.userPic);
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
                                            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                                            String fNameS=fName.toLowerCase();
                                            writeNewUser(currentFirebaseUser.getUid(),id, fName, lName, email, password, fNameS);
                                            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                                            FirebaseStorage storage = FirebaseStorage.getInstance();
                                            final StorageReference storageRef = storage.getReference();
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
    private void writeNewUser(String userId, String id, String fName, String lName, String email, String password,String fNameS) {

        mDatabase.child("users").child(id).setValue(id);
        mDatabase.child("users").child(id).child("fName").setValue(fName);
        mDatabase.child("users").child(id).child("lName").setValue(lName);
        mDatabase.child("users").child(id).child("email").setValue(email);
        mDatabase.child("users").child(id).child("password").setValue(password);
        mDatabase.child("users").child(id).child("userId").setValue(userId);
        mDatabase.child("users").child(id).child("bio").setValue(" ");
        mDatabase.child("users").child(id).child("major").setValue(" ");
        mDatabase.child("users").child(id).child("location").setValue("offline ");
        mDatabase.child("users").child(id).child("year").setValue(" ");
        mDatabase.child("users").child(id).child("fNameS").setValue(fNameS);
        mDatabase.child("users").child(id).child("flist").setValue("Friends List");



    }

    //makes sure the user's fields are correct
    private Boolean validate(String fName, String lName, final String id, String email, String password, String confirmPass) {
        if (!fName.isEmpty() && !lName.isEmpty() && !id.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPass.isEmpty() && password.equals(confirmPass) && password.length() >= 6 && id.length()==9) {
            return true;
        } else if (password.length() < 6) {
            Toast.makeText(Register.this, "Passwords must be 6 numbers or longer!", Toast.LENGTH_SHORT).show();
        } else if(id.length()<9|| id.length()>9){
            Toast.makeText(Register.this, "Saint Rose ID#s are 9 digits long", Toast.LENGTH_SHORT).show();
        }else if (!password.equals(confirmPass)) {
            Toast.makeText(Register.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Register.this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}