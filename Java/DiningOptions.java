package com.example.dinr;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DiningOptions extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private CardView diningCV;
    private CardView camelotCV;
    private CardView brubacherCV;
    private CardView starbucksCV;
    private CardView lallyCV;
    private FirebaseAuth firebaseAuth;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dining_options);

        diningCV = findViewById(R.id.diningHallCard);
        camelotCV = findViewById(R.id.camelotCard);
        brubacherCV = findViewById(R.id.brubacherCard);
        starbucksCV = findViewById(R.id.starbucksCard);
        lallyCV = findViewById(R.id.lallyCard);


        diningCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DiningOptions.this, DiningRoom.class));
            }
        });

        camelotCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DiningOptions.this, CamelotRoom.class));
            }
        });

        brubacherCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DiningOptions.this, BrubacherCafe.class));
            }
        });

        starbucksCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DiningOptions.this, StarbucksCafe.class));
            }
        });

        lallyCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DiningOptions.this, LallyCafe.class));
            }
        });

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = currentFirebaseUser.getUid();
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query query = rootRef.child("users").orderByChild("userId").equalTo(userId);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id = (String) ds.getKey();//retrieves user's id to know where to put info into profile
                    userId = id;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);
    }
        public void alert(final String userId){
            final String[] items = new String[]{"Offline","Dining Hall", "Camelot Room", "Brubacher Cafe", "Starbucks", "Lally Cafe"};
            final String[] timeW = {"20 minutes", "30 minutes", "40 minutes", "50 minutes", "60 minutes"};
            final int[] timeMS = {1200000, 1800000, 2400000, 3000000, 3600000};//holds the millisecond values
            final int[] timeM = {20, 30, 40, 50, 60};//holds the minute values
            AlertDialog.Builder builder2 = new AlertDialog.Builder(DiningOptions.this);//first alert box
            builder2.setTitle("What do you want to change your location to");
            builder2.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String location = items[which];
                    if(!location.equals("Offline")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(DiningOptions.this);//second alert box
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
                                    public void run() {
                                        NotificationCompat.Builder b = new NotificationCompat.Builder(DiningOptions.this);
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
                                        NotificationManager nm = (NotificationManager) DiningOptions.this.getSystemService(Context.NOTIFICATION_SERVICE);
                                        nm.notify(1, b.build());
                                    }
                                }, timerMili);
                                editUser(userId, location, locationTimeW);
                            }
                        });
                        builder.show();
                    }
                    else {//if offline is chosen skips second alert
                        String locationTIme = " ";
                        editUser(userId, location, locationTIme);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dining_options_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        firebaseAuth = firebaseAuth.getInstance();
        switch (item.getItemId()){
            case R.id.Settings:
                startActivity(new Intent(DiningOptions.this, Settings.class));
                return true;
            case R.id.Location:
                alert(userId);
                return true;
            case R.id.Logout:
                Toast.makeText(DiningOptions.this, "Logging Out...", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                startActivity(new Intent(DiningOptions.this, LoginScreen.class));
                return true;
            case R.id.Help:
                startActivity(new Intent(DiningOptions.this, Faq.class));
                return true;
            case R.id.MyProfile:
                startActivity(new Intent(DiningOptions.this, MyProfile.class));
                return true;
            case R.id.EditProfile:
                startActivity(new Intent(DiningOptions.this, EditProfile.class));
                return true;
            case R.id.Home:
                startActivity(new Intent(DiningOptions.this, HomeScreen.class));
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
