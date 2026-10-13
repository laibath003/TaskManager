package com.example.notesapp2.presentation.notes.list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp2.R
import com.example.notesapp2.databinding.FragmentNotesListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotesListFragment : Fragment(R.layout.fragment_notes_list) {

    private val viewModel: NotesListViewModel by viewModels()

    private var _binding: FragmentNotesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var notesAdapter: NotesAdapter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentNotesListBinding.bind(view)

        setupRecyclerView()

        binding.addNoteButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_notesListFragment_to_addNoteFragment
            )
        }

        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }

        binding.searchEditText.doAfterTextChanged { text ->

            val searchText =
                text?.toString()?.trim()

            viewModel.loadNotes(
                search = searchText
            )
        }

        viewModel.loadNotes()

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.notesState.collect { state ->

                when (state) {

                    is NotesState.Idle -> {
                        showLoading(false)
                    }

                    is NotesState.Loading -> {
                        showLoading(true)
                    }

                    is NotesState.Success -> {

                        showLoading(false)

                        notesAdapter.submitList(
                            state.notes
                        )
                    }

                    is NotesState.Error -> {

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

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.logoutState.collect { state ->

                when (state) {

                    is LogoutState.Idle -> {
                    }

                    is LogoutState.Success -> {

                        findNavController().navigate(
                            R.id.action_notesListFragment_to_loginFragment
                        )
                    }

                    is LogoutState.Error -> {

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

        binding.notesProgressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE

        binding.addNoteButton.isEnabled =
            !isLoading

        binding.logoutButton.isEnabled =
            !isLoading
    }

    private fun setupRecyclerView() {

        notesAdapter = NotesAdapter { note ->

            val bundle = Bundle().apply {
                putInt("noteId", note.id)
            }

            findNavController().navigate(
                R.id.action_notesListFragment_to_editNoteFragment,
                bundle
            )
        }

        binding.notesRecyclerView.apply {

            layoutManager =
                LinearLayoutManager(requireContext())

            adapter = notesAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}