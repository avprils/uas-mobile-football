package com.hiero.uas_pam.model

data class LeagueItems(val idLeague: String?, val strLeague: String?) {

    override fun toString(): String {
        return strLeague.toString()
    }
}