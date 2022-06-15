package com.example.shoplist.domain

interface ShopItemRepository {

    fun addShopItem(shopItem: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)

    fun getShopItem(id: Int): ShopItem

    fun getShopList(): List<ShopItem>

    fun updateShopItem(shopItem: ShopItem)
}