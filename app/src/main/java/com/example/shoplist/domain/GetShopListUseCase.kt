package com.example.shoplist.domain

import kotlinx.coroutines.flow.StateFlow

class GetShopListUseCase(private val shopItemRepository: ShopItemRepository) {

    fun getShopList(): StateFlow<List<ShopItem>> {
        return shopItemRepository.getShopList()
    }
}