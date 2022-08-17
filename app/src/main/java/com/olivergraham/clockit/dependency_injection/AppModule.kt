package com.olivergraham.clockit.dependency_injection

import android.app.Application
import androidx.room.Room
import com.olivergraham.clockit.data.ActivityDatabase
import com.olivergraham.clockit.data.ActivityRepositoryImplementation
import com.olivergraham.clockit.domain.repository.ActivityRepository
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
}