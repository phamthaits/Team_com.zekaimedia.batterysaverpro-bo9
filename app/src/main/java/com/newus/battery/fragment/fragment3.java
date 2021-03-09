package com.newus.battery.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.newus.battery.BatteryMode.BatteryInfo;
import com.newus.battery.R;
import com.newus.battery.Utilsb.HistoryPref;
import com.newus.battery.service.BatteryService;

import java.util.ArrayList;
import java.util.Calendar;

public class fragment3 extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    LineDataSet set1,set2,set3;

    // newInstance constructor for creating fragment with arguments
    public static fragment3 newInstance(int page, String title) {
        fragment3 fragmentToday = new fragment3();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentToday.setArguments(args);
        return fragmentToday;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    LineChart chart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        chart = view.findViewById(R.id.today_chart);


        //  yAxis.setValueFormatter(new MyXaxisValueFormater(values2));

        initHistoryChart();




        return view;
    }


    public class MyXaxisValueFormater implements IAxisValueFormatter {
        private String [] mValues;
        public MyXaxisValueFormater (String [] values){
            mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int)value];
        }
    }

//    public void intChart(String){
//
//

//
//
//

//
//        //Set3
//
//        ArrayList <ILineDataSet> dataSets = new ArrayList<>();
//        dataSets.add(set1);
//        dataSets.add(set2);
//        LineData data = new LineData(dataSets);
//
//
//

//
//    }

    public void initHistoryChart(){
        Boolean b = true;
        int pointTemp=0;
        int indicatoBattery=0 ;
        int level=0;

        ArrayList<Entry> valuesBattery1 = new ArrayList<>();
        ArrayList <Entry> valuesBattery2 = new ArrayList<>();
        ArrayList <Entry> valuesBattery3 = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        int dateCur = calendar.get(Calendar.DAY_OF_MONTH);
        int hourCur = calendar.get(Calendar.HOUR_OF_DAY);
        int tempHour =0;
        int [] test = {70,-1,40,-1,-1,20,60,-1,-1,40,-1,-1,-1,-1,80,60,20,30,90,15,100,60,65,71};
        while(tempHour<=hourCur){
            level = HistoryPref.getLevel(getContext() , HistoryPref.getKeyFromTime(dateCur, tempHour));
            //level = move_up_button[tempHour];
            if(tempHour==0){
                if(level!=-1){

                    valuesBattery2.add(new Entry(0,level));
                    indicatoBattery = level;
                }else{
                    valuesBattery1.add(new Entry(0,0));
                }
                pointTemp = tempHour;
                tempHour++;
                continue;
            }

            if(level!=-1&&b==true){
                if(tempHour%2==0){
                    valuesBattery2.add(new Entry(tempHour,level));
                    valuesBattery1.add(new Entry(tempHour,level));
                    pointTemp = tempHour;
                    indicatoBattery = level;
                }



                b= false;

                tempHour++;
                continue;
            }
            if(level!=-1){
                if(tempHour%2==0){
                    valuesBattery2.add(new Entry(tempHour,level));
                    pointTemp = tempHour;
                    indicatoBattery = level;


                }


            }else{
                if(indicatoBattery!=0  ){
                    if(tempHour%2==0){
                        valuesBattery2.add(new Entry(tempHour,indicatoBattery));
                        pointTemp = tempHour;

                    }

                }

            }

            tempHour++;

        }
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(tempHour==24) tempHour =0;
        if(hour==0) hour = 24;
        level = HistoryPref.getLevel(getContext(),
                HistoryPref.getKeyFromTimeNow(day, tempHour));


        if(level!=-1){
            valuesBattery3.add(new Entry(pointTemp,indicatoBattery));
            valuesBattery3.add(new Entry(hour,level));
        }


        //set1
        set1 = new LineDataSet(valuesBattery1,"");
        set2 = new LineDataSet(valuesBattery2,"");
        set3 = new LineDataSet(valuesBattery3,"");
        ArrayList <ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        LineData data = new LineData(dataSets);
        chart.setData(data);

        set1.setFillAlpha(100);
        set1.setColor(0xFF9B9AAA);
        set1.setLineWidth(1);
        set1.setDrawValues(false);
        set1.setCircleColor(ContextCompat.getColor(getContext(), R.color.point_chart));
        set1.enableDashedLine(10f, 10f, 0f);
        set1.setDrawCircles(false);
        set1.setDrawFilled(true);
        set1.setFillColor(0xFF9B9AAA);
        set1.setDrawFilled(true);
        set1.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return chart.getAxisLeft().getAxisMinimum();
            }
        });

        // set color of filled area
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fill_green);
            set1.setFillDrawable(drawable);
        } else {
            set1.setFillColor(Color.BLACK);
        }
        //set2

        set2.setFillAlpha(100);
        set2.setColor(0xFF9B9AAA);
        set2.setLineWidth(1);
        //Xoa gia tri ngay diem
        set2.setDrawValues(false);
        set2.setCircleColor(ContextCompat.getColor(getContext(), R.color.point_chart));
        set2.setDrawFilled(true);
        set2.setFillColor(0xFF9B9AAA);
        set2.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return chart.getAxisLeft().getAxisMinimum();
            }
        });

        // set color of filled area
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fill_green);
            set2.setFillDrawable(drawable);
        } else {
            set2.setFillColor(Color.BLACK);
        }

        //set3

        set3.setFillAlpha(100);
        set3.setColor(0xFF9B9AAA);
        set3.setLineWidth(1);


        set3.setDrawValues(false);
        set3.setCircleColor(ContextCompat.getColor(getContext(), R.color.point_chart));
        set3.enableDashedLine(10f, 10f, 0f);
        set3.setDrawCircles(false);
        set3.setDrawFilled(true);
        set3.setFillColor(0xFF9B9AAA);
        set3.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return chart.getAxisLeft().getAxisMinimum();
            }
        });

        // set color of filled area
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fill_green);
            set3.setFillDrawable(drawable);
        } else {
            set3.setFillColor(Color.BLACK);
        }


        //chung


        String [] values = new String[]{"00:00","","","","04:00","","","","08:00","","","","12:00","","","","16:00","","","","20:00","","","","24:00"};
        String [] values2 = new String[]{"","","","","","","","","","","","","","","","","","","","","20%","","","","","","","","","","","","","","","","","","","","40%","","","","","","","","","","","","","","","","","","","","60%","","","","","","","","","","","","","","","","","","","","80%","","","","","","","","","","","","","","","","","","","","100%"};

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(ContextCompat.getColor(getContext(),R.color.description));
        xAxis.setAxisMaximum(24);
        xAxis.setAxisMinimum(0);
        xAxis.setLabelCount(6);
        xAxis.setValueFormatter(new MyXaxisValueFormater(values));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setTextColor(ContextCompat.getColor(getContext(),R.color.description));
        yAxis.setAxisMaximum(100);
        yAxis.setAxisMinimum(0);
        yAxis.setLabelCount(6);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setDrawGridLines(false);
        yAxis.setValueFormatter(new MyXaxisValueFormater(values2));

        yAxis.enableGridDashedLine(10f, 10f, 0f);
        chart.setDescription(null);    // Hide the description
        chart.getXAxis().setDrawAxisLine(false);
        yAxis.setDrawAxisLine(false);
        chart.getLegend().setEnabled(false);
        chart.setTouchEnabled(false);
    }


    BroadcastReceiver receiver;

    {
        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                if (intent.getAction().equals(BatteryService.ACTION_MAX_BATTERY_CHANGED_SEND)) {
                    BatteryInfo info = intent.getParcelableExtra(BatteryInfo.BATTERY_INFO_KEY);
                    //Update trang thai pin
                    initHistoryChart();
                    chart.notifyDataSetChanged();
                    chart.invalidate();

                }
            }
        };
    }
    public void mRegisterReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(BatteryService.ACTION_MAX_BATTERY_CHANGED_SEND);
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onResume() {
        super.onResume();
        initHistoryChart();
        mRegisterReceiver();
        Intent intent = new Intent();
        intent.setAction(BatteryService.ACTION_MAX_BATTERY_NEED_UPDATE);
        getActivity().sendBroadcast(intent);





    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }
}

