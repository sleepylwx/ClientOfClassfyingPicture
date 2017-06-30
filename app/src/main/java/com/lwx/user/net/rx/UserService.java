package com.lwx.user.net.rx;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by henry on 17-4-6.
 */

public interface UserService {

    @POST("/login.action") @FormUrlEncoded
    Call<String> login(@Field("username") String username,   @Field("password") String password);

    @POST("/auth.action") @FormUrlEncoded
    Call<String> auth(@Field("token") String token);

    @POST("/deauth.action") @FormUrlEncoded
    Call<String> logout(@Field("token") String token);

    @POST("/gsnickname.action") @FormUrlEncoded
    Call<String> getNickName(@Field("uid") String uid);

    @POST("/gsnickname.action") @FormUrlEncoded
    Call<String> setNickName( @Field("token") String token, @Field("nick") String nickName);

    @POST("/register.action") @FormUrlEncoded
    Call<String> signUp(@Field("username") String username, @Field("password") String password, @Field("nickname") String nickname);

    @POST("/getusermarkedtag.action") @FormUrlEncoded
    Call<String> getUserMarkedTag(@Field("uid") String uid);

    @POST("/markpictag.action") @FormUrlEncoded
    Call<String> markPicTag(@Field("token") String token, @Field("uuid") String uuid, @Field("tagname") String tagName);

    @Multipart
    @POST("/uploadheadpic.action")
    Call<ResponseBody> uploadHeadPic(@Part("token") RequestBody token,
                                     @Part MultipartBody.Part file);

    @POST("/gsuserdata.action")
    @FormUrlEncoded
    Call<String> getUserInfo(@Field("token") String token,@Field("uid") long uid);

    @POST("/gsuserdata.action")
    @FormUrlEncoded
    Call<String> updateUserInfo(@Field("token") String token,@Field("uid") long uid,@Field("newuserdata") String newData);

    @POST("postfeedback.action")
    @FormUrlEncoded
    Call<String> uploadFeedBack(@Field("token") String token,@Field("content")String content);


    @POST("finishjob.action")
    @FormUrlEncoded
    Call<String> finishTask(@Field("token")String token,@Field("jobid")int jobid,@Field("param") int num);
}
