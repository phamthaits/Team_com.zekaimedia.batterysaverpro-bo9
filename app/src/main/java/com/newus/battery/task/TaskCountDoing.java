package com.newus.battery.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.newus.battery.R;

public class TaskCountDoing extends AsyncTask<Void, Integer, Void> {

    private Context mContext;
    TextView titleApp;
    int mCount;

    public TaskCountDoing(Context context, TextView tvCount ,int count) {

        mContext = context;
        this.titleApp = tvCount;
        mCount = count ;
    }

    @Override
    protected Void doInBackground(Void... params) {

        for (int i = 0;i<mCount;i++){
            publishProgress(i);
            try {

                Thread.sleep(40);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if(values[0]!=0)
            titleApp.setText(values[0] +"+"+mContext.getString(R.string.bd_power_issue));

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void result) {


    }



}
