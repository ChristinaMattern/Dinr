package com.example.dinr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.ms.square.android.expandabletextview.ExpandableTextView;

public class Faq extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    ExpandableTextView expandableTextView1;
    ExpandableTextView expandableTextView2;
    ExpandableTextView expandableTextView3;
    ExpandableTextView expandableTextView4;
    ExpandableTextView expandableTextView5;
    ExpandableTextView expandableTextView6;
    ExpandableTextView expandableTextView7;

    String quest1 ="How do I change my profile picture?" + "\n\n" + "Go to edit your profile and click on the picture and you will be asked to select a photo from your library.";
    String quest2 ="How do I let others know I am currently at a dining location?" + "\n\n" + "On your profile beneath your name there is a location list where you can choose where you are currently located or if you are offline, so when others click on your profile they will see your location.";
    String quest3 ="How do I change my password?" + "\n\n" + "You can change your password by going into Settings and clicking on 'Change Password'";
    String quest4 ="How do I search for friends?" + "\n\n" + "You can access friend search on the home page of the app. Once you click on 'Friend Search' you will have the option to search for your friends by name, where you can access their profile, add or remove them as friends, and view their location and duration.";
    String quest5 ="Why can't I find my friend?" + "\n\n" + "You may not be able to find someone because their profile might be set to private or they may not have created an account yet.";
    String quest6 ="Do I need to sign in on the Dining pages?" + "\n\n" + "No, these pages are run by The College of Saint Rose. You do not need to sign in to view the web page.";
    String quest7 ="Can't find your answer?" + "\n\n" + "Email us at info@dinr.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq);

        expandableTextView1 = (ExpandableTextView) findViewById(R.id.expandable_text_view1);
        expandableTextView1.setText(quest1);
        expandableTextView2 = (ExpandableTextView) findViewById(R.id.expandable_text_view2);
        expandableTextView2.setText(quest2);
        expandableTextView3 = (ExpandableTextView) findViewById(R.id.expandable_text_view3);
        expandableTextView3.setText(quest3);
        expandableTextView4 = (ExpandableTextView) findViewById(R.id.expandable_text_view4);
        expandableTextView4.setText(quest4);
        expandableTextView5 = (ExpandableTextView) findViewById(R.id.expandable_text_view5);
        expandableTextView5.setText(quest5);
        expandableTextView6 = (ExpandableTextView) findViewById(R.id.expandable_text_view6);
        expandableTextView6.setText(quest6);
        expandableTextView7 = (ExpandableTextView) findViewById(R.id.expandable_text_view7);
        expandableTextView7.setText(quest7);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.faq_menu, menu);
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
