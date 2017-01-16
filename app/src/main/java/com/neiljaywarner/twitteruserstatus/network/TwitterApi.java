package com.neiljaywarner.twitteruserstatus.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

/**
 * Created by neil on 1/15/17.
 */

public interface TwitterApi {


    String GRANT_TYPE_CLIENT = "client_credential";

    //https://api.twitter.com/oauth2/token
    @POST("/oauth2/token")
    /**
     * The only grantType is "client_credentials"
     */
    Call<BearerTokenResponse> getBearerToken(@Body BearerTokenRequest bearerTokenRequest);
}
