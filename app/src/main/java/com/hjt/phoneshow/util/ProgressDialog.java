package com.hjt.phoneshow.util;

import android.content.Context;

/**
 * Created by Administrator on 2016/11/22.
 */

public class ProgressDialog {
    public android.app.ProgressDialog progressDialog;

    public android.app.ProgressDialog showProgressDialog(Context mActivity) {
        progressDialog = new android.app.ProgressDialog(mActivity);
        progressDialog.setMessage("加载中");
        progressDialog.show();
        return progressDialog;
    }

    public android.app.ProgressDialog showProgressDialog(CharSequence message,Context mActivity) {
        progressDialog = new android.app.ProgressDialog(mActivity);
        progressDialog.setMessage(message);
        progressDialog.show();
        return progressDialog;
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            // progressDialog.hide();会导致android.view.WindowLeaked
            progressDialog.dismiss();
        }
    }




}
