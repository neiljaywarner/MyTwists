package com.neiljaywarner.twitteruserstatus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by neil on 1/15/17.
 */

public class Tweet {
    @SerializedName("created_at")
    @Expose
    public String createdAt;

    @SerializedName("id_str")
    @Expose
    public String idStr;

    @SerializedName("text")
    @Expose
    public String text;
}
