package app.tahachi.batterydoctor.activity;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import app.tahachi.batterydoctor.R;
import app.tahachi.batterydoctor.Utilsb.SharePreferenceUtils;
import app.tahachi.batterydoctor.Utilsb.Utils;

public class DoNotDisturbActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout rlDNDStart;
    private LinearLayout rlDNDStop;
    private TextView tvDNDStart;
    private TextView tvDNDStartSecond;
    private TextView tvDNDStop;
    private TextView tvDNDStopSecond,tvEnable;
    SwitchCompat swDND;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLocate(this);
        setContentView(R.layout.activity_do_not_disturb);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_activity_do_not_disturb));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intView();
        intEvent();
        intData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void intView(){
        swDND = findViewById(R.id.swDND);
        rlDNDStart = findViewById(R.id.rl_DND_start);
        rlDNDStop = findViewById(R.id.rl_DND_stop);
        tvDNDStart = findViewById(R.id.tv_DND_start);
        tvDNDStartSecond = findViewById(R.id.tv_DND_start_time);
        tvDNDStop = findViewById(R.id.tv_DND_stop);
        tvDNDStopSecond = findViewById(R.id.tv_DND_stop_time);
        tvEnable = findViewById(R.id.tvEnable);

    }
    public void intData(){

        intTimeOn(SharePreferenceUtils.getInstance(this).getDndStart());
        intTimeOff(SharePreferenceUtils.getInstance(this).getDndStop());
        setColorText(SharePreferenceUtils.getInstance(this).getDnd());
        if(SharePreferenceUtils.getInstance(this).getDnd()){
            tvEnable.setText(getString(R.string.enabled));
        }else{
            tvEnable.setText(getString(R.string.auto_disable));
        }
    }
    public void intEvent(){
        rlDNDStart.setOnClickListener(this);
        rlDNDStop.setOnClickListener(this);
        findViewById(R.id.rlDND).setOnClickListener(this);

    }
    public void intTimeOn(int time){
        int dNDStartTime =time;
        int i = dNDStartTime / 100;
        dNDStartTime %= 100;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%02d", Integer.valueOf(i)));
        stringBuilder.append(":");
        stringBuilder.append(String.format("%02d", Integer.valueOf(dNDStartTime)));
        tvDNDStartSecond.setText(stringBuilder.toString());
    }
    public  void intTimeOff(int time){
        int dNDEndTime = time;
        int i = dNDEndTime / 100;
        dNDEndTime %= 100;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%02d", Integer.valueOf(i)));
        stringBuilder.append(":");
        stringBuilder.append(String.format("%02d", Integer.valueOf(dNDEndTime)));
        tvDNDStopSecond.setText(stringBuilder.toString());
    }
    private void setColorText(boolean isChecked) {
        if (isChecked) {
            tvEnable.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            swDND.setChecked(isChecked);
            rlDNDStart.setEnabled(true);
            tvDNDStartSecond.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            tvDNDStopSecond.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            tvDNDStart.setTextColor(ContextCompat.getColor(this, R.color.text_color));
            tvDNDStop.setTextColor(ContextCompat.getColor(this, R.color.text_color));

        } else {
            tvEnable.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
            swDND.setChecked(isChecked);
            rlDNDStart.setEnabled(false);
            tvDNDStartSecond.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
            tvDNDStopSecond.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
            tvDNDStart.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
            tvDNDStop.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rlDND:
                if(SharePreferenceUtils.getInstance(DoNotDisturbActivity.this).getDnd()){
                    setColorText(false);
                    tvEnable.setText(getString(R.string.auto_disable));
                    SharePreferenceUtils.getInstance(DoNotDisturbActivity.this).setDnd(false);
                }else{
                    tvEnable.setText(getString(R.string.enabled));
                    setColorText(true);
                    SharePreferenceUtils.getInstance(DoNotDisturbActivity.this).setDnd(true);
                }

                break;
            case R.id.rl_DND_start:
                int timeStart = SharePreferenceUtils.getInstance(DoNotDisturbActivity.this).getDndStart();
                TimePickerDialog timePickerDialog = new TimePickerDialog(DoNotDisturbActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker timePicker, int i, int i2) {
                        SharePreferenceUtils.getInstance(DoNotDisturbActivity.this).setDndStart((i * 100) + i2);
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(String.format("%02d", Integer.valueOf(i)));
                        stringBuilder.append(":");
                        stringBuilder.append(String.format("%02d", Integer.valueOf(i2)));
                        tvDNDStartSecond.setText(stringBuilder.toString());
                    }
                }, timeStart / 100, timeStart % 100, true);
                timePickerDialog.setTitle(DoNotDisturbActivity.this.getString(R.string.start_at));
                timePickerDialog.show();

                break;
            case R.id.rl_DND_stop:

                int timeStop = SharePreferenceUtils.getInstance(DoNotDisturbActivity.this).getDndStop();
                TimePickerDialog timePickerDialog2 = new TimePickerDialog(DoNotDisturbActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker timePicker, int i, int i2) {
                        SharePreferenceUtils.getInstance(DoNotDisturbActivity.this).setDndStop((i * 100) + i2);
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(String.format("%02d", Integer.valueOf(i)));
                        stringBuilder.append(":");
                        stringBuilder.append(String.format("%02d", Integer.valueOf(i2)));
                        tvDNDStopSecond.setText(stringBuilder.toString());
                    }
                }, timeStop / 100, timeStop % 100, true);
                timePickerDialog2.setTitle(DoNotDisturbActivity.this.getString(R.string.stop_at));
                timePickerDialog2.show();
                break;
            default:
                break;
        }
    }
}
