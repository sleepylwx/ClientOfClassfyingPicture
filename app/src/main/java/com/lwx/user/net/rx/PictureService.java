package com.lwx.user.net.rx;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by henry on 17-4-12.
 */

public interface PictureService {

    @POST("/getrandpic.action")
    Call<String> getRandPic();

    @POST("/getpictag.action")
    Call<String> getPickTags(@Field("uuid") String uuid);

    @POST("/getrandpic.action")
    Call<String> getRandPic(@Field("num") Integer num);

    @POST("/getuserlikedpic.action")
    Call<String> getUserLikePics(@Field("token") String token, @Field("num") Integer num);

    @POST("")
    @FormUrlEncoded
    Call<String> markMutiTags(@Field("token") String token,@Field("tagnames") String tagNames, @Field("uuid") String uuid);
}
