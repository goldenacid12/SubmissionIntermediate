package com.dicoding.latihan.submissionintermediate.api

import android.media.Image
import com.dicoding.latihan.submissionintermediate.response.NewStoriesResponse
import com.dicoding.latihan.submissionintermediate.response.PostLoginResponse
import com.dicoding.latihan.submissionintermediate.response.PostSignUpResponse
import com.dicoding.latihan.submissionintermediate.response.StoriesResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun postSignUp(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password:String
    ):Call<PostSignUpResponse>

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email:String,
        @Field("password") password: String
    ):Call<PostLoginResponse>

    @FormUrlEncoded
    @Headers("Authorization: Bearer <token>")
    @GET("stories")
    fun getStories(
    ):Call<StoriesResponse>

    @FormUrlEncoded
    @Headers("Authorization: Bearer <token>")
    @POST("stories")
    fun postStories(
        @Field("description") description: String,
        @Field("photo") photo: Image
    ): Call<NewStoriesResponse>
}