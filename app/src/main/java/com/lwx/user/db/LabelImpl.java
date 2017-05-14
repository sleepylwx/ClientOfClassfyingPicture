package com.lwx.user.db;

import com.elvishew.xlog.XLog;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.lwx.user.db.model.ImageLabel;
import com.lwx.user.db.model.Label;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;


/**
 * Created by henry on 17-4-19.
 */
public class LabelImpl implements LabelRepo{
    private Dao<Label, String> labelDAO;
    private Dao<ImageLabel, Long> imageLabelLongDAO;

    private static LabelImpl ourInstance = new LabelImpl();

    public static LabelImpl getInstance() {
        return ourInstance;
    }

    private LabelImpl() {
        try {
            labelDAO = DbHelper.getInstance().getDao(Label.class);
            imageLabelLongDAO = DbHelper.getInstance().getDao(ImageLabel.class);
        }catch (Exception e){
            XLog.e("SQL Exception", e);
        }
    }

    public Completable saveLabel(Label label){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                labelDAO.createOrUpdate(label);
                e.onComplete();
            }
        });
    }

    public Completable deleteLabel(Label label){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                labelDAO.delete(label);
                DeleteBuilder deleteBuilder = imageLabelLongDAO.deleteBuilder();
                deleteBuilder.where()
                        .eq(ImageLabel.LABEL_FIELD, label.uid);
                deleteBuilder.delete();

            }
        });
    }

    public Observable<List<Label>> getAllLabels(){
        return Observable.create(new ObservableOnSubscribe<List<Label>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Label>> e) throws Exception {
                e.onNext(labelDAO.queryForAll());
            }
        });
    }

    public Observable<Label> getLabel(String label){
        return Observable.create(new ObservableOnSubscribe<Label>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Label> e) throws Exception {
                e.onNext(labelDAO.queryForId(label));
            }
        });
    }

}
