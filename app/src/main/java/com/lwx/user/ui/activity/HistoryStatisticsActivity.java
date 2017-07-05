package com.lwx.user.ui.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryStatisticsActivity extends AppCompatActivity implements HistoryStatisticsContract.View{

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.date_text)TextView dateText;
    @BindView(R.id.chart)BarChart barChart;
    @BindView(R.id.piechart)PieChart pieChart;
    @BindView(R.id.num_text)TextView numText;

    private int count = 0;

    private HistoryStatisticsContract.Presenter presenter;
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

        barChart.setTouchEnabled(false);
        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);


        //chart.setDrawValueAboveBar(true);
        //chart.setHighlightFullBarEnabled(true);
//        xAxis.setXOffset(0);
//        chart.getAxisLeft().setAxisMinimum(0);

       // chart.setDoubleTapToZoomEnabled(true);

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1,10));
        entries.add(new BarEntry(2,20));
        entries.add(new BarEntry(3,30));
        entries.add(new BarEntry(4,40));
        entries.add(new BarEntry(5,50));
        entries.add(new BarEntry(6,60));
        entries.add(new BarEntry(7,70));

        BarDataSet barDataSet = new BarDataSet(entries,"labels");
       // barDataSet.setColors(new int[]{Color.rgb(255, 241, 226),Color.rgb(155, 241, 226),Color.rgb(255, 21, 226), Color.rgb(255, 241, 26)});

        BarData barData = new BarData(barDataSet);

        barChart.setData(barData);
        barChart.invalidate();
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
    public void onGetTimeNumSuccess(List<Pair> list) {


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
