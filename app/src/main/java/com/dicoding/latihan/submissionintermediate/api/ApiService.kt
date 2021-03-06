package com.dicoding.latihan.submissionintermediate.api

import com.dicoding.latihan.submissionintermediate.response.NewStoriesResponse
import com.dicoding.latihan.submissionintermediate.response.PostLoginResponse
import com.dicoding.latihan.submissionintermediate.response.PostSignUpResponse
import com.dicoding.latihan.submissionintermediate.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun postSignUp(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<PostSignUpResponse>

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<PostLoginResponse>

    @GET("stories")
    fun getStories(
        @Header("Authorization") token: String
    ): Call<StoriesResponse>

    @Multipart
    @POST("stories")
    fun postStories(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): Call<NewStoriesResponse>
}