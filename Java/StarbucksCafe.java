package com.example.dinr;

/**
 * @author Angela Cebada
 * @date 04/17/2019
 * This is the Starbucks Cafe webpage
 */

import android.content.Intent;
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

public class StarbucksCafe extends AppCompatActivity {

    private WebView webView;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dining_room);

        webView = (WebView) findViewById(R.id.activity_main_webview);

        // Enable Javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("https://strose.campusdish.com/LocationsAndMenus/StarbucksCafe");

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
        inflater.inflate(R.menu.starbucks_cafe_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        firebaseAuth = firebaseAuth.getInstance();
        switch (item.getItemId()){
            case R.id.Settings:
                startActivity(new Intent(StarbucksCafe.this, Settings.class));
                return true;
            case R.id.Logout:
                Toast.makeText(StarbucksCafe.this, "Logging Out...", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                startActivity(new Intent(StarbucksCafe.this, LoginScreen.class));
                return true;
            case R.id.Help:
                startActivity(new Intent(StarbucksCafe.this, Faq.class));
                return true;
            case R.id.MyProfile:
                startActivity(new Intent(StarbucksCafe.this, MyProfile.class));
                return true;
            case R.id.EditProfile:
                startActivity(new Intent(StarbucksCafe.this, EditProfile.class));
                return true;
            case R.id.Home:
                startActivity(new Intent(StarbucksCafe.this, HomeScreen.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
