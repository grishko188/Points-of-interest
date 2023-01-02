package com.grishko188.data.features.poi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import com.grishko188.data.core.UNSPECIFIED_ID
import com.grishko188.domain.features.poi.models.PoiSortOption
import kotlinx.datetime.Instant

@Entity(tableName = "table_poi")
data class PoiEntity(
    @ColumnInfo(name = "content_link")
    val contentLink: String,
    val title: String,
    val body: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String?,
    @ColumnInfo(name = "creation_date_time")
    val creationDateTime: Instant,
    @ColumnInfo(name = "comments_count")
    val commentsCount: Int,
    val severity: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = UNSPECIFIED_ID
}

@Fts4(contentEntity = PoiEntity::class)
@Entity(tableName = "table_poi_fts")
data class PoiFtsEntity(
    @PrimaryKey @ColumnInfo(name = "rowid") val id: Int,
    @ColumnInfo(name = "content_link")
    val contentLink: String,
    val title: String,
    val body: String,
)

enum class OrderByColumns(val columnName: String) {
    DATE("creation_date_time"), SEVERITY("severity"), TITLE("title")
}

fun PoiSortOption.toOrderBy() =
    when (this) {
        PoiSortOption.DATE -> OrderByColumns.DATE
        PoiSortOption.SEVERITY -> OrderByColumns.SEVERITY
        PoiSortOption.TITLE -> OrderByColumns.TITLE
    }