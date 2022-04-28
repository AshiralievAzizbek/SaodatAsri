package me.owapps.saodatasri.ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import me.owapps.saodatasri.databinding.AudioListFragmentBinding
import me.owapps.saodatasri.ui.adapters.AudioItemsAdapter

@AndroidEntryPoint
class AudioListFragment : Fragment() {

    private lateinit var _binding: AudioListFragmentBinding
    private val mBinding: AudioListFragmentBinding get() = _binding

    private lateinit var homeViewModel: HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AudioListFragmentBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        val adapter = AudioItemsAdapter()
        mBinding.audioItemsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mBinding.audioItemsRecyclerView.adapter = adapter

        homeViewModel.mBooks.value.also {
            if (!it.isNullOrEmpty())
                adapter.setItems(it.first().raws)
        }


    }


}