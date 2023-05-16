package me.owapps.saodatasri.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import me.owapps.saodatasri.R
import me.owapps.saodatasri.databinding.HomeFragmentBinding
import me.owapps.saodatasri.ui.adapters.BooksAdapter
import me.owapps.saodatasri.ui.viewmodels.PlayerViewModel

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var _binding: HomeFragmentBinding
    private val mBinding get() = _binding

    private lateinit var viewModel: PlayerViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = HomeFragmentBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]

        val onBookClickListener = { bookId: Int ->
            val fragment = AudioListFragment()
            fragment.arguments = bundleOf("book_id" to bookId)
            replaceFragment(fragment)
        }

        val adapter = BooksAdapter(onBookClickListener)

        mBinding.rvBooks.layoutManager = LinearLayoutManager(requireContext())
        mBinding.rvBooks.adapter = adapter

        viewModel.mBooks.observe(viewLifecycleOwner) { books ->
            Log.d("TAGGGGG", "onViewCreated:$books ")
            adapter.setList(books)
        }

        viewModel.fetchBooks()


    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.main_container, fragment)
            .addToBackStack("fromHomeTo${fragment::class.java.simpleName}").commit()
    }


}