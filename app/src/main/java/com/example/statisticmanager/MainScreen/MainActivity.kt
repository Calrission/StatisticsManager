package com.example.statisticmanager.MainScreen

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.statisticmanager.MatchScreen.MatchActivity
import com.example.statisticmanager.R
import com.example.statisticmanager.common.*
import com.example.statisticmanager.database.RoomViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: RoomViewModel
    private lateinit var layoutManagerMain: LinearLayoutManager
    private var dialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(RoomViewModel::class.java)

        layoutManagerMain = LinearLayoutManager(this)
        val mainAdapterRecyclerViewMain = AdapterRecyclerViewMain(onDeleteItem = object: OnDeleteItem{
            override fun onDelete(pos: Int) {
                val list = (rec_match.adapter as AdapterRecyclerViewMain).listMatch
                viewModel.delete(list[pos])
                Toast.makeText(applicationContext, "Удалено", Toast.LENGTH_SHORT).show()
                rec_match.adapter!!.notifyItemRemoved(pos)
                if (list.size - 1 == 0){
                    empty.visibility = View.VISIBLE
                }
            }
        }, onTapItem = object: OnTapItem{
            override fun onClick(pos: Int) {
                ToNextScreen.match = (rec_match.adapter as AdapterRecyclerViewMain).listMatch[pos]
                ToNextScreen.viewModel = viewModel
                startActivity(Intent(this@MainActivity, MatchActivity::class.java))
            }
        })

        rec_match.apply {
            adapter = mainAdapterRecyclerViewMain
            layoutManager = layoutManagerMain
        }

        viewModel.allMatch.observe(this, { matches ->
            matches.let {
                mainAdapterRecyclerViewMain.setData(matches)
                if (matches.isNotEmpty()){
                    empty.visibility = View.GONE
                }
            }
        })

        dialog = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))
            .setView(R.layout.dialog_create_match)
            .setTitle("Создание Матча")
            .setPositiveButton("Создать") { _, _ ->
                val team_1_name = dialog!!.findViewById<TextView>(R.id.edittext_1)
                val team_2_name = dialog!!.findViewById<TextView>(R.id.edittext_2)
                viewModel.insert(ModelMatch(UUID.randomUUID().toString(), team_1_name!!.text.toString(), team_2_name!!.text.toString(), 0, 0, arrayListOf(
                    //ModelState("test", 10, 8, 2)
                )))
                dialog!!.dismiss()
                Toast.makeText(this, "Матч создан !", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Отмена") { _, _ ->

            }
            .create()

        add_match.setOnClickListener {
            dialog!!.show()
        }
    }
}