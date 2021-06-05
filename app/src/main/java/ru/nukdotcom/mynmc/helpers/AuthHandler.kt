package ru.nukdotcom.mynmc.helpers

import android.content.Context
import android.content.SharedPreferences
import ru.nukdotcom.mynmc.models.schedule_model.Model

class AuthHandler(val context: Context) {
    var token: String? = null
    var group: Model? = null
    var role: String? = null
    var name: String? = null
    var email: String? = null
    var avatar: String? = null
    var id: Long? = null
    

    companion object {
        public val AUTH_HANDLER_SHARED_PREFERENCES_NAME = "auth"
        public val KEY_TOKEN = "user_token"
        public val KEY_GROUP_ID = "user_group_id"
        public val KEY_GROUP_NAME = "user_group_name"
        public val KEY_ROLE = "user_role"
        public val KEY_NAME = "user_name"
        public val KEY_EMAIL = "user_email"
        public val KEY_ID = "user_id"
        public val KEY_AVATAR = "user_avatar"

    }

    protected fun getSharedPreferences(mode: Int): SharedPreferences{
        return context.getSharedPreferences(AUTH_HANDLER_SHARED_PREFERENCES_NAME, mode)
    }

    public fun reload(){
        val sharedPreferences = getSharedPreferences(Context.MODE_PRIVATE)
        id = sharedPreferences.getLong(KEY_ID, -1L)

        if (id == -1L){
            token = null
            group = null
            name = null
            email = null
            role = null
            avatar = null
            id = null
            return
        }
        else{
            token = sharedPreferences.getString(KEY_TOKEN, null)
            group = Model(
                sharedPreferences.getString(KEY_GROUP_NAME, null),
                sharedPreferences.getLong(KEY_GROUP_ID, -1L)
            )
            if (group!!.id == -1L){
                group = null
            }
            name = sharedPreferences.getString(KEY_NAME, null)
            email = sharedPreferences.getString(KEY_EMAIL, null)
            avatar = sharedPreferences.getString(KEY_AVATAR, null)
            role = sharedPreferences.getString(KEY_ROLE, null)
        }
    }

    public fun save(){
        val sharedPreferences = getSharedPreferences(Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        if (token  == null) editor.remove(KEY_TOKEN)  else editor.putString(KEY_TOKEN , token    )
        if (name   == null) editor.remove(KEY_NAME)   else editor.putString(KEY_NAME  , name     )
        if (email  == null) editor.remove(KEY_EMAIL)  else editor.putString(KEY_EMAIL , email    )
        if (role   == null) editor.remove(KEY_ROLE)   else editor.putString(KEY_ROLE  , role     )
        if (id     == null) editor.remove(KEY_ID)     else editor.putLong  (KEY_ID    , id!!     )
        if (avatar == null) editor.remove(KEY_AVATAR) else editor.putString(KEY_AVATAR, avatar!! )

        if (group == null || group!!.id == null || group!!.name == null){
            editor.remove(KEY_GROUP_ID)
            editor.remove(KEY_GROUP_NAME)
        }
        else{
            editor.putLong(KEY_GROUP_ID, group!!.id!!)
            editor.putString(KEY_GROUP_NAME, group!!.name!!)
        }
        editor.commit()
    }

    public fun isAuthenticated(): Boolean {
        reload()
        return id != null && token != null
    }
}