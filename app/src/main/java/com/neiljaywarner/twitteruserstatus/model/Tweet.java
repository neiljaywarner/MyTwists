package com.neiljaywarner.twitteruserstatus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by neil on 1/15/17.
 */

public class Tweet {
    // Note: It maybe OK to expose fields and not use getters in this case since it's such a thin class.
    @SerializedName("created_at")
    @Expose
    public String createdAt;

    @SerializedName("id_str")
    @Expose
    public String idStr;

    @SerializedName("text")
    @Expose
    public String text;

    // We could add "User" to get the picture via the profile_image_url, that might be nice.
}
