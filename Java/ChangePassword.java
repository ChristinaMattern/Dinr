package com.example.dinr;

/**
 * @author Angela Cebada
 * @date 04/29/2019
 * This is the page where the user can update their password
 * It links to the Firebase database where it will verify the user and update if successful
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChangePassword extends AppCompatActivity {

    private DatabaseReference mDatabase;
    EditText oldPassword;
    EditText changePassword1;
    EditText changePassword2;
    FirebaseAuth firebaseAuth;
    ProgressDialog dialog;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        oldPassword = (EditText) findViewById(R.id.changePassTV);
        changePassword1 = (EditText) findViewById(R.id.changePassTV1);
        changePassword2 = (EditText) findViewById(R.id.changePassTV2);
        firebaseAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        userId=currentFirebaseUser.getUid();//retrieves user id of signed in user

        final Button button = findViewById(R.id.saveChangeButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String oldPass = oldPassword.getText().toString();
                changePass1(v, oldPass, userId);// Code here executes on main thread after user presses button
            }
        });
    }

    public void changePass1(View v, String oldPassword, final String userId) {
        final String newPassword = changePassword1.getText().toString();
        final String confirmPass = changePassword2.getText().toString();
        final String[] userId2 = {userId};
        final String oldPass = oldPassword;
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();//gets current user logged in and verifies credentials
        if (!oldPassword.isEmpty()) {
            final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            final Query query = rootRef.child("users").orderByChild("userId").equalTo(userId);//finds the email in the database
            final ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String password = "";
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String id = (String) ds.getKey();//retrieves user's id to know where to put info into profile
                        userId2[0] =id;
                        password = (String) ds.child("password").getValue();//retrieves the email associated with the id
                    }
                    if (oldPass.equals(password)) {//compares the data base information with the entry
                        if (validate(newPassword, confirmPass)) {       //verifies that both password entries match
                            if (user != null) {      //if user is logged in proceed to change password
                                dialog.setMessage("Changing password, please wait...");
                                dialog.show();

                                user.updatePassword(confirmPass).addOnCompleteListener(new OnCompleteListener<Void>() {    //updates password in database
                                    @Override
                                    public void onComplete(Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            dialog.dismiss();
                                            editUser(userId2[0], confirmPass);
                                            Toast.makeText(ChangePassword.this, "Password has been updated!", Toast.LENGTH_SHORT).show();
                                            firebaseAuth.signOut();
                                            finish();
                                            Intent intent = new Intent(ChangePassword.this, LoginScreen.class);
                                            startActivity(intent);
                                        } else {
                                            dialog.dismiss();
                                            Toast.makeText(ChangePassword.this, "Password could not be updated", Toast.LENGTH_SHORT).show();
                                        }
                                    }


                                });
                            }else {
                                Toast.makeText(ChangePassword.this, "Sign in again to change password", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(ChangePassword.this, "Unsuccessful", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(ChangePassword.this, "Old password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled (@NonNull DatabaseError databaseError){
                }
            };
            query.addListenerForSingleValueEvent(valueEventListener);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.change_password_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        firebaseAuth = firebaseAuth.getInstance();
        switch (item.getItemId()) {
            case R.id.Logout:
                Toast.makeText(ChangePassword.this, "Logging Out...", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                startActivity(new Intent(ChangePassword.this, LoginScreen.class));
                return true;
            case R.id.Help:
                startActivity(new Intent(ChangePassword.this, Faq.class));
                return true;
            case R.id.Home:
                startActivity(new Intent(ChangePassword.this, HomeScreen.class));
                return true;
            case R.id.MyProfile:
                startActivity(new Intent(ChangePassword.this, MyProfile.class));
                return true;
            case R.id.EditProfile:
                startActivity(new Intent(ChangePassword.this, EditProfile.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private Boolean validate(String password, String confirmPass) {
        if (!password.isEmpty() && !confirmPass.isEmpty() && password.equals(confirmPass) && password.length() >= 6) {
                return true;
            } else if (password.length() < 6) {
                Toast.makeText(ChangePassword.this, "Passwords must be 6 numbers or longer!", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPass)) {
                Toast.makeText(ChangePassword.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ChangePassword.this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    private void editUser(String id,String password) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(id).child("password");
        mDatabase.setValue(password);
        return;
    }
}
