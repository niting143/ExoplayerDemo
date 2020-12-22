package com.arraigntech.mobioticstask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arraigntech.mobioticstask.databinding.ActivityHomeBinding
import com.arraigntech.mobioticstask.ui.homeActivity.HomeViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.listLiveData.observe(this, Observer { it ->
            binding.rvRecycler.adapter = HomeItemAdapter(it)
        })

    }
}