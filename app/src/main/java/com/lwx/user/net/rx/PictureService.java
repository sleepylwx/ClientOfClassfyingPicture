package com.lwx.user.net.rx;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;

/**
 * Created by henry on 17-4-12.
 */

public interface PictureService {

    @POST("/getrandpic.action")
    Call<String> getRandPic();

    @POST("/getpictag.action")
    Call<String> getPickTags(@Field("uuid") String uuid);
}
