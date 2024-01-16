package com.example.ilerimobilfinalapp.app

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class MovieEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val release: String,
    @ColumnInfo val playtime: String,
    @ColumnInfo val description: String,
    @ColumnInfo val plot: String,
    @ColumnInfo val poster: String
) : Parcelable
