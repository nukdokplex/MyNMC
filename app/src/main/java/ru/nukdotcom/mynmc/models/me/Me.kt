package ru.nukdotcom.mynmc.models.me

import com.google.gson.annotations.SerializedName
import ru.nukdotcom.mynmc.models.schedule_model.Model

data class Me(

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Long? = null,

	@field:SerializedName("avatar")
	val avatar: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("group")
	val group: Model? = null
)
