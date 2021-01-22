package ru.sign6891.lessonsqlitekotlin.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.sign6891.lessonsqlitekotlin.EditActivity
import ru.sign6891.lessonsqlitekotlin.R
import ru.sign6891.lessonsqlitekotlin.db.ListItemModel
import ru.sign6891.lessonsqlitekotlin.db.MyDbManager
import ru.sign6891.lessonsqlitekotlin.db.MyIntentConstants

class MyAdapter(var listTitle : ArrayList<ListItemModel>, contextM: Context) : RecyclerView.Adapter<MyAdapter.MyHolder>() {

    var listArray = listTitle
    var context = contextM

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rc_item, parent, false)
        return MyHolder(view, context)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(listArray.get(position))

    }

    override fun getItemCount(): Int = listArray.size


    class MyHolder(itemView: View, contextV: Context) : RecyclerView.ViewHolder(itemView) {
        var tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val context = contextV

        fun setData(item:ListItemModel){
            tvTitle.text = item.title
            itemView.setOnClickListener {
                val intent = Intent(context, EditActivity::class.java).apply {
                    putExtra(MyIntentConstants.INTENT_TITLE_KEY, item.title)
                    putExtra(MyIntentConstants.INTENT_DESC_KEY, item.desc)
                    putExtra(MyIntentConstants.INTENT_URI_KEY, item.uri)
                }
                context.startActivity(intent)
            }
        }
    }

    fun updateAdapter(listItems:List<ListItemModel>) {
        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }

    fun removeItem(pos:Int, dbManager: MyDbManager) {
        dbManager.removeItemFromDb(listArray[pos].id.toString())
        listArray.removeAt(pos)
        notifyItemRangeChanged(0, listArray.size)
        notifyItemRemoved(pos)
    }
}