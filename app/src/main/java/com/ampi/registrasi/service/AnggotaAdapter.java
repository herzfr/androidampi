package com.ampi.registrasi.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ampi.registrasi.R;
import com.ampi.registrasi.model.Anggota;

import java.util.ArrayList;

public class AnggotaAdapter extends BaseAdapter {

    private Context context;
    private  int layout;
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

    private class ViewHolder{
        ImageView imageAnggota;
        TextView nameAnggota, noAnggota, statusAnggota;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.nameAnggota = (TextView) row.findViewById(R.id.nameAnggota);
            holder.noAnggota = (TextView) row.findViewById(R.id.noAnggota);
            holder.statusAnggota = (TextView) row.findViewById(R.id.statusAnggota);
            holder.imageAnggota = (ImageView) row.findViewById(R.id.imageAnggota);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        Anggota agt = anggotaList.get(position);

        holder.nameAnggota.setText(agt.getName());
        holder.noAnggota.setText(agt.getNoreg());
        holder.statusAnggota.setText(agt.getStatus());

        byte[] foodImage = agt.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(foodImage, 0, foodImage.length);
        holder.imageAnggota.setImageBitmap(bitmap);

        return row;

    }
}
