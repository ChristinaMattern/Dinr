package com.example.dinr;

/**
 * @author Angela Cebada
 * @date 04/17/2019
 * This is the Brubacher Cafe webpage
 */

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

public class BrubacherCafe extends AppCompatActivity {

    private WebView webView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dining_room);

        webView = (WebView) findViewById(R.id.activity_main_webview);

        // Enable Javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("https://strose.campusdish.com/LocationsAndMenus/BrubacherCafe");

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
        AlertDialog.Builder builder2 = new AlertDialog.Builder(BrubacherCafe.this);//first alert box
        builder2.setTitle("What do you want to change your location to");
        builder2.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String location = items[which];
                if(!location.equals("Offline")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(BrubacherCafe.this);//second alert box
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
                                    NotificationCompat.Builder b = new NotificationCompat.Builder(BrubacherCafe.this);
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
                                    NotificationManager nm = (NotificationManager) BrubacherCafe.this.getSystemService(Context.NOTIFICATION_SERVICE);
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

    //adds user's profile to database
    private void editUser(String id,String location, String locationTimeW) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(id).child("location");
        mDatabase.setValue(location);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(id).child("locationTime");
        mDatabase.setValue(locationTimeW);
        return;
    }

    public void onBackPressed(){
        if(webView.canGoBack()){
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.brubacher_cafe_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        firebaseAuth = firebaseAuth.getInstance();
        switch (item.getItemId()){
            case R.id.Settings:
                startActivity(new Intent(BrubacherCafe.this, Settings.class));
                return true;
            case R.id.Location:
                alert(userId);
                return true;
            case R.id.Logout:
                Toast.makeText(BrubacherCafe.this, "Logging Out...", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                startActivity(new Intent(BrubacherCafe.this, LoginScreen.class));
                return true;
            case R.id.Help:
                startActivity(new Intent(BrubacherCafe.this, Faq.class));
                return true;
            case R.id.MyProfile:
                startActivity(new Intent(BrubacherCafe.this, MyProfile.class));
                return true;
            case R.id.EditProfile:
                startActivity(new Intent(BrubacherCafe.this, EditProfile.class));
                return true;
            case R.id.Home:
                startActivity(new Intent(BrubacherCafe.this, HomeScreen.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
