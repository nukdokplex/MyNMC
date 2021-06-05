package ru.nukdotcom.mynmc.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class ScheduleSettingsHandler(val context: Context) {
    companion object{
        public val SHARED_PREFERENCES_NAME = "schedule_settings"
        public val KEY_MODEL_TYPE = "model_type"
        public val KEY_MODEL_ID = "model_id"
    }

    public var model_type:String? = null
    public var model_id:Long? = null

    protected var preferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    public fun reload(){
        model_type = preferences.getString(KEY_MODEL_TYPE, null)
        model_id = preferences.getLong(KEY_MODEL_ID, -1L)

        if (model_id == -1L) model_id = null
    }

    @SuppressLint("ApplySharedPref")
    public fun save(){
        val editor = preferences.edit()

        if (model_type == null) editor.remove(KEY_MODEL_TYPE) else editor.putString(KEY_MODEL_TYPE, model_type!!)
        if (model_id == null) editor.remove(KEY_MODEL_ID) else editor.putLong(KEY_MODEL_ID, model_id!!)

        editor.commit()
    }
}