package me.owapps.saodatasri.ui.viewmodels

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SplashViewModel : ViewModel() {
    private val _timer = MutableLiveData<Boolean>(false)
    val mTimer: LiveData<Boolean> get() = _timer

    fun startTimer() {
        Handler().postDelayed({
            _timer.postValue(true)
        }, 3000)
    }

}