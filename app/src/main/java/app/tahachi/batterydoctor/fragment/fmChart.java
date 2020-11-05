package app.tahachi.batterydoctor.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import app.ads.control.AdControlHelp;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import app.tahachi.batterydoctor.R;
import app.tahachi.batterydoctor.Utilsb.SharePreferenceUtils;
import app.tahachi.batterydoctor.adapter.ChartAdapter;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class fmChart extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_charge_history, container, false);
        intView(view);
        intEvent(view);
        intData();
        context = getContext();
        adControlHelp = AdControlHelp.getInstance(context);

        adapterViewPager = new ChartAdapter(getActivity().getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        vpPager.setCurrentItem(2);

        adControlHelp.loadBannerFragment(getActivity(), view);
        return view;
    }

    public void intView(View v) {
        tvNormal = v.findViewById(R.id.tvNormal);
        tvOver = v.findViewById(R.id.tvOver);
        tvHealthy = v.findViewById(R.id.tvHealthy);
        tvLastFull = v.findViewById(R.id.tvLastFull);
        tvChargeType = v.findViewById(R.id.tvChargeType);
        tvTimeCharge = v.findViewById(R.id.tvTimeCharge);
        tvQuantity = v.findViewById(R.id.tvQuantity);
        tvCount = v.findViewById(R.id.tvCount);
        imgColor = v.findViewById(R.id.imgColor);
        tvDate = v.findViewById(R.id.tvDate);
        vpPager = v.findViewById(R.id.viewPager);
        indicator = v.findViewById(R.id.indicator);
        indicator.setupWithViewPager(vpPager, true);
    }

    public void intEvent(View v) {


        intChart(v);
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
                tvCount.setText(String.valueOf(SharePreferenceUtils.getInstance(getActivity()).getChargeNormal()));
            }
        });
        viewPagerListenItem();
    }

    public void setPercent(float i) {

        float normal = (float) SharePreferenceUtils.getInstance(getActivity()).getChargeNormal();
        float healthy = (float) SharePreferenceUtils.getInstance(getActivity()).getChargeHealthy();
        float over = (float) SharePreferenceUtils.getInstance(getActivity()).getChargeOver();
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

    public void intChart(View v) {
        //      tfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        chart = v.findViewById(R.id.chartPie);
        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
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


        if (SharePreferenceUtils.getInstance(getActivity()).getChargeNormal() != 0) {
            tvNormal.setText(String.valueOf(SharePreferenceUtils.getInstance(getActivity()).getChargeNormal()));
        }
        if (SharePreferenceUtils.getInstance(getActivity()).getChargeOver() != 0) {
            tvOver.setText(String.valueOf(SharePreferenceUtils.getInstance(getActivity()).getChargeOver()));
        }
        if (SharePreferenceUtils.getInstance(getActivity()).getChargeHealthy() != 0) {
            tvHealthy.setText(String.valueOf(SharePreferenceUtils.getInstance(getActivity()).getChargeHealthy()));

        }
        if (SharePreferenceUtils.getInstance(getActivity()).getChargeFull() != null) {
            tvLastFull.setText(SharePreferenceUtils.getInstance(getActivity()).getChargeFull());
        }
        tvChargeType.setText(SharePreferenceUtils.getInstance(getActivity()).getChargeType());
        tvTimeCharge.setText(formatHourMinutune(SharePreferenceUtils.getInstance(getActivity()).getTimeCharge()));
        tvQuantity.setText(SharePreferenceUtils.getInstance(getActivity()).getChargeQuantity() + "%");


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
        float b = (float) Math.round(SharePreferenceUtils.getInstance(getActivity()).getChargeNormal()) / 40;

        float normal = (float) SharePreferenceUtils.getInstance(getActivity()).getChargeNormal();
        float healthy = (float) SharePreferenceUtils.getInstance(getActivity()).getChargeHealthy();
        float over = (float) SharePreferenceUtils.getInstance(getActivity()).getChargeOver();
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

        colors.add(ContextCompat.getColor(getActivity(), R.color.color_normal));
        colors.add(ContextCompat.getColor(getActivity(), R.color.color_healthy));
        colors.add(ContextCompat.getColor(getActivity(), R.color.color_over));


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
