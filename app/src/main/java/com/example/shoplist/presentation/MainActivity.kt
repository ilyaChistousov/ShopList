package com.example.shoplist.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplist.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private lateinit var adapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = ShopListAdapter()
        binding.recyclerListItems.adapter = adapter
        lifecycleScope.launchWhenStarted {
            mainViewModel.shopList.collect {
                adapter.submitList(it)
            }
        }

        setupLongClickListener()
        setupClickListener()
        setupSwipeListener()
    }

    private fun setupLongClickListener() {
        adapter.onItemShopLongClickListener = {
            mainViewModel.updateItemShop(it)
        }
    }

    private fun setupClickListener() {
        adapter.onItemShopClickListener = {
            Snackbar.make(binding.root, "$it was clicked", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setupSwipeListener() {
        val callback =  object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                mainViewModel.deleteItemShop(adapter.currentList[viewHolder.adapterPosition])
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerListItems)
    }

}