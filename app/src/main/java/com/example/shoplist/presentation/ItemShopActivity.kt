package com.example.shoplist.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.shoplist.R
import com.example.shoplist.databinding.ActivityShopItemBinding
//import com.example.shoplist.databinding.ActivityItemShopBinding
import com.example.shoplist.domain.ShopItem

class ItemShopActivity : AppCompatActivity(), ShopItemFragment.OnEditChangeListener {

    private lateinit var binding: ActivityShopItemBinding
    private var shopItemId: Int? = null
    private var screenMode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        screenMode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        if(savedInstanceState == null) {
            selectScreenMode()
        }
    }

    override fun onEditChange() {
        finish()
    }

    private fun selectScreenMode() {
        val fragment = when(screenMode) {
            MODE_EDIT -> ShopItemFragment.newInstanceEditMode(shopItemId!!)
            MODE_ADD -> ShopItemFragment.newInstanceAddMode()
            else -> throw RuntimeException("unknown screen: $screenMode")
        }
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.containerShopItem, fragment)
                .commit()
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"

        fun newIntentAddItem(context: Context): Intent {
            val mode = Intent(context, ItemShopActivity::class.java)
            mode.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return mode
        }

        fun newIntentUpdateItem(context: Context, shopItemId: Int): Intent {
            val mode = Intent(context, ItemShopActivity::class.java)
            mode.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            mode.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return mode
        }
    }
}