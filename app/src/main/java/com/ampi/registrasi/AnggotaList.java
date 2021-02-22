package com.ampi.registrasi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.ampi.registrasi.model.Anggota;
import com.ampi.registrasi.service.AnggotaAdapter;
import com.ampi.registrasi.utility.ConstantValue;
import com.ampi.registrasi.utility.Utilitas;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import es.dmoral.toasty.Toasty;

public class AnggotaList extends AppCompatActivity implements SearchView.OnQueryTextListener {

    GridView gridView;
    ArrayList<Anggota> list;
    AnggotaAdapter anggotaAdapter = null;
    FirebaseFirestore db;
    private SwipeRefreshLayout swipeContainer;
    private LinearLayout parentPanelLL;
    private SearchView searchName;
    private Button btnRefreshTamu;
    String TAG = "AnggotaList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anggota_list);

        gridView = findViewById(R.id.gridView);
        list = new ArrayList<>();
        anggotaAdapter = new AnggotaAdapter(this, R.layout.button_image_layout, list);
        gridView.setAdapter(anggotaAdapter);

        anggotaAdapter.notifyDataSetChanged();
        initFirebase();

        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setEnabled(false);

        searchName = findViewById(R.id.searchName);
        searchName.setOnQueryTextListener(this);

        btnRefreshTamu = findViewById(R.id.btnRefreshTamu);
        btnRefreshTamu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilitas.updateAll(v.getContext());
            }
        });


//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                try {
//                    anggotaAdapter.notifyDataSetChanged();
//                    swipeContainer.setRefreshing(false);
//                }
//                catch(NullPointerException e){
//                    list.clear();
//                    initFirebase();
//                    anggotaAdapter = new AnggotaAdapter(getApplicationContext(), R.layout.button_image_layout, list);
//                    gridView.setAdapter(anggotaAdapter);
//                    anggotaAdapter.notifyDataSetChanged();
//                    swipeContainer.setRefreshing(false);
//                }
//
//            }
//        });

        parentPanelLL = findViewById(R.id.parentPanelLL);


//        swipeContainer.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                Log.e(TAG, "onScrollChanged: " + parentPanelLL.getScaleY() );
//                if (parentPanelLL.getScrollY() == 1.0) {
//                    swipeContainer.setEnabled(true);
//                } else {
//                    swipeContainer.setEnabled(false);
//                }
//            }
//        });
    }

    private void initSearch() {
    }

    private void initFirebase() {
        db = FirebaseFirestore.getInstance();
        loadData();
    }

    private void loadData() {
        list.clear();
        db.collection("tamu")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int i = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.e(TAG, document.getId() + " => " + document.getData());
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
                });
    }

    @Override
    public boolean onQueryTextSubmit(String searchValue) {
        List<Anggota> listAnggota = new ArrayList<>();
        String searchText = searchValue.toLowerCase(); // jadikan kata pencarian huruf kecil semua
        if (!searchValue.isEmpty()) {
            //load data dari firestore
            db.collection("tamu")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nama = document.getData().get("nama").toString();
                                String jabatan = document.getData().get("jabatan").toString().isEmpty() ? "-" : document.getData().get("jabatan").toString();
                                String image = document.getData().get("image").toString().isEmpty() ? ConstantValue.defaultImage : document.getData().get("image").toString();
                                String id = document.getId();
                                String status = document.getData().get("status").toString();
                                listAnggota.add(new Anggota(i, nama, id, image, status, jabatan, 1));
                                i++;
                            }

                            List<Anggota> listClone = new ArrayList();

                            for (Anggota data : listAnggota) { //loop data anggota
                                String name = data.getName().toLowerCase(); //jadikan data anggota dari list huruf kecil semua
                                if (name.matches(".*(" + searchText + ").*")) { //check apakah nama anggota di list sama dengan pencarian
                                    listClone.add(data); //masukan data anggota jika cocok
                                }
                            }


                            if (listClone.size() != 0) {
                                list.clear(); //kosongkan list data anggota utama
                                for (Anggota dataAgt : listClone) {
                                    //isi list data anggota utama
                                    list.add(dataAgt);
                                }
                                //reload ulang gridview
                                anggotaAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                            Toasty.error(AnggotaList.this, "Gagal memuat data, check koneksi anda", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //jika pencarian kosong load semua data kembali
        if (newText.isEmpty()) {
            loadData();
        }
        return false;
    }


}