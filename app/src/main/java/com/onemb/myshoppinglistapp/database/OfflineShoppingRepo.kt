package com.onemb.myshoppinglistapp.database

import kotlinx.coroutines.flow.Flow

class OfflineShoppingRepo(private val shoppingListDao: ShoppingListDao) : ShoppinglistRepo {

    override fun getAll(): Flow<List<ShoppingListEntity>> {
        return shoppingListDao.getAll()
    }

    override suspend fun insert(shoppingRow: ShoppingListEntity) {
        shoppingListDao.insert(shoppingRow)
    }

}