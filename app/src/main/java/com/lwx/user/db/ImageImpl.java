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

    @Override
    public Completable saveImages(List<Image> images) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                for(Image image : images)
                    imageDAO.createOrUpdate(image);
                XLog.v("保存Images:" + images);
            }
        });
    }

    @Override
    public Observable<Image> getImage(String imageId) {
        return Observable.create(new ObservableOnSubscribe<Image>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Image> e) throws Exception {
                Image image = imageDAO.queryForId(imageId);
                e.onNext(image);
                XLog.v("(数据库)查询Image:" + image);
            }
        });
    }

    @Override
    public Observable<List<Label>> getImageLabels(String imageId) {
        return Observable.create(new ObservableOnSubscribe<List<Label>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Label>> e) throws Exception {

                QueryBuilder imageLabelQuery = imageLabelDAO.queryBuilder();
                imageLabelQuery
                        .selectColumns(ImageLabel.LABEL_FIELD)
                        .where()
                        .in(ImageLabel.IMAGE_FIELD, imageId);

                QueryBuilder labelQuery = labelDAO.queryBuilder();
                labelQuery.where()
                        .in(Label.LABEL_FIELD, imageLabelQuery);
                List<Label> labels = labelQuery.query();
                XLog.v("(数据库)getPictures 执行查询：%s,\n 结果:%s" , labelQuery.prepareStatementString(),labels);
                e.onNext(labels);
            }
        });
    }

    @Override
    public Completable saveLabels(long uid, String imageId, List<String> labels) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                Image image = imageDAO.queryForId(imageId);
                if (image == null) {
                    e.onError(new Throwable("没有该图片"));
                    return;
                }
                for(String iLabel : labels){
                    Label iLabelBean = null;
                    List<Label> iLabels = labelDAO.queryForEq(Label.LABEL_FIELD, iLabel);
                    if(iLabels.size() > 0){
                        iLabelBean = iLabels.get(0);
                    } else {
                        iLabelBean = new Label(iLabel, uid);
                        labelDAO.createOrUpdate(iLabelBean);
                    }
                    imageLabelDAO.createOrUpdate(new ImageLabel(image, iLabelBean));
                }
            }
        });
    }

    @Override
    public Completable saveLabel(long uid, String imageId, String labels) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {

                Image image = imageDAO.queryForId(imageId);
                if (image == null) {
                    e.onError(new Throwable("没有该图片"));
                    return;
                }
                Label iLabelBean = null;
                List<Label> iLabels = labelDAO.queryForEq(Label.LABEL_FIELD, labels);
                if(iLabels.size() > 0){
                    iLabelBean = iLabels.get(0);
                } else {
                    iLabelBean = new Label(labels, uid);
                    labelDAO.createOrUpdate(iLabelBean);
                }
                imageLabelDAO.createOrUpdate(new ImageLabel(image, iLabelBean));
            }
        });
    }


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
