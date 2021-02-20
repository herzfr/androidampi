package com.ampi.registrasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView;
    private Button btnScan , btnList, btnRegistrasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        textView = findViewById(R.id.txtView);
        btnScan = findViewById(R.id.btnScan);
        btnList = findViewById(R.id.btnList);
        btnRegistrasi = findViewById(R.id.btnRegistrasi);

        btnRegistrasi.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegistrasi:
                startActivity(new Intent(this, InputActivity.class));
                break;
            default:
                break;
        }
    }
}