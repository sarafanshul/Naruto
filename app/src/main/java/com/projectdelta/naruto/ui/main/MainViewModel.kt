package com.projectdelta.naruto.ui.main

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel(){
	private val _bottomNavVisibility = MutableLiveData<Int>()

	val bottomNavVisibility: LiveData<Int> = _bottomNavVisibility

	fun showBottomNav() {
		_bottomNavVisibility.postValue(View.VISIBLE)
	}

	fun hideBottomNav() {
		_bottomNavVisibility.postValue(View.GONE)
	}

}