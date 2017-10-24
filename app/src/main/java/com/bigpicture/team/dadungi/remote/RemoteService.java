package com.bigpicture.team.dadungi.remote;

import com.bigpicture.team.dadungi.item.EnterpriseInfoItem;
import com.bigpicture.team.dadungi.item.KeepItem;
import com.bigpicture.team.dadungi.item.MemberInfoItem;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 서버에 호출할 메소드를 선언하는 인터페이스
 */
public interface RemoteService {
    String BASE_URL = "http://52.79.45.23:3000/";
    String MEMBER_ICON_URL = BASE_URL + "/member/";
    String IMAGE_URL = BASE_URL + "/img/";

    //사용자 정보
    @GET("/member/{member_id}")
    Call<MemberInfoItem> selectMemberInfo(@Path("member_id") String phone_num);

    @POST("/member/info")
    Call<String> insertMemberInfo(@Body MemberInfoItem memberInfoItem);

    @FormUrlEncoded
    @POST("/member/member_id")
    Call<String> insertMemberPhone(@Field("member_id") String phone_num);

    @Multipart
    @POST("/member/icon_upload")
    Call<ResponseBody> uploadMemberIcon(@Part("member_seq") RequestBody memberSeq,
                                        @Part MultipartBody.Part file);

    //업체 정보
    @GET("/enterprise/info")
    Call<EnterpriseInfoItem> selectEnterpriseInfo(@Query("seq") int seq);

    @Multipart
    @POST("/esc/info/image")
    Call<ResponseBody> uploadESCImage(@Part("cpId") RequestBody infoSeq,
                                       @Part("image_memo") RequestBody imageMemo,
                                       @Part MultipartBody.Part file);


    @GET("/enterprise/list2")
    Call<ArrayList<EnterpriseInfoItem>> listEnterpriseInfo(@Query("type") String type,
                                                           @Query("district") String district,
                                                           @Query("name") String name);


    //지도
    @GET("/enterprise/map/list")
    Call<ArrayList<EnterpriseInfoItem>> listMap(@Query("latitude") double latitude,
                                                @Query("longitude") double longitude,
                                                @Query("distance") int distance,
                                                @Query("user_latitude") double userLatitude,
                                                @Query("user_longitude") double userLongitude,
                                                @Query("type") String type);


    //즐겨찾기
    @POST("/keep/{member_seq}/{info_seq}")
    Call<String> insertKeep(@Path("member_seq") int memberSeq, @Path("info_seq") int infoSeq);

    @DELETE("/keep/{member_seq}/{info_seq}")
    Call<String> deleteKeep(@Path("member_seq") int memberSeq, @Path("info_seq") int infoSeq);

    @GET("/keep/list")
    Call<ArrayList<KeepItem>> listKeep(@Query("member_seq") int memberSeq,
                                       @Query("user_latitude") double userLatitude,
                                       @Query("user_longitude") double userLongitude);
}