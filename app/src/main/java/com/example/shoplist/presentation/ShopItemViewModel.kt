package com.example.shoplist.presentation

import androidx.lifecycle.ViewModel
import com.example.shoplist.data.ShopItemRepositoryImpl
import com.example.shoplist.domain.AddShopItemUseCase
import com.example.shoplist.domain.GetShopItemUseCase
import com.example.shoplist.domain.ShopItem
import com.example.shoplist.domain.UpdateShopItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ShopItemViewModel : ViewModel() {

    private val repository =  ShopItemRepositoryImpl

    private val updateItemUseCase = UpdateShopItemUseCase(repository)
    private val addItemUseCase = AddShopItemUseCase(repository)
    private val getItemUseCase = GetShopItemUseCase(repository)

    private val _validateCount = MutableStateFlow(true)
    val validateCount: StateFlow<Boolean> = _validateCount

    private val _validateName = MutableStateFlow(true)
    val validateName: StateFlow<Boolean> = _validateName

    private val _currentShopItem = MutableStateFlow<ShopItem?>(null)
    val currentShopItem: StateFlow<ShopItem?> = _currentShopItem

    fun getShopItem(id: Int) {
        val shopItem = getItemUseCase.getShopItem(id)
        _currentShopItem.value = shopItem
    }

    private val _shouldCloseScreen = MutableStateFlow(false)
    val shouldCloseScreen: StateFlow<Boolean> = _shouldCloseScreen

    fun addShopItem(name: String?, count: String?) {
        val itemName = parseName(name)
        val itemCount = parseCount(count)
        if(validateInput(itemName, itemCount)) {
            addItemUseCase.addShopItem(ShopItem(name = itemName, count = itemCount, enabled = true))
            finishWork()
        }
    }

    fun updateShopItem(name: String?, count: String?) {
        val itemName = parseName(name)
        val itemCount = parseCount(count)
        if(validateInput(itemName, itemCount)) {
            _currentShopItem.value?.let {
                updateItemUseCase.updateShopItem(it.copy(name = itemName, count = itemCount))
                finishWork()
            }
        }
    }


    private fun parseName(name: String?) = name?.trim() ?: ""

    private fun parseCount(count: String?): Int {
        return try {
            count?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if(name.isBlank()) {
            result = false
            _validateName.value = true
        }
        if(count <= 0) {
            result = false
            _validateCount.value = true
        }
        return result
    }

    fun resetErrorName() {
        _validateName.value = false
    }


    fun resetErrorCount() {
        _validateCount.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = true
    }
}