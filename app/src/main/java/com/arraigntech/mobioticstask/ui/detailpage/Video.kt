package com.arraigntech.mobioticstask.ui.detailpage

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
@Entity(tableName = "video")
data class Video(
    @PrimaryKey
    var id: Int,
    var continueVideo: Long,
    var duration: Long
)