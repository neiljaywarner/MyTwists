package com.neiljaywarner.twitteruserstatus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.neiljaywarner.twitteruserstatus.model.Tweet;
import com.neiljaywarner.twitteruserstatus.network.BearerTokenResponse;
import com.neiljaywarner.twitteruserstatus.network.ServiceGenerator;
import com.neiljaywarner.twitteruserstatus.network.TwitterApi;
import com.neiljaywarner.twitteruserstatus.network.TwitterAuthUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HTTP;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "NJW";
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewTweets);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //TODO: Butterknife

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

    private  void updateList(List<Tweet> tweets) {
        ItemArrayAdapter itemArrayAdapter = new ItemArrayAdapter(R.layout.list_item, tweets);

        recyclerView.setAdapter(itemArrayAdapter);
    }

    private void makeGetTweetsNetworkCall() {
        Log.d("NJW", "***** GET TWEETS NETWORK CALL");

        String authToken = TwitterAuthUtils.getBearerTokenFromPrefs(this);
        TwitterApi twitterApi = ServiceGenerator.createService(TwitterApi.class, authToken);

        Call<List<Tweet>> tweetsCall =
                twitterApi.getTweets();

        tweetsCall.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                Log.d(TAG, "tweetsCall:Response code: " + response.code());
                if (response.code() == 200) {
                    List<Tweet> tweets = response.body();
                    updateList(tweets);
                    Log.d("NJW", "numTWeets:" + tweets.size());
                    Log.d("NJW", "FirstTweetText:"+ tweets.get(0).text);
                    //TODO: Handle if mistype screen name; if invalid characters
                    // tell them on client
                    //if just not there, let them retry after network request.
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Log.e(TAG, "tweetsCall Failure:" + call.request().toString()
                        + t.getMessage());
            }
        });
    }

    private void retrieveBearerToken() {
        Log.d(TAG, "*** Retrieving bearer token.");
        TwitterApi twitterApi = ServiceGenerator.createBearerKeyService(
                TwitterAuthUtils.generateEncodedBearerTokenCredentials());

        Call<BearerTokenResponse> bearerTokenCall =
                twitterApi.getBearerToken(TwitterApi.GRANT_TYPE_CLIENT);

        bearerTokenCall.enqueue(new Callback<BearerTokenResponse>() {
            @Override
            public void onResponse(Call<BearerTokenResponse> call, Response<BearerTokenResponse> response) {
                Log.d(TAG, "bearerTokenCall:Response code: " + response.code());
                if (response.code() == 200) {
                    BearerTokenResponse bearerTokenResponse = response.body();

                    //Validate token_type per twitter docs
                    Log.d(TAG, "bearerTokenCall:token_type=" + bearerTokenResponse.getTokenType());

                        TwitterAuthUtils.saveBearerTokenToPrefs(MainActivity.this,
                                bearerTokenResponse.getAccessToken());
                        makeGetTweetsNetworkCall();

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
