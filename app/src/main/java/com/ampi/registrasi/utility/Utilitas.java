package com.ampi.registrasi.utility;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.ampi.registrasi.R;
import com.droidbyme.dialoglib.DroidDialog;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

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

    //show qr custom dialog
    public static void showQrCustomDialog(Context context, String noReg) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);

        ImageView image = dialog.findViewById(R.id.iv_icon);
        image.setImageResource(R.mipmap.ic_launcher);
        image.setImageBitmap(generateQrCode(noReg));

        Button okButton = dialog.findViewById(R.id.bt_ok);
        Button shareBtn = dialog.findViewById(R.id.bt_Share);

        okButton.setOnClickListener(v -> {
            Log.e("DIALOG", "showQrCustomDialog: ");
            dialog.dismiss();
        });
        shareBtn.setOnClickListener(v -> {
            shareQR(context, noReg);
        });
        dialog.show();
    }

    public static void shareQR(Context context, String noReg) {
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                generateQrCode(noReg), "Design", null);
        Uri uri = Uri.parse(path);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.putExtra(Intent.EXTRA_TEXT, "Undangan Tamu");
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

}
