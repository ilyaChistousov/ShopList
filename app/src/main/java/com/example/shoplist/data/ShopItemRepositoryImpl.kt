package com.example.shoplist.data

import com.example.shoplist.domain.ShopItem
import com.example.shoplist.domain.ShopItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.RuntimeException
import kotlin.random.Random

object ShopItemRepositoryImpl : ShopItemRepository {

    private val shopList = sortedSetOf<ShopItem>({ o1, o2 -> o1.id.compareTo(o2.id) })

    private val shopListSF = MutableStateFlow<List<ShopItem>>(emptyList())

    private var autoIncrementId = 0

    init {
        for (i in 0 until 100) {
            addShopItem(ShopItem(name = "Name $i", count = i, enabled = Random.nextBoolean()))
        }
    }


    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shopList.add(shopItem)
        updateStateFlow()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateStateFlow()
    }

    override fun getShopItem(id: Int): ShopItem {
        return shopList.find { it.id == id } ?: throw RuntimeException("Item not found")
    }

    override fun getShopList(): StateFlow<List<ShopItem>> {
        return shopListSF
    }

    override fun updateShopItem(shopItem: ShopItem) {
        val oldItem = getShopItem(shopItem.id)
        shopList.remove(oldItem)
        addShopItem(shopItem)
    }

    private fun updateStateFlow() {
        shopListSF.value = shopList.toList()
    }
}