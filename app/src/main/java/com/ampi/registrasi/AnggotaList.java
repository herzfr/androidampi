package com.ampi.registrasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.ampi.registrasi.model.Anggota;
import com.ampi.registrasi.service.AnggotaAdapter;
import com.ampi.registrasi.utility.ConstantValue;
import com.ampi.registrasi.utility.Utilitas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

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

        gridView = (GridView) findViewById(R.id.gridView);
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

    private void initSearch(){

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

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.e(TAG, "onQueryTextSubmit: " + query );
//        DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
//        Query query = mFirebaseDatabaseReference.child("userTasks").orderByChild("title").equalTo("#Yahoo");
//        query.addValueEventListener(valueEventListener);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.e(TAG, "onQueryTextSubmit: " + newText );
        return false;
    }
}