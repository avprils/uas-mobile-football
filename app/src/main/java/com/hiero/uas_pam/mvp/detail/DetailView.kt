package com.hiero.uas_pam.mvp.detail

import com.hiero.uas_pam.model.EventItems

interface DetailView {

    fun showLoading()
    fun hideLoading()
    fun showTeamDetails(dataHomeTeam: List<TeamItems>, dataAwayTeam: List<TeamItems>)
}
