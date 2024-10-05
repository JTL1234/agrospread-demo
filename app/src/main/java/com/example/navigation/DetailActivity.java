package com.example.navigation;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View; // Impor untuk View

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private int clickCount; // Tambahkan variabel untuk menyimpan jumlah klik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView detailImage = findViewById(R.id.detailImageView);
        TextView bookTitleTextView = findViewById(R.id.bookTitleTextView);
        TextView clickCountTextView = findViewById(R.id.clickCountTextView);
        WebView webView = findViewById(R.id.webView);  // Inisialisasi WebView

        // Ambil data dari Intent
        int imageResId = getIntent().getIntExtra("imageResId", -1);
        clickCount = getIntent().getIntExtra("clickCount", 0); // Ambil clickCount dari Intent
        String bookTitle = getIntent().getStringExtra("bookTitle");

        // Set data ke UI
        if (imageResId != -1) {
            detailImage.setImageResource(imageResId);
        }
        bookTitleTextView.setText(bookTitle);
        clickCountTextView.setText("Jumlah Klik: " + clickCount);

        // Inisialisasi dan set WebView Settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Muat URL di WebView
        webView.loadUrl("https://www.erlanggaonline.com/");

        // Tambahkan OnClickListener untuk detailImage
        detailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount++; // Tambah jumlah klik
                clickCountTextView.setText("Jumlah Klik: " + clickCount); // Perbarui tampilan jumlah klik
            }
        });
    }
}
