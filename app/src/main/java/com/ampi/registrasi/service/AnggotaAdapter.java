package com.ampi.registrasi.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ampi.registrasi.R;
import com.ampi.registrasi.model.Anggota;
import com.ampi.registrasi.utility.Utilitas;

import java.util.ArrayList;

public class AnggotaAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Anggota> anggotaList;


    public AnggotaAdapter(Context context, int layout, ArrayList<Anggota> anggotaList) {
        this.context = context;
        this.layout = layout;
        this.anggotaList = anggotaList;
    }

    @Override
    public int getCount() {
        return anggotaList.size();
    }

    @Override
    public Object getItem(int position) {
        return anggotaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView imageAnggota;
        TextView nameAnggota, noAnggota, statusAnggota, jabatanAnggota;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.noAnggota = row.findViewById(R.id.noAnggota);
            holder.nameAnggota = row.findViewById(R.id.nameAnggota);
            holder.jabatanAnggota = row.findViewById(R.id.jabatanAnggota);
            holder.statusAnggota = row.findViewById(R.id.statusAnggota);
            holder.imageAnggota = row.findViewById(R.id.imageAnggota);
            row.setTag(holder);


        } else {
            holder = (ViewHolder) row.getTag();
        }

        Anggota agt = anggotaList.get(position);
        String statusKehadiran = agt.getStatus().equalsIgnoreCase("false") ? "Belum Hadir" : "Hadir";

        holder.noAnggota.setText(agt.getNoreg());
        holder.nameAnggota.setText(agt.getName());
        holder.jabatanAnggota.setText(agt.getJabatan());
        holder.statusAnggota.setText(statusKehadiran);
        row.setOnClickListener(v -> {
            Log.e("ADAPTER", "getView: clicked : " + agt.getNoreg());
//            Utilitas.showCustomDialog(context, "Title", "Anda Menekan ID : " + agt.getNoreg(), "OK");
            Utilitas.showQrCustomDialog(context, agt.getNoreg());

        });

        byte[] anggotaImage = Base64.decode(agt.getImage(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(anggotaImage, 0, anggotaImage.length);
        holder.imageAnggota.setImageBitmap(decodedImage);


        return row;

    }
}
