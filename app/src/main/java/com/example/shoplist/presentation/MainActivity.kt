package com.example.shoplist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.shoplist.R
import com.example.shoplist.domain.ShopItem
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        lifecycleScope.launchWhenStarted {
            mainViewModel.shopList.collect{
                Log.d("MainActivity", it.toString())
            }
        }

        lifecycleScope.launchWhenStarted {
            mainViewModel.deleteItemShop(mainViewModel.shopList.value[0])
        }

        lifecycleScope.launchWhenStarted {
            mainViewModel.updateItemShop(mainViewModel.shopList.value[1])
        }

        lifecycleScope.launchWhenStarted {
            mainViewModel.shopList.collect{
                Log.d("MainActivity", it.toString())
            }
        }
    }
}