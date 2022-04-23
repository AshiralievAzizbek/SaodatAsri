package me.owapps.saodatasri.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.owapps.saodatasri.databinding.AudioListFragmentBinding

class AudioListFragment : Fragment() {

    private lateinit var _binding: AudioListFragmentBinding
    private val mBinding: AudioListFragmentBinding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AudioListFragmentBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }



}