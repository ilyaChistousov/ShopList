package com.example.shoplist.domain

class UpdateShopItemUseCase(private val shopItemRepository: ShopItemRepository) {

    fun updateShopItem(shopItem: ShopItem) {
        shopItemRepository.updateShopItem(shopItem)
    }
}