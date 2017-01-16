package com.neiljaywarner.twitteruserstatus.network;

/**
 * Created by neil on 1/15/17.
 * Response from https://api.twitter.com/oauth2/token
 * e.g. {"token_type":"bearer","access_token":"AAAA%2FAAA%3DAAAAAAAA"}

 */

public class BearerTokenResponse {
    public String token_type;
    public String access_token;
}
