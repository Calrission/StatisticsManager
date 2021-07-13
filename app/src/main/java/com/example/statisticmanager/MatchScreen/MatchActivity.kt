package com.example.statisticmanager.MatchScreen

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.statisticmanager.R
import com.example.statisticmanager.common.*
import com.example.statisticmanager.database.RoomViewModel
import kotlinx.android.synthetic.main.activity_match.*

class MatchActivity : AppCompatActivity() {
    lateinit var viewModel: RoomViewModel
    lateinit var dialog: AlertDialog
    lateinit var match: ModelMatch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)

        viewModel = ToNextScreen.viewModel
        match = ToNextScreen.match
        updateData()

        val click_team_1_win = View.OnClickListener {
            ToNextScreen.match.team_1_win ++
            updateData()
            updateInfo(ToNextScreen.match)
        }

        val down_click_team_1_win = View.OnLongClickListener {
            ToNextScreen.match.team_1_win --
            updateData()
            updateInfo(ToNextScreen.match)
            return@OnLongClickListener true
        }

        val down_click_team_2_win = View.OnLongClickListener {
            ToNextScreen.match.team_2_win --
            updateData()
            updateInfo(ToNextScreen.match)
            return@OnLongClickListener true
        }

        val click_team_2_win = View.OnClickListener {
            ToNextScreen.match.team_2_win ++
            updateData()
            updateInfo(ToNextScreen.match)
        }

        team_1.setOnClickListener(click_team_1_win)
        team_1_win_count.setOnClickListener(click_team_1_win)
        team_2.setOnClickListener(click_team_2_win)
        team_2_win_count.setOnClickListener(click_team_2_win)

        team_1.setOnLongClickListener(down_click_team_1_win)
        team_1_win_count.setOnLongClickListener(down_click_team_1_win)
        team_2.setOnLongClickListener(down_click_team_2_win)
        team_2_win_count.setOnLongClickListener(down_click_team_2_win)

        rec_stats.apply {
            adapter = AdapterRecyclerViewStats(match.listState,
                object: OnDeleteItem{
                    override fun onDelete(pos: Int) {
                        match.listState.removeAt(pos)
                        updateInfo(match)
                        rec_stats.adapter!!.notifyDataSetChanged()
                    }
            }, object: OnTapItem{
                    override fun onClick(pos: Int) {

                    }
            })
            layoutManager = LinearLayoutManager(this@MatchActivity)
        }
        dialog = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))
            .setView(R.layout.dialog_create_stat)
            .setTitle("Создание новой статы")
            .setPositiveButton("Создать") {_, _ ->
                val stat_name = dialog.findViewById<TextView>(R.id.edittext_stat)
                val model = ModelState(stat_name!!.text.toString(), 0, 0, 0)
                match.listState.add(model)
                updateInfo(match)
                rec_stats.adapter!!.notifyDataSetChanged()
            }
            .setNegativeButton("Отмена") {_, _ ->
                dialog.dismiss()
            }
            .create()

        add_state.setOnClickListener {
            dialog.show()
        }
    }

    private fun updateInfo(model: ModelMatch){
        ToNextScreen.match = model
        viewModel.update(model)
    }

    private fun updateData() {
        team_1.text = ToNextScreen.match.team_1
        team_2.text = ToNextScreen.match.team_2
        team_1_win_count.text = ToNextScreen.match.team_1_win.toString()
        team_2_win_count.text = ToNextScreen.match.team_2_win.toString()
    }
}