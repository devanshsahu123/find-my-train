package find.my.train.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getDarkMode(): Flow<String>
    fun isNotificationsEnabled(): Flow<Boolean>
    suspend fun setDarkMode(mode: String)
    suspend fun setNotificationsEnabled(enabled: Boolean)
}
