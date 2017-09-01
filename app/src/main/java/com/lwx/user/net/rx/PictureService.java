package com.lwx.user.net.rx;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by henry on 17-4-12.
 */

public interface PictureService {

    @POST("/getrandpic.action")
    @FormUrlEncoded
    Call<String> getRandPic();

    @POST("/getpictag.action")
    @FormUrlEncoded
    Call<String> getPickTags(@Field("uuid") String uuid);

    @POST("/getrandpic.action")
    @FormUrlEncoded
    Call<String> getRandPic(@Field("num") Integer num);

    @POST("/getuserlikedpic.action")
    @FormUrlEncoded
    Call<String> getUserLikePics(@Field("token") String token, @Field("num") Integer num);

    @POST("/markpicmutitag.action")
    @FormUrlEncoded
    Call<String> markMutiTags(@Field("token") String token,@Field("tagnames") String tagNames, @Field("uuid") String uuid);

    @POST("/getusertagcntall.action")
    @FormUrlEncoded
    Call<String> getUserTotalLabelsNum(@Field("uid") long uid);

    @POST("/getusertagcnt.action")
    @FormUrlEncoded
    Call<String> getLabelsNum(@Field("uid")long uid);


    @POST("/getpicbytagname.action")
    @FormUrlEncoded
    Call<String> getPicsByTag(@Field("tagname") String tag);

    @POST("/getpictag.action")
    @FormUrlEncoded
    Call<String> getFirstLabel(@Field("uuid") String uuid,@Field("inittag") int initTag);


    @POST("/getusermarkedpic.action")
    @FormUrlEncoded
    Call<String> getUserMarkedPics(@Field("uid") long uid);

    @POST("/getusermarkedtag.action")
    @FormUrlEncoded
    Call<String> getUserMarkedTag(@Field("uid")long uid);

    @POST("/getusermarkedpicwithtag.action")
    @FormUrlEncoded
    Call<String> getCertainLabelMarkedPics(@Field("uid")long uid,@Field("tagname")String tag);


    @POST("/getusermarkedtagonpic.action")
    @FormUrlEncoded
    Call<String> getPicMarkedLabels(@Field("uid") long uid,@Field("uuid") String uuid);


    @POST("/unmarkpictag.action")
    @FormUrlEncoded
    Call<String> removePicTags(@Field("token")String token,@Field("uuid")String uuid,@Field("tagname")String tagname);
}
