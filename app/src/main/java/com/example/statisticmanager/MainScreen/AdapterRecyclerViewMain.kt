package com.example.statisticmanager.MainScreen

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.statisticmanager.MatchScreen.MatchActivity
import com.example.statisticmanager.R
import com.example.statisticmanager.common.ModelMatch
import com.example.statisticmanager.common.OnDeleteItem
import com.example.statisticmanager.common.OnTapItem
import com.example.statisticmanager.common.ToNextScreen
import com.example.statisticmanager.database.RoomViewModel

class AdapterRecyclerViewMain(var listMatch: MutableList<ModelMatch> = arrayListOf(), val onDeleteItem: OnDeleteItem, val onTapItem: OnTapItem): RecyclerView.Adapter<AdapterRecyclerViewMain.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val team_1_name = itemView.findViewById<TextView>(R.id.team_1_name)
        val team_2_name = itemView.findViewById<TextView>(R.id.team_2_name)
        val team_1_win = itemView.findViewById<TextView>(R.id.team_1_win)
        val team_2_win = itemView.findViewById<TextView>(R.id.team_2_win)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.team_1_name.text = listMatch[position].team_1
        holder.team_2_name.text = listMatch[position].team_2
        holder.team_1_win.text = listMatch[position].team_1_win.toString()
        holder.team_2_win.text = listMatch[position].team_2_win.toString()

        holder.itemView.setOnClickListener {
            onTapItem.onClick(position)
        }

        holder.itemView.setOnLongClickListener {
            onDeleteItem.onDelete(position)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int = listMatch.size

    fun setData(listData: List<ModelMatch>){
        listMatch = listData.toMutableList()
        notifyDataSetChanged()
    }

}