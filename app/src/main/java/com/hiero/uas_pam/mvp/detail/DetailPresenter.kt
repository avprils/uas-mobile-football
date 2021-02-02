package com.hiero.uas_pam.mvp.detail


import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import com.google.gson.Gson
import com.hiero.uas_pam.help.database
import com.hiero.uas_pam.model.EventItems
import com.hiero.uas_pam.model.TeamDetailResponse
import com.hiero.uas_pam.network.ApiRepository
import com.hiero.uas_pam.network.TheSportsDbApi
import com.hiero.uas_pam.utility.CoroutineContextProvider
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.toast

class DetailPresenter(private val view: DetailView,
                      private val apiRepository: ApiRepository,
                      private val gson: Gson,
                      private val context: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getTeamDetails(idHomeTeam: String?, idAwayTeam: String?) {
        view.showLoading()

        async(context.main) {
            val dataHomeTeam = bg {
                gson.fromJson(apiRepository
                    .doRequest(TheSportsDbApi.getTeamDetails(idHomeTeam.toString())),
                    TeamDetailResponse::class.java
                )
            }

            val dataAwayTeam = bg {
                gson.fromJson(apiRepository
                    .doRequest(TheSportsDbApi.getTeamDetails(idAwayTeam.toString())),
                    TeamDetailResponse::class.java
                )
            }

            view.hideLoading()
            view.showTeamDetails(dataHomeTeam.await().teams!!, dataAwayTeam.await().teams!!)
        }
    }

    fun addFavorites(context: Context, data: EventItems) {
        try {
            context.database.use {
                insert(EventItems.TABLE_FAVORITES,
                    EventItems.ID_EVENT to data.idEvent,
                    EventItems.DATE to data.dateEvent,

                    // home team
                    EventItems.HOME_ID to data.id_HomeTeamHomeTeam,
                    EventItems.HOME_TEAM to data.strHomeTeam,
                    EventItems.HOME_SCORE to data.intHomeScore,
                    EventItems.HOME_FORMATION to data.strHomeFormation,
                    EventItems.HOME_GOAL_DETAILS to data.strHomeGoalDetails,
                    EventItems.HOME_SHOTS to data.intHomeShots,
                    EventItems.HOME_LINEUP_GOALKEEPER to data.strHomeLineupGoalkeeper,
                    EventItems.HOME_LINEUP_DEFENSE to data.strHomeLineupDefense,
                    EventItems.HOME_LINEUP_MIDFIELD to data.strHomeLineupMidfield,
                    EventItems.HOME_LINEUP_FORWARD to data.strHomeLineupForward,
                    EventItems.HOME_LINEUP_SUBSTITUTES to data.strHomeLineupSubstitutes,

                    // away team
                    EventItems.AWAY_ID to data.id_AwayTeamAwayTeam,
                    EventItems.AWAY_TEAM to data.strAwayTeam,
                    EventItems.AWAY_SCORE to data.intAwayScore,
                    EventItems.AWAY_FORMATION to data.strAwayFormation,
                    EventItems.AWAY_GOAL_DETAILS to data.strAwayGoalDetails,
                    EventItems.AWAY_SHOTS to data.intAwayShots,
                    EventItems.AWAY_LINEUP_GOALKEEPER to data.strAwayLineupGoalkeeper,
                    EventItems.AWAY_LINEUP_DEFENSE to data.strAwayLineupDefense,
                    EventItems.AWAY_LINEUP_MIDFIELD to data.strAwayLineupMidfield,
                    EventItems.AWAY_LINEUP_FORWARD to data.strAwayLineupForward,
                    EventItems.AWAY_LINEUP_SUBSTITUTES to data.strAwayLineupSubstitutes)
            }
        } catch (e: SQLiteConstraintException) {
            context.toast("Error: ${e.message}")
        }
    }

    fun removeFavorites(context: Context, data: EventItems) {
        try {
            context.database.use {
                delete(EventItems.TABLE_FAVORITES,
                    EventItems.ID_EVENT + " = {id}",
                    "id" to data.idEvent.toString())
            }
        } catch (e: SQLiteConstraintException) {
            context.toast("Error: ${e.message}")
        }
    }

    fun isFavorite(context: Context, data: EventItems): Boolean {
        var bFavorite = false

        context.database.use {
            val favorites = select(EventItems.TABLE_FAVORITES)
                .whereArgs(EventItems.ID_EVENT + " = {id}",
                    "id" to data.idEvent.toString())
                .parseList(classParser<EventItems>())

            bFavorite = !favorites.isEmpty()
        }

        return bFavorite
    }
}
