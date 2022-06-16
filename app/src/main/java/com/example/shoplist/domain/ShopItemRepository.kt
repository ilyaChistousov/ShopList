package com.example.shoplist.domain

import kotlinx.coroutines.flow.StateFlow

interface ShopItemRepository {

    fun addShopItem(shopItem: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)

    fun getShopItem(id: Int): ShopItem

    fun getShopList(): StateFlow<List<ShopItem>>

    fun updateShopItem(shopItem: ShopItem)
}