package com.example.notesapp2.presentation.notes.add

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.notesapp2.R
import com.example.notesapp2.databinding.FragmentAddNoteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddNoteFragment : Fragment(R.layout.fragment_add_note) {

    private val viewModel: AddNoteViewModel by viewModels()

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAddNoteBinding.bind(view)

        binding.saveNoteButton.setOnClickListener {

            val title =
                binding.titleEditText.text.toString()

            val content =
                binding.contentEditText.text.toString()

            viewModel.addNote(
                title = title,
                content = content
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.addNoteState.collect { state ->

                when (state) {

                    is AddNoteState.Idle -> {
                        showLoading(false)
                    }

                    is AddNoteState.Loading -> {
                        showLoading(true)
                    }

                    is AddNoteState.Success -> {

                        showLoading(false)

                        Toast.makeText(
                            requireContext(),
                            "Note created successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        findNavController().navigateUp()
                    }

                    is AddNoteState.Error -> {

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

        binding.addNoteProgressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE

        binding.saveNoteButton.isEnabled =
            !isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}