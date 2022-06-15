package com.example.shoplist.data

import com.example.shoplist.domain.ShopItem
import com.example.shoplist.domain.ShopItemRepository

class ShopItemRepositoryImpl : ShopItemRepository {

    private val shopList = mutableListOf<ShopItem>()
    private var autoIncrementId = 0

    override fun addShopItem(shopItem: ShopItem) {
        if(shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shopList.add(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
    }

    override fun getShopItem(id: Int): ShopItem {
        return shopList[id]
    }

    override fun getShopList(): List<ShopItem> {
        return shopList.toList()
    }

    override fun updateShopItem(shopItem: ShopItem) {
        shopList.remove(getShopItem(shopItem.id))
        addShopItem(shopItem)
    }
}