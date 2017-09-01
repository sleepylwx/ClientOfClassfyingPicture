package com.lwx.user.model;



import java.util.Calendar;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by 36249 on 2017/4/13.
 */

public interface CounterRepo {



    Completable addDayNum(long uid, int year, int month, int day);

    Completable addMonthNum(long uid, int year, int month);

    Completable addYearNum(long uid, int year);

    Observable<List<Integer>> getTimeNum(long uid, int kind, Calendar start, Calendar end);


}
