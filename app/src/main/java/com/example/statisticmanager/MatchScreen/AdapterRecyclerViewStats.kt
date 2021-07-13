package com.example.statisticmanager.MatchScreen

import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.statisticmanager.MatchScreen.MatchActivity
import com.example.statisticmanager.R
import com.example.statisticmanager.common.*
import com.example.statisticmanager.database.RoomViewModel

class AdapterRecyclerViewStats(var listStats: MutableList<ModelState> = arrayListOf(), val onDeleteItem: OnDeleteItem, val onTapItem: OnTapItem): RecyclerView.Adapter<AdapterRecyclerViewStats.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val team_1_count = itemView.findViewById<TextView>(R.id.team_1_count)
        val team_2_count = itemView.findViewById<TextView>(R.id.team_2_count)
        val team_1_progress = itemView.findViewById<ProgressBar>(R.id.progress_1)
        val team_2_progress = itemView.findViewById<ProgressBar>(R.id.progress_2)
        val stat_name = itemView.findViewById<TextView>(R.id.stat_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_stat, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.stat_name.text = listStats[position].nameState

        holder.itemView.setOnLongClickListener {
            onDeleteItem.onDelete(position)
            return@setOnLongClickListener true
        }

        holder.itemView.setOnClickListener {
            onTapItem.onClick(position)
        }

        updateViewData(holder, position)

        val plus_team_1 = View.OnClickListener {
            listStats[position].team_1_count ++
            listStats[position].count ++
            holder.team_1_count.text = (listStats[position].team_1_count).toString()
            updateViewData(holder, position)
            updateBD(listStats[position], position)
        }

        val plus_team_2 = View.OnClickListener {
            listStats[position].team_2_count ++
            listStats[position].count ++
            holder.team_2_count.text = (listStats[position].team_2_count).toString()
            updateViewData(holder, position)
            updateBD(listStats[position], position)
        }

        val minus_team_1 = View.OnLongClickListener {
            if (listStats[position].team_1_count != 0) {
                listStats[position].team_1_count--
                listStats[position].count--
                holder.team_1_count.text = (listStats[position].team_1_count).toString()
                updateViewData(holder, position)
                updateBD(listStats[position], position)
            }
            return@OnLongClickListener true
        }

        val minus_team_2 = View.OnLongClickListener {
            if (listStats[position].team_2_count != 0) {
                listStats[position].team_2_count--
                listStats[position].count--
                holder.team_2_count.text = (listStats[position].team_2_count).toString()
                updateViewData(holder, position)
                updateBD(listStats[position], position)
            }
            return@OnLongClickListener true
        }

        holder.team_1_count.setOnClickListener(plus_team_1)
        holder.team_1_count.setOnLongClickListener(minus_team_1)
        holder.team_2_count.setOnClickListener(plus_team_2)
        holder.team_2_count.setOnLongClickListener(minus_team_2)

        holder.team_1_progress.setOnClickListener(plus_team_1)
        holder.team_1_progress.setOnLongClickListener(minus_team_1)
        holder.team_2_progress.setOnClickListener(plus_team_2)
        holder.team_2_progress.setOnLongClickListener(minus_team_2)
    }

    override fun getItemCount(): Int = listStats.size

    private fun updateViewData(holder: ViewHolder, position: Int){
        holder.team_1_progress.max = listStats[position].count
        holder.team_2_progress.max = listStats[position].count
        holder.team_2_progress.progress = listStats[position].team_2_count
        holder.team_1_progress.progress = listStats[position].team_1_count
        holder.team_1_count.text = listStats[position].team_1_count.toString()
        holder.team_2_count.text = listStats[position].team_2_count.toString()

        if (listStats[position].team_1_count == listStats[position].team_2_count){
            holder.team_1_progress.progressTintList = ColorStateList.valueOf(holder.itemView.context.getColor(R.color.colorAccentNoBest))
            holder.team_2_progress.progressTintList = ColorStateList.valueOf(holder.itemView.context.getColor(R.color.colorAccentNoBest))
            holder.team_1_count.setTextColor(holder.itemView.context.getColor(R.color.colorHint))
            holder.team_2_count.setTextColor(holder.itemView.context.getColor(R.color.colorHint))
        }

        if (listStats[position].team_1_count > listStats[position].team_2_count){
            holder.team_1_progress.progressTintList = ColorStateList.valueOf(holder.itemView.context.getColor(R.color.colorAccentBest))
            holder.team_2_progress.progressTintList = ColorStateList.valueOf(holder.itemView.context.getColor(R.color.colorAccentNoBest))
            holder.team_1_count.setTextColor(holder.itemView.context.getColor(R.color.colorText))
            holder.team_2_count.setTextColor(holder.itemView.context.getColor(R.color.colorHint))
        }

        if (listStats[position].team_1_count < listStats[position].team_2_count){
            holder.team_1_progress.progressTintList = ColorStateList.valueOf(holder.itemView.context.getColor(R.color.colorAccentNoBest))
            holder.team_2_progress.progressTintList = ColorStateList.valueOf(holder.itemView.context.getColor(R.color.colorAccentBest))
            holder.team_1_count.setTextColor(holder.itemView.context.getColor(R.color.colorHint))
            holder.team_2_count.setTextColor(holder.itemView.context.getColor(R.color.colorText))
        }
    }

    fun setData(listData: List<ModelState>){
        listStats = listData.toMutableList()
        notifyDataSetChanged()
    }

    fun getData(): List<ModelState>{
        return listStats
    }

    private fun updateBD(modelStats: ModelState, position: Int){
        ToNextScreen.match.listState[position] = modelStats
        ToNextScreen.viewModel.update(ToNextScreen.match)
    }

}