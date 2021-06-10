package com.ads.control;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ads.control.funtion.UtilsApp;

public class RateApp extends Dialog {
    Context mContext;
    String mEmail, mTitleEmail;
    int mStyle = 0;
    Activity mActivity;

    public RateApp(Context context, String email, String TitleEmail, int style, Activity activity) {
        super(context);
        mActivity = activity;
        mContext = context;
        mEmail = email;
        mTitleEmail = TitleEmail;
        mStyle = style;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rate_app);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RatingBar ratingbar = findViewById(R.id.rating);
        TextView btnFeedRate = findViewById(R.id.btn_feed_rate);
        TextView rdMsg = findViewById(R.id.rd_msg);
        TextView btnNo = findViewById(R.id.btn_no);
        TextView titleExitApp = findViewById(R.id.title_exit_app);
        ImageView imgRateExit = findViewById(R.id.img_rate_exit);

        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (ratingBar.getRating() < 0.5) {
                    btnFeedRate.setTextColor(ContextCompat.getColor(mContext, R.color.white_60));
                    btnFeedRate.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ads_bg_disable));
                    btnFeedRate.setEnabled(false);
                }
                if (ratingBar.getRating() >= 0.5) {
                    btnFeedRate.setTextColor(ContextCompat.getColor(mContext, R.color.color_white));
                    btnFeedRate.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ads_bg_lib));
                    btnFeedRate.setEnabled(true);
                }
                if (ratingBar.getRating() <= 3) {
                    btnFeedRate.setText("Feedback");
                    btnFeedRate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UtilsApp.OpenEmail(mContext);
                            ((Activity) (mContext)).finish();
                        }
                    });
                } else {
                    btnFeedRate.setEnabled(true);
                    btnFeedRate.setText("Rate App");
                    btnFeedRate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + mContext.getPackageName()));
                            mContext.startActivity(intent);
                            Rate.getInstance(mContext).show_rate(true);
                            ((Activity) (mContext)).finish();
                        }
                    });
                }
            }
        });

        if (Rate.getInstance(mContext).show_rate()) {
            imgRateExit.setImageResource(R.drawable.img_exit_app);
            ratingbar.setVisibility(View.GONE);
            btnFeedRate.setVisibility(View.GONE);
            rdMsg.setVisibility(View.GONE);
            btnNo.setVisibility(View.VISIBLE);
            titleExitApp.setVisibility(View.VISIBLE);
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }

        TextView btn_late = findViewById(R.id.btn_late);
        btn_late.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                ((Activity) (mContext)).finish();
            }
        });
    }
}
