package com.ampi.registrasi;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.GridView;

import com.ampi.registrasi.model.Anggota;
import com.ampi.registrasi.service.AnggotaAdapter;
import com.ampi.registrasi.service.SQLiteHelper;

import java.util.ArrayList;

public class AnggotaList extends AppCompatActivity {

    GridView gridView;
    ArrayList<Anggota> list;
    AnggotaAdapter anggotaAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anggota_list);

        gridView = (GridView) findViewById(R.id.gridView);
        list = new ArrayList<>();
        anggotaAdapter = new AnggotaAdapter(this, R.layout.button_image_layout, list);
        gridView.setAdapter(anggotaAdapter);

        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);

        // get all data from sqlite
        Cursor cursor = sqLiteHelper.getData("SELECT * FROM ANGGOTA");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String registrasi = cursor.getString(2);
            byte[] image = cursor.getBlob(3);
            String status = cursor.getString(4);
            int time = cursor.getInt(5);

            list.add(new Anggota(id, name, registrasi, image, status, time));
        }

        anggotaAdapter.notifyDataSetChanged();

    }
}