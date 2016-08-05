package com.rokpetek.breadcrumbapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;


public class GuiUtils {

    public static void showDialog(Context context, String title, String description) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(description)
                .setPositiveButton(android.R.string.ok, (DialogInterface dialog, int which) -> {
                            dialog.dismiss();
                        }
                ).show();
    }

}
