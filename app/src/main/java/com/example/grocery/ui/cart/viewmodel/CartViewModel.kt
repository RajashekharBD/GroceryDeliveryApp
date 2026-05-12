package com.example.grocery.ui.cart.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.grocery.data.CartDatabase
import com.example.grocery.model.CartItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val cartDatabase = CartDatabase.getInstance(application)
    private val cartDao = cartDatabase.cartDao()

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    private val _cartCount = MutableStateFlow(0)
    val cartCount: StateFlow<Int> = _cartCount.asStateFlow()

    init {
        loadCartItems()
    }

    private fun loadCartItems() {
        viewModelScope.launch {
            val items = withContext(Dispatchers.IO) { cartDao.getAllCartItems() }
            _cartItems.value = items
            updateCartCount()
        }
    }

    private fun updateCartCount() {
        val totalCount = _cartItems.value?.sumOf { it.quantity } ?: 0
        _cartCount.value = totalCount
    }

    fun addToCart(productId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val existingItem = cartDao.getCartItemByProductId(productId)
                if (existingItem != null) {
                    cartDao.updateCartItem(existingItem.copy(quantity = existingItem.quantity + 1))
                }
            }
            loadCartItems()
        }
    }

    fun updateQuantity(cartItem: CartItem, newQuantity: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (newQuantity > 0) {
                    cartDao.updateCartItem(cartItem.copy(quantity = newQuantity))
                } else {
                    cartDao.deleteCartItem(cartItem)
                }
            }
            loadCartItems()
        }
    }

    fun removeFromCart(cartItem: CartItem) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { cartDao.deleteCartItem(cartItem) }
            loadCartItems()
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { cartDao.deleteAllCartItems() }
            loadCartItems()
        }
    }
}