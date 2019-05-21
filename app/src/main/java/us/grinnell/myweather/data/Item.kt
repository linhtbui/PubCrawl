package us.grinnell.myweather.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.ColumnInfo
import java.io.Serializable

@Entity(tableName = "Pubs")
data class Item(
    @PrimaryKey(autoGenerate = true) var pubId: Long?,
    @ColumnInfo(name = "drink") var drink : String,
    @ColumnInfo(name = "name") var name : String,
    @ColumnInfo(name = "went") var went : Boolean,
    @ColumnInfo(name = "distance") var distance: Long?,
    @ColumnInfo(name = "rating") var rating: Int,
    @ColumnInfo(name = "pubDescription") var pubDescription: String,
    @ColumnInfo(name = "price") var price: String
) : Serializable