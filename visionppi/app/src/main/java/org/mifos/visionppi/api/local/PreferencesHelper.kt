package org.mifos.visionppi.api.local

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.TextUtils
import javax.inject.Inject
import javax.inject.Singleton
import org.mifos.visionppi.api.BaseURL
import org.mifos.visionppi.api.VisionPPIInterceptor
import org.mifos.visionppi.injection.ApplicationContext

/**
 * @author yashk2000
 * @since 22/06/2020
 */
@Singleton
class PreferencesHelper @Inject constructor(@ApplicationContext context: Context?) {
    private val sharedPreferences: SharedPreferences

    fun clear() {
        val editor = sharedPreferences.edit()
        // prevent deletion of url and tenant
        for ((key) in sharedPreferences.all) {
            if (!(key == BASE_URL || key == TENANT)) {
                editor.remove(key)
            }
        }
        editor.apply()
    }

    fun getInt(preferenceKey: String?, preferenceDefaultValue: Int): Int {
        return sharedPreferences.getInt(preferenceKey, preferenceDefaultValue)
    }

    fun putInt(preferenceKey: String?, preferenceValue: Int) {
        sharedPreferences.edit().putInt(preferenceKey, preferenceValue).apply()
    }

    fun getLong(preferenceKey: String?, preferenceDefaultValue: Long): Long {
        return sharedPreferences.getLong(preferenceKey, preferenceDefaultValue)
    }

    fun putLong(preferenceKey: String?, preferenceValue: Long) {
        sharedPreferences.edit().putLong(preferenceKey, preferenceValue).apply()
    }

    fun getString(preferenceKey: String?, preferenceDefaultValue: String?): String? {
        return sharedPreferences.getString(preferenceKey, preferenceDefaultValue)
    }

    fun putString(preferenceKey: String?, preferenceValue: String?) {
        sharedPreferences.edit().putString(preferenceKey, preferenceValue).apply()
    }

    fun putBoolean(preferenceKey: String?, preferenceValue: Boolean) {
        sharedPreferences.edit().putBoolean(preferenceKey, preferenceValue).apply()
    }

    fun getBoolean(preferenceKey: String?, preferenceDefaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(preferenceKey, preferenceDefaultValue)
    }

    fun saveToken(token: String?) {
        putString(TOKEN, token)
    }

    fun clearToken() {
        putString(TOKEN, "")
    }

    val token: String?
        get() = getString(TOKEN, "")

    val isAuthenticated: Boolean
        get() = !TextUtils.isEmpty(token)

    var userId: Long
        get() = getLong(USER_ID, -1)
        set(id) {
            putLong(USER_ID, id)
        }

    val tenant: String?
        get() = getString(TENANT, VisionPPIInterceptor.DEFAULT_TENANT)

    var userName: String?
        get() = getString(USER_NAME, "")
        set(userName) {
            putString(USER_NAME, userName)
        }

    val baseUrl: String?
        get() = getString(BASE_URL, BaseURL().defaultBaseUrl)

    companion object {
        private const val USER_ID = "preferences_user_id"
        private const val TOKEN = "preferences_token"
        private const val USER_NAME = "preferences_user_name"
        private const val TENANT = "preferences_base_tenant"
        private const val BASE_URL = "preferences_base_url_key"
    }

    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }
}
