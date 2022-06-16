package com.example.shoplist.presentation

import androidx.lifecycle.ViewModel
import com.example.shoplist.data.ShopItemRepositoryImpl
import com.example.shoplist.domain.*
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private val repository = ShopItemRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val updateShopItemUseCase = UpdateShopItemUseCase(repository)

    private val _shopList = getShopListUseCase.getShopList()

    val shopList: StateFlow<List<ShopItem>> = _shopList

    fun deleteItemShop(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }

    fun updateItemShop(shopItem: ShopItem) {
        val copy = shopItem.copy(enabled = !shopItem.enabled)
        updateShopItemUseCase.updateShopItem(copy)
    }
}