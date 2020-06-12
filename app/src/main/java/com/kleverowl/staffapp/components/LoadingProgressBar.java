package com.kleverowl.staffapp.components;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.core.graphics.drawable.DrawableCompat;
import com.kleverowl.staffapp.R;


/**
 * @Created by Vinod Kulkarni
 * @Date on 7/25/2017.
 */

public class LoadingProgressBar {
    private static LoadingProgressBar mCShowProgress;
    private Dialog mDialog;

    public LoadingProgressBar() {
    }

    public static LoadingProgressBar getInstance() {
        if (mCShowProgress == null) {
            mCShowProgress = new LoadingProgressBar();
        }
        return mCShowProgress;
    }

    public void showProgress(Context mContext, String title) {
        mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.loading_progress);
        mDialog.findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);

        ProgressBar progressBar = mDialog.findViewById(R.id.progress_bar);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            Drawable wrapDrawable = DrawableCompat.wrap(progressBar.getIndeterminateDrawable());
            progressBar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
        } else {
            //progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(mContext, R.color.them_green), PorterDuff.Mode.SRC_IN);
        }


        TextView titleTextView = mDialog.findViewById(R.id.title_text_view);
        titleTextView.setText(title);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    public void hideProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
