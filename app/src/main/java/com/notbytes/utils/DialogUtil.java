package com.notbytes.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class DialogUtil {

    private DialogUtil() {

    }

    public static ProgressDialog showProgressDialog(Context context, String title, String message, DialogInterface.OnCancelListener cancelListener) {
        ProgressDialog progressDialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
        progressDialog.setCancelable(cancelListener != null);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
        return progressDialog;
    }

    public static void showDialog(Context context, String title, String message, String positiveButtonText, DialogInterface.OnClickListener positiveButtonListener) {
        showDialog(context, title, message, positiveButtonText, null, null, positiveButtonListener, null, null, null);
    }

    public static void showDialog(Context context, String title, String message,
                                  String positiveButtonText, String negativeButtonText,
                                  DialogInterface.OnClickListener positiveButtonListener,
                                  DialogInterface.OnClickListener negativeButtonListener) {
        showDialog(context, title, message, positiveButtonText, negativeButtonText, null, positiveButtonListener, negativeButtonListener, null, null);
    }

    public static void showDialog(Context context, String title, String message,
                                  String positiveButtonText, String negativeButtonText, String neutralButtonText,
                                  DialogInterface.OnClickListener positiveButtonListener,
                                  DialogInterface.OnClickListener negativeButtonListener,
                                  DialogInterface.OnClickListener neutralButtonListener, View customView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        builder.setCancelable(false);

        if (!TextUtils.isEmpty(positiveButtonText)) {
            builder.setPositiveButton(positiveButtonText, positiveButtonListener);
        }
        if (!TextUtils.isEmpty(negativeButtonText)) {
            builder.setNegativeButton(negativeButtonText, negativeButtonListener);
        }
        if (!TextUtils.isEmpty(neutralButtonText)) {
            builder.setNeutralButton(neutralButtonText, neutralButtonListener);
        }
        if (customView != null) {
            builder.setView(customView);
        }
        builder.create().show();
    }

    public static void showNumberPickerDialog(Context context, String title, String message,
                                              String positiveButtonText, String negativeButtonText,
                                              DialogInterface.OnClickListener positiveButtonListener,
                                              DialogInterface.OnClickListener negativeButtonListener,
                                              EditText editText) {
        showDialog(context, title, message, positiveButtonText, negativeButtonText, null, positiveButtonListener, negativeButtonListener, null, editText);
    }
}
