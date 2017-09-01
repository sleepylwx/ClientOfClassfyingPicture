package com.lwx.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lwx.user.R;
import com.lwx.user.contracts.HistoryLabelContract;
import com.lwx.user.ui.activity.HistoryLabelActivity;
import com.moxun.tagcloudlib.view.TagsAdapter;

import java.util.List;

/**
 * Created by 36249 on 2017/9/1.
 */

public class TagCloudAdapter extends TagsAdapter {


    private List<String> list;

    private HistoryLabelContract.View activity;

    public TagCloudAdapter() {
        super();
    }

    public TagCloudAdapter(HistoryLabelContract.View activity,List<String > list){

        this.activity = activity;
        this.list = list;

    }

    @Override
    protected void setOnDataSetChangeListener(OnDataSetChangeListener listener) {
        super.setOnDataSetChangeListener(listener);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(Context context, int position, ViewGroup parent) {

        String str = list.get(position);
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_tagcloud,null);

        ((TextView)linearLayout.getChildAt(0)).setText(str);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.jumpToHistoryImageActivity(((TextView) linearLayout.getChildAt(0)).getText().toString());
            }
        });
        return linearLayout;
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public int getPopularity(int position) {
        return 0;
    }

    @Override
    public void onThemeColorChanged(View view, int themeColor) {

    }
}
