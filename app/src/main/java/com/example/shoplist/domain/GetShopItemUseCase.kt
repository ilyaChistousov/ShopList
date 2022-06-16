package com.example.shoplist.domain

import kotlinx.coroutines.flow.StateFlow

class GetShopItemUseCase(private val shopItemRepository: ShopItemRepository) {

    fun getShopItem(id: Int) : ShopItem {
        return shopItemRepository.getShopItem(id)
    }
}