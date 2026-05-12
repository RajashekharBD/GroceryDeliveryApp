package com.example.grocery.ui.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.grocery.data.CartDatabase
import com.example.grocery.model.CartItem
import com.example.grocery.model.Category
import com.example.grocery.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GroceryViewModel(application: Application) : AndroidViewModel(application) {

    private val cartDao = CartDatabase.getInstance(application).cartDao()

    private val _categories = MutableLiveData<List<Category>>(emptyList())
    val categories: LiveData<List<Category>> = _categories

    private val _products = MutableLiveData<List<Product>>(emptyList())
    val products: LiveData<List<Product>> = _products

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var allProducts: List<Product> = emptyList()

    init {
        loadMockData()
        setupSearch()
    }

    private fun loadMockData() {
        // Mock categories
        val mockCategories = listOf(
            Category(1, "Fruits & Vegetables", "https://picsum.photos/seed/fruits/150/150"),
            Category(2, "Dairy & Eggs", "https://picsum.photos/seed/dairy/150/150"),
            Category(3, "Snacks & Beverages", "https://picsum.photos/seed/snacks/150/150"),
            Category(4, "Beauty & Hygiene", "https://picsum.photos/seed/beauty/150/150")
        )
        _categories.value = mockCategories

        // Mock products
        allProducts = listOf(
            Product(1, "Apple", "https://picsum.photos/seed/apple/150/150", 120.0, 150.0, 20, 4.5f, true),
            Product(2, "Banana", "https://picsum.photos/seed/banana/150/150", 60.0, 80.0, 25, 4.2f, true),
            Product(3, "Milk", "https://picsum.photos/seed/milk/150/150", 50.0, 60.0, 17, 4.0f, true),
            Product(4, "Bread", "https://picsum.photos/seed/bread/150/150", 40.0, 50.0, 20, 4.3f, true),
            Product(5, "Chips", "https://picsum.photos/seed/chips/150/150", 80.0, 100.0, 20, 4.1f, true),
            Product(6, "Shampoo", "https://picsum.photos/seed/shampoo/150/150", 150.0, 200.0, 25, 4.4f, true)
        )
        _products.value = allProducts
    }

    private fun setupSearch() {
        viewModelScope.launch {
            _searchQuery.collect { query ->
                _products.value = if (query.isEmpty()) {
                    allProducts
                } else {
                    allProducts.filter { 
                        it.name.lowercase().contains(query.lowercase()) 
                    }
                }
            }
        }
    }

    fun searchProducts(query: String) {
        _searchQuery.value = query
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val existing = cartDao.getCartItemByProductId(product.id)
                if (existing != null) {
                    cartDao.updateCartItem(existing.copy(quantity = existing.quantity + 1))
                } else {
                    cartDao.insertCartItem(
                        CartItem(
                            productId = product.id,
                            productName = product.name,
                            productPrice = product.price,
                            productImageUrl = product.imageUrl,
                            quantity = 1
                        )
                    )
                }
            }
        }
    }
}
