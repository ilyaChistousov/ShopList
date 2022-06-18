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
import com.example.shoplist.databinding.ActivityItemShopBinding
import com.example.shoplist.domain.ShopItem

class ItemShopActivity : AppCompatActivity() {
    private val shopItemViewModel: ShopItemViewModel by lazy {
        ViewModelProvider(this)[ShopItemViewModel::class.java]
    }
    private lateinit var binding: ActivityItemShopBinding
    private var shopItemId: Int? = null
    private var screenMode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemShopBinding.inflate(layoutInflater)
        setContentView(binding.root)
        screenMode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        selectScreenMode()
        addTextChangeListeners()
        observeStateFlow()
    }

    private fun selectScreenMode() {
        when(screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }
    private fun observeStateFlow() {
        lifecycleScope.launchWhenStarted {
            shopItemViewModel.validateName.collect{
                val message = if(it) {
                    getString(R.string.error_inpit_name)
                } else {
                    null
                }
                binding.textLayoutName.error = message
            }
        }
        lifecycleScope.launchWhenStarted {
            shopItemViewModel.validateCount.collect{
                val message = if(it) {
                    getString(R.string.error_inpit_count)
                } else {
                    null
                }
                binding.textLayoutCount.error = message
            }
        }

        lifecycleScope.launchWhenStarted {
            shopItemViewModel.shouldCloseScreen.collect{
                if (it) {
                    finish()
                }
            }
        }
    }
    private fun addTextChangeListeners() {
        binding.editTextItemName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                shopItemViewModel.resetErrorName()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.editTextItemCount.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                shopItemViewModel.resetErrorCount()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun launchAddMode() {
        binding.buttonSave.setOnClickListener{
            shopItemViewModel.addShopItem(
                binding.editTextItemName.text.toString(),
                binding.editTextItemCount.text.toString())
        }
    }

    private fun launchEditMode() {
        shopItemViewModel.getShopItem(shopItemId!!)
        lifecycleScope.launchWhenStarted {
            shopItemViewModel.currentShopItem.collect {
                binding.editTextItemName.setText(it?.name)
                binding.editTextItemCount.setText(it?.count.toString())
            }
        }
        binding.buttonSave.setOnClickListener{
            shopItemViewModel.updateShopItem(
                binding.editTextItemName.text.toString(),
                binding.editTextItemCount.text.toString())
        }
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"

        fun newIntentAddItem(context: Context) : Intent{
            val mode = Intent(context, ItemShopActivity::class.java)
            mode.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return mode
        }

        fun newIntentUpdateItem(context: Context, shopItemId: Int) : Intent {
            val mode = Intent(context, ItemShopActivity::class.java)
            mode.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            mode.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return mode
        }
    }

}