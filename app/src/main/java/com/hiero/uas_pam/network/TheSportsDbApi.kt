package com.hiero.uas_pam.network

import com.hiero.uas_pam.BuildConfig

object TheSportsDbApi {

    fun getLeagueAll(): String {
        return "${BuildConfig.BASE_URL}${BuildConfig.TSDB_API_KEY}" +
                "/all_leagues.php"
    }

    fun getLeaguePrev(id: String): String {
        return "${BuildConfig.BASE_URL}${BuildConfig.TSDB_API_KEY}" +
                "/eventspastleague.php?id=${id}"
    }

    fun getLeagueNext(id: String): String {
        return "${BuildConfig.BASE_URL}${BuildConfig.TSDB_API_KEY}" +
                "/eventsnextleague.php?id=${id}"
    }

    fun getTeamDetails(id: String): String {
        return "${BuildConfig.BASE_URL}${BuildConfig.TSDB_API_KEY}" +
                "/lookupteam.php?id=${id}"
    }
}
