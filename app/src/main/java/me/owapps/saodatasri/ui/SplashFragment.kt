package me.owapps.saodatasri.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.owapps.saodatasri.R
import me.owapps.saodatasri.base.BaseFragment
import me.owapps.saodatasri.ui.viewmodels.SplashViewModel
import java.util.ArrayList

class SplashFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    //    : ArrayList<ArrayList<T>>
    private fun <T> map(array: ArrayList<T>): ArrayList<ArrayList<T>> {
        val result = ArrayList<ArrayList<T>>()
        for (i in 0..array.lastIndex step 2) {
            if (i + 1 <= array.lastIndex)
                result.add(arrayListOf(array[i], array[i + 1]))
            else
                result.add(arrayListOf(array[i]))
        }
        return result
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel: SplashViewModel = ViewModelProvider(this)[SplashViewModel::class.java]
        viewModel.startTimer()
        viewModel.mTimer.observe(viewLifecycleOwner){
            if (it) replaceFragment(HomeFragment())
        }

    }
}