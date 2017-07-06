package com.lwx.user.contracts;

import com.lwx.user.db.model.Pair;

import java.util.Calendar;
import java.util.List;

/**
 * Created by 36249 on 2017/7/5.
 */

public interface HistoryStatisticsContract{

    public interface View extends BaseContract.View<HistoryStatisticsContract.Presenter>{


        void onGetTimeNumSuccess(List<Integer> list);
        void onGetLabelsNumSuccess(List<Pair> list);
        void onGetTotalNumSuccess(int num);
    }

    public interface Presenter extends BaseContract.Presenter<HistoryStatisticsContract.View>{

        void getTimeNum(long uid, int kind, Calendar start,Calendar end);
        void getLabelsNum(long uid);
        void getTotalNum(long uid);
    }

}
