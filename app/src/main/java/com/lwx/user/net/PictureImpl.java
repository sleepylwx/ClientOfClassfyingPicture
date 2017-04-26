package com.lwx.user.net;

import com.lwx.user.App;
import com.lwx.user.db.model.Image;
import com.lwx.user.net.rx.PictureService;
import com.lwx.user.net.rx.StringConverterFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
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
    private PictureService picService;

    private PictureImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(App.BASE_URL)
                .addConverterFactory(StringConverterFactory.create())
                .build();
        picService = retrofit.create(PictureService.class);
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

                    e.onNext(new Image(jsonObject.getString("uuid"),
                            App.BASE_URL + "/getpic.action?uuid=" + jsonObject.getString("uuid")));

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

                    //e.onNext();

                } catch (IOException |JSONException ex) {
                    e.onError(ex);
                }
            }
        });
    }
}