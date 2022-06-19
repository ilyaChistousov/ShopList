package com.example.shoplist.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.shoplist.R
import com.example.shoplist.databinding.FragmentItemShopBinding
import com.example.shoplist.domain.ShopItem

class ShopItemFragment : Fragment() {

    private lateinit var binding: FragmentItemShopBinding
    private val shopItemViewModel: ShopItemViewModel by lazy {
        ViewModelProvider(this)[ShopItemViewModel::class.java]
    }

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID
    private lateinit var onEditChangeListener: OnEditChangeListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnEditChangeListener) {
            onEditChangeListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditChangeListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentItemShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectScreenMode()
        addTextChangeListeners()
        observeStateFlow()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parsParams()
    }

    private fun parsParams() {
        val args = requireArguments()
        if (!args.containsKey(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Not contain screen mode")
        }
        val mode = args.getString(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Unknown screen mode: $screenMode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Shop item id is absent")
            }
            shopItemId = args.getInt(EXTRA_SHOP_ITEM_ID)
        }
    }


    private fun selectScreenMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun observeStateFlow() {
        lifecycleScope.launchWhenStarted {
            shopItemViewModel.validateName.collect {
                val message = if (it) {
                    getString(R.string.error_inpit_name)
                } else {
                    null
                }
                binding.textLayoutName.error = message
            }
        }
        lifecycleScope.launchWhenStarted {
            shopItemViewModel.validateCount.collect {
                val message = if (it) {
                    getString(R.string.error_inpit_count)
                } else {
                    null
                }
                binding.textLayoutCount.error = message
            }
        }

        lifecycleScope.launchWhenStarted {
            shopItemViewModel.shouldCloseScreen.collect {
                if (it) {
                    activity?.onBackPressed()
                }
            }
        }
    }

    private fun addTextChangeListeners() {
        binding.editTextItemName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                shopItemViewModel.resetErrorName()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.editTextItemCount.addTextChangedListener(object : TextWatcher {
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
        binding.buttonSave.setOnClickListener {
            shopItemViewModel.addShopItem(
                binding.editTextItemName.text.toString(),
                binding.editTextItemCount.text.toString()
            )
        }
    }

    private fun launchEditMode() {
        shopItemViewModel.getShopItem(shopItemId)
        lifecycleScope.launchWhenStarted {
            shopItemViewModel.currentShopItem.collect {
                binding.editTextItemName.setText(it?.name)
                binding.editTextItemCount.setText(it?.count.toString())
            }
        }
        binding.buttonSave.setOnClickListener {
            shopItemViewModel.updateShopItem(
                binding.editTextItemName.text.toString(),
                binding.editTextItemCount.text.toString()
            )
        }
    }

    interface OnEditChangeListener {
        fun onEditChange()
    }

    companion object {
        const val EXTRA_SCREEN_MODE = "extra_mode"
        const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        const val MODE_EDIT = "mode_edit"
        const val MODE_ADD = "mode_add"
        const val MODE_UNKNOWN = ""

        fun newInstanceAddMode(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditMode(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_SCREEN_MODE, MODE_EDIT)
                    putInt(EXTRA_SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }
}