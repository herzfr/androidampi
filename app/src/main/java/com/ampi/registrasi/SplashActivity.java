package com.ampi.registrasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    private int loading = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // setelah loading maka akan langsung berpindah ke Main activity
                Intent home = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(home);

                finish();
            }
        },loading);

    }
}