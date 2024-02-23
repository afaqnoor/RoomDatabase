package com.example.roomdatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabase.adapter.NoteAdapter
import com.example.roomdatabase.adapter.NoteItemClick
import com.example.roomdatabase.database.Dao
import com.example.roomdatabase.database.NoteDatabase
import com.example.roomdatabase.database.UserNote
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), NoteItemClick {

    private var dao: Dao? = null // Assuming you want to use the DAO interface
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fabAdd: FloatingActionButton = findViewById(R.id.floatingActionButton)
        fabAdd.setOnClickListener {
            showDialog()
        }
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewNote)
        recyclerView.layoutManager = LinearLayoutManager(this)
        noteAdapter = NoteAdapter(emptyList(), this)
        recyclerView.adapter = noteAdapter

        lifecycleScope.launch(Dispatchers.IO) {
            val noteDatabase = NoteDatabase.getInstance(applicationContext)
            dao = noteDatabase.noteDao()
        }
    }

    private fun updateRecyclerView() {
        lifecycleScope.launch(Dispatchers.Main) {
            dao?.getAllNotes()?.observe(this@MainActivity) { notes ->
                noteAdapter.updateData(notes)
            }
        }
    }

    private fun showDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_note, null)
        val editTitle: EditText =
            dialogView.findViewById(R.id.editTextTitle) // Find from dialogView
        val editDescription: EditText =
            dialogView.findViewById(R.id.editTextDescription) // Find from dialogView
        val addButton: TextView = dialogView.findViewById(R.id.buttonAdd)

        val alertDialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Add Note")

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        addButton.setOnClickListener {
            val title = editTitle.text.toString()
            val des = editDescription.text.toString()
            if (title.isNotBlank() && des.isNotBlank()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    dao?.insert(UserNote(title = title, description = des))
                    updateRecyclerView()
                }
                editTitle.text.clear()
                editDescription.text.clear()
            }
            alertDialog.dismiss()
        }
    }

    private fun showUpdateDialog(userNote: UserNote) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_note, null)
        val updateTitle: EditText = dialogView.findViewById(R.id.editTextTitle)
        val updateDescription: EditText = dialogView.findViewById(R.id.editTextDescription)
        val updateButton: TextView = dialogView.findViewById(R.id.buttonAdd)

        updateTitle.setText(userNote.title)
        updateDescription.setText(userNote.description)
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Update Note")

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        updateButton.setOnClickListener {
            val title = updateTitle.text.toString()
            val des = updateDescription.text.toString()
            if (title.isNotBlank() && des.isNotBlank()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    dao?.update(UserNote(userNote.uid, title, des))
                    updateRecyclerView()
                }
                alertDialog.dismiss()
            }
        }

    }

    override fun deleteClicked(userNote: UserNote) {
        lifecycleScope.launch(Dispatchers.IO) {
            dao?.delete(userNote)
            updateRecyclerView()
        }
    }

    override fun updateClicked(userNote: UserNote) {
        showUpdateDialog(userNote)
    }

}