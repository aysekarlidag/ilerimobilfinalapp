package com.example.ilerimobilfinalapp

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ilerimobilfinalapp.app.AppAdapter
import com.example.ilerimobilfinalapp.app.AppViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var menu: Menu
    private lateinit var viewModel: AppViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var appAdapter: AppAdapter

    private var selectedSortItemId = R.id.radioIdAsc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onBackPressedDispatcher.addCallback {
            val dialog = Dialog(applicationContext)
            dialog.setContentView(R.layout.custom_exit_dialog)

            dialog.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }

            dialog.findViewById<Button>(R.id.btnYes).setOnClickListener {
                dialog.dismiss()
                finish()
            }

            dialog.setCancelable(false)
            dialog.show()
        }

        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        recyclerView = findViewById(R.id.recyclerView)

        viewModel.liveData.observe(this) {
            if (it.isNullOrEmpty()) {
                val emptyScreen = Intent(this, EmptyScreenActivity::class.java)
                startActivity(emptyScreen)
                finish()
            } else {
                val progressbar = findViewById<RelativeLayout>(R.id.progressbar)
                val detailScreen = Intent(this, DetailScreenActivity::class.java)

                appAdapter = AppAdapter(it) {
                    detailScreen.putExtra("data", it)
                    startActivity(detailScreen)
                }

                recyclerView.adapter = appAdapter
                recyclerView.visibility = View.VISIBLE
                progressbar.visibility = View.GONE
            }
        }

        viewModel.getList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.app_menu, menu)

        val view = menu.findItem(R.id.search).actionView as SearchView
        view.queryHint = "Ara..."

        view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                appAdapter.filter.filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                appAdapter.filter.filter(query)
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort -> {
                val dialog = Dialog(this)
                dialog.setContentView(R.layout.sort_dialog)

                dialog.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                    dialog.dismiss()
                }

                val radioGroup = dialog.findViewById<RadioGroup>(R.id.radioGroup)
                radioGroup.check(selectedSortItemId)

                dialog.findViewById<Button>(R.id.btnOk).setOnClickListener {
                    dialog.dismiss()

                    selectedSortItemId = radioGroup.checkedRadioButtonId
                    val radioButton = radioGroup.findViewById<RadioButton>(selectedSortItemId)
                    val type = radioGroup.indexOfChild(radioButton)
                    appAdapter.sort(type)

                    recyclerView.adapter = appAdapter
                }

                dialog.show()
            }
            R.id.view_type -> {
                val manager = recyclerView.layoutManager as GridLayoutManager
                manager.spanCount = if (manager.spanCount == 1) 2 else 1

                recyclerView.adapter = appAdapter
            }
        }

        return super.onOptionsItemSelected(item)
    }
}