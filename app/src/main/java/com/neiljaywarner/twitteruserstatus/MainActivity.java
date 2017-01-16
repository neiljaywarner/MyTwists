package com.neiljaywarner.twitteruserstatus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.neiljaywarner.twitteruserstatus.network.BearerTokenRequest;
import com.neiljaywarner.twitteruserstatus.network.BearerTokenResponse;
import com.neiljaywarner.twitteruserstatus.network.ServiceGenerator;
import com.neiljaywarner.twitteruserstatus.network.TwitterApi;
import com.neiljaywarner.twitteruserstatus.network.TwitterAuthUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HTTP;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "NJW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: Validate with a little OO of some sort, and/or in view
        // done button...
        // < 20 characters, alphanumberic + _ only...
        // IME Action
        // https://support.twitter.com/articles/1012

        if (TextUtils.isEmpty(TwitterAuthUtils.getBearerTokenFromPrefs(this))) {
            retrieveBearerToken();
        } else {
            makeGetTweetsNetworkCall();
        }

    }

    private void makeGetTweetsNetworkCall() {

    }

    private void retrieveBearerToken() {
        Log.d(TAG, "*** Retrieving bearer token.");
        TwitterApi twitterApi = ServiceGenerator.createBearerKeyService(
                TwitterAuthUtils.generateEncodedBearerTokenCredentials());

        Call<BearerTokenResponse> bearerTokenCall =
                twitterApi.getBearerToken(new BearerTokenRequest());

        bearerTokenCall.enqueue(new Callback<BearerTokenResponse>() {
            @Override
            public void onResponse(Call<BearerTokenResponse> call, Response<BearerTokenResponse> response) {
                Log.d(TAG, "bearerTokenCall:Response code: " + response.code());
                if (response.code() == 200) {
                    BearerTokenResponse bearerTokenResponse = response.body();

                    //Validate token_type per twitter docs
                    Log.d(TAG, "bearerTokenCall:token_type=" + bearerTokenResponse.token_type);
                    if (bearerTokenResponse.token_type.equalsIgnoreCase("bearer")) {
                        TwitterAuthUtils.saveBearerTokenToPrefs(MainActivity.this,
                                bearerTokenResponse.access_token);
                        makeGetTweetsNetworkCall();
                    } else {
                        Log.wtf(TAG, "Error: Bearer token result from twitter should be token type 'bearer'");
                    }
                } else {
                    Log.e(TAG, "Response invalid, check consumer key/secret combination if 403");
                }

            }

            @Override
            public void onFailure(Call<BearerTokenResponse> call, Throwable t) {
                Log.e(TAG, "bearerTokenCall Failure:" + call.request().toString()
                        + t.getMessage());
            }
        });
    }




    /*
    statuses/user_timeline
    see: https://dev.twitter.com/rest/reference/get/statuses/user_timeline
    e.g. https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=twitterapi&count=200


    https://dev.twitter.com/oauth/application-only

    - Get registered as developer, get consumer key and secret key, etc.

    TODO: Do mockwebserver and testing with sample response... to show progress and do
    fully tested app... show my skills...

     */
}
