package com.neiljaywarner.twitteruserstatus.model

import java.util.Collections.emptyList

/**
 * Created by neil on 10/13/17.
 */

data class Response (
    val tweetList: List<Tweet> = emptyList()
)
