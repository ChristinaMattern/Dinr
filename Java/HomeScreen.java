
package com.example.dinr;
/**
 * @author Christina Mattern
 * @date 3/25/2019
 * @updated 5/6/2019
 * This is the home screen for the DINR application. It contains a RecyclerView for the HomeScreen items
 * Uses Home_Screen_Adapter.class and item.class
 */

// import statements
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity implements Home_Screen_Adapter.OnItemListener {
    private FirebaseAuth firebaseAuth;
    private TextView greeting;
    private String userId;
    private TextView locationText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        RecyclerView recyclerView = findViewById(R.id.rv_list);
        List<item> mList = new ArrayList<>(); // creates the Array List that holds the HomeScreen items
        mList.add(new item(R.drawable.cafe,"Dining Options")); // adds the Dining Options card to Array List
        mList.add(new item(R.drawable.search, "Canvas")); // adds the Canvas card to Array List
        mList.add(new item(R.drawable.friends,"Find Friends")); // adds the Friends card to Array List
        Home_Screen_Adapter adapter = new Home_Screen_Adapter(HomeScreen.this, mList, this);
        recyclerView.setAdapter(adapter); // sets the adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser(); // gets the current user
        userId=currentFirebaseUser.getUid();
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();  // creates a Database reference
        Query query = rootRef.child("users").orderByChild("userId").equalTo(userId); // finds the current user in the database
        locationText=findViewById(R.id.location);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fName="";
                String location="";
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    fName= (String) ds.child("fName").getValue();
                    location= (String) ds.child("location").getValue(); // gets the location from the database to be displayed
                }

                greeting = findViewById(R.id.home_screenTV);
                greeting.setText("Welcome, " + fName);
                if(location.equals("Offline")){
                    locationText.setText("You are currently offline");
                }else{
                    locationText.setText("Your current location is "+location);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);


    }

    /**
     * onCreateOptionsMenu creates the menu bar at the top of every activity
     * for navigational purposes
     * @param menu - uses the home_screen_menu in res->menu
     * @return Boolean true
     */
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
           /** case R.id.Friends:
                startActivity(new Intent(HomeScreen.this, FriendList.class));
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * onItemClick makes the cards in the RecyclerView clickable
     * @param position - takes the position of the item clicked and brings the user to its corresponding page
     */
    @Override
    public void onItemClick(int position) {
        switch(position){
            case 0:
                //Toast.makeText(this, "Dining Options", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, DiningOptions.class));
                break;
            case 1:
                startActivity(new Intent(this, Canvas1.class));
                break;
            case 2:
                //Toast.makeText(this, "Find Friends", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, FriendSearch.class));
                break;
            default:
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}