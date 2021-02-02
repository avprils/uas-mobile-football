package com.hiero.uas_pam.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.support.v4.content.ContextCompat
import android.widget.ExpandableListView
import androidx.recyclerview.widget.RecyclerView
import com.hiero.uas_pam.model.EventItems
import com.hiero.uas_pam.model.EventItems
import com.hiero.uas_pam.utility.DateTime
import org.jetbrains.anko.*

class MatchAdapter (private val items: List<EventItems>,
                    private val clickListener: (EventItems) -> Unit) : RecyclerView.Adapter<MatchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RecyclerView.ViewHolder(ItemUI().createView(AnkoContext.create(parent.context,parent)))
    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], clickListener)
    }

    inner class ViewHolder (val view: View) : RecyclerView.ViewHolder(view) {
        private val matchDate: TextView = view.findViewById(ID_DATE)
        private val matchHome_Team: TextView = view.findViewById(ID_HOME_TEAM)
        private val matchHome_Score: TextView = view.findViewById(ID_HOME_SCORE)
        private val matchAway_Team: TextView = view.findViewById(ID_AWAY_TEAM)
        private val matchAway_Score: TextView = view.findViewById(ID_AWAY_SCORE)

        fun bind(item: EventItems, clickListener: (EventItems) -> Unit) {
            matchDate.text = DateTime.getLongDate(item.dateEvent!!)
            matchHome_Team.text = item.strHome_Team
            matchHome_Score.text = item.intHome_Score
            matchAway_Team.text = item.strAway_Team
            matchAway_Score.text = item.intAway_Score

            itemView.setOnClickListener { clickListener(item) }
        }
    }

    companion object {
        const val ID_DATE = 1
        const val ID_HOME_TEAM = 2
        const val ID_HOME_SCORE = 3
        const val ID_AWAY_TEAM = 4
        const val ID_AWAY_SCORE = 5
    }

    inner class ItemUI : AnkoComponent<ViewGroup> {
        override fun createView(ui:AnkoContext<ViewGroup>) = with(ui) {
            linearLayout {
                lparams(matchParent, wrapContent)
                orientation = LinearLayout.VERTICAL

                linearLayout {
                    backgroundColor = Color.WHITE
                    orientation = LinearLayout.VERTICAL
                    padding = dip(8)

                    textView {
                        id = ID_DATE
                        textColor = ContextCompat.getColor(ctx, R.color.colorPrimary)
                        gravity = Gravity.CENTER
                    }.lparams(matchParent, wrapContent)

                    linearLayout {
                        gravity = Gravity.CENTER_VERTICAL

                        textView {
                            id = ID_HOME_TEAM
                            gravity = Gravity.CENTER
                            textSize = 18f
                            text = "home"
                        }.lparams(matchParent, wrapContent, 1f)

                        linearLayout {
                            gravity = Gravity.CENTER_VERTICAL

                            textView {
                                id = ID_HOME_SCORE
                                padding = dip(8)
                                textSize = 20f
                                setTypeface(null, Typeface.BOLD)
                                text = "0"
                            }

                            textView {
                                text = "vs"
                            }

                            textView {
                                id = ID_AWAY_SCORE
                                padding = dip(8)
                                textSize = 20f
                                setTypeface(null, Typeface.BOLD)
                                text = "0"
                            }
                        }

                        textView {
                            id = ID_AWAY_TEAM
                            gravity = Gravity.CENTER
                            textSize = 18f
                            text = "away"
                        }.lparams(matchParent, wrapContent, 1f)
                    }
                }.lparams(matchParent, matchParent) {
                    setMargins(dip(16), dip(8), dip(16), dip(8))
                }
            }
        }
    }
}