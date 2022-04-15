package me.owapps.saodatasri.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import me.owapps.saodatasri.databinding.HomeFragmentBinding

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var _binding: HomeFragmentBinding
    private val mBinding get() = _binding

    private lateinit var viewModel: HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = HomeFragmentBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        val onBookClickListener = View.OnClickListener {
            Toast.makeText(requireContext(), "book clicked", Toast.LENGTH_SHORT).show()
        }

        val adapter = BooksAdapter(onBookClickListener)

        mBinding.rvBooks.layoutManager = LinearLayoutManager(requireContext())
        mBinding.rvBooks.adapter = adapter

        viewModel.fetchBooks()

        viewModel.mBooks.observe(viewLifecycleOwner) { books ->
            adapter.setList(books)
        }

    }


}