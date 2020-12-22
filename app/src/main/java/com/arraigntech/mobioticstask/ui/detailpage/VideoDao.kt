package com.arraigntech.mobioticstask.ui.detailpage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(video: Video)

    @Query("SELECT * FROM video WHERE id LIKE :ids ")
    suspend fun getItem(ids: Int): Video
}