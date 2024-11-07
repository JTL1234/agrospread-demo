package com.limpag.agrospread;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View; // Impor untuk View

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private int clickCount; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView detailImage = findViewById(R.id.detailImageView);
        TextView agroTitleTextView = findViewById(R.id.agroTitleTextView);
        TextView clickCountTextView = findViewById(R.id.clickCountTextView);
        WebView webView = findViewById(R.id.webView);  


        int imageResId = getIntent().getIntExtra("imageResId", -1);
        clickCount = getIntent().getIntExtra("clickCount", 0);
        String bookTitle = getIntent().getStringExtra("bookTitle");

        if (imageResId != -1) {
            detailImage.setImageResource(imageResId);
        }
        agroTitleTextView.setText(bookTitle);
        clickCountTextView.setText("Agro Titles: " + clickCount);


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("https://jethrolimpag.wordpress.com/");


        detailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount++;
                clickCountTextView.setText("Agro Titles: " + clickCount);
            }
        });
    }
}
