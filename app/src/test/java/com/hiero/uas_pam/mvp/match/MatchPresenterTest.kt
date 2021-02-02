package com.hiero.uas_pam.mvp.match

import com.google.gson.Gson
import com.hiero.uas_pam.model.Event_Response
import com.hiero.uas_pam.model.EventItems
import com.hiero.uas_pam.model.League_Response
import com.hiero.uas_pam.model.LeagueItems
import com.hiero.uas_pam.network.ApiRepository
import com.hiero.uas_pam.network.TheSportsDbApi
import com.hiero.uas_pam.utility.TestContextProvider
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class MatchPresenterTest {

    @Mock
    lateinit var view: MatchView

    @Mock
    lateinit var apiRepository: ApiRepository

    @Mock
    lateinit var gson: Gson

    lateinit var presenter: MatchPresenter

    @Before
    fun setupEnv() {
        MockitoAnnotations.initMocks(this)
        presenter = MatchPresenter(view, apiRepository, gson, TestContextProvider())
    }

    @Test
    fun getLeagueAll() {
        val data: MutableList<LeagueItems> = mutableListOf()
        val response = League_Response(data)

        `when`(gson.fromJson(apiRepository
            .doRequest(TheSportsDbApi.getLeagueAll()),
            League_Response::class.java)
        ).thenReturn(response)

        presenter.getLeagueAll()

        verify(view).showLoading()
        verify(view).showLeagueList(response)
        verify(view).hideLoading()
    }

    @Test
    fun getEventsPrev() {
        val data: MutableList<EventItems> = mutableListOf()
        val response = Event_Response(data)
        val id = "1234"

        `when`(gson.fromJson(apiRepository
            .doRequest(TheSportsDbApi.getLeaguePrev(id)),
            Event_Response::class.java)
        ).thenReturn(response)

        presenter.getEventsPrev(id)

        verify(view).showLoading()
        verify(view).showEventList(data)
        verify(view).hideLoading()
    }

    @Test
    fun getEventsNext() {
        val data: MutableList<EventItems> = mutableListOf()
        val response = Event_Response(data)
        val id = "4321"

        `when`(gson.fromJson(apiRepository
            .doRequest(TheSportsDbApi.getLeagueNext(id)),
            Event_Response::class.java)
        ).thenReturn(response)

        presenter.getEventsNext(id)

        verify(view).showLoading()
        verify(view).showEventList(data)
        verify(view).hideLoading()
    }
}
