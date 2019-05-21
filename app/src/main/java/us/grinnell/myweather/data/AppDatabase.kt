package us.grinnell.myweather.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import us.grinnell.weatherapp.data.PubListDAO

@Database(entities = arrayOf(Item::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pubsListDao(): PubListDAO

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, "pubs.db")
                    .build()
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
