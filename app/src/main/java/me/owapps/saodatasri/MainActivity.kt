package me.owapps.saodatasri

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import me.owapps.saodatasri.base.BaseFragment
import me.owapps.saodatasri.ui.SplashFragment
import me.owapps.saodatasri.ui.viewmodels.SplashViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(SplashFragment())

        val viewModel = ViewModelProvider(this)[SplashViewModel::class.java]

    }

    private fun replaceFragment(fragment: BaseFragment) {
        supportFragmentManager.beginTransaction().replace(R.id.main_container, fragment)
            .commit()
    }


}