
 package com.example.dinr;
 /**
 * @author Christina Mattern
 * @date 3/25/2019
 * This is the home screen for the DINR application
 */

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        RecyclerView recyclerView = findViewById(R.id.rv_list);
        List<item> mList = new ArrayList<>();
        mList.add(new item(R.drawable.cafe,"Go to Dining Options"));
        mList.add(new item(R.drawable.search, "Go to Canvas"));
        mList.add(new item(R.drawable.friends,"Find Friends"));
        Home_Screen_Adapter adapter = new Home_Screen_Adapter(HomeScreen.this, mList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        userId=currentFirebaseUser.getUid();
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query query = rootRef.child("users").orderByChild("userId").equalTo(userId);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fName="";
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    fName= (String) ds.child("fName").getValue();
                }

                greeting = findViewById(R.id.home_screenTV);
                greeting.setText("Welcome, " + fName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);


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
                Toast.makeText(HomeScreen.this, "Settings", Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(this, MyProfile.class));
                break;
            case 1:
                startActivity(new Intent(this, EditProfile.class));
                break;
            case 2:
                startActivity(new Intent(this, Faq.class));
                break;
            default:
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}