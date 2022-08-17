package com.olivergraham.clockit.data

import androidx.room.*
import com.olivergraham.clockit.domain.model.Activity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ActivityEntity)

    @Query("SELECT * FROM activity_table")
    fun getAllActivities(): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM activity_table WHERE id = :activityId LIMIT 1")
    fun getAnActivity(activityId: Int): ActivityEntity

    @Update
    suspend fun updateActivity(activity: ActivityEntity)

    @Delete
    suspend fun deleteActivity(activity: ActivityEntity)

}