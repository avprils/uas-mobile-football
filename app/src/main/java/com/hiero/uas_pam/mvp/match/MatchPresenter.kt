package com.hiero.uas_pam.mvp.match

import android.content.Context
import com.google.gson.Gson
import com.hiero.uas_pam.help.database
import com.hiero.uas_pam.model.Event_Response
import com.hiero.uas_pam.model.EventItems
import com.hiero.uas_pam.model.League_Response
import com.hiero.uas_pam.network.ApiRepository
import com.hiero.uas_pam.network.TheSportsDbApi
import com.hiero.uas_pam.utility.CoroutineContextProvider
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

class MatchPresenter(private val view: _root_ide_package_.com.uas_pam.uas_pam.mvp.match.MatchView,
                     private val apiRepository: ApiRepository,
                     private val gson: Gson,
                     private val context: CoroutineContextProvider = CoroutineContextProvider()) {

    var menu = 1

    fun getLeagueAll() {
        view.showLoading()

        async(context.main) {
            val data = bg {
                gson.fromJson(apiRepository
                    .doRequest(TheSportsDbApi.getLeagueAll()),
                    League_Response::class.java
                )
            }

            view.hideLoading()
            view.showLeagueList(data.await())
        }
    }

    fun getEventsPrev(id: String) {
        menu = 1
        view.showLoading()

        async(context.main) {
            val data = bg {
                gson.fromJson(apiRepository
                    .doRequest(TheSportsDbApi.getLeaguePrev(id)),
                    Event_Response::class.java
                )
            }

            view.hideLoading()

            try {
                view.showEventList(data.await().events!!)
            } catch (e: NullPointerException) {
                view.showEmptyData()
            }
        }
    }

    fun getEventsNext(id: String) {
        menu = 2
        view.showLoading()

        async(context.main) {
            val data = bg {
                gson.fromJson(apiRepository
                    .doRequest(TheSportsDbApi.getLeagueNext(id)),
                    Event_Response::class.java
                )
            }

            view.hideLoading()

            try {
                view.showEventList(data.await().events!!)
            } catch (e: NullPointerException) {
                view.showEmptyData()
            }
        }
    }

    fun getFavoritesAll(context: Context) {
        menu = 3
        view.showLoading()

        val data: MutableList<EventItems> = mutableListOf()

        context.database.use {
            val favorites = select(EventItems.TABLE_FAVORITES)
                .parseList(classParser<EventItems>())

            data.addAll(favorites)
        }

        view.hideLoading()

        if (data.size > 0) {
            view.showEventList(data)
        } else {
            view.showEmptyData()
        }
    }
}