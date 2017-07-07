package com.lwx.user.ui.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.lwx.user.App;
import com.lwx.user.R;
import com.lwx.user.contracts.HistoryStatisticsContract;
import com.lwx.user.model.model.Pair;
import com.lwx.user.presenter.HistoryStatisticsPresenter;
import com.lwx.user.utils.Date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HistoryStatisticsActivity extends AppCompatActivity implements HistoryStatisticsContract.View{

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.date_text)TextView dateText;
    @BindView(R.id.day)BootstrapButton dayButton;
    @BindView(R.id.month)BootstrapButton monthButton;
    @BindView(R.id.year)BootstrapButton yearButon;
    @BindView(R.id.left)Button left;
    @BindView(R.id.right)Button right;
    @BindView(R.id.chart)BarChart barChart;
    @BindView(R.id.piechart)PieChart pieChart;
    @BindView(R.id.num_text)TextView numText;

    private int count = 0;

    private int kind = 0;


    private HistoryStatisticsContract.Presenter presenter;

    private Calendar start;
    private Calendar end;

    public static final int YEARCHANGE = 5;
    public static final int MONTHCHANGE = 6;
    public static final int DAYCHANGE = 7;

    @OnClick(R.id.left)
    void onClick(){

        if(kind == 0 ){

            start.add(Calendar.DAY_OF_MONTH,-DAYCHANGE);
            end.add(Calendar.DAY_OF_MONTH,-DAYCHANGE);
            presenter.getTimeNum(App.getInstance().getUid(),kind,start,end);
            //initDateText();
        }
        else if(kind == 1){

            start.add(Calendar.MONTH,-MONTHCHANGE);
            end.add(Calendar.MONTH,-MONTHCHANGE);
            presenter.getTimeNum(App.getInstance().getUid(),
                    kind,start,end);
            //initDateText();
        }
        else{


            start.add(Calendar.YEAR,-YEARCHANGE);
            end.add(Calendar.YEAR,-YEARCHANGE);
            presenter.getTimeNum(App.getInstance().getUid(),
                    kind,start,end);
            //initDateText();
        }
    }

    @OnClick(R.id.right)
    void onClick1(){

        if(kind == 0 ){

            start.add(Calendar.DAY_OF_MONTH,DAYCHANGE);
            end.add(Calendar.DAY_OF_MONTH,DAYCHANGE);
            presenter.getTimeNum(App.getInstance().getUid(),kind,start,end);
            //initDateText();
        }
        else if(kind == 1){

            start.add(Calendar.MONTH,MONTHCHANGE);
            end.add(Calendar.MONTH,MONTHCHANGE);
            presenter.getTimeNum(App.getInstance().getUid(),
                    kind,start,end);
            //initDateText();
        }
        else{


            start.add(Calendar.YEAR,YEARCHANGE);
            end.add(Calendar.YEAR,YEARCHANGE);
            presenter.getTimeNum(App.getInstance().getUid(),
                    kind,start,end);
            //initDateText();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historystatistics);
        ButterKnife.bind(this);

        presenter = new HistoryStatisticsPresenter(this);

        init();

    }


    private void init(){

        initToolbar();
        initButton();
        initBarChart();
        initPieChart();
        initNumText();
    }

    private void initButton(){

        dayButton.setOnCheckedChangedListener(new BootstrapButton.OnCheckedChangedListener() {
            @Override
            public void OnCheckedChanged(BootstrapButton bootstrapButton, boolean isChecked) {

                if(!isChecked){

                    return;
                }
                kind = 0;
                end = Calendar.getInstance();
                start = (Calendar) end.clone();
                start.add(Calendar.DAY_OF_MONTH,-(DAYCHANGE -1));
                presenter.getTimeNum(App.getInstance().getUid(),kind,start,end);
            }
        });

        monthButton.setOnCheckedChangedListener(new BootstrapButton.OnCheckedChangedListener() {
            @Override
            public void OnCheckedChanged(BootstrapButton bootstrapButton, boolean isChecked) {

                if(!isChecked){

                    return;
                }
                kind = 1;
                end = Calendar.getInstance();
                start = (Calendar) end.clone();
                start.add(Calendar.MONTH,-(MONTHCHANGE - 1));
                presenter.getTimeNum(App.getInstance().getUid(),kind,start,end);
            }
        });

        yearButon.setOnCheckedChangedListener(new BootstrapButton.OnCheckedChangedListener() {
            @Override
            public void OnCheckedChanged(BootstrapButton bootstrapButton, boolean isChecked) {

                if(!isChecked){

                    return;
                }

                kind = 2;
                end = Calendar.getInstance();
                start = (Calendar) end.clone();
                start.add(Calendar.YEAR,-(YEARCHANGE - 1));
                presenter.getTimeNum(App.getInstance().getUid(),kind,start,end);
            }
        });
    }

    private void initToolbar(){

        toolbar.setTitle("统计信息");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


    }


    private void initBarChart(){

        barChart.setDrawBorders(false);
        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);
        barChart.getLegend().setEnabled(false);
        XAxis  xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                int a = (int)value;
                if(kind == 0){

                    Date date = new Date();
                    int year = start.get(Calendar.YEAR);
                    int month = start.get(Calendar.MONTH)+1;
                    int num = date.getMonthDayNum(year,month);
                    if(a > num){

                        a = a - num;
                    }
                }
                else if(kind == 1){

                    if(a > 12){

                        a = a - 12;
                    }
                }
                return String.valueOf(a);
            }
        });



        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.animateXY(1000,2000);

//        barChart.setTouchEnabled(false);
//        barChart.setDragEnabled(false);
//        barChart.setScaleEnabled(false);


        //temp.
        //dateText.setText(temp.getYear() +"年" +temp.getMonth() + "月" +
       // start + "日~" +  end + "日") ;

        Calendar end = Calendar.getInstance();
        Calendar start = (Calendar) end.clone();
        this.start =start;
        this.end = end;
        start.add(Calendar.DAY_OF_MONTH,-(DAYCHANGE - 1));
        presenter.getTimeNum(App.getInstance().getUid(),
                kind,start,end);

        //chart.setDrawValueAboveBar(true);
        //chart.setHighlightFullBarEnabled(true);
//        xAxis.setXOffset(0);
//        chart.getAxisLeft().setAxisMinimum(0);

       // chart.setDoubleTapToZoomEnabled(true);


    }

    private void initPieChart(){



        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        //pieChart.getLegend().setEnabled(false);
        //pieChart.setTouchEnabled(false);
        pieChart.setUsePercentValues(true);
       /// / pieChart.setHighlightPerTapEnabled(true);

        //pieChart.setDrawHoleEnabled(false);
        pieChart.setDrawEntryLabels(false);
        //pieChart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
        pieChart.getLegend().setWordWrapEnabled(true);

        pieChart.setDrawCenterText(true);
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                pieChart.setCenterText((int)(((PieEntry)e).getValue()) + "次");
            }

            @Override
            public void onNothingSelected() {

                pieChart.setCenterText("");
            }
        });

        pieChart.animateXY(1000,2000);
        presenter.getLabelsNum(App.getInstance().getUid());

    }

    private void initNumText(){


        presenter.getTotalNum(App.getInstance().getUid());
    }

    @Override
    public HistoryStatisticsContract.Presenter getPresenter() {

        return presenter;
    }

    @Override
    public void onNetWorkError() {

        if(count == 0){

            ++count;
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onGetTimeNumSuccess(List<Integer> list) {



        Log.d(TAG,"size + " + list.size());

        for(int i = 0; i < list.size() ; ++i){

            Log.d(TAG,"value + "+ list.get(i));
        }
        List<BarEntry> entries = new ArrayList<>();


        if(kind == 0){

            int startDay = start.get(Calendar.DAY_OF_MONTH);
            for(int i = 0; i < list.size() ; ++i){


                entries.add(new BarEntry(startDay+i,list.get(i)));

            }

        }
        else if(kind == 1){

            int startMonth = start.get(Calendar.MONTH)+1;
            for(int i = 0; i < list.size() ; ++i){

                entries.add(new BarEntry(startMonth+i,list.get(i)));

            }
        }
        else{

            int startYear = start.get(Calendar.YEAR);
            for(int i = 0; i < list.size() ;++i){

                entries.add(new BarEntry(startYear+i,list.get(i)));

            }
        }


        BarDataSet barDataSet = new BarDataSet(entries,"");
        // barDataSet.setColors(new int[]{Color.rgb(255, 241, 226),Color.rgb(155, 241, 226),Color.rgb(255, 21, 226), Color.rgb(255, 241, 26)});

//        for(int i = 0; i < entries.size() ; ++i){
//
//            barDataSet.addEntryOrdered(entries.get(i));
//        }



        barDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

                int a = (int)entry.getY();
                if(a == 0){

                    return " ";
                }
                return String.valueOf(a);

            }
        });


        BarData barData = new BarData(barDataSet);


        barChart.setData(barData);
        barChart.invalidate();

        initDateText();
    }

    private void initDateText(){

        dateText.setText(
                start.get(Calendar.YEAR) + "年" + (start.get(Calendar.MONTH)+1) + "月"
                        + start.get(Calendar.DAY_OF_MONTH) + "日 ~ "
                        + end.get(Calendar.YEAR) + "年" + (end.get(Calendar.MONTH)+1) + "月"
                        + end.get(Calendar.DAY_OF_MONTH) + "日"
        );

    }
    @Override
    public void onGetLabelsNumSuccess(List<Pair> list) {

        if(list.size() == 0){

            return;
        }

        List<PieEntry> entries = new ArrayList<>();

        int count = list.size() > 7 ? 7 : list.size();
        for(int i = 0; i < count ; ++i){

            Pair pair = list.get(i);
            entries.add(new PieEntry(pair.value,pair.key));
        }



        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setColors(new int[]{Color.rgb(255, 241, 226),Color.rgb(155, 241, 226),Color.rgb(255, 197, 226), Color.rgb(255, 241, 26),
                Color.rgb(157,220,79),Color.rgb(56,123,220),Color.rgb(170,60,70)});
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(10f);
        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    public static final String TAG = "HistoryStatiActivity";
    @Override
    public void onGetTotalNumSuccess(int num) {
        Log.d(TAG,"totalNum " + num);
        numText.setText("" + num);
    }
}
