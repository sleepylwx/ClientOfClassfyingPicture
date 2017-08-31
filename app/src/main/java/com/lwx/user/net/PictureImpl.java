package com.lwx.user.net;

import android.util.Log;

import com.lwx.user.App;
import com.lwx.user.model.model.Image;
import com.lwx.user.model.model.Pair;
import com.lwx.user.net.rx.PictureService;
import com.lwx.user.net.rx.StringConverterFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by henry on 17-4-12.
 */
public class PictureImpl implements PictureAgent {


    private static PictureImpl ourInstance = new PictureImpl();
    public static PictureImpl getInstance() {
        return ourInstance;
    }

    public static final String TAG = "PictureImpl";
    private PictureService picService;

    private PictureImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(App.BASE_URL)
                .addConverterFactory(StringConverterFactory.create())
                .build();
        picService = retrofit.create(PictureService.class);
    }

    @Override
    public Completable markMutiTags(String token, String uuid, List<String> tags) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                JSONArray jsonArray = new JSONArray(tags);
                try {
                    Response<String> response = picService.markMutiTags(token,jsonArray.toString(), uuid).execute();
                    String content = response.body();
                   // XLog.d(response.errorBody().string());
                    JSONObject jsonObject = new JSONObject(content);


                    if(jsonObject.getBoolean("err")) {
                        e.onError(new Exception(jsonObject.getString("msg")));
                    }
                    e.onComplete();
                } catch (IOException |JSONException ex) {
                    e.onError(ex);
                }
            }
        });
    }

    @Override
    public Observable<Image> getRandPic() {
        return Observable.create(new ObservableOnSubscribe<Image>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Image> e) throws Exception {
                try {
                    Response<String> response = picService.getRandPic().execute();
                    String content = response.body();
                    JSONObject jsonObject = new JSONObject(content);

                    if(jsonObject.getBoolean("err")) {
                        e.onError(new Exception(jsonObject.getString("msg")));
                        return;
                    }

                    e.onNext(new Image(-1L,jsonObject.getString("uuid"),
                            App.BASE_URL + "getpic.action?uuid=" + jsonObject.getString("uuid")));

                } catch (IOException |JSONException ex) {
                    e.onError(ex);
                }
            }
        });
    }

    @Override
    public Observable<List<String>> getPicTags(String uuid) {
        return Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<String>> e) throws Exception {
                try {
                    Response<String> response = picService.getPickTags(uuid).execute();
                    String content = response.body();
                    JSONObject jsonObject = new JSONObject(content);

                    if(jsonObject.getBoolean("err")) {
                        e.onError(new Exception(jsonObject.getString("msg")));
                        return;
                    }

                    JSONArray jsonArray = jsonObject.getJSONArray("tags");
                    List<String> ansList = new ArrayList<String>();
                    int len = jsonArray.length();
                    for(int i = 0 ; i < len ;i++){
                        ansList.add(jsonArray.getString(i));
                    }

                    e.onNext(ansList);

                } catch (IOException |JSONException ex) {
                    e.onError(ex);
                }
            }
        });
    }

    @Override
    public Observable<List<String>> getRandPicAddr(Integer num) {
        return Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<String>> e) throws Exception {
                try {
                    Response<String> response = picService.getRandPic(num).execute();
                    String content = response.body();
                    JSONObject jsonObject = new JSONObject(content);

                    if(jsonObject.getBoolean("err")) {
                        e.onError(new Exception(jsonObject.getString("msg")));
                        return;
                    }

                    JSONArray jsonArray = jsonObject.getJSONArray("uuids");
                    List<String> ansList = new ArrayList<String>();
                    int len = jsonArray.length();
                    for(int i = 0 ; i < len ;i++){
                        ansList.add(jsonArray.getString(i));
                    }
                    e.onNext(ansList);

                } catch (IOException |JSONException ex) {
                    e.onError(ex);
                }
            }
        });
    }

    @Override
    public Observable<List<String>> getUserLikePics(String token, Integer num) {
        return Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<String>> e) throws Exception {
                try {
                    Response<String> response = picService.getUserLikePics(token,num).execute();
                    String content = response.body();
                    JSONObject jsonObject = new JSONObject(content);

                    if(jsonObject.getBoolean("err")) {
                        e.onError(new Exception(jsonObject.getString("msg")));
                        return;
                    }

                    JSONArray jsonArray = jsonObject.getJSONArray("uuids");
                    List<String> ansList = new ArrayList<String>();
                    int len = jsonArray.length();
                    for(int i = 0 ; i < len ;i++){
                        ansList.add(jsonArray.getString(i));
                    }
                    e.onNext(ansList);

                } catch (IOException |JSONException ex) {
                    e.onError(ex);
                }
            }
        });
    }

    @Override
    public Observable<List<Image>> getPicByToken(String token, Integer num) {
        return getUserLikePics(token, num)
                .flatMap(new Function<List<String>, Observable<List<Image>>>() {
                    @Override
                    public Observable<List<Image>> apply(@NonNull List<String> strings) throws Exception {
                        List<Image> images = new ArrayList<Image>();
                        for(int i=0;i<strings.size();i++){
                            String t = strings.get(i);
                            images.add(new Image(-1L, t,
                                    App.BASE_URL + "getpic.action?uuid=" + t ));
                        }
                        return Observable.just(images);
                    }
                });
    }

    @Override
    public Observable<List<Image>> getRandPic(Integer num) {
        return getRandPicAddr(num)
                .flatMap(new Function<List<String>, Observable<List<Image>>>() {
                    @Override
                    public Observable<List<Image>> apply(@NonNull List<String> strings) throws Exception {
                        List<Image> images = new ArrayList<Image>();
                        for(int i=0;i<strings.size();i++){
                            String t = strings.get(i);
                            images.add(new Image(-1L,t,
                                    App.BASE_URL + "getpic.action?uuid=" + t ));
                        }
                        return Observable.just(images);
                    }
                });
    }

    @Override
    public Observable<Image> getSpecificPic(String uuid) {


        return new Observable<Image>() {
            @Override
            protected void subscribeActual(Observer<? super Image> observer) {

                observer.onNext(new Image(-1L,uuid,App.BASE_URL + "getpic.action?uuid=" + uuid));
            }
        };
    }

    @Override
    public Observable<Integer> getTotalLabelsNum(long uid) {

        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {



                try{

                    Response<String> response = picService.getUserTotalLabelsNum(uid).execute();
                    String content = response.body();
                    JSONObject jsonObject = new JSONObject(content);
                    e.onNext(jsonObject.getInt("tagcnt"));

                }
                catch (IOException |JSONException ex){

                    e.onError(ex);
                }
            }
        });
    }


    @Override
    public Observable<List<Pair>> getLabelsNum(long uid) {

        return Observable.create(new ObservableOnSubscribe<List<Pair>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Pair>> e) throws Exception {

                try{

                    Response<String> response = picService.getLabelsNum(uid).execute();
                    String content = response.body();
                    JSONObject jsonObject = new JSONObject(content);

                    JSONArray array = jsonObject.getJSONArray("tags");

                    List<Pair> list = new ArrayList<Pair>();
                    Log.d(TAG,"jsonarray size : " + list.size());
                    for(int i = 0; i < array.length(); ++i){

                        JSONObject map = array.getJSONObject(i);
                        list.add(new Pair(map.getString("tagname"),
                                map.getInt("cnt")));
                    }

                    Collections.sort(list);
                    e.onNext(list);
                }

                catch (IOException | JSONException ex){

                    e.onError(ex);
                }
            }
        });
    }

    @Override
    public Observable<List<Image>> getPicByLabel(String label) {


        return Observable.create(new ObservableOnSubscribe<List<Image>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Image>> e) throws Exception {


                Response<String> response = picService.getPicsByTag(label).execute();
                String content = response.body();
                JSONObject jsonObject = new JSONObject(content);

                JSONArray array = jsonObject.getJSONArray("uuids");

                List<Image> images = new ArrayList<Image>();

                for(int i = 0; i < array.length(); ++i){

                    String uuid = array.getString(i);
                    images.add(new Image(App.getInstance().getUid(),
                            uuid,App.BASE_URL + "getpic.action?uuid=" + uuid));
                }

                e.onNext(images);
            }
        });
    }


    @Override
    public Observable<List<String>> getFirstLabel(String uuid) {

        return Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<String>> e) throws Exception {

                Response<String> response =picService.getFirstLabel(uuid,1).execute();
                String content = response.body();
                JSONObject jsonObject = new JSONObject(content);
                JSONArray array = jsonObject.getJSONArray("tags");

                List<String> list = new ArrayList<String>();

                if(array.length() == 0){

                    e.onNext(list);

                }
                else if(array.length() == 1){

                    list.add(array.getJSONObject(0).getString("tagname"));
                    e.onNext(list);

                }
                else{

                    int firstScore = array.getJSONObject(0).getInt("score");
                    int secondScore = array.getJSONObject(1).getInt("score");
                    String firstStr;
                    String secondStr;

                    if(firstScore < secondScore){

                        firstStr = array.getJSONObject(1).getString("tagname");
                        secondStr = array.getJSONObject(0).getString("tagname");

                        secondScore = firstScore^secondScore;
                        firstScore = firstScore^secondScore;
                        secondScore = firstScore^secondScore;

                    }
                    else{

                        firstStr = array.getJSONObject(0).getString("tagname");
                        secondStr = array.getJSONObject(1).getString("tagname");
                    }



                    for(int i = 2; i < array.length(); ++i){

                        int temp = array.getJSONObject(i).getInt("score");
                        if(temp > firstScore){

                            secondScore =firstScore;
                            secondStr = firstStr;

                            firstScore = temp;
                            firstStr = array.getJSONObject(i).getString("tagname");

                        }
                        else if(temp > secondScore){

                            secondScore = temp;
                            secondStr = array.getJSONObject(i).getString("tagname");
                        }

                    }




                    list.add(doStr(firstStr.split(";")[0]));

                    list.add(doStr(secondStr.split(";")[0]));

                    e.onNext(list);
                }




            }
        });

    }


    private String doStr(String str){


        StringBuffer sb = new StringBuffer();

        for(int i = 0; i <str.length(); ++i){

            if(str.charAt(i) == ',' || str.charAt(i) == '，'){

                continue;
            }

            sb.append(str.charAt(i));
        }


        return sb.toString();

    }

    @Override
    public Observable<List<Image>> getFinishedPics(long uid) {

        return Observable.create(new ObservableOnSubscribe<List<Image>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Image>> e) throws Exception {


                Response<String> response = picService.getUserMarkedPics(uid).execute();
                String content = response.body();

                JSONObject jsonObject = new JSONObject(content);

                JSONArray array = jsonObject.getJSONArray("pics");

                List<Image> list = new ArrayList<Image>();
                for(int i = 0; i < array.length();++i){


                    if(array.getJSONObject(i).getBoolean("taged")){

                        String uuid = array.getJSONObject(i).getString("uuid");
                        list.add(new Image(App.getInstance().getUid(),uuid,App.BASE_URL + "getpic.action?uuid="+uuid));
                    }
                }

                e.onNext(list);

            }
        });
    }
}
