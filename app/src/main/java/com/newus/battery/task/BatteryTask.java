package com.newus.battery.task;

import android.content.Context;
import android.os.AsyncTask;
import androidx.core.content.ContextCompat;
import android.widget.TextView;

import com.newus.battery.Utilsb.Utils;
import com.newus.battery.R;

import me.itangqi.waveloadingview.WaveLoadingView;


    public class BatteryTask extends AsyncTask<Void, Integer, Boolean> {

        private Context mContext;
        private TextView mTvbattery;
        private int level ;
        private OnTaskBatteryListener mOnTaskBatteryListener;
        private WaveLoadingView mWaveLoadingView;


        public BatteryTask(Context context,  TextView tvBattey, WaveLoadingView waveLoadingView, OnTaskBatteryListener onTaskBatteryListener) {
            mContext = context;
            this.mTvbattery = tvBattey;
            mOnTaskBatteryListener = onTaskBatteryListener;
            mWaveLoadingView = waveLoadingView;
            updateStatus(Utils.getBatteryLevel(mContext));
            mWaveLoadingView.setProgressValue(1);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            level = Utils.getBatteryLevel(mContext);
            for (int i = 0; i <= level; i++) {
                try {

                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mTvbattery.setText(values[0] +"%  ");
            mWaveLoadingView.setProgressValue(values[0]);

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            updateStatus(level);
            if (null != mOnTaskBatteryListener) {
                mOnTaskBatteryListener.OnResult();
            }

        }

        public interface OnTaskBatteryListener {
            void OnResult();
        }

        public void updateStatus(int level ){
            if(0<=level&&level<=5){
                mWaveLoadingView.setWaveColor(ContextCompat.getColor(mContext, R.color.battery_almost_die));
                mWaveLoadingView.setProgressValue(level);
                return;
            }
            if(5<level&&level<=15){
                mWaveLoadingView.setWaveColor(ContextCompat.getColor(mContext,R.color.battery_bad));
                mWaveLoadingView.setProgressValue(level);
                return;
            }
            if(15<level&&level<=30){
                mWaveLoadingView.setWaveColor(ContextCompat.getColor(mContext,R.color.battery_averange));
                mWaveLoadingView.setProgressValue(level);
                return;
            }
            if(30<level&&level<=60){
                mWaveLoadingView.setWaveColor(ContextCompat.getColor(mContext, R.color.battery_good));
                mWaveLoadingView.setProgressValue(level);
                return;
            }
            if(60<level&&level<=100){
                mWaveLoadingView.setWaveColor(ContextCompat.getColor(mContext,R.color.battery_good));
                mWaveLoadingView.setProgressValue(level);
                return;
            }
            //

        }
    }

