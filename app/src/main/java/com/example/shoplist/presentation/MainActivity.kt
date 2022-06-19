package com.example.shoplist.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplist.R
import com.example.shoplist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditChangeListener {

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
        binding.buttonAddItem.setOnClickListener {
            if (isOnePaneMode()) {
                val intent = ItemShopActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                launchContainer(ShopItemFragment.newInstanceAddMode())
            }
        }
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

    private fun isOnePaneMode(): Boolean {
        return binding.fragmentContainer == null
    }

    private fun launchContainer(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupClickListener() {
        adapter.onItemShopClickListener = {
            if (isOnePaneMode()) {
                val intent = ItemShopActivity.newIntentUpdateItem(this, it.id)
                startActivity(intent)
            } else {
                launchContainer(ShopItemFragment.newInstanceEditMode(it.id))
            }
        }
    }

    private fun setupSwipeListener() {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
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

    override fun onEditChange() {
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }
}