package com.tforlive.batterysaverpro.fragment;

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
import com.tforlive.batterysaverpro.R;
import com.tforlive.batterysaverpro.Utilsb.HistoryPref;

import java.util.ArrayList;
import java.util.Calendar;

public class fragment1 extends Fragment {

    // Store instance variables
    private String title;
    private int page;
    LineDataSet set1,set2,set3;

    // newInstance constructor for creating fragment with arguments
    public static fragment1 newInstance(int page, String title) {
        fragment1 fragmentBeforeYesterday = new fragment1();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentBeforeYesterday.setArguments(args);
        return fragmentBeforeYesterday;
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
        int [] test = {-1,-1,40,-1,-1,20,60,-1,-1,40,-1,-1,-1,-1,80,-1,-1,30,-1,15,-1,-1,-1,-1};


        ArrayList<Entry> valuesBattery2 = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -2);
        int dateCur = calendar.get(Calendar.DAY_OF_MONTH);
        int hourCur = 23;
        int tempHour =0;
        while(tempHour<=hourCur){
            level = HistoryPref.getLevel(getContext(),
                    HistoryPref.getKeyFromTime(dateCur, tempHour));

            if(tempHour==0){
                if(level!=-1){
                    valuesBattery2.add(new Entry(0,level));
                    pointTemp = tempHour;
                    indicatoBattery = level;
                }else{
                    valuesBattery2.add(new Entry(0,0));
                }
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
                if(indicatoBattery!=0){
                    if(tempHour%2==0){
                        valuesBattery2.add(new Entry(tempHour,indicatoBattery));
                        pointTemp = tempHour;

                    }


                }

            }

            tempHour++;

        }
        Calendar calendar2= Calendar.getInstance();
        calendar2.add(Calendar.DAY_OF_MONTH, -1);
        int day = calendar2.get(Calendar.DAY_OF_MONTH);
        level = HistoryPref.getLevel(getContext(),
                HistoryPref.getKeyFromTime(day, 0));

        if(level!=-1){
            valuesBattery2.add(new Entry(24,level));
        }else{
            valuesBattery2.add(new Entry(24,0));
        }

        if(indicatoBattery!=0){

            set2 = new LineDataSet(valuesBattery2,"");
            set2.setFillAlpha(100);
            set2.setColor(0xFF9B9AAA);
            set2.setLineWidth(1);
            //Xoa gia tri ngay diem
            set2.setDrawValues(false);
            set2.setCircleColor(ContextCompat.getColor(getContext(), R.color.point_chart));
            set2.setDrawFilled(true);
            set2.setFillColor(0xFF717EEE);
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


            //chung
            ArrayList <ILineDataSet> dataSets = new ArrayList<>();

            dataSets.add(set2);
            LineData data = new LineData(dataSets);
            chart.setData(data);

            String [] values = new String[]{"00","","","","04","","","","08","","","","12","","","","16","","","","20","","","","24"};
            String [] values2 = new String[]{"","","","","","","","","","","","","","","","","","","","","20","","","","","","","","","","","","","","","","","","","","40","","","","","","","","","","","","","","","","","","","","60","","","","","","","","","","","","","","","","","","","","80","","","","","","","","","","","","","","","","","","","","100"};

            XAxis xAxis = chart.getXAxis();
            xAxis.setTextColor(ContextCompat.getColor(getContext(),R.color.secondary_text));
            xAxis.setAxisMaximum(24);
            xAxis.setAxisMinimum(0);
            xAxis.setLabelCount(6);
            xAxis.setValueFormatter(new fragment1.MyXaxisValueFormater(values));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            chart.getXAxis().setDrawGridLines(false);

            YAxis yAxis = chart.getAxisLeft();
            yAxis.setTextColor(ContextCompat.getColor(getContext(),R.color.title_dark));
            yAxis.setAxisMaximum(100);
            yAxis.setAxisMinimum(0);
            yAxis.setLabelCount(6);
            chart.getAxisRight().setEnabled(false);

            yAxis.setValueFormatter(new fragment1.MyXaxisValueFormater(values2));

            yAxis.enableGridDashedLine(10f, 10f, 0f);
            chart.setDescription(null);    // Hide the description
            chart.getXAxis().setDrawAxisLine(false);
            yAxis.setDrawAxisLine(false);
            chart.getLegend().setEnabled(false);
            chart.setTouchEnabled(false);

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        initHistoryChart();

        removeDay();
    }

    public void removeDay(){
        Calendar calendar = Calendar.getInstance();
        int dateCur = calendar.get(Calendar.DAY_OF_MONTH);
        int temp = 0;
        while (temp<=23){
            HistoryPref.removeLevel(getActivity(), HistoryPref.getKeyFromTime(dateCur-3, temp));
            temp++;
        }

    }


}

