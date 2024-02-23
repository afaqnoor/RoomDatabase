package com.example.roomdatabase.adapter

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabase.R
import com.example.roomdatabase.database.UserNote

interface NoteItemClick {
    fun deleteClicked(userNote: UserNote)
    fun updateClicked(userNote: UserNote)
}

class NoteAdapter(
    private var noteList: List<UserNote>,
    private val itemClick: NoteItemClick,
) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    fun updateData(newList: List<UserNote>) {
        noteList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.ViewHolder {
        val item =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_each_item, parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: NoteAdapter.ViewHolder, position: Int) {
        val currentItem = noteList[position]
        holder.titleText.text = currentItem.title.toEditable()
        holder.desText.text = currentItem.description.toEditable()

        holder.deleteBtn.setOnClickListener {
            itemClick.deleteClicked(currentItem)
        }
        holder.updateBtn.setOnClickListener {
            itemClick.updateClicked(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.showTitle)
        val desText: TextView = itemView.findViewById(R.id.showDes)
        val deleteBtn: TextView = itemView.findViewById(R.id.deleteBtn)
        val updateBtn : TextView = itemView.findViewById(R.id.updateBtn)
    }
}