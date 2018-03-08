package com.neiljaywarner.twitteruserstatus;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.neiljaywarner.twitteruserstatus.model.Tweet;
import com.neiljaywarner.twitteruserstatus.network.BearerTokenResponse;
import com.neiljaywarner.twitteruserstatus.network.ServiceGenerator;
import com.neiljaywarner.twitteruserstatus.network.TwitterApi;
import com.neiljaywarner.twitteruserstatus.network.TwitterAuthUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private EditText mEditTextScreenName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewTweets);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mEditTextScreenName = (EditText) findViewById(R.id.editTextTwitterUserName);
        mEditTextScreenName.setImeActionLabel(getString(R.string.fetch), EditorInfo.IME_ACTION_NEXT);
        mEditTextScreenName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int editorActionId, KeyEvent keyEvent) {

                if (editorActionId == EditorInfo.IME_ACTION_NEXT) {
                    makeGetTweetsNetworkCall(mEditTextScreenName.getText().toString());
                }
                return true;
            }
        });


        if (TextUtils.isEmpty(TwitterAuthUtils.getBearerTokenFromPrefs(this))) {
            retrieveBearerToken();
        }
        //TOOD: Spinner before even allow typing or screen shown if no bearerToken

        findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeGetTweetsNetworkCall(mEditTextScreenName.getText().toString());
            }
        });
        //todo: cardviews and clickable links.
    }

    private  void updateList(List<Tweet> tweets) {
        ItemArrayAdapter itemArrayAdapter = new ItemArrayAdapter(R.layout.list_item, tweets);

        recyclerView.setAdapter(itemArrayAdapter);
    }

    /**
     * Stub function
     * @param screenName - twitter screen name
     * @return true/false is valid screen name
     */
    private boolean isScreenNameValid(String screenName) {
        //TODO: fully validate with regex the other parts including alpha, underscore, etc.
        boolean isValid = true;
        if (screenName.length() > 20) {
            isValid = false;
        }

        return isValid;
    }
    private void makeGetTweetsNetworkCall(final String screenName) {
        Log.d(TAG, "***** GET TWEETS NETWORK CALL");

        if (!isScreenNameValid(screenName)) {
            mEditTextScreenName.setError(getString(R.string.invalid_screen_name));
            return;
        }
        String authToken = TwitterAuthUtils.getBearerTokenFromPrefs(this);
        TwitterApi twitterApi = ServiceGenerator.createService(TwitterApi.class, authToken);

        Call<com.neiljaywarner.twitteruserstatus.model.Response> tweetsCall =
                twitterApi.getTweets(screenName);

        final Context context = this;
        tweetsCall.enqueue(new Callback<com.neiljaywarner.twitteruserstatus.model.Response>() {
            @Override
            public void onResponse(Call<com.neiljaywarner.twitteruserstatus.model.Response> call, Response<com.neiljaywarner.twitteruserstatus.model.Response> response) {
                Log.d(TAG, "tweetsCall:Response code: " + response.code());
                if (response.code() == 200) {
                    MainActivity.this.setTitle(screenName);
                    com.neiljaywarner.twitteruserstatus.model.Response tweets = response.body();

                    updateList(tweets.getTweetList());
                } else {
                    //TODO: Could check other response codes or if have network connection
                    showScreenNameErrorDialog(context, R.string.invalid_screen_name);
                }
            }

            @Override
            public void onFailure(Call<com.neiljaywarner.twitteruserstatus.model.Response> call, Throwable t) {
                Log.e(TAG, "tweetsCall Failure:" + call.request().toString()
                        + t.getMessage());
                showScreenNameErrorDialog(context, R.string.invalid_screen_name_server_error);
            }
        });

    }

    /**
     * Show Error Dialog if there's an issue with the screen name or network.
     * @param context
     * @param messageId - String resource Id for description of error
     */
    private void showScreenNameErrorDialog(Context context, @StringRes int messageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(android.R.string.dialog_alert_title);
        builder.setMessage(messageId);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                mEditTextScreenName.requestFocus();
            }
        });
        builder.show();
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

                    //TODO: Validate token_type per twitter docs with error messaging from business/product.
                    Log.d(TAG, "bearerTokenCall:token_type=" + bearerTokenResponse.getTokenType());

                    TwitterAuthUtils.saveBearerTokenToPrefs(MainActivity.this,
                                bearerTokenResponse.getAccessToken());

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

}
