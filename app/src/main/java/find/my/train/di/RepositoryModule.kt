package find.my.train.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import find.my.train.data.repository.HistoryRepositoryImpl
import find.my.train.data.repository.SettingsRepositoryImpl
import find.my.train.data.repository.TrainRepositoryImpl
import find.my.train.domain.repository.HistoryRepository
import find.my.train.domain.repository.SettingsRepository
import find.my.train.domain.repository.TrainRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTrainRepository(
        trainRepositoryImpl: TrainRepositoryImpl
    ): TrainRepository

    @Binds
    @Singleton
    abstract fun bindHistoryRepository(
        historyRepositoryImpl: HistoryRepositoryImpl
    ): HistoryRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository
}
