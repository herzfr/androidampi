package com.ampi.registrasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ampi.registrasi.utility.ConstantValue;
import com.ampi.registrasi.utility.Utilitas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;

public class ScannerActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "SCAN";
    private Button buttonScan;
    private ImageView imageAnggota;
    private TextView textName, textJabatan;

    //qr code scanner object
    private IntentIntegrator qrScan;

    //firebase
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        initView();
    }

    private void initView() {
        //View objects
        buttonScan = findViewById(R.id.buttonScan);
        textName = findViewById(R.id.tvName);
        textJabatan = findViewById(R.id.tvJabatan);
        imageAnggota = findViewById(R.id.imageAnggota);
        buttonScan.setOnClickListener(this::onClick);

        qrScan = new IntentIntegrator(this);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View v) {
        //initiating the qr code scan
        qrScan.initiateScan();
        clearViewData();
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                Log.e(TAG, "onActivityResult: " + result.getContents());
                getData(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //get data base on qrcode no reg
    private void getData(String noReg) {
        Log.e(TAG, "getData from firebase");
        DocumentReference docRef = db.collection("tamu").document(noReg);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.e(TAG, "DocumentSnapshot data: " + document.getData());

                        String noReg = document.getId();
                        String nama = document.getData().get("nama").toString();
                        String jabatan = document.getData().get("jabatan").toString();
                        String image = document.getData().get("image").toString();

                        textName.setText(nama);
                        textJabatan.setText(jabatan);
                        imageAnggota.setImageBitmap(Utilitas.base64toBitmap(image));

                        updateData(noReg, nama, jabatan, image);
                    } else {
                        Log.e(TAG, "No such document");
                        Utilitas.showCustomDialog(ScannerActivity.this, "Undangan Tidak Di Temukan", "Data Tamu Tidak Di Temukan ", "Tutup");
                        clearViewData();
                    }
                } else {
                    Log.e(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


    private void updateData(String noReg, String nama, String jabatan, String image) {
        Map<String, Object> user = new HashMap<>();
        user.put("status", true);

//        Map<String, Object> data = new HashMap<>();
//        data.put("capital", true);

        db.collection("tamu").document(noReg)
                .set(user, SetOptions.merge())
                .addOnSuccessListener(command -> {
                    Utilitas.showCustomDialog(this, "Data Terupdate", "Data Tamu Terupdate, ID : " + noReg, "Tutup");
                })
                .addOnFailureListener(command -> {
                    Toast.makeText(getApplicationContext(), "Gagal mengupdate data tamu", Toast.LENGTH_SHORT).show();
                });
    }

    private void clearViewData() {
        textJabatan.setText("---");
        textName.setText("---");
        imageAnggota.setImageBitmap(Utilitas.base64toBitmap(ConstantValue.defaultImage));
    }

}