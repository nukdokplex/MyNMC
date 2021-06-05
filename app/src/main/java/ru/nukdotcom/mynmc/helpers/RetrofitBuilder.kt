package ru.nukdotcom.mynmc.helpers

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.nukdotcom.mynmc.R

class RetrofitBuilder {
    companion object{
        public fun buildDefaultRetrofit(context: Context): Retrofit{
            return Retrofit.Builder()
                .baseUrl(
                    context.getString(R.string.app_url_schema) +
                            context.getString(R.string.app_domain) +
                            context.getString(R.string.app_dir)
                )
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}