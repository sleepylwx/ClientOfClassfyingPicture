package com.lwx.user.db;

import com.elvishew.xlog.XLog;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
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
    public Completable saveLabeledImage(String uuid, long uid, List<String> labels) {
        return null;
    }

    @Override
    public Observable<List<Image>> getAllLabeledImage(long uid) {
        return null;
    }

    @Override
    public Observable<List<String>> getLabeledImageLabels(String uuid, long uid) {
        return null;
    }

    @Override
    public Completable deleteImage(long uid, String uuid) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                DeleteBuilder deleteBuilder = imageDAO.deleteBuilder();
                deleteBuilder
                        .where()
                        .eq(Image.UID_FIELD, uid)
                        .and()
                        .eq(Image.UUID_FIELD, uuid);
                deleteBuilder.delete();

                e.onComplete();
            }
        });
    }

    @Override
    public Completable deleteAllImages(long uid) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                DeleteBuilder deleteBuilder = imageDAO.deleteBuilder();
                deleteBuilder
                        .where()
                        .eq(Image.UID_FIELD, uid);
                deleteBuilder.delete();

                e.onComplete();
            }
        });
    }

    @Override
    public Completable saveImages(long uid, List<Image> images) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {

                for(Image image : images) {

                    image.uid = uid;
                    Dao.CreateOrUpdateStatus status;
                    try{

                        status = imageDAO.createOrUpdate(image);
                        XLog.v("created:" + status.isCreated() +
                                " updated:" + status.isUpdated() + " changed lines:"
                                +status.getNumLinesChanged());

                    }
                    catch (Exception ex){

                        XLog.v(ex.getMessage());
                    }


                }

                e.onComplete();
            }
        });
    }

    @Override
    public Observable<Image> getImage(long uid, String imageId) {
        return Observable.create(new ObservableOnSubscribe<Image>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Image> e) throws Exception {
                List<Image> images = imageDAO.queryBuilder()
                        .where()
                        .eq(Image.UUID_FIELD, imageId)
                        .and()
                        .eq(Image.UID_FIELD, uid)
                        .query();
                if(images.size() > 0)
                    e.onNext(images.get(0));
                else
                    e.onNext(null);
                XLog.v("(数据库)查询Image:" + (images.size() > 0 ? images.get(0) : null));
            }
        });
    }

    @Override
    public Observable<List<Label>> getImageLabels(long uid, String imageId) {
        return Observable.create(new ObservableOnSubscribe<List<Label>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Label>> e) throws Exception {
                List<Image> images = imageDAO.queryBuilder()
                        .where()
                        .eq(Image.UUID_FIELD, imageId)
                        .and()
                        .eq(Image.UID_FIELD, uid)
                        .query();

                if(images.size() == 0){
                    e.onError(new Throwable("没有该用户对应图片"));
                    return;
                }

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
                Image image = null;
                List<Image> images = imageDAO.queryBuilder()
                        .where()
                        .eq(Image.UUID_FIELD, imageId)
                        .and()
                        .eq(Image.UID_FIELD, uid)
                        .query();
                if (images.size() == 0) {
                    e.onError(new Throwable("没有该图片"));
                    return;
                }
                image = images.get(0);
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

                e.onComplete();
            }
        });
    }

    @Override
    public Completable saveLabel(long uid, String imageId, String labels) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {

                Image image = null;

                List<Image> images = imageDAO.queryBuilder()
                        .where()
                        .eq(Image.UUID_FIELD, imageId)
                        .and()
                        .eq(Image.UID_FIELD, uid)
                        .query();

                if (images.size() == 0) {
                    e.onError(new Throwable("没有该图片"));
                    return;
                }

                image = images.get(0);

                Label iLabelBean = null;
                List<Label> iLabels = labelDAO.queryForEq(Label.LABEL_FIELD, labels);
                if(iLabels.size() > 0){
                    iLabelBean = iLabels.get(0);
                } else {
                    iLabelBean = new Label(labels, uid);
                    labelDAO.createOrUpdate(iLabelBean);
                }
                imageLabelDAO.createOrUpdate(new ImageLabel(image, iLabelBean));

                e.onComplete();
            }


        });
    }


    Dao<Image, String> imageDAO = null;
    Dao<ImageLabel, Long> imageLabelDAO = null;
    Dao<Label, String> labelDAO = null;


    @Override
    public Observable<List<Image>> getAllPictures(long uid) {
        return Observable.create(new ObservableOnSubscribe<List<Image>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Image>> e) throws Exception {
                e.onNext(imageDAO.queryForEq(Image.UID_FIELD, uid));
            }
        });
    }

    @Override
    public Completable saveImage(long uid, Image image) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                image.uid = uid;
                try{

                    imageDAO.createOrUpdate(image);
                }
                catch (Exception ex){

                    XLog.v(ex.getMessage());
                }

                e.onComplete();
            }

        });
    }

    @Override
    public Observable<List<Image>> getPictures(long uid, String label) {
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
                        .in(Image.UUID_FIELD, imageLabelQuery)
                        .and()
                        .eq(Image.UID_FIELD, uid);

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
