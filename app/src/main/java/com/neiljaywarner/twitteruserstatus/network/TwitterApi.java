package com.neiljaywarner.twitteruserstatus.network;

import com.neiljaywarner.twitteruserstatus.model.Tweet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by neil on 1/15/17.
 */

public interface TwitterApi {


    String GRANT_TYPE_CLIENT = "client_credentials";

    //https://api.twitter.com/oauth2/token
    @POST("/oauth2/token")
    /**
     * The only grantType is "client_credentials"
     */
    Call<BearerTokenResponse> getBearerToken(@Body BearerTokenRequest bearerTokenRequest);

    @FormUrlEncoded
    @POST("/oauth2/token")
    Call<BearerTokenResponse> getBearerToken(@Field("grant_type") String grantType);


    // https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=twitterapi&count=2
    @GET("1.1/statuses/user_timeline.json?screen_name=twitterapi&count=2")
    Call<List<Tweet>> getTweets();

}
