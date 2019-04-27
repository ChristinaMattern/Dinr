package com.example.dinr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.in;

public class Settings extends AppCompatActivity implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    Spinner spinnermenu;
    TextView textView;
    Switch loginSwitch;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        spinnermenu = (Spinner) findViewById(R.id.spinner);

        List<String> fontList = new ArrayList<>();
        fontList.add("Default");
        fontList.add("Small");
        fontList.add("Large");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fontList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnermenu.setAdapter(adapter);
        spinnermenu.setOnItemSelectedListener(this);

        textView = (TextView) findViewById(R.id.profileTV);
        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Settings.this, EditProfile.class);
                startActivity(intent);
            }
        });

        loginSwitch = (Switch) findViewById(R.id.loginSwitch);  //initiate switch
        loginSwitch.setOnCheckedChangeListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {

            }
        else {
        }
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

