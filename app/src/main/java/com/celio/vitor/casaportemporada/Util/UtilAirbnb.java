package com.celio.vitor.casaportemporada.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.celio.vitor.casaportemporada.R;
import com.celio.vitor.casaportemporada.activity.authentication.CriarContaActivity;
import com.celio.vitor.casaportemporada.activity.authentication.LoginActivity;

import java.io.ByteArrayOutputStream;

public class UtilAirbnb {

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 60, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap base64ToBitmap(String base64) {
        try {
            byte[] encodeByte = Base64.decode(base64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static void salvarInternamente(String img, SharedPreferences preferences) {
        SharedPreferences.Editor ed = preferences.edit();
        ed.putString("img", img);
        ed.apply();
    }

    public static void mudarCorTextoDrawableDoBotao(Button btn, Context context, int cor) {
        btn.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, cor)));
        btn.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(context, cor)));
    }

    public static void showDialogLogin(Context context, String title, String message, String negativeButton, String positiveButton, String neutralButton, boolean layout) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        if (layout) {
            builder.setView(R.layout.form_login_externa);
        }

        if (message != null) {
            builder.setMessage(message);
        }

        builder.setCancelable(true);

        if (negativeButton != null) {
            builder.setNegativeButton(negativeButton, (dialogInterface, i) -> dialogInterface.dismiss());
        }

        if (positiveButton != null) {
            builder.setPositiveButton(positiveButton, (dialogInterface, i) -> {
                context.startActivity(new Intent(context, LoginActivity.class));
            });
        }

        if (neutralButton != null) {
            builder.setNeutralButton(neutralButton, (dialogInterface, i) -> {
                context.startActivity(new Intent(context, CriarContaActivity.class));
            });
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
