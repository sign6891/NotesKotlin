package ru.sign6891.lessonsqlitekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import ru.sign6891.lessonsqlitekotlin.adapter.MyAdapter
import ru.sign6891.lessonsqlitekotlin.db.MyDbManager

class MainActivity : AppCompatActivity() {

    private val manager = MyDbManager(this)
    private val myAdapter = MyAdapter(ArrayList(), this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        initSearchView()
    }

    override fun onResume() {
        super.onResume()
        manager.openDb()
        fillAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        manager.closeDb()
    }

    fun onClickNew(view: View) {
        val i = Intent(this, EditActivity::class.java)
        startActivity(i)
    }

    fun initRecyclerView() {
        rcView.layoutManager = LinearLayoutManager(this)
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(rcView)
        rcView.adapter = myAdapter
    }

    private fun initSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val list = manager.readDbData(newText!!)
                myAdapter.updateAdapter(list)
                return true
            }
        })
    }

    fun fillAdapter() {
        val list = manager.readDbData("")
        myAdapter.updateAdapter(list)
        if (list.size < 1) {
            tvNoElements.visibility = View.VISIBLE
        } else tvNoElements.visibility = View.GONE
    }

    private fun getSwapMg(): ItemTouchHelper {
        return ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myAdapter.removeItem(viewHolder.adapterPosition, manager)
            }
        })
    }
}