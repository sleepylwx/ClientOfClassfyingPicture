package com.lwx.user.model;

import com.elvishew.xlog.XLog;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.lwx.user.model.model.DayNum;
import com.lwx.user.model.model.MonthNum;
import com.lwx.user.model.model.YearNum;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by henry on 17-4-18.
 */
public class CounterImpl implements CounterRepo {


    @Override
    public Completable addDayNum(long uid, int year, int month, int day) {

        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {

                QueryBuilder builder = dayNumDAO.queryBuilder();


                List<DayNum> list  =   builder.where()
                        .eq(DayNum.UID,uid)
                        .and()
                        .eq(DayNum.YEAR,year)
                        .and()
                        .eq(DayNum.MONTH,month)
                        .and()
                        .eq(DayNum.DAY,day)
                        .query();

                DayNum temp ;
                if(list == null || list.size() == 0){

                    temp = new DayNum(uid,year,month,day);
                }
                else{

                    temp = list.get(0);
                }
                ++temp.num;
                dayNumDAO.createOrUpdate(temp);
                e.onComplete();

            }
        });

    }

    @Override
    public Completable addMonthNum(long uid, int year, int month) {

        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {

                QueryBuilder builder = monthNumDAO.queryBuilder();


                List<MonthNum> list  =   builder.where()
                        .eq(DayNum.UID,uid)
                        .and()
                        .eq(DayNum.YEAR,year)
                        .and()
                        .eq(DayNum.MONTH,month)
                        .query();
                MonthNum temp;
                if(list == null || list.size() == 0){

                    temp = new MonthNum(uid,year,month);
                }
                else{

                    temp = list.get(0);
                }
                ++temp.num;
                monthNumDAO.createOrUpdate(temp);
                e.onComplete();
            }
        });
    }

    @Override
    public Completable addYearNum(long uid, int year) {


        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {

                QueryBuilder builder = yearNumDAO.queryBuilder();


                List<YearNum> list =   builder.where()
                        .eq(DayNum.UID,uid)
                        .and()
                        .eq(DayNum.YEAR,year)
                        .query();
                YearNum temp;
                if(list == null || list.size() == 0){

                    temp = new YearNum(uid,year);
                }
                else{

                    temp = list.get(0);
                }

                ++temp.num;
                yearNumDAO.createOrUpdate(temp);
                e.onComplete();
            }
        });
    }

    @Override
    public Observable<List<Integer>> getTimeNum(long uid, int kind, Calendar start, Calendar end) {

        return Observable.create(new ObservableOnSubscribe<List<Integer>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Integer>> e) throws Exception {

                Calendar temp = (Calendar) start.clone();
                List<Integer> list = new ArrayList<Integer>();
                if(kind == 0){

                    while(temp.before(end) || temp.equals(end)){

                        int year = temp.get(Calendar.YEAR);
                        int month = temp.get(Calendar.MONTH) + 1;
                        int day = temp.get(Calendar.DAY_OF_MONTH);

                        QueryBuilder builder = dayNumDAO.queryBuilder();

                        List<DayNum> qlist =   builder.where()
                                .eq(DayNum.UID,uid)
                                .and()
                                .eq(DayNum.YEAR,year)
                                .and()
                                .eq(DayNum.MONTH,month)
                                .and()
                                .eq(DayNum.DAY,day)
                                .query();

                        if(qlist == null || qlist.size() == 0){

                            list.add(0);
                        }
                        else{

                            list.add(qlist.get(0).num);
                        }

                        temp.add(Calendar.DAY_OF_MONTH,1);
                    }


                    e.onNext(list);
                }
                else if(kind == 1){


                    while(temp.before(end) || temp.equals(end)){

                        int year = temp.get(Calendar.YEAR);
                        int month = temp.get(Calendar.MONTH)+1;

                        QueryBuilder builder = monthNumDAO.queryBuilder();

                        List<MonthNum> qlist =   builder.where()
                                .eq(DayNum.UID,uid)
                                .and()
                                .eq(DayNum.YEAR,year)
                                .and()
                                .eq(DayNum.MONTH,month)
                                .query();

                        if(qlist == null || qlist.size() == 0){

                            list.add(0);
                        }
                        else{

                            list.add(qlist.get(0).num);
                        }

                        temp.add(Calendar.MONTH,1);
                    }

                    e.onNext(list);
                }
                else{

                    while(temp.before(end) || temp.equals(end)){


                        int year = temp.get(Calendar.YEAR);

                        QueryBuilder builder = yearNumDAO.queryBuilder();

                        List<YearNum> qlist =   builder.where()
                                .eq(DayNum.UID,uid)
                                .and()
                                .eq(DayNum.YEAR,year)
                                .query();

                        if(qlist == null || qlist.size() == 0){

                            list.add(0);
                        }
                        else{

                            list.add(qlist.get(0).num);
                        }

                        temp.add(Calendar.YEAR,1);
                    }

                    e.onNext(list);
                }

            }
        });
    }


    private static CounterImpl ourInstance = new CounterImpl();

    public static CounterImpl getInstance() {
        return ourInstance;
    }

    private CounterImpl() {
        try {

            dayNumDAO = DbHelper.getInstance().getDao(DayNum.class);
            monthNumDAO = DbHelper.getInstance().getDao(MonthNum.class);
            yearNumDAO = DbHelper.getInstance().getDao(YearNum.class);

        }catch (Exception e){
            XLog.e("SQL Exception" , e);
        }
    }


    Dao<DayNum,Long> dayNumDAO = null;
    Dao<MonthNum,Long> monthNumDAO = null;
    Dao<YearNum,Long> yearNumDAO = null;

}
