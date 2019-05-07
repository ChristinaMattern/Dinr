package com.example.dinr;
/**
 * @author Christina Mattern
 * @date 3/25/2019
 * This is the home screen for the DINR application
 * @updated by Angela Cebada on 5/7/2019
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class HomeScreen extends AppCompatActivity implements Home_Screen_Adapter.OnItemListener {
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private TextView greeting;
    private String userId;
    private TextView locationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        RecyclerView recyclerView = findViewById(R.id.rv_list);
        List<item> mList = new ArrayList<>();
        mList.add(new item(R.drawable.test_icon, "Dining Options"));
        mList.add(new item(R.drawable.test_icon, "Canvas"));
        mList.add(new item(R.drawable.test_icon, "Find Friends"));
        Home_Screen_Adapter adapter = new Home_Screen_Adapter(HomeScreen.this, mList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = currentFirebaseUser.getUid();
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query query = rootRef.child("users").orderByChild("userId").equalTo(userId);
        locationText=findViewById(R.id.location);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fName = "";
                String location = " ";
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id = (String) ds.getKey();//retrieves user's id to know where to put info into profile
                    userId = id;
                    fName = (String) ds.child("fName").getValue();
                    location = (String) ds.child("location").getValue();//retrieves location
                }

                greeting = findViewById(R.id.home_screenTV);
                greeting.setText("Welcome, " + fName);
                if(location.equals("Offline")){
                    locationText.setText("You are currently offline");
                }else{
                    locationText.setText("Your current location is "+ location);
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
        final int[] timeN = {20, 30, 40, 50, 60};//holds values for conversion
        AlertDialog.Builder builder2 = new AlertDialog.Builder(HomeScreen.this);//first alert box
        builder2.setTitle("What do you want to change your location to");
        builder2.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String location = items[which];
                if(!location.equals("Offline")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);//second alert box
                    builder.setTitle("How long do you plan on being there?");
                    builder.setItems(timeW, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int timer = timeN[which];//retrieves the corresponding number with word
                            timer = timer * 10000;//converts to milliseconds
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                            Toast.makeText(HomeScreen.this, "Current Time: " + format.format(calendar.getTime()), Toast.LENGTH_SHORT).show();

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    locationText.setText("You are currently offline");
                                    NotificationCompat.Builder b = new NotificationCompat.Builder(HomeScreen.this);
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
                                    NotificationManager nm = (NotificationManager) HomeScreen.this.getSystemService(Context.NOTIFICATION_SERVICE);
                                    nm.notify(1, b.build());
                                }
                            }, timer);
                            locationText.setText("Your current location is "+location);//changes location
                            editUser(userId, location);
                        }
                    });
                    builder.show();
                }
                else {//if offline is chosen skips second alert
                    locationText.setText("You are currently offline");//changes location
                    editUser(userId, location);
                }
            }
        });
        builder2.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_screen_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        firebaseAuth = firebaseAuth.getInstance();
        switch (item.getItemId()){
            case R.id.Settings:
                startActivity(new Intent(HomeScreen.this, Settings.class));
                return true;
            case R.id.Location:
                alert(userId);
                return true;
            case R.id.Logout:
                Toast.makeText(HomeScreen.this, "Logging Out...", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                startActivity(new Intent(HomeScreen.this, LoginScreen.class));
                return true;
            case R.id.Help:
                startActivity(new Intent(HomeScreen.this, Faq.class));
                return true;
            case R.id.MyProfile:
                startActivity(new Intent(HomeScreen.this, MyProfile.class));
                return true;
            case R.id.EditProfile:
                startActivity(new Intent(HomeScreen.this, EditProfile.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onItemClick(int position) {
        switch(position){
            case 0:
                startActivity(new Intent(this, DiningOptions.class));
                break;
            case 1:
                startActivity(new Intent(this, Canvas.class));
                break;
            case 2:
                startActivity(new Intent(this, FriendSearch.class));
                break;
            default:
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
    //adds user's profile to database
    private void editUser(String id,String location) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(id).child("location");
        mDatabase.setValue(location);
        return;
    }
}
