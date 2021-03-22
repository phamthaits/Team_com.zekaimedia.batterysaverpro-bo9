package com.newus.battery.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.ads.control.AdControlHelp;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;

import com.newus.battery.R;
import com.newus.battery.Utilsb.SharePreferenceUtils;
import com.newus.battery.adapter.ChartAdapter;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ChartActivity extends AppCompatActivity  {


    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.lr_back:
                finish();
                return;
            default:
                return;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_history);
        intView();
        intEvent();
        intData();
        context = this;
        adControlHelp = AdControlHelp.getInstance(context);

        /* ------------------- StatusBar Navigation text dark bg white ----------------- */
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.windowBackground));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.windowBackground));
        /* ------------------------------------------------------------------ */

        adapterViewPager = new ChartAdapter(this.getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        vpPager.setCurrentItem(2);

        adControlHelp.loadBanner(this, findViewById(R.id.banner));
    }

    TextView tvNormal, tvOver, tvHealthy, tvLastFull, tvChargeType, tvTimeCharge, tvQuantity, tvDate;
    private PieChart chart;
    ImageView imgColor;
    TextView tvCount;
    private ViewPager vpPager;
    ChartAdapter adapterViewPager;
    private TabLayout indicator;
    private AdControlHelp adControlHelp;
    private Context context;



    public void intView() {
        tvNormal = findViewById(R.id.tvNormal);
        tvOver = findViewById(R.id.tvOver);
        tvHealthy = findViewById(R.id.tvHealthy);
        tvLastFull = findViewById(R.id.tvLastFull);
        tvChargeType = findViewById(R.id.tvChargeType);
        tvTimeCharge = findViewById(R.id.tvTimeCharge);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvCount = findViewById(R.id.tvCount);
        imgColor = findViewById(R.id.imgColor);
        tvDate = findViewById(R.id.tvDate);
        vpPager = findViewById(R.id.viewPager);
        indicator = findViewById(R.id.indicator);
        indicator.setupWithViewPager(vpPager, true);
    }

    public void intEvent() {
        intChart();
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;
                Log.i("VAL SELECTED",
                        "Value: " + e.getY() + ", index: " + h.getX()
                                + ", DataSet index: " + h.getDataSetIndex());
                imgColor.setVisibility(View.VISIBLE);
                setPercent(h.getX());
            }

            @Override
            public void onNothingSelected() {

                imgColor.setVisibility(View.INVISIBLE);
                tvCount.setText(String.valueOf(SharePreferenceUtils.getInstance(context).getChargeNormal()));
            }
        });
        viewPagerListenItem();
    }

    public void setPercent(float i) {
        float normal = (float) SharePreferenceUtils.getInstance(context).getChargeNormal();
        float healthy = (float) SharePreferenceUtils.getInstance(context).getChargeHealthy();
        float over = (float) SharePreferenceUtils.getInstance(context).getChargeOver();
        float sum = normal + healthy + over;
        if (i == 0) {
            imgColor.setBackgroundResource(R.drawable.shape_status_normal);
            tvCount.setText(Math.round((normal / sum) * 100) + "%");
        } else if (i == 1) {
            tvCount.setText(Math.round((healthy / sum) * 100) + "%");
            imgColor.setBackgroundResource(R.drawable.shape_status_healthy);
        } else {
            tvCount.setText(Math.round((over / sum) * 100) + "%");
            imgColor.setBackgroundResource(R.drawable.shape_status_over);
        }
    }

    public void intChart() {
        //      tfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        chart =findViewById(R.id.chartPie);
        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.TRANSPARENT);

        chart.setTransparentCircleColor(Color.TRANSPARENT);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.setDescription(null);
        //   chart.setDrawSliceText(false);

        chart.getLegend().setEnabled(false);
    }

    public void viewPagerListenItem() {
        tvDate.setText(DateFormat.getDateInstance().format(Calendar.getInstance().getTime()));
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {  // if you want the second page, for example
                    //Your code here
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_MONTH, -2);
                    tvDate.setText(DateFormat.getDateInstance().format(calendar.getTime()));
                }
                if (position == 1) {

                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    tvDate.setText(DateFormat.getDateInstance().format(calendar.getTime()));
                }
                if (position == 2) {

                    tvDate.setText(DateFormat.getDateInstance().format(Calendar.getInstance().getTime()));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        intData();


    }

    public void intData() {


        if (SharePreferenceUtils.getInstance(context).getChargeNormal() != 0) {
            tvNormal.setText(String.valueOf(SharePreferenceUtils.getInstance(context).getChargeNormal()));
        }
        if (SharePreferenceUtils.getInstance(context).getChargeOver() != 0) {
            tvOver.setText(String.valueOf(SharePreferenceUtils.getInstance(context).getChargeOver()));
        }
        if (SharePreferenceUtils.getInstance(context).getChargeHealthy() != 0) {
            tvHealthy.setText(String.valueOf(SharePreferenceUtils.getInstance(context).getChargeHealthy()));

        }
        if (SharePreferenceUtils.getInstance(context).getChargeFull() != null) {
            tvLastFull.setText(SharePreferenceUtils.getInstance(context).getChargeFull());
        }
        tvChargeType.setText(SharePreferenceUtils.getInstance(context).getChargeType());
        tvTimeCharge.setText(formatHourMinutune(SharePreferenceUtils.getInstance(context).getTimeCharge()));
        tvQuantity.setText(SharePreferenceUtils.getInstance(context).getChargeQuantity() + "%");


        setData();
    }

    public String formatHourMinutune(long totaltime) {
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

    private void setData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        float b = (float) Math.round(SharePreferenceUtils.getInstance(context).getChargeNormal()) / 40;

        float normal = (float) SharePreferenceUtils.getInstance(context).getChargeNormal();
        float healthy = (float) SharePreferenceUtils.getInstance(context).getChargeHealthy();
        float over = (float) SharePreferenceUtils.getInstance(context).getChargeOver();
        float max = normal;
        if (normal == 0 && healthy == 0 && over == 0) {
            entries.add(new PieEntry(40, 0));
            entries.add(new PieEntry(1, 1));
            entries.add(new PieEntry(1, 2));
        } else {

            if (max < healthy) {
                max = healthy;
            }

            if (max < over) {
                max = over;
            }
        }
        if (normal <= (max / 40)) {
            entries.add(new PieEntry(max / 40, 0));
        } else {
            entries.add(new PieEntry(normal, 0));
        }
        if (healthy <= (max / 40)) {
            entries.add(new PieEntry(max / 40, 0));
        } else {
            entries.add(new PieEntry(healthy, 0));
        }
        if (over <= (max / 40)) {
            entries.add(new PieEntry((max / 40), 0));
        } else {
            entries.add(new PieEntry(over, 0));
        }


        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors
        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(ContextCompat.getColor(this, R.color.color_normal));
        colors.add(ContextCompat.getColor(this, R.color.color_healthy));
        colors.add(ContextCompat.getColor(this, R.color.color_over));


        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);


        PieData data = new PieData(dataSet);
        //   data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setDrawValues(false);
        chart.setData(data);
        chart.setDrawSliceText(false);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }

    public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + " $"; // e.g. append a dollar-sign
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
