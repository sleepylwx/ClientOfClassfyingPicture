package com.lwx.user.db;

import com.elvishew.xlog.XLog;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.lwx.user.db.model.Image;
import com.lwx.user.db.model.ImageLabel;
import com.lwx.user.db.model.Label;

import java.util.ArrayList;
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
    public Completable deleteImage(long uid, String uuid,boolean isLabeled) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {


                QueryBuilder imageQuery = imageDAO.queryBuilder();
                List<Image> imageList = imageQuery.where()
                        .eq(Image.UID_FIELD,uid)
                        .and()
                        .eq(Image.UUID_FIELD,uuid)
                        .and()
                        .eq(Image.ISLABLED_FIELD,isLabeled)
                        .query();

                Image image = imageList.get(0);

                QueryBuilder imageLabelQuery = imageLabelDAO.queryBuilder();
                List<ImageLabel> imageLabelList = imageLabelQuery.where()
                        .eq(ImageLabel.IMAGE_FIELD,image)
                        .query();

                for(int i = 0 ; i < imageLabelList.size() ; ++i){

                    labelDAO.delete(imageLabelList.get(i).label);
                    imageLabelDAO.delete(imageLabelList.get(i));
                }

                e.onComplete();
            }
        });
    }

    @Override
    public Completable deleteAllImages(long uid,boolean isLabeled) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {

                QueryBuilder imageQuery = imageDAO.queryBuilder();
                List<Image> imageList = imageQuery.where()
                        .eq(Image.UID_FIELD,uid)
                        .and()
                        .eq(Image.ISLABLED_FIELD,isLabeled)
                        .query();

                for(int i = 0 ; i < imageList.size(); ++i){

                    QueryBuilder imageLabelQuery = imageLabelDAO.queryBuilder();

                    List<ImageLabel> imageLabelList = imageLabelQuery.where()
                            .eq(ImageLabel.IMAGE_FIELD,imageList.get(i))
                            .query();

                    for(int j = 0; j < imageLabelList.size() ; ++j){

                        labelDAO.delete(imageLabelList.get(i).label);

                        imageLabelDAO.delete(imageLabelList.get(i));
                    }
                }

                e.onComplete();
            }
        });
    }

    @Override
    public Completable saveImages(long uid, List<Image> images,boolean isLabeled) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {

                for(Image image : images) {

                    image.uid = uid;
                    image.isLabeled = isLabeled;

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
    public Observable<Image> getImage(long uid, String imageId,boolean isLabeled) {
        return Observable.create(new ObservableOnSubscribe<Image>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Image> e) throws Exception {
                List<Image> images = imageDAO.queryBuilder()
                        .where()
                        .eq(Image.UUID_FIELD, imageId)
                        .and()
                        .eq(Image.UID_FIELD, uid)
                        .and()
                        .eq(Image.ISLABLED_FIELD,isLabeled)
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
    public Observable<List<Label>> getLabelsByImage(long uid, String imageId,boolean isLabeled) {
        return Observable.create(new ObservableOnSubscribe<List<Label>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Label>> e) throws Exception {

                QueryBuilder imageQuery = imageDAO.queryBuilder();
                List<Image> imageList = imageQuery.where()
                        .eq(Image.UID_FIELD,uid)
                        .and()
                        .eq(Image.UUID_FIELD,imageId)
                        .and()
                        .eq(Image.ISLABLED_FIELD,isLabeled)
                        .query();

                if(imageList == null || imageList.size() == 0){

                    e.onError(new Throwable("没有找到该图片"));
                    return;
                }
                Image image = imageList.get(0);
                if(image == null){

                    e.onError(new Throwable("没有找到该图片"));
                    return ;
                }

                QueryBuilder imageLabelQuery = imageLabelDAO.queryBuilder();
                List<ImageLabel> imageLabelList = imageLabelQuery.where()
                        .eq(ImageLabel.IMAGE_FIELD,image)
                        .query();
                List<Label>labelList = new ArrayList<Label>();
                for(int i = 0 ; i < imageLabelList.size() ; ++i){

                    labelList.add(labelDAO.queryForSameId(imageLabelList.get(i).label));
                }

                e.onNext(labelList);
            }
        });
    }

    @Override
    public Completable saveLabels(long uid, String imageId, List<String> labels,boolean isLabeled) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                Image image = null;
                List<Image> images = imageDAO.queryBuilder()
                        .where()
                        .eq(Image.UUID_FIELD, imageId)
                        .and()
                        .eq(Image.UID_FIELD, uid)
                        .and()
                        .eq(Image.ISLABLED_FIELD,isLabeled)
                        .query();

                if (images.size() == 0) {
                    e.onError(new Throwable("没有该图片"));
                    return;
                }

                image = images.get(0);
                for(String iLabel : labels){
                    Label iLabelBean = null;
                    List<Label> iLabels = labelDAO.queryBuilder()
                            .where()
                            .eq(Label.LABEL_FIELD,iLabel)
                            .and()
                            .eq(Label.POST_FIELD,isLabeled)
                            .and()
                            .eq(Label.UID_FIELD,uid)
                            .query();
//                            .queryForEq(Label.LABEL_FIELD, iLabel);
                    if(iLabels.size() > 0){
                        iLabelBean = iLabels.get(0);
                    } else {
                        iLabelBean = new Label(iLabel, uid,isLabeled);

                    }

                    ++iLabelBean.counter;
                    labelDAO.createOrUpdate(iLabelBean);
                    imageLabelDAO.createOrUpdate(new ImageLabel(image, iLabelBean));
                }

                e.onComplete();
            }
        });
    }

    @Override
    public Completable saveLabel(long uid, String imageId, String labels,boolean isLabled) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {

                Image image = null;

                List<Image> images = imageDAO.queryBuilder()
                        .where()
                        .eq(Image.UUID_FIELD, imageId)
                        .and()
                        .eq(Image.UID_FIELD, uid)
                        .and()
                        .eq(Image.ISLABLED_FIELD,isLabled)
                        .query();

                if (images.size() == 0) {
                    e.onError(new Throwable("没有该图片"));
                    return;
                }

                image = images.get(0);

                Label iLabelBean = null;
                List<Label> iLabels = labelDAO.queryBuilder()
                        .where()
                        .eq(Label.LABEL_FIELD,labels)
                        .and()
                        .eq(Label.UID_FIELD,uid)
                        .and()
                        .eq(Label.POST_FIELD,isLabled)
                        .query();

                if(iLabels.size() > 0){
                    iLabelBean = iLabels.get(0);
                } else {

                    iLabelBean = new Label(labels, uid);
                }

                ++iLabelBean.counter;
                labelDAO.createOrUpdate(iLabelBean);
                imageLabelDAO.createOrUpdate(new ImageLabel(image, iLabelBean));

                e.onComplete();
            }


        });
    }


    Dao<Image, String> imageDAO = null;
    Dao<ImageLabel, Long> imageLabelDAO = null;
    Dao<Label, String> labelDAO = null;


    @Override
    public Observable<List<Image>> getAllImages(long uid,boolean isLabeled) {
        return Observable.create(new ObservableOnSubscribe<List<Image>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Image>> e) throws Exception {
               // e.onNext(imageDAO.queryForEq(Image.UID_FIELD, uid));

                List<Image> images = imageDAO.queryBuilder()
                        .where()
                        .eq(Image.UID_FIELD, uid)
                        .and()
                        .eq(Image.ISLABLED_FIELD,isLabeled)
                        .query();
                e.onNext(images);

            }
        });
    }

    @Override
    public Completable saveImage(long uid, Image image,boolean isLabeled) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                image.uid = uid;
                image.isLabeled = isLabeled;
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
    public Observable<List<Image>> getImagesByLabel(long uid, String label,boolean isLabeled) {

        return Observable.create(new ObservableOnSubscribe<List<Image>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Image>> e) throws Exception {

                QueryBuilder labelQuery = labelDAO.queryBuilder();

                List<Label> labelList = labelQuery.where()
                        .eq(Label.UID_FIELD,uid)
                        .and()
                        .eq(Label.LABEL_FIELD,label)
                        .and()
                        .eq(Label.POST_FIELD,isLabeled)
                        .query();

                if(labelList == null || labelList.size() == 0){

                    e.onError(new Throwable("没有找到该标签"));
                    return;
                }

                Label label = labelList.get(0);

                QueryBuilder imageLabelQuery = imageLabelDAO.queryBuilder();
                List<ImageLabel> imageLabelList = imageLabelQuery.where()
                        .eq(ImageLabel.LABEL_FIELD,label)
                        .query();
                List<Image> imageList = new ArrayList<Image>();
                for(int i = 0; i < imageLabelList.size() ; ++i){

                    imageList.add(imageDAO.queryForSameId(imageLabelList.get(i).image));
                }


                e.onNext(imageList);
            }


        });
    }


    //OK
    @Override
    public Observable<List<Label>> getAllLabels(long uid, boolean isLabeled) {

        return Observable.create(new ObservableOnSubscribe<List<Label>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Label>> e) throws Exception {

                QueryBuilder labelQuery = labelDAO.queryBuilder();
                List<Label> list = labelQuery.where()
                        .eq(Label.UID_FIELD,uid)
                        .and()
                        .eq(Label.POST_FIELD,isLabeled)
                        .query();

                e.onNext(list);

            }
        });
    }

    //OK
    @Override
    public Completable deleteLabelByImage(long uid,String uuid, boolean isLabeled) {

        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {

                QueryBuilder labelQuery = labelDAO.queryBuilder();

                List<Image> imageList = imageDAO.queryBuilder()
                        .where()
                        .eq(Image.UID_FIELD,uid)
                        .and()
                        .eq(Image.ISLABLED_FIELD,isLabeled)
                        .and()
                        .eq(Image.UUID_FIELD,uuid)
                        .query();

                List<ImageLabel> list = imageLabelDAO.queryBuilder()
                        .where()
                        .eq(ImageLabel.IMAGE_FIELD,imageList.get(0))
                        .query();

                for(int i = 0 ; i < list.size() ;++i){

                    Label label = labelDAO.queryForSameId(list.get(i).label);

                    --label.counter;
                    if(label.counter <= 0){

                        labelDAO.delete(list.get(i).label);

                    }

                    imageLabelDAO.delete(list.get(i));

                }


                e.onComplete();

            }
        });
    }

    //OK
    @Override
    public Completable deleteAllLabels(long uid, boolean isLabeled) {

        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {


                QueryBuilder labelQuery = labelDAO.queryBuilder();

                List<Label> list = labelQuery.where()
                        .eq(Label.UID_FIELD,uid)
                        .and()
                        .eq(Label.POST_FIELD,isLabeled)
                        .query();

                if(list == null){

                    return;
                }

                DeleteBuilder imageLabelDelete = imageLabelDAO.deleteBuilder();
                for(int i = 0 ; i < list.size() ;++i){

                    labelDAO.delete(list.get(i));
                    imageLabelDelete.where()
                            .eq(ImageLabel.LABEL_FIELD,list.get(i));
                    imageLabelDelete.delete();

                }
                e.onComplete();
            }
        });

    }

    //OK
    @Override
    public Completable changeImageLabeledProperty(long uid, String uuid,boolean oddIsLabeled, boolean isLabeled) {

        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {

                QueryBuilder imageQuery = imageDAO.queryBuilder();

                List<Image> list = imageQuery.where()
                        .eq(Image.UID_FIELD,uid)
                        .and()
                        .eq(Image.UUID_FIELD,uuid)
                        .and()
                        .eq(Image.ISLABLED_FIELD,oddIsLabeled)
                        .query();

                for(int i = 0 ; i < list.size() ; ++i){

                    list.get(i).isLabeled = isLabeled;
                    imageDAO.createOrUpdate(list.get(i));

                    QueryBuilder imageLabelQuery = imageLabelDAO.queryBuilder();

                    List<ImageLabel> imageLabelList = imageLabelQuery.where()
                            .eq(ImageLabel.IMAGE_FIELD,list.get(i))
                            .query();

                    for(int j = 0 ; j < imageLabelList.size() ; ++j){

                        Label label = labelDAO.queryForSameId(imageLabelList.get(i).label);

                        --label.counter;
                        if(label.counter <= 0){

                            labelDAO.delete(imageLabelList.get(i).label);
                        }

                        imageLabelDAO.delete(imageLabelList.get(j));

                    }
                }

                e.onComplete();
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
