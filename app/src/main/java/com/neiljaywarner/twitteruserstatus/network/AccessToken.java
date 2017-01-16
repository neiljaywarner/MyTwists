package com.neiljaywarner.twitteruserstatus.network;

/**
 * Created by neil on 1/15/17.
 * from https://futurestud.io/tutorials/oauth-2-on-android-with-retrofit
 */
public class AccessToken {

    private String access_token;
    private String tokenType;

    public String getAccessToken() {
        return access_token;
    }

    public String getTokenType() {
        // OAuth requires uppercase Authorization HTTP header value for token type
        if ( ! Character.isUpperCase(tokenType.charAt(0))) {
            tokenType =
                    Character
                            .toString(tokenType.charAt(0))
                            .toUpperCase() + tokenType.substring(1);
        }

        return tokenType;
    }
}