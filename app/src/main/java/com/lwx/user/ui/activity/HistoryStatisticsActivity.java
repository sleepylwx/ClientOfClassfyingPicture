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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.lwx.user.App;
import com.lwx.user.R;
import com.lwx.user.contracts.HistoryStatisticsContract;
import com.lwx.user.db.model.Pair;
import com.lwx.user.presenter.HistoryStatisticsPresenter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    @OnClick(R.id.left)
    void onClick(){

        if(kind == 0 ){

            start.add(Calendar.DAY_OF_MONTH,-7);
            end.add(Calendar.DAY_OF_MONTH,-7);
            presenter.getTimeNum(App.getInstance().getUid(),kind,start,end);
        }
        else if(kind == 1){

            start.add(Calendar.MONTH,-7);
            end.add(Calendar.MONTH,-7);
            presenter.getTimeNum(App.getInstance().getUid(),
                    kind,start,end);
        }
        else{


            start.add(Calendar.YEAR,-4);
            end.add(Calendar.YEAR,-4);
            presenter.getTimeNum(App.getInstance().getUid(),
                    kind,start,end);
        }
    }

    @OnClick(R.id.right)
    void onClick1(){

        if(kind == 0 ){

            start.add(Calendar.DAY_OF_MONTH,7);
            end.add(Calendar.DAY_OF_MONTH,7);
            presenter.getTimeNum(App.getInstance().getUid(),kind,start,end);
        }
        else if(kind == 1){

            start.add(Calendar.MONTH,7);
            end.add(Calendar.MONTH,7);
            presenter.getTimeNum(App.getInstance().getUid(),
                    kind,start,end);
        }
        else{


            start.add(Calendar.YEAR,4);
            end.add(Calendar.YEAR,4);
            presenter.getTimeNum(App.getInstance().getUid(),
                    kind,start,end);
        }
    }
    @OnClick(R.id.day)
    void onClick2(){

        kind = 0;
        end = Calendar.getInstance();
        start = (Calendar) end.clone();
        start.add(Calendar.DAY_OF_MONTH,-6);
        presenter.getTimeNum(App.getInstance().getUid(),kind,start,end);
    }

    @OnClick(R.id.month)
    void onClick3(){
        kind = 1;
        end = Calendar.getInstance();
        start = (Calendar) end.clone();
        start.add(Calendar.MONTH,-6);
        presenter.getTimeNum(App.getInstance().getUid(),kind,start,end);
    }

    @OnClick(R.id.year)
    void onClick4(){

        kind = 2;
        end = Calendar.getInstance();
        start = (Calendar) end.clone();
        start.add(Calendar.YEAR,-3);
        presenter.getTimeNum(App.getInstance().getUid(),kind,start,end);
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
        initBarChart();
        initPieChart();
        initNumText();
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
        start.add(Calendar.DAY_OF_MONTH,-6);
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
        List<BarEntry> entries = new ArrayList<>();

        Calendar temp = (Calendar) start.clone();

        if(kind == 0){


            for(int i = 0; i < list.size() ; ++i){

                entries.add(new BarEntry(temp.get(Calendar.DAY_OF_MONTH),list.get(i)));
                temp.add(Calendar.DAY_OF_MONTH,1);
            }

        }
        else if(kind == 1){

            for(int i = 0; i < list.size() ; ++i){

                entries.add(new BarEntry(temp.get(Calendar.MONTH)+1,list.get(i)));
                temp.add(Calendar.MONTH,1);
            }
        }
        else{

            for(int i = 0; i < list.size() ;++i){

                entries.add(new BarEntry(temp.get(Calendar.YEAR),list.get(i)));
                temp.add(Calendar.YEAR,1);
            }
        }

        BarDataSet barDataSet = new BarDataSet(entries,"");
        // barDataSet.setColors(new int[]{Color.rgb(255, 241, 226),Color.rgb(155, 241, 226),Color.rgb(255, 21, 226), Color.rgb(255, 241, 26)});

        BarData barData = new BarData(barDataSet);


        barChart.setData(barData);
        barChart.invalidate();

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
