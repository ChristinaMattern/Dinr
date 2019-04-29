package com.example.dinr;

/**
 * @author Angela Cebada
 * @date 04/25/2019
 * @updated 04/29/2019
 * This is the Settings page
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;


public class Settings extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    TextView textView1;
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        textView1 = (TextView) findViewById(R.id.changePasswordTV);
        textView1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Settings.this, ChangePassword.class);
                startActivity(intent);
            }
        });

        textView2 = (TextView) findViewById(R.id.profileTV);
        textView2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Settings.this, EditProfile.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        firebaseAuth = firebaseAuth.getInstance();
        switch (item.getItemId()){
            case R.id.Logout:
                Toast.makeText(Settings.this, "Logging Out...", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                startActivity(new Intent(Settings.this, LoginScreen.class));
                return true;
            case R.id.Help:
                startActivity(new Intent(Settings.this, Faq.class));
                return true;
            case R.id.Home:
                startActivity(new Intent(Settings.this, HomeScreen.class));
                return true;
            case R.id.MyProfile:
                startActivity(new Intent(Settings.this, MyProfile.class));
                return true;
            case R.id.EditProfile:
                startActivity(new Intent(Settings.this, EditProfile.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


