package com.example.notesapp2.presentation.notes.edit

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.notesapp2.R
import com.example.notesapp2.databinding.FragmentEditNoteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditNoteFragment : Fragment(R.layout.fragment_edit_note) {

    private val viewModel: EditNoteViewModel by viewModels()

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentEditNoteBinding.bind(view)

        val noteId =
            requireArguments().getInt("noteId")

        viewModel.loadNote(noteId)

        binding.updateNoteButton.setOnClickListener {

            val title =
                binding.titleEditText.text.toString()

            val content =
                binding.contentEditText.text.toString()

            viewModel.updateNote(
                id = noteId,
                title = title,
                content = content
            )
        }

        binding.deleteNoteButton.setOnClickListener {
            showDeleteConfirmationDialog(noteId)
        }

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.noteState.collect { state ->

                when (state) {

                    is EditNoteState.Idle -> {
                        showLoading(false)
                    }

                    is EditNoteState.Loading -> {
                        showLoading(true)
                    }

                    is EditNoteState.Loaded -> {

                        showLoading(false)

                        binding.titleEditText.setText(
                            state.note.title
                        )

                        binding.contentEditText.setText(
                            state.note.content
                        )
                    }

                    is EditNoteState.Updated -> {

                        showLoading(false)

                        Toast.makeText(
                            requireContext(),
                            "Note updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        findNavController().navigateUp()
                    }

                    is EditNoteState.Deleted -> {

                        showLoading(false)

                        Toast.makeText(
                            requireContext(),
                            "Note deleted successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        findNavController().navigateUp()
                    }

                    is EditNoteState.Error -> {

                        showLoading(false)

                        Toast.makeText(
                            requireContext(),
                            state.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {

        binding.editNoteProgressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE

        binding.updateNoteButton.isEnabled =
            !isLoading

        binding.deleteNoteButton.isEnabled =
            !isLoading
    }

    private fun showDeleteConfirmationDialog(
        noteId: Int
    ) {

        AlertDialog.Builder(requireContext())
            .setTitle("Delete Note")
            .setMessage(
                "Are you sure you want to delete this note?"
            )
            .setNegativeButton(
                "Cancel",
                null
            )
            .setPositiveButton(
                "Delete"
            ) { _, _ ->
                viewModel.deleteNote(noteId)
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}