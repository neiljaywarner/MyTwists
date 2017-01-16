package com.neiljaywarner.twitteruserstatus.network;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.neiljaywarner.twitteruserstatus.BuildConfig;
import com.neiljaywarner.twitteruserstatus.utils.Prefs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static android.content.ContentValues.TAG;

/**
 * Created by neil on 1/15/17.
 * Utilities for twitter authentication as per https://dev.twitter.com/oauth/application-only
 */
public class TwitterAuthUtils {
    private static final String BEARER_TOKEN_PREFS_KEY = "bearer_prefs_key";


    //TODO: In a production app we'd see about caching the authkey
    /**
     * See: https://dev.twitter.com/oauth/application-only
     * @return Credentials that can be sent to https://api.twitter.com/oauth2/token
     */
    public static String generateEncodedBearerTokenCredentials() {
        String consumerSecret = BuildConfig.TWIST_API_SECRET;
        String consumerKey = BuildConfig.TWIST_API_KEY;
        /*
        try {
            consumerSecret = URLEncoder.encode(consumerSecret, "UTF-8");
            consumerKey = URLEncoder.encode(consumerKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf(TAG, "UTF-8 should definitely be a supported encoding scheme.");
        }
        */

        String combinedCredentials = consumerKey + ":" + consumerSecret;
        Log.e("NJW", "combinedCredentials:" + combinedCredentials);
        // Base64 encode the string; discovered via dzone.com NO_WRAP rather than default.
        String base64Encoded = Base64.encodeToString(combinedCredentials.getBytes(),
                Base64.NO_WRAP);

        return base64Encoded;
    }

    public static String getBearerTokenFromPrefs(Context context) {
        return Prefs.getFromPrefs(context, BEARER_TOKEN_PREFS_KEY, "");
    }

    public static void saveBearerTokenToPrefs(Context context, String value) {
        Prefs.saveToPrefs(context, BEARER_TOKEN_PREFS_KEY, value);
    }
}
