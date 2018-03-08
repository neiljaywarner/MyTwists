package com.neiljaywarner.twitteruserstatus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by neil on 1/15/17.
 */

public class Tweet {

    @SerializedName("text")
    @Expose
    public String text;

    // We could add "User" to get the picture via the profile_image_url, that might be nice.
}
