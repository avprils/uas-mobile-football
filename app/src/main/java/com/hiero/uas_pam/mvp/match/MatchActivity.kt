package com.hiero.uas_pam.mvp.match


import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import com.google.gson.Gson
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.hiero.uas_pam.R
import com.hiero.uas_pam.adapter.MatchAdapter
import com.hiero.uas_pam.model.EventItems
import com.hiero.uas_pam.model.League_Response
import com.hiero.uas_pam.model.LeagueItems
import com.hiero.uas_pam.mvp.detail.DetailActivity
import com.hiero.uas_pam.mvp.detail.INTENT_DETAIL
import com.hiero.uas_pam.network.ApiRepository
import com.hiero.uas_pam.utility.gone
import com.hiero.uas_pam.utility.invisible
import com.hiero.uas_pam.utility.progressBar
import com.hiero.uas_pam.utility.visible
import org.jetbrains.anko.*
import org.jetbrains.anko.design.bottomNavigationView
import org.jetbrains.anko.recyclerview.v7.recyclerView

class MatchActivity : AppCompatActivity(),
    _root_ide_package_.com.uas_pam.uas_pam.mvp.match.MatchView {

    private lateinit var presenter: MatchPresenter
    private lateinit var adapter: MatchAdapter

    private lateinit var spinnerLayout: LinearLayout
    private lateinit var spinner: Spinner
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyDataView: LinearLayout

    private lateinit var league: LeagueItems

    private var events: MutableList<EventItems> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupLayout()
        setupEnv()
    }

    override fun onResume() {
        super.onResume()

        if (presenter.menu == 3) presenter.getFavoritesAll(ctx)
    }

    override fun showLoading() {
        progressBar.visible()
        recyclerView.invisible()
        emptyDataView.invisible()

        if (presenter.menu == 3) spinnerLayout.gone()
        else spinnerLayout.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
        recyclerView.visible()
        emptyDataView.invisible()
    }

    override fun showEmptyData() {
        progressBar.invisible()
        recyclerView.invisible()
        emptyDataView.visible()
    }

    override fun showLeagueList(data: League_Response) {
        spinner.adapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, data.leagues)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                league = spinner.selectedItem as LeagueItems

                when (presenter.menu) {
                    1 -> presenter.getEventsPrev(league.idLeague!!)
                    2 -> presenter.getEventsNext(league.idLeague!!)
                }
            }
        }
    }

    override fun showEventList(data: List<EventItems>) {
        showEventListData(data)
    }

    private fun setupLayout() {
        linearLayout {
            orientation = LinearLayout.VERTICAL

            spinnerLayout = linearLayout {
                orientation = LinearLayout.VERTICAL
                backgroundColor = Color.LTGRAY

                spinner = spinner {
                    id = R.id.spinner
                    padding = dip(16)
                    minimumHeight = dip(80)
                }
            }

            relativeLayout {
                emptyDataView = linearLayout {
                    orientation = LinearLayout.VERTICAL

                    imageView {
                        setImageResource(R.drawable.ic_no_data)
                    }

                    textView {
                        gravity = Gravity.CENTER
                        padding = dip(8)
                        text = "No Data Provided"
                    }
                }.lparams {
                    centerInParent()
                }

                recyclerView = recyclerView {
                    id = R.id.recycler_view
                    layoutManager = LinearLayoutManager(ctx)
                }.lparams(matchParent, matchParent) {
                    topOf(R.id.bottom_navigation_view)
                }

                progressBar(R.id.progress_bar).lparams {
                    centerInParent()
                }

                bottomNavigationView {
                    id = R.id.bottom_navigation_view
                    backgroundColor = Color.WHITE

                    menu.apply {
                        add(0, R.id.bnv_match_prev, 0, "Prev. Match")
                            .setIcon(R.drawable.ic_trophy)
                            .setOnMenuItemClickListener {
                                presenter.getEventsPrev(league.idLeague!!)
                                false
                            }

                        add(0, R.id.bnv_match_next, 0, "Next Match")
                            .setIcon(R.drawable.ic_event)
                            .setOnMenuItemClickListener {
                                presenter.getEventsNext(league.idLeague!!)
                                false
                            }

                        add(0, R.id.bnv_favorites, 0, "Favorites")
                            .setIcon(R.drawable.ic_favorites)
                            .setOnMenuItemClickListener {
                                presenter.getFavoritesAll(ctx)
                                false
                            }
                    }
                }.lparams(matchParent, wrapContent) {
                    alignParentBottom()
                }
            }
        }
    }

    private fun setupEnv() {
        progressBar = find(R.id.progress_bar)

        presenter = MatchPresenter(this, ApiRepository(), Gson())
        adapter = MatchAdapter(events) {
            startActivity<DetailActivity>(INTENT_DETAIL to it)
        }

        presenter.getLeagueAll()
        recyclerView.adapter = adapter
    }

    private fun showEventListData(data: List<EventsItem>) {
        events.clear()
        events.addAll(data)
        adapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(0)
    }
}
