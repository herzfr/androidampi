package com.ampi.registrasi.utility;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ampi.registrasi.MainActivity;
import com.ampi.registrasi.R;
import com.ampi.registrasi.ScannerActivity;
import com.ampi.registrasi.model.Anggota;
import com.droidbyme.dialoglib.DroidDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utilitas {


    final private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private volatile Bundle controlBundle;

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    //conver base4 to bitmap
    public static Bitmap base64toBitmap(String imageString) {
        byte[] anggotaImage = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(anggotaImage, 0, anggotaImage.length);

        return decodedImage;
    }

    //convert bitmap to string value
    public static String imageBitmapToString(Bitmap bitmap) {
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 380, 380, true);
//        resized = Bitmap.createScaledBitmap(nameYourBitmap,(int)(nameYourBitmap.getWidth()*0.8), (int)(nameYourBitmap.getHeight()*0.8), true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageString;
    }

    //generate random char string
    public static String getRandomString(int n) {
        String AlphaString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int) (AlphaString.length() * Math.random());
            sb.append(AlphaString.charAt(index));
        }
        return sb.toString();
    }

    //generate time in minute and second
    public static String getTime() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mmss");
        String formattedDate = simpleDateFormat.format(c);
        return formattedDate;
    }

    //generate random alphanumeric string
    public static String generateRandom() {
        String randomValue = getRandomString(3) + getTime();
        return randomValue;
    }

    //show custom dialog
    public static void showCustomDialog(Context context, String title, String message, String btnTxt) {
        new DroidDialog.Builder(context)
                .icon(R.drawable.ic_action_tick)
                .title(title)
                .content(message)
                .cancelable(true, false)
                .positiveButton(btnTxt, dialog -> {
                    dialog.dismiss();
                })
                .show();
    }

    public static int showCustomSuccess(Context context, String title, String message, String btnTxt) {
        new DroidDialog.Builder(context)
                .icon(R.drawable.ic_action_tick)
                .title(title)
                .content(message)
                .cancelable(true, false)
                .positiveButton(btnTxt, dialog -> {
                    dialog.dismiss();
                })
                .show();
        return 0;
    }


    //show qr custom dialog
    public static void showQrCustomDialog(Context context, String noReg, String name) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView image = dialog.findViewById(R.id.iv_icon);
        image.setImageResource(R.mipmap.ic_launcher);
        image.setImageBitmap(generateQrCode(noReg));

        TextView textView = dialog.findViewById(R.id.tv_nama);
        textView.setText(name);

        Button okButton = dialog.findViewById(R.id.bt_ok);
        Button shareBtn = dialog.findViewById(R.id.bt_Share);
        Button deleteBtn = dialog.findViewById(R.id.bt_Delete);

        okButton.setOnClickListener(v -> {
            Log.e("DIALOG", "showQrCustomDialog: ");
            dialog.dismiss();
        });
        shareBtn.setOnClickListener(v -> {
            shareQR(context, noReg, name);
        });

        deleteBtn.setOnClickListener(v -> {
            Log.e("DIALOG", "showQrCustomDialog: Delete");
            db.collection("tamu").document(noReg)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.e("TAG", "DocumentSnapshot successfully deleted!");
//                            showCustomDialog(context, "Data Sudah di Hapus", "Data Tamu Di Hapus dengan ID : " + noReg, "Tutup");
                            int ret = showCustomSuccess(context, "Data Sudah di Hapus", "Data Tamu Di Hapus dengan ID : " + noReg, "Tutup");
                            if (ret == 0) {
                                dialog.dismiss();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("TAG", "Error deleting document", e);
                        }
                    });
        });
        dialog.show();
    }



    public static void shareQR(Context context, String noReg, String name) {
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                generateQrCode(noReg), name, null);
        Uri uri = Uri.parse(path);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.putExtra(Intent.EXTRA_TEXT, name);
        context.startActivity(Intent.createChooser(share, "Bagi Undangan"));
    }

    public static Bitmap generateQrCode(String noReg) {
        String text = noReg; // Whatever you need to encode in the QR code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
//            imageView.setImageBitmap(bitmap);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    //show qr custom dialog
    public static void updateAll(Context context) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog_list);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        Button okButton = dialog.findViewById(R.id.bt_ok);
        Button noButton = dialog.findViewById(R.id.bt_no);


        okButton.setOnClickListener(v -> {
            Log.e("DIALOG", "update: ");
//            dialog.dismiss();
            db.collection("tamu").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ArrayList<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }
                    Log.d("TAG", list.toString());
//                        updateDatas(context, list, dialog); // *** new ***
                    for (int k = 0; k < list.size(); k++) {
                        db.collection("tamu").document(list.get(k).toString())
                                .update("status", false).addOnSuccessListener(new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i("Update", "Value Updated");
                                Toast.makeText(context, "Data Update", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                Intent i=new Intent(context, MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(i);


//                                    showCustomSuccess(context, "Data Sudah di Update", "Data Kembali Seperti Semula ", "Tutup");
//                                    if (ret == 0) {
//                                        dialog.dismiss();
//                                    }
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(context, "Error In Updating Details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            });
        });
        noButton.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }


    public static void updateDatas(Context context, ArrayList list, Dialog dialog) {
        for (int k = 0; k < list.size(); k++) {
            db.collection("tamu").document(list.get(k).toString())
                    .update("status", false).addOnSuccessListener(new OnSuccessListener<Void>()
            {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i("Update", "Value Updated");
                    Toast.makeText(context, "Value Updated: " + aVoid, Toast.LENGTH_SHORT).show();
                    showCustomDialog(context, "Data Sudah di Update", "Data Kembali Seperti Semula ", "Tutup");
//                    if (ret == 0) {
//                        dialog.dismiss();
//                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(context, "Error In Updating Details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    static void updateData(ArrayList list) {
//        Map<String, Object> user = new HashMap<>();
//        user.put("status", true);

        // Get a new write batch
        WriteBatch batch = db.batch();

        // Iterate through the list
        for (int k = 0; k < list.size(); k++) {

            // Update each list item
            Log.e("TAG", "updateData: => " + list.get(k) );
//            list.get(k);
            DocumentReference ref = db.collection("tamu").document(list.get(k).toString());
            batch.update(ref, "status", false);
            
        }

        // Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Yay its all done in one go!
                Log.e("TAG", "onComplete: " + task );
            }
        });

    }


    private static void saveImage(Context context, LinearLayout llsave) throws IOException {
        LinearLayout content = llsave;
        content.setDrawingCacheEnabled(true);
        Bitmap bitmap = content.getDrawingCache();
        File file, f = null;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
        {
            file =new File(android.os.Environment.getExternalStorageDirectory(),"TTImages_cache");
            if(!file.exists())
            {
                file.mkdirs();

            }
            f = new File(file.getAbsolutePath()+file.separator+ "filename"+".png");
        }
        FileOutputStream ostream = new FileOutputStream(f);
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, ostream);
        ostream.close();
    }
}
