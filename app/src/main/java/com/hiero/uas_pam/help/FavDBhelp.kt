package com.hiero.uas_pam.help


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class FavDBhelp (context: Context) : ManagedSQLiteOpenHelper(context, "Fav.db", null, 1)) {
    companion object {
        private var instance: FavDBhelp? = null

        @Synchronized
        fun getInstance(context: Context): FavDBhelp {
            if (instance == null) {
                instance = FavDBhelp(context.applicationContext)
            }

            return instance as FavDBhelp
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(EventItems.TABLE_FAVORITES, true,
            EventItems.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            EventItems.ID_EVENT to TEXT,
            EventItems.DATE to TEXT,

            // home team
            EventItems.HOME_ID to TEXT,
            EventItems.HOME_TEAM to TEXT,
            EventItems.HOME_SCORE to TEXT,
            EventItems.HOME_FORMATION to TEXT,
            EventItems.HOME_GOAL_DETAILS to TEXT,
            EventItems.HOME_SHOTS to TEXT,
            EventItems.HOME_LINEUP_GOALKEEPER to TEXT,
            EventItems.HOME_LINEUP_DEFENSE to TEXT,
            EventItems.HOME_LINEUP_MIDFIELD to TEXT,
            EventItems.HOME_LINEUP_FORWARD to TEXT,
            EventItems.HOME_LINEUP_SUBSTITUTES to TEXT,

            // away team
            EventItems.AWAY_ID to TEXT,
            EventItems.AWAY_TEAM to TEXT,
            EventItems.AWAY_SCORE to TEXT,
            EventItems.AWAY_FORMATION to TEXT,
            EventItems.AWAY_GOAL_DETAILS to TEXT,
            EventItems.AWAY_SHOTS to TEXT,
            EventItems.AWAY_LINEUP_GOALKEEPER to TEXT,
            EventItems.AWAY_LINEUP_DEFENSE to TEXT,
            EventItems.AWAY_LINEUP_MIDFIELD to TEXT,
            EventItems.AWAY_LINEUP_FORWARD to TEXT,
            EventItems.AWAY_LINEUP_SUBSTITUTES to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(EventItems.TABLE_FAVORITES, true)
    }
}

val Context.database: FavDBhelp
    get() = FavDBhelp.getInstance(applicationContext)
