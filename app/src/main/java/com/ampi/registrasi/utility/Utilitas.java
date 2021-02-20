package com.ampi.registrasi.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;

import com.ampi.registrasi.R;
import com.droidbyme.dialoglib.DroidDialog;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Utilitas {

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    //convert bitmap to string value
    public static String imageBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
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

}
