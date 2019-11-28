package com.amt.batterysaver.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ads.control.AdmobHelp;
import com.amt.batterysaver.BatteryMode.BatteryInfo;
import com.amt.batterysaver.Utilsb.BatteryPref;
import com.amt.batterysaver.Utilsb.SharePreferenceUtils;
import com.amt.batterysaver.Utilsb.Utils;
import com.amt.batterysaver.service.BatteryService;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.amt.batterysaver.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class ChargeResultActivity extends Activity implements View.OnClickListener{

    TextView tvTime,tvHour,tvMin,tvQuati,tvChargeTime,tvChargeType,tvPercent,tvDate,tvTimeLeftTitle,tvFormatTime,tvFullCharge;
    LinearLayout lrTimeLeft;
    ProgressBar mProgressBar;
    ImageView ivCharge;
    Shimmer shFast,shFull,shTrickle,shOptimize;
    ShimmerTextView tvFast,tvFull,tvTrickle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLocate(this);
        overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up);
        setContentView(R.layout.activity_charge_dialog);
        intView();
        intData();
        intEvent();

        AdmobHelp.getInstance().loadNative(ChargeResultActivity.this);




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_down);
    }
    public String formatHourMinutune(long totaltime){
        long seconds = (totaltime / 1000) % 60;
        long minutes = (totaltime / (1000 * 60)) % 60;
        long hours = totaltime / (1000 * 60 * 60);

        StringBuilder b = new StringBuilder();
        b.append(hours == 0 ? "00" : hours < 10 ? "0" + hours :
                String.valueOf(hours));
        b.append(":");
        b.append(minutes == 0 ? "00" : minutes < 10 ? "0" + minutes :
                String.valueOf(minutes));
        return b.toString();
    }
    public void intView(){
        tvFullCharge = findViewById(R.id.tvFullCharge);

        lrTimeLeft = findViewById(R.id.view_time_left);
        tvTimeLeftTitle = findViewById(R.id.tvTimeLeftTitle);
        tvFull = findViewById(R.id.tvFull);
        tvTrickle = findViewById(R.id.tvTrick);
        tvFast = findViewById(R.id.tvFast);
        tvFormatTime  = findViewById(R.id.tvFormatTime);
        tvTime = findViewById(R.id.tvTime);
        tvHour = findViewById(R.id.tvHour);
        tvMin = findViewById(R.id.tvMin);
        tvQuati = findViewById(R.id.tvQuati);
        tvChargeTime = findViewById(R.id.tvChargeTime);
        tvChargeType = findViewById(R.id.tvChargeType);
        tvPercent = findViewById(R.id.tvPercent);
        tvDate = findViewById(R.id.tvDate);
        mProgressBar = findViewById(R.id.progressBattery);
        ivCharge = findViewById(R.id.ivCharge);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fmOff:
                startActivity(new Intent(this,ChargeSettingActivity.class));
                break;
            case R.id.fmClose:
                finish();
                break;
                default:
                    break;
        }

    }


    public  void intData(){
        shFast = new Shimmer();
        shFull = new Shimmer();
        shTrickle = new Shimmer();

        tvFull.setText("> "+getString(R.string.full_charging));
        tvTrickle.setText("> "+getString(R.string.trickle_charging));
        initTime();
        tvChargeType.setText(SharePreferenceUtils.getInstance(this).getChargeType());
        tvChargeTime.setText(formatHourMinutune(SharePreferenceUtils.getInstance(this).getTimeCharge()));
        tvQuati.setText(SharePreferenceUtils.getInstance(this).getChargeQuantity() +"%");
    }
    public void intEvent(){

    }
    BroadcastReceiver timeChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context paramContext, Intent paramIntent) {

            if (paramIntent.getAction().equals("android.intent.action.TIME_TICK") ) {
                initTime();
            }

        }
    };
    public void initTime() {
        Calendar mCalendar = Calendar.getInstance();
        Date date = new Date();
        tvTime.setText(new SimpleDateFormat("hh:mm").format(date));
        tvFormatTime.setText(new SimpleDateFormat("a").format(date));
        SimpleDateFormat df1 = new SimpleDateFormat("EEEE");
        String day = df1.format(new Date());
        tvDate.setText(day + ", " + DateFormat.getDateInstance().format(mCalendar.getTime()));
    }

    @Override
    protected void onStart() {
        super.onStart();

        // registerReceiver(mReceiverBattery, new IntentFilter(
        // Intent.ACTION_BATTERY_CHANGED));

        registerReceiver(this.timeChangeReceiver, new IntentFilter("android.intent.action.TIME_TICK"));
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

        unregisterReceiver(this.timeChangeReceiver);

    }

    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(BatteryService.ACTION_MAX_BATTERY_CHANGED_SEND)) {
                BatteryInfo info = intent.getParcelableExtra(BatteryInfo.BATTERY_INFO_KEY);
                //Update trang thai pin
                boolean isCharging = info.status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        info.status == BatteryManager.BATTERY_STATUS_FULL;

                updateCharge(isCharging, info);
                if (isCharging) {
                    info.plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                    boolean usbCharge = info.plugged == BatteryManager.BATTERY_PLUGGED_USB;
                    int time;
                    if (usbCharge) {
                        time = BatteryPref.initilaze(context).getTimeChargingUsb(context, info.level);
                    } else {
                        time = BatteryPref.initilaze(context).getTimeChargingAc(context, info.level);
                    }

                    info.hourleft = time / 60;
                    info.minleft = time % 60;


                } else {
                    int time = BatteryPref.initilaze(context).getTimeRemainning(context, info.level);
                    info.hourleft = time / 60;
                    info.minleft = time % 60;

                }

                updateStatus(info);

            }
        }
    };

    public void updateStatus(BatteryInfo info ){
        if(Utils.getChargeFull(this)){
            tvFullCharge.setText(getString(R.string.power_full));
            tvFullCharge.setVisibility(View.VISIBLE);
            lrTimeLeft.setVisibility(View.GONE);

        }else{
            tvFullCharge.setVisibility(View.GONE);
            lrTimeLeft.setVisibility(View.VISIBLE);
        }
        tvPercent.setText(info.level +"%  ");
        mProgressBar.setProgress(info.level);
        tvHour.setText(String.format("%02d", info.hourleft));
        tvMin.setText(String.format("%02d", info.minleft));


    }
    public void updateCharge(boolean isCharge,BatteryInfo info){

        double temp = (info.level*getBatteryCapacity())/100;


        if(isCharge){
            Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.blink_charge);
            tvPercent.startAnimation(loadAnimation);
            ivCharge.startAnimation(loadAnimation);
            tvTimeLeftTitle.setText(getString(R.string.noti_battery_charging_timer_left));
            ivCharge.setVisibility(View.VISIBLE);
            if(info.level<=30){
                shFast.start(tvFast);



            } else if(30<info.level&&info.level<90){
                shFull.start(tvFull);
                shFast.cancel();

            }else{

                shFast.cancel();
                shFull.cancel();
                shTrickle.start(tvTrickle);


            }
        }else{
            tvPercent.clearAnimation();
            ivCharge.clearAnimation();
            tvTimeLeftTitle.setText(getString(R.string.time_left));
            ivCharge.setVisibility(View.GONE);
            shFast.cancel();
            shTrickle.cancel();
            shFull.cancel();

        }


    }

    public String getTemp(int i) {
        if (!SharePreferenceUtils.getInstance(ChargeResultActivity.this).getTempFormat()) {
            double b = Math.ceil(((i / 10f) * 9 / 5 + 32) * 100) / 100;
            String r = String.valueOf(b);
            return (r + this.getString(R.string.fahrenheit));
        } else {

            String str = Double.toString(Math.ceil((i / 10f)*100)/100);
            return (str + this.getString(R.string.celsius));
        }
    }

    public double getVol(int i){
        double voltage = Math.ceil((i / 1000f) * 100) / 100;
        if (voltage > 1000)
            return voltage / 1000f;
        else
            return voltage;

    }
    public double getBatteryCapacity() {
        Object mPowerProfile_ = null;

        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(ChargeResultActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            return  (Double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getAveragePower", java.lang.String.class)
                    .invoke(mPowerProfile_, "battery.capacity");
//            Toast.makeText(MainActivity.this, batteryCapacity + " mah",
//                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    private String[] intToArray(Context context, int i) {
        String str = Double.toString(i / 10f);
        if(str.length()>4)
            str = str.substring(0, 4);

        if (true) {
            // do C
            String string = context.getString(R.string.celsius);
            return new String[] { str, string };
        } else {
            // do F
            String string = context.getString(R.string.fahrenheit);
            return new String[] { str, string };
        }
    }
    public void mRegisterReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(BatteryService.ACTION_MAX_BATTERY_CHANGED_SEND);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRegisterReceiver();
        Intent intent = new Intent();
        intent.setAction(BatteryService.ACTION_MAX_BATTERY_NEED_UPDATE);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
