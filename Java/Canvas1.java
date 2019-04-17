package com.example.dinr;

/**
 * @author Angela Cebada
 * @date 04/17/2019
 * This is the Canvas page
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Canvas1 extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canvas1);

        webView = (WebView) findViewById(R.id.activity_main_webview);

        // Enable Javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("https://login.strose.edu/adfs/ls/?SAMLRequest=fZLLbsIwEEX3%2FYrI%2B7woJMEikSioKhJtEdAuuqmMMwFLiZ167D7%2BviahKl2U7fiee%2BfhCbKmbunUmoNcw5sFNN5nU0uk3UNOrJZUMRRIJWsAqeF0M71f0kEQ0VYro7iqyRlymWCIoI1QkniLeU5ed0kWJ1mW%2BilnI3%2BYRuBnMeP%2BruIw5pCW1%2BOEeM%2Bg0TE5cRYORLSwkGiYNK4UxWM%2FGvpxuo0TGo3oIHsh3tzNISQzHXUwpkUahrXaCxmg0QohgNKGrKwwrDEk3vSnr5mSaBvQG9DvgsPTevnLn0jhorXlxmoIuGp62%2FA4O%2FFWp43cCFkKub%2B8jF0vQnq33a781eNmS4rJ0Yd2I%2BriGPx%2F7lE5mITnwKS%2F5oOLWsxXqhb8y7tVumHm%2F07iIO4qovSrTkqtxBa4qASUbjN1rT5mGpiBnLh8IGHRh%2F79NcXVNw%3D%3D&SigAlg=http%3A%2F%2Fwww.w3.org%2F2001%2F04%2Fxmldsig-more%23rsa-sha256&Signature=oSUL2IHlQ%2F43JxBMVNLTp%2FqqbtMyzLAiusEfYlsI0MPENEA%2F52uAHkRoe9VHliIIbBtePfgMVYK8G1VaYB9a%2BKFsSUBfM1SCH1YNg1XtSBsRJ5GaGtnBGyda9rW8kEL4TouMBbw0pdyCZgXHHK%2B6Eg5Kq%2B6v40rmnUbv%2Bzh9Be3hB5pUW1HaakdciU0phcXnWy0nkqAeqvt3U0qU0yriKG30OMrewY%2B0JX29HHD514HvYU6zWST1aGacRQDyb%2BYqW3mN86pnQLxnw0%2BpPlJavXBJ4ED8Bt3rybu7e43DvDAob3iaajdSfHei5ur9MRmfaQD871bZoZc2OW0w%2Bvs61A%3D%3D");

    }

    public void onBackPressed(){
        if(webView.canGoBack()){
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}