package com.olivergraham.clockit.dependency_injection


import android.app.Application
import androidx.room.Room
import com.olivergraham.clockit.feature_activity.data.ActivityDatabase
import com.olivergraham.clockit.feature_activity.data.ActivityRepositoryImplementation
import com.olivergraham.clockit.feature_activity.domain.repository.ActivityRepository
import com.olivergraham.clockit.feature_activity.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideActivityDatabase(app: Application): ActivityDatabase {
        return Room.databaseBuilder(
            app,
            ActivityDatabase::class.java,
            "activity_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideActivityRepository(db: ActivityDatabase): ActivityRepository {
        return ActivityRepositoryImplementation(db.dao)
    }

    @Singleton
    @Provides
    fun provideActivityUseCases(repository: ActivityRepository): ActivityUseCases {
        return ActivityUseCases(
            addActivity = AddActivity(repository),
            deleteActivity = DeleteActivity(repository),
            getActivities = GetActivities(repository),
            getActivity = GetActivity(repository),
            updateActivity = UpdateActivity(repository)
        )
    }
}