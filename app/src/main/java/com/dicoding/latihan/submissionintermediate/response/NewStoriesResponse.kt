package com.dicoding.latihan.submissionintermediate.response

import com.google.gson.annotations.SerializedName

data class NewStoriesResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
