package com.lwx.user.net;

import android.util.Log;

import com.lwx.user.App;
import com.lwx.user.model.model.User;
import com.lwx.user.net.rx.StringConverterFactory;
import com.lwx.user.net.rx.UserService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by henry on 17-4-6.
 */
public class UserAgentImpl implements UserAgent{

    UserService userService = null;

    private static UserAgentImpl ourInstance = new UserAgentImpl();

    public static UserAgentImpl getInstance() {
        return ourInstance;
    }

    private UserAgentImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(App.BASE_URL)
                .addConverterFactory(StringConverterFactory.create())
                .build();

        userService = retrofit.create(UserService.class);
    }

    public Observable<User> login(String username, String password){
        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<User> e) throws Exception {
                try {
                    Response<String> response = userService.login(username, password).execute();
                    String content = response.body();
                    JSONObject jsonObject = new JSONObject(content);

                    if(jsonObject.getBoolean("err")) {
                        e.onError(new Exception(jsonObject.getString("msg")));
                        return;
                    }

                    e.onNext(new User(jsonObject.getLong("uid"),
                            jsonObject.getString("token"),
                            jsonObject.getString("username"),
                            jsonObject.getString("nickname")));
                } catch (IOException|JSONException ex) {
                    e.onError(ex);
                }
            }
        });

    }

    public Completable auth(String token){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                try {
                    Response<String> response = userService.auth(token).execute();
                    String content = response.body();

                    JSONObject jsonObject = new JSONObject(content);

                    if(jsonObject.getBoolean("err")) {
                        e.onError(new Exception(jsonObject.getString("msg")));
                    }
                    e.onComplete();
                } catch (IOException|JSONException ex) {
                    e.onError(ex);
                }
            }
        });
    }

    public Completable logout(String token){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                try {
                    Response<String> response = userService.logout(token).execute();
                    String content = response.body();
                    JSONObject jsonObject = new JSONObject(content);

                    if(jsonObject.getBoolean("err")) {
                        e.onError(new Exception(jsonObject.getString("msg")));
                        return;
                    }

                    e.onComplete();
                } catch (IOException|JSONException ex) {
                    e.onError(ex);
                }
            }
        });
    }

    @Override
    public Observable<String> getNickName(Long uid){
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                try {
                    Response<String> response = userService.getNickName(uid.toString()).execute();
                    String content = response.body();
                    JSONObject jsonObject = new JSONObject(content);

                    if(jsonObject.getBoolean("err")) {
                        e.onError(new Exception(jsonObject.getString("msg")));
                        return;
                    }

                    e.onNext(jsonObject.getString("nickname"));
                } catch (IOException|JSONException ex) {
                    e.onError(ex);
                }
            }
        });
    }

    public Completable setNickName(String token, String nickName){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                try {
                    Response<String> response = userService.setNickName(token, nickName).execute();
                    String content = response.body();
                    JSONObject jsonObject = new JSONObject(content);

                    if(jsonObject.getBoolean("err")) {
                        e.onError(new Exception(jsonObject.getString("msg")));
                        return;
                    }
                    e.onComplete();
                } catch (IOException|JSONException ex) {
                    e.onError(ex);
                }
            }
        });
    }

    @Override
    public Observable<List<String>> getMarkedTags(Long uid) {
        return Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<String>> e) throws Exception {
                try {
                    Response<String> response = userService.getUserMarkedTag(uid.toString()).execute();
                    String content = response.body();
                    JSONObject jsonObject = new JSONObject(content);

                    if(jsonObject.getBoolean("err")) {
                        e.onError(new Exception(jsonObject.getString("msg")));
                        return;
                    }
                    JSONArray jsonElements = jsonObject.getJSONArray("tags");
                    List<String> tmpList = new ArrayList<String>();
                    for(int i=0;i<jsonElements.length();++i){
                        tmpList.add(jsonElements.getString(i));
                    }
                    e.onNext(tmpList);
                } catch (IOException|JSONException ex) {
                    e.onError(ex);
                }
            }
        });
    }

    private static final String IMG = "multipart/form-data";
    private static final String TXT_PLAIN = "text/plain";

    @Override
    public Completable uploadHeadPic(String token, String absolutePath) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                File file = new File(absolutePath);

                RequestBody requestBody = RequestBody.create(MediaType.parse(IMG),
                        file);

                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("pic", token, requestBody);


                RequestBody description =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), token);


                Response response = userService.uploadHeadPic(description, body).execute();
                e.onComplete();
            }
        });
    }

    @Override
    public Completable signUp(String username, String password) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                try {
                    Response<String> response = userService.signUp(username, password, username).execute();
                    String content = response.body();
                    JSONObject jsonObject = new JSONObject(content);

                    if(jsonObject.getBoolean("err")) {
                        e.onError(new Exception(jsonObject.getString("msg")));
                        return;
                    }
                    e.onComplete();
                } catch (IOException|JSONException ex) {
                    e.onError(ex);
                }
            }
        });
    }

    @Override
    public Completable markPicTag(String token, String uuid, String tagName) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                try {
                    Response<String> response = userService.markPicTag(token, uuid, tagName).execute();
                    String content = response.body();
                    JSONObject jsonObject = new JSONObject(content);

                    if(jsonObject.getBoolean("err")) {
                        e.onError(new Exception(jsonObject.getString("msg")));
                        return;
                    }

                    e.onComplete();
                } catch (IOException|JSONException ex) {
                    e.onError(ex);
                }
            }
        });
    }

    @Override
    public Observable<String> getHeaderPath(long uid) {

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {

                e.onNext(App.BASE_URL + "headpic/" + uid);
            }
        });
    }


    @Override
    public Observable<User> getUserAllMessage(long uid, String token) {

        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<User> e) throws Exception {


                try{

                    Response<String> response = userService.getUserInfo(token,uid).execute();
                    String content = response.body();

                    JSONObject jsonObject = new JSONObject(content);
                    if(jsonObject.getBoolean("err")){

                        e.onError(new Throwable(jsonObject.getString("msg")));
                    }

                    JSONObject message = jsonObject.getJSONObject("userdata");
                    User user = new User();
                    user.uid = jsonObject.getLong("uid");
                    user.token = message.getString("token");
                    user.user = message.getString("username");
                    user.nickName = message.getString("nickname");
                    user.num = message.getInt("score");
                    user.headPath = App.BASE_URL + "headpic/" + user.uid;
                    JSONArray jsonArray = message.getJSONArray("likedtags");


                    if(jsonArray == null || jsonArray.length() == 0){

                        user.favorite1 = "";
                        user.favorite2 = "";
                        user.favorite3 = "";
                    }
                    else if(jsonArray.length() == 1){

                        user.favorite1 = jsonArray.getString(0);
                        user.favorite2 = "";
                        user.favorite3 = "";
                    }
                    else if(jsonArray.length() == 2){

                        user.favorite1 = jsonArray.getString(0);
                        user.favorite2 = jsonArray.getString(1);
                        user.favorite3 = "";
                    }
                    else{

                        user.favorite1 = jsonArray.getString(0);
                        user.favorite2 = jsonArray.getString(1);
                        user.favorite3 = jsonArray.getString(2);
                    }
                    user.extra = message.getString("password");
                    if(user.extra == null){

                        Log.d("mimain","null");
                    }
                    else{

                        Log.d("mimain",(String)user.extra);
                    }
                    e.onNext(user);
                }
                catch (IOException | JSONException ex){

                    e.onError(ex);
                }

            }
        });
    }

    @Override
    public Completable uploadUserMessage(User user) {


        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {

                if(user.extra == null){

                    Log.d("mimaout","null");
                }
                else{

                    Log.d("mimaout",(String)user.extra);

                }
                try{

                    String token = user.token;
                    long uid = user.uid;
                    String newValue = toJson(user);

                    Response<String> response = userService.updateUserInfo(token,uid,newValue).execute();
                    String content = response.body();

                    JSONObject jsonObject = new JSONObject(content);

                    if(jsonObject.getBoolean("err")){

                        e.onError(new Exception(jsonObject.getString("msg")));
                        return;
                    }

                    e.onComplete();

                }
                catch (IOException | JSONException ex){

                    e.onError(ex);
                }

            }
        });
    }

    private String toJson(User user){

        StringBuffer stringBuffer = new StringBuffer();
//        if(!((user.favorite1 == null || user.favorite1.equals(""))
//                && (user.favorite2 == null || user.favorite2.equals(""))
//                && (user.favorite3 == null || user.favorite3.equals("")))){
//
//
//        }

        stringBuffer.append("{\"likedtags\": [\"");
        stringBuffer.append(user.favorite1);
        stringBuffer.append("\",\"");
        stringBuffer.append(user.favorite2);
        stringBuffer.append("\",\"");
        stringBuffer.append(user.favorite3);
        stringBuffer.append("\"],\"nickname\": \"");
        stringBuffer.append(user.nickName);
        stringBuffer.append("\",\"password\":\"");
        stringBuffer.append((String)user.extra);
        stringBuffer.append("\"}");

        String temp = stringBuffer.toString();
        Log.d("UserAgentImpl",temp);
        return temp;
    }

    @Override
    public Completable postFeedBack(String token, String content) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {

                try{

                    Response<String> response = userService.uploadFeedBack(token,content).execute();
                    String content = response.body();
                    JSONObject jsonObject = new JSONObject(content);
                    if(jsonObject.getBoolean("err")){

                        e.onError(new Throwable(jsonObject.getString("mes")));
                        return;
                    }

                    e.onComplete();
                }
                catch (IOException | JSONException ex){

                    e.onError(ex);
                }
            }
        });
    }

    @Override
    public Observable<Integer> finishTask(String token, int num) {


        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {

                try{

                    Response<String> response = userService.finishTask(token,1,num).execute();

                    String content = response.body();
                    JSONObject jsonObject = new JSONObject(content);
                    if(jsonObject.getBoolean("err")){

                        e.onError(new Throwable(jsonObject.getString("msg")));
                        return;
                    }

                    e.onNext(jsonObject.getInt("score"));
                }

                catch (IOException | JSONException ex){

                    e.onError(ex);
                }

            }
        });
    }
}
