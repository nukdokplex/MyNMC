package ru.nukdotcom.mynmc.helpers

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import ru.nukdotcom.mynmc.models.me.Me
import ru.nukdotcom.mynmc.models.schedule_model.Model
import ru.nukdotcom.mynmc.models.token_response.TokenResponse

interface MyNMCWebAPI {
    @POST("api/auth/login")
    fun login(@Query("email") email: String, @Query("password") password: String): Call<TokenResponse>

    @GET("api/me")
    fun me(@Header("Authorization") authHeader: String): Call<Me>

    @GET("api/{modelType}")
    fun getModels(@Path("modelType") modelType:String): Call<ArrayList<Model>>
}