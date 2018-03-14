package com.neiljaywarner.twitteruserstatus.network;

import com.neiljaywarner.twitteruserstatus.model.MemverseResponse;
import com.neiljaywarner.twitteruserstatus.model.Response;
import com.neiljaywarner.twitteruserstatus.model.Tweet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by neil on 1/15/17.
 */

public interface TwitterApi {


    String GRANT_TYPE_CLIENT = "client_credentials";

    String GRANT_TYPE_PASSWORD = "password";
    // TODO: See if this is advised against, see what they do in iOS..

    // also see
    //https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/intro_understanding_username_password_oauth_flow.htm

    //https://api.twitter.com/oauth2/token
    /**
     * The only grantType is "client_credentials"
     */
    @FormUrlEncoded
    @POST("/oauth/token")
    Call<BearerTokenResponse> getBearerToken(@Field("grant_type") String grantType);

    @POST("oauth/token")
    Call<BearerTokenResponse> getBearerToken(@Body PasswordTokenRequest passwordTokenRequest);


    // https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=twitterapi&count=2
    @GET("1.1/statuses/user_timeline.json?screen_name=icochotnews&count=30")
    Call<List<Tweet>> getTweets();

    // https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=twitterapi&count=2
    @GET("1/verses/search")
    Call<Response> getTweets(@Query("searchParams") String screenName);


    // https://www.memverse.com/api/index.html#!/memverse/showMemverses
    // e.g. https://www.memverse.com/1/memverses?page=1
    @GET("1/memverses?page=1&sort=id")
    Call<MemverseResponse> getMemverses();

    // was gen 1:1 without the sort


    // oauth only one iwth no 1/ in it.
}
