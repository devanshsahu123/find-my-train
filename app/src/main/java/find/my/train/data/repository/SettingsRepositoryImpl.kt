package find.my.train.data.repository

import find.my.train.data.local.datastore.SettingsDataStore
import find.my.train.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) : SettingsRepository {
    override fun getDarkMode(): Flow<String> = settingsDataStore.darkModeFlow
    override fun isNotificationsEnabled(): Flow<Boolean> = settingsDataStore.notificationsEnabledFlow
    override suspend fun setDarkMode(mode: String) = settingsDataStore.setDarkMode(mode)
    override suspend fun setNotificationsEnabled(enabled: Boolean) = settingsDataStore.setNotificationsEnabled(enabled)
}
