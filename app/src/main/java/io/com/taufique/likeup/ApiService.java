package io.com.taufique.likeup;


import java.util.List;

import io.com.taufique.likeup.JsonModelObjects.ChecksumPaytm;
import io.com.taufique.likeup.JsonModelObjects.FeedItem;
import io.com.taufique.likeup.JsonModelObjects.ImageUploadResponse;
import io.com.taufique.likeup.JsonModelObjects.LikedResponse;
import io.com.taufique.likeup.JsonModelObjects.MyContest;
import io.com.taufique.likeup.JsonModelObjects.ResponseBodyLogin;
import io.com.taufique.likeup.JsonModelObjects.TransactionStatus;
import io.com.taufique.likeup.Models.Contest;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {


    @POST("/auth/google/revoke-token/")
    Call<Void> getToken(@Field("token") String token);
                       // @Field("client_id") String clientid,
                        //@Field("client_secret") String clientsecret);

   @FormUrlEncoded
                     @POST("/photoapp/join/contest/")
    Call<ChecksumPaytm> getCustomerOrderAddPayment(  //StatusSuccessOrError
                                                     @Field("token") String accessToken,
                                                     @Field("id") String contest_id
                                                    );



    @FormUrlEncoded
    @POST("/photoapp/pay/verify/")
    Call<TransactionStatus> checkTransactionStatus(
            @Field("ORDER_ID") String orderId,
            @Field("token") String accessToken,
            @Field("id") String contest_id

    );

    @POST("/auth/google/get-token/")
    Call<ResponseBodyLogin> facebookLogin(@Body RequestBody request);


    @GET("/photoapp/contests")
    Call<List<Contest>> getContests();
    @FormUrlEncoded
    @POST("/photoapp/mycontests/")
    Call<List<MyContest>> getMyContests(@Field("token") String accessToken);

    @FormUrlEncoded
    @POST("/photoapp/feed/items/")
    Call<List<FeedItem>>getFeedItems(@Field("token") String accessToken);

   @FormUrlEncoded
    @POST("/photoapp/photo/liked/")
    Call<LikedResponse>getFeedItems(@Field("token") String accessToken,
                                    @Field("id") String id,
                                    @Field("isLiked") boolean isLiked
    );



    ///////////image upload
    @Multipart
    @POST("/photoapp/image/upload/")
    Call<ImageUploadResponse> uploadImage(@Part MultipartBody.Part file, @Part("token") RequestBody token,@Part("contest_id") RequestBody contest_id);




}