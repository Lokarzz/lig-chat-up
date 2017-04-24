package com.lokarz.ligchatup.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by Lokarz on 4/23/2017.
 */

public class ViewUtil {

    private static Toast mToast;
    private static ProgressDialog mProgressDialog;

    public static void displayToastMsg(Context context, String msg){
        if(mToast != null){
            mToast.cancel();
        }
        mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static ProgressDialog getProgressDialog(Context context) {

        if (mProgressDialog != null && mProgressDialog.getOwnerActivity().isFinishing()) {
            mProgressDialog = null;
        }

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setOwnerActivity((Activity)context);
        }

        return mProgressDialog;
    }
}
