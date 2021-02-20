package com.ampi.registrasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import com.ampi.registrasi.model.Anggota;
import com.ampi.registrasi.service.AnggotaAdapter;
import com.ampi.registrasi.utility.ConstantValue;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class AnggotaList extends AppCompatActivity {

    GridView gridView;
    ArrayList<Anggota> list;
    AnggotaAdapter anggotaAdapter = null;
    FirebaseFirestore db;
    String TAG = "AnggotaList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anggota_list);

        gridView = (GridView) findViewById(R.id.gridView);
        list = new ArrayList<>();
        anggotaAdapter = new AnggotaAdapter(this, R.layout.button_image_layout, list);
        gridView.setAdapter(anggotaAdapter);

        anggotaAdapter.notifyDataSetChanged();

        initFirebase();
    }


    private void initFirebase() {
        db = FirebaseFirestore.getInstance();
        loadData();
    }


    private void loadData() {
        db.collection("tamu")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e(TAG, document.getId() + " => " + document.getData());
                                String nama = document.getData().get("nama").toString();
                                String jabatan = document.getData().get("jabatan").toString().isEmpty() ? "-" : document.getData().get("jabatan").toString();
                                String image = document.getData().get("image").toString().isEmpty() ? ConstantValue.defaultImage : document.getData().get("image").toString();
                                String id = document.getId();
                                String status = document.getData().get("status").toString();

                                list.add(new Anggota(i, nama, id, image, status, jabatan, 1));
                                i++;
                                anggotaAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                            Toasty.error(AnggotaList.this, "Gagal memuat data, check koneksi anda", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}