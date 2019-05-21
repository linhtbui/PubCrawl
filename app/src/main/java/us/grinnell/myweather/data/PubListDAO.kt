package us.grinnell.weatherapp.data

import android.arch.persistence.room.*
import us.grinnell.myweather.data.Item

@Dao
interface PubListDAO {

    @Query("SELECT * FROM Pubs")
    fun findAllPubs(): List<Item>

    //returns the ID of the item inserted
    @Insert
    fun insertPub(item: Item) : Long

    @Delete
    fun deletePub(item: Item)

    @Update
    fun updatePub(item: Item)

    @Query("DELETE FROM Pubs")
    fun deleteAll()

    @Query("DELETE FROM Pubs WHERE went")
    fun deleteAllBought()

}