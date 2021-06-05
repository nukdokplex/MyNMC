package ru.nukdotcom.mynmc.models.token_response

import com.google.gson.annotations.SerializedName

data class TokenResponse(

	@field:SerializedName("access_token")
	val accessToken: String? = null,

	@field:SerializedName("token_type")
	val tokenType: String? = null
)
