package com.example.notesapp2.presentation.notes.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp2.databinding.NoteItemBinding
import com.example.notesapp2.domain.model.Note

class NotesAdapter(
    private val onNoteClick: (Note) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private var notes = emptyList<Note>()

    fun submitList(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteViewHolder {

        val binding = NoteItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: NoteViewHolder,
        position: Int
    ) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    inner class NoteViewHolder(
        private val binding: NoteItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {

            binding.noteTitleTextView.text = note.title
            binding.noteContentTextView.text = note.content

            binding.root.setOnClickListener {
                onNoteClick(note)
            }
        }
    }
}