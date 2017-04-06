package com.lwx.user.net.rx;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
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
    Call<String> getNickName(@Field("uid") long uid);

    @POST("/gsnickname.action") @FormUrlEncoded
    Call<String> setNickName( @Field("token") String token, @Field("nick") String nickName);

}
