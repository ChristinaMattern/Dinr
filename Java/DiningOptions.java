package com.example.dinr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class DiningOptions extends AppCompatActivity {

    private CardView diningCV;
    private CardView camelotCV;
    private CardView brubacherCV;
    private CardView starbucksCV;
    private CardView lallyCV;
    private FirebaseAuth firebaseAuth;

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
                Toast.makeText(DiningOptions.this, "Dining Hall!", Toast.LENGTH_SHORT).show();
            }
        });

        camelotCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DiningOptions.this, "Camelot!", Toast.LENGTH_SHORT).show();
            }
        });

        brubacherCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DiningOptions.this, "Brubacher!", Toast.LENGTH_SHORT).show();
            }
        });

        starbucksCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DiningOptions.this, "Starbucks!", Toast.LENGTH_SHORT).show();
            }
        });

        lallyCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DiningOptions.this, "Lally!", Toast.LENGTH_SHORT).show();
            }
        });
    } @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dining_options_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        firebaseAuth = firebaseAuth.getInstance();
        switch (item.getItemId()){
            case R.id.Home:
                startActivity(new Intent(DiningOptions.this, HomeScreen.class));
            case R.id.Settings:
                startActivity(new Intent(DiningOptions.this, Settings.class));
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
