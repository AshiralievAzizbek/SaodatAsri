package me.owapps.saodatasri.ui


import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import me.owapps.saodatasri.data.entities.book.Audio
import me.owapps.saodatasri.databinding.AudioListFragmentBinding
import me.owapps.saodatasri.databinding.PlayerBottomSheetBinding
import me.owapps.saodatasri.ui.adapters.AudioItemsAdapter
import me.owapps.saodatasri.ui.viewmodels.AudioListViewModel
import me.owapps.saodatasri.ui.viewmodels.PlayerViewModel
import me.owapps.saodatasri.util.AudioItem

@AndroidEntryPoint
class AudioListFragment : Fragment(), AudioItem {

    private lateinit var _binding: AudioListFragmentBinding
    private val mBinding: AudioListFragmentBinding get() = _binding

    private lateinit var _bottomSheet: PlayerBottomSheetBinding
    private val bottomSheet get() = _bottomSheet


    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var audioListViewModel: AudioListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AudioListFragmentBinding.inflate(layoutInflater, container, false)
        _bottomSheet = PlayerBottomSheetBinding.inflate(layoutInflater, container, false)
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        audioListViewModel = ViewModelProvider(requireActivity())[AudioListViewModel::class.java]
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playerViewModel.currentPlayingSong.observe(viewLifecycleOwner) {
            bottomSheet.audioName.text = it?.getText(MediaMetadataCompat.METADATA_KEY_ARTIST)
        }

        val bookId = arguments?.getInt("book_id")
        val adapter = AudioItemsAdapter(this)

        mBinding.audioItemsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mBinding.audioItemsRecyclerView.adapter = adapter

        playerViewModel.currentMediaItems.observe(viewLifecycleOwner) { items ->
            adapter.setItems(items)
        }

        playerViewModel.fetchAudiItemsByBookId(bookId?: 1)

    }

    override fun onPlay(raw: Audio) {
        playerViewModel.playOrToggle(raw)
    }

    override fun onDownload(raw: Audio) {
        audioListViewModel.download(raw, requireContext())
    }


}