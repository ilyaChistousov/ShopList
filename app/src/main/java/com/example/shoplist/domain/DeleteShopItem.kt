package com.example.shoplist.domain

class DeleteShopItem(private val shopItemRepository: ShopItemRepository) {

    fun deleteShopItem(shopItem: ShopItem) {
        shopItemRepository.deleteShopItem(shopItem)
    }
}