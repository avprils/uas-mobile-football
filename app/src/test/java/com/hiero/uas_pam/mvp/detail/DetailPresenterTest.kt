package com.hiero.uas_pam.mvp.detail

import com.google.gson.Gson
import com.hiero.uas_pam.model.TeamDetail_Response
import com.hiero.uas_pam.model.TeamItems
import com.hiero.uas_pam.network.ApiRepository
import com.hiero.uas_pam.network.TheSportsDbApi
import com.hiero.uas_pam.utility.TestContextProvider
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class DetailPresenterTest {

    @Mock
    lateinit var view: DetailView

    @Mock
    lateinit var apiRepository: ApiRepository

    @Mock
    lateinit var gson: Gson

    lateinit var presenter: DetailPresenter

    @Before
    fun setupEnv() {
        MockitoAnnotations.initMocks(this)
        presenter = DetailPresenter(view, apiRepository, gson, TestContextProvider())
    }

    @Test
    fun getTeamDetails() {
        val data: MutableList<TeamItems> = mutableListOf()
        val response = TeamDetail_Response(data)
        val id = "1234"

        `when`(gson.fromJson(apiRepository
            .doRequest(TheSportsDbApi.getTeamDetails(id)),
            TeamDetail_Response::class.java)
        ).thenReturn(response)

        presenter.getTeamDetails(id, id)

        verify(view).showLoading()
        verify(view).showTeamDetails(response.teams!!, response.teams!!)
        verify(view).hideLoading()
    }
}
