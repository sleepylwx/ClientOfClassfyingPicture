package com.lwx.user.db;

import com.elvishew.xlog.XLog;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.lwx.user.db.model.Image;
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
 * Created by henry on 17-4-18.
 */
public class ImageImpl implements ImageRepo {

    Dao<Image, String> imageDAO = null;
    Dao<ImageLabel, Long> imageLabelDAO = null;
    Dao<Label, String> labelDAO = null;


    @Override
    public Observable<List<Image>> getAllPictures() {
        return Observable.create(new ObservableOnSubscribe<List<Image>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Image>> e) throws Exception {
                e.onNext(imageDAO.queryForAll());
            }
        });
    }

    @Override
    public Completable saveImage(Image image) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                imageDAO.createOrUpdate(image);
            }
        });
    }

    @Override
    public Observable<List<Image>> getPictures(String label) {
        return Observable.create(new ObservableOnSubscribe<List<Image>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Image>> e) throws Exception {
                QueryBuilder labelQuery = labelDAO.queryBuilder();
                labelQuery.where()
                        .eq(Label.LABEL_FIELD, label);

                QueryBuilder imageLabelQuery = imageLabelDAO.queryBuilder();
                imageLabelQuery.where()
                        .in(ImageLabel.LABEL_FIELD, labelQuery);

                QueryBuilder imageQuery = imageDAO.queryBuilder();
                imageQuery.where()
                        .in(Image.UUID_FIELD, imageLabelQuery);

                XLog.v("getPictures 执行查询：" + imageQuery.prepareStatementString());
                e.onNext(imageQuery.query());

            }
        });
    }

    private static ImageImpl ourInstance = new ImageImpl();

    public static ImageImpl getInstance() {
        return ourInstance;
    }

    private ImageImpl() {
        try {
            imageDAO = DbHelper.getInstance().getDao(Image.class);
            imageLabelDAO = DbHelper.getInstance().getDao(ImageLabel.class);
            labelDAO = DbHelper.getInstance().getDao(Label.class);
        }catch (Exception e){
            XLog.e("SQL Exception" , e);
        }
    }
}
