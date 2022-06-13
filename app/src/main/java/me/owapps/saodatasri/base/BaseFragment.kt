package me.owapps.saodatasri.base

import androidx.fragment.app.Fragment
import me.owapps.saodatasri.R

abstract class BaseFragment : Fragment() {

    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        requireActivity().supportFragmentManager.beginTransaction()
            .addToBackStack("from${this::class.java.simpleName}to${fragment::class.java.simpleName}")
            .replace(R.id.main_container, fragment)
            .commit()
    }


}