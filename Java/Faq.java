
package com.example.dinr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Faq extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq);


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
                startActivity(new Intent(Faq.this, Settings.class));
                return true;
            case R.id.Logout:
                Toast.makeText(Faq.this, "Logging Out...", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                startActivity(new Intent(Faq.this, LoginScreen.class));
                return true;
            case R.id.Home:
                startActivity(new Intent(Faq.this, HomeScreen.class));
                return true;
            case R.id.MyProfile:
                startActivity(new Intent(Faq.this, MyProfile.class));
                return true;
            case R.id.EditProfile:
                startActivity(new Intent(Faq.this, EditProfile.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
