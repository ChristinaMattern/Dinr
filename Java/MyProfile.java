package com.example.dinr;
/*@author Nola Smtih
@date 4/17/2019 */

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    private Button locationB;
    private TextView locationWord;
    private TextView locationTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile);
        fullName = (TextView)findViewById(R.id.fullName);
        bioText = (TextView)findViewById(R.id.bioText);
        yearText = (TextView)findViewById(R.id.yearText);
        majorText = (TextView)findViewById(R.id.majorText);
        editButton = (Button)findViewById(R.id.editButton);
        locationWord = (TextView)findViewById(R.id.locationWord);
        locationB = (Button)findViewById(R.id.location);
        locationTime=(TextView)findViewById(R.id.locationTime);
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
                String locationTimeD=" ";
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id = (String) ds.getKey();//retrieves user's id to know where to put info into profile
                    userId=id;
                    fName= (String) ds.child("fName").getValue();//retrieves first name
                    lName= (String) ds.child("lName").getValue();//retrieves last name
                    bio= (String) ds.child("bio").getValue();//retrieves bio
                    year= (String) ds.child("year").getValue();//retrieves year
                    major= (String) ds.child("major").getValue();//retrieves major
                    location=(String) ds.child("location").getValue();//retrieves location
                    locationTimeD=(String)ds.child("locationTime").getValue();//retrieves location time
                    //retrieves the photo
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    StorageReference photoReference= storageReference.child(id).child("image");
                    userPic = (ImageView)findViewById(R.id.userPic);
                    final long ONE_MEGABYTE = 4000 * 4000;//how big the photo can be
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
                    locationWord.setText(location);
                    locationTime.setVisibility(View.GONE);//does not show a location time
                }
                else {
                    locationWord.setText("At: " + location);//sets location
                    locationTime.setVisibility(View.VISIBLE);
                    locationTime.setText("Till: " + locationTimeD);//sets location time
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);

        locationB.setOnClickListener(new View.OnClickListener(){//button to change location
            @Override
            public void onClick(View view) {
                alert(userId);//alert that allows you to choose your location and how long and sets each
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//takes you to the user's edit profile page

                startActivity(new Intent(MyProfile.this, EditProfile.class));
            }
        });
    }

    public void alert(final String userId){//alert that allows you to choose your location and how long and sets each
        final String[] items = new String[]{"Offline","Dining Hall", "Camelot Room", "Brubacher Cafe", "Starbucks", "Lally Cafe"};//holds the locations
        final String[] timeW = {"20 minutes", "30 minutes", "40 minutes", "50 minutes", "60 minutes"};//holds the time options
        final int[] timeMS = {1200000, 1800000, 2400000, 3000000, 3600000};//holds the millisecond values
        final int[] timeM = {20, 30, 40, 50, 60};//holds the minute values
        AlertDialog.Builder builder2 = new AlertDialog.Builder(MyProfile.this);//first alert box
        builder2.setTitle("What do you want to change your location to");
        builder2.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String location = items[which];
                if(!location.equals("Offline")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyProfile.this);//second alert box
                    builder.setTitle("How long do you plan on being there?");
                    builder.setItems(timeW, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int timerMinute = timeM[which];//retrieves the corresponding minute with word
                            int timerMili=timeMS[which];//retrieves the corresponding millisecond value with word
                            String locationTimeW=" ";
                            try {
                                locationTimeW = timeDisplay(timerMinute);//retrieves the new time the user said they will be there untill
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {//suppose to send a notification after the time time is up and set the location to offline
                                    locationWord.setText("Offline");
                                    NotificationCompat.Builder b = new NotificationCompat.Builder(MyProfile.this);
                                    b.setAutoCancel(true)
                                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                                            .setWhen(System.currentTimeMillis())
                                            .setSmallIcon(R.drawable.test_icon)
                                            .setTicker("Are you still at your location?")
                                            .setContentTitle("Are You Still There?")
                                            .setContentText("The amount of time you said " +
                                                    "you would be at your location has been reach. You are being put offline until you " +
                                                    "update your time and location on your profile.")
                                            .setContentInfo("INFO");
                                    NotificationManager nm = (NotificationManager) MyProfile.this.getSystemService(Context.NOTIFICATION_SERVICE);
                                    nm.notify(1, b.build());
                                }
                            }, timerMili);
                            locationWord.setText("At: "+location);//changes the location
                            locationTime.setVisibility(View.VISIBLE);
                            locationTime.setText("Till: "+locationTimeW);//changes the location time
                            editUser(userId, location, locationTimeW);//changes it in the database
                        }
                    });
                    builder.show();
                }
                else {//if offline is chosen skips second alert
                    locationWord.setText(location);//changes location
                    locationTime.setVisibility(View.GONE);
                    String locationTimeW=" ";
                    editUser(userId, location,locationTimeW);//changes it in the database
                }
            }
        });
        builder2.show();
    }

    public String timeDisplay(int minutes) throws ParseException {//adds th eminutes to the current time to get the location time

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String currentDateandTime = df.format(new Date());
        Date date = df.parse(currentDateandTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        String newTime = df.format(calendar.getTime());
        return newTime;
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
    private void editUser(String id,String location, String locationTimeW) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(id).child("location");
        mDatabase.setValue(location);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(id).child("locationTime");
        mDatabase.setValue(locationTimeW);
        return;
    }

}